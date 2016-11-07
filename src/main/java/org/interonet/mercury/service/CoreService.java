package org.interonet.mercury.service;

import org.interonet.mercury.domain.auth.User;
import org.interonet.mercury.domain.core.Slice;
import org.interonet.mercury.domain.core.SwitchToSwitchTunnel;
import org.interonet.mercury.domain.core.SwitchToVMTunnel;
import org.interonet.mercury.domain.core.TimeTable;
import org.interonet.mercury.domain.core.datetime.SliceDuration;
import org.interonet.mercury.domain.core.pool.RunnableSlicePool;
import org.interonet.mercury.domain.core.pool.RunningSlicePool;
import org.interonet.mercury.domain.core.pool.RunningWaitingSlicePool;
import org.interonet.mercury.domain.core.pool.SlicePool;
import org.interonet.mercury.domain.core.pool.TerminatableSlicePool;
import org.interonet.mercury.domain.core.pool.TerminatedSlicePool;
import org.interonet.mercury.domain.core.pool.TerminatedWaitingPool;
import org.interonet.mercury.domain.core.pool.TimeWaitingSlicePool;
import org.interonet.mercury.domain.ldm.pool.LDMTaskRunningFuturePool;
import org.interonet.mercury.domain.ldm.pool.LDMTaskTerminatedFuturePool;
import org.interonet.mercury.domain.ldm.task.LDMStartTask;
import org.interonet.mercury.domain.ldm.task.LDMStopTask;
import org.interonet.mercury.domain.ldm.task.LDMTaskReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class CoreService {

    @Autowired
    ConfigService configService;
    @Autowired
    AuthService authService;
    private Logger logger = LoggerFactory.getLogger(CoreService.class);
    @Autowired
    private LDMService ldmService;

    @Autowired
    private TimeTable timeTable;

    @Autowired
    private SlicePool slicePool;

    @Autowired
    private TimeWaitingSlicePool timeWaitingSlicePool;

    @Autowired
    private RunnableSlicePool runnableSlicePool;

    @Autowired
    private RunningWaitingSlicePool runningWaitingSlicePool;

    @Autowired
    private RunningSlicePool runningSlicePool;

    @Autowired
    private TerminatableSlicePool terminatableSlicePool;

    @Autowired
    private TerminatedWaitingPool terminatedWaitingSlicePool;

    @Autowired
    private TerminatedSlicePool terminatedSlicePool;

    @Autowired
    private LDMTaskRunningFuturePool ldmTaskRunningFuturePool;

    @Autowired
    private LDMTaskTerminatedFuturePool ldmTaskTerminatedFuturePool;

    public Slice submitSlice(Slice slice) throws Exception {
        logger.debug(slice.toString());

        int switchesNum = slice.getSwitchesNum();
        int vmsNum = slice.getVmsNum();
        ZonedDateTime beginTime = slice.getBeginTime();
        ZonedDateTime endTime = slice.getEndTime();
        String sliceId = UUID.randomUUID().toString();
        slice.setId(sliceId);

        Map<String, Integer> resourceRequest = new HashMap<>(2);
        resourceRequest.put("switch", switchesNum);
        resourceRequest.put("vm", vmsNum);
        Map<String, List<Integer>> reservedResources = timeTable.tryToReserve(sliceId, resourceRequest, beginTime, endTime);

        List<Integer> reservedSwitch = reservedResources.get("switch");
        List<Integer> reservedVM = reservedResources.get("vm");

        if (reservedSwitch.size() != switchesNum || reservedVM.size() != vmsNum) return null;

        slice.setSwitchIdList(reservedSwitch);
        slice.setSwitchList(configService.getSwitchList(reservedSwitch));
        slice.setVmIdList(reservedVM);
        slice.setVmList(configService.getVMList(reservedVM));
        slice.setStatus(Slice.SliceStatus.TIME_WAITING);

        slicePool.submit(slice);
        timeWaitingSlicePool.submit(slice);

        return slice;
    }

    public List<Slice> getSlicePool(String slicePoolType) {
        if (slicePoolType == null) {
            return new ArrayList<>();
        }
        if (slicePoolType.equalsIgnoreCase("SlicePool")) {
            return slicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("TimeWaitingSlicePool")) {
            return timeWaitingSlicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("RunnableSlicePool")) {
            return runnableSlicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("RunningWaitingSlicePool")) {
            return runningWaitingSlicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("RunningSlicePool")) {
            return runningSlicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("TerminatableSlicePool")) {
            return terminatableSlicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("TerminatedWaitingPool")) {
            return terminatedWaitingSlicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("TerminatedSlicePool")) {
            return terminatedSlicePool.getSlicePoolSnapShot();
        }
        return slicePool.getSlicePoolSnapShot();
    }

    public Slice getSlice(User user, String sliceId) {
        String username = user.getUsername();
        for (Slice slice : slicePool.getSlicePoolSnapShot()) {
            if (slice.getUsername().equals(username) && slice.getId().equals(sliceId)) {
                return slice;
            }
        }
        return null;
    }

    public Map<Integer, TreeSet<SliceDuration>> getSwitchTimeTable() {
        return timeTable.getSwitchTimeTableSnapShot();
    }

    public Map<Integer, TreeSet<SliceDuration>> getVMTimeTable() {
        return timeTable.getVmTimeTableSnapShot();
    }

    public Slice tryToTerminateSlice(String sliceId) {
        if (sliceId == null) return null;
        Slice slice = slicePool.consumeBySliceId(sliceId);
        if (slice != null) {
            Slice.SliceStatus status = slice.getStatus();
            /*
            * move the slice to terminatedPool if it's in the TIME_WAITING & RUNNABLE pool
            * */
            if (status == Slice.SliceStatus.TIME_WAITING || status == Slice.SliceStatus.RUNNABLE) {
                ZonedDateTime now = ZonedDateTime.now(slice.getBeginTime().getZone());
                slice.setEndTime(now);
                slice.setStatus(Slice.SliceStatus.TERMINATED);
                slice.setException(Slice.SliceException.USER_OPERATION);
                terminatedSlicePool.submit(slice);
                return slice;
            }
            /*
            * Setting to endTime early to let the sliceEndTimerChecker to kill it.
            * set the delay for 30 seconds to end time.
            * */
            if (status == Slice.SliceStatus.RUNNING) {
                ZonedDateTime now = ZonedDateTime.now(slice.getBeginTime().getZone());
                slice.setEndTime(now.plusSeconds(30));
                return slice;
            }
            return null;
        }
        return null;
    }

    @Scheduled(fixedRate = 500)
    public void checkSliceBeginTime() {
        ZonedDateTime now = ZonedDateTime.now();
        List<Slice> list = timeWaitingSlicePool.consumeSliceBeginTimeBeforeThan(now);
        for (Slice slice : list) {
            slice.setStatus(Slice.SliceStatus.RUNNABLE);
            logger.info("submit slice to RUNNABLE: sliceId=" + slice.getId() + " beginTime=" + slice.getBeginTime());
            runnableSlicePool.submit(slice);
        }
    }

    @Scheduled(fixedRate = 500)
    public void runSlice() {
        try {
            List<Slice> list = runnableSlicePool.consumeAllSlice();
            for (Slice slice : list) {
                List<Integer> switchesIDs = slice.getSwitchIdList();
                List<Integer> vmIDs = slice.getVmIdList();
                Map<String, Integer> userSW2domSW = new HashMap<>();
                Map<String, Integer> userVM2domVM = new HashMap<>();
                for (int i = 0; i < switchesIDs.size(); i++)
                    userSW2domSW.put("s" + Integer.toString(i), switchesIDs.get(i));

                for (int i = 0; i < vmIDs.size(); i++)
                    userVM2domVM.put("h" + Integer.toString(i), vmIDs.get(i));

                List<SwitchToSwitchTunnel> switchToSwitchTunnels = SwitchToSwitchTunnel.getswswTunnel(slice.getTopology(), userSW2domSW, userVM2domVM);
                List<SwitchToVMTunnel> switchToVMTunnels = SwitchToVMTunnel.getswvmTunnel(slice.getTopology(), userSW2domSW, userVM2domVM);

                if (switchToSwitchTunnels.size() != 0 && switchToVMTunnels.size() == 0) {
                    slice.setStatus(Slice.SliceStatus.TERMINATED);
                    slice.setException(Slice.SliceException.WRONG_TOPOLOGY_FORMAT);
                    continue;
                }
                slice.setUserSW2domSW(userSW2domSW);
                slice.setUserVM2domVM(userVM2domVM);
                slice.setSwitchToSwitchTunnelList(switchToSwitchTunnels);
                slice.setSwitchToVMTunnelList(switchToVMTunnels);

                LDMStartTask ldmStartTask = new LDMStartTask(slice);
                Future<LDMTaskReturn> startFuture = ldmService.submit(ldmStartTask);

                logger.info("submit slice starting ldm to ldmConnector: sliceId=" + slice.getId());
                ldmTaskRunningFuturePool.submit(startFuture);

                logger.info("submit slice to RUNNING_WAITING: sliceId=" + slice.getId() + " beginTime=" + slice.getBeginTime());
                slice.setStatus(Slice.SliceStatus.RUNNING_WAITING);
                runningWaitingSlicePool.submit(slice);
            }
        } catch (Exception e) {
            logger.error("InterruptedException:", e);
            throw e;
        }
    }

    @Scheduled(fixedRate = 500)
    public void checkSliceRunning() {
        try {
            List<Future<LDMTaskReturn>> list = ldmTaskRunningFuturePool.consumeAllFinishedTask();
            for (Future<LDMTaskReturn> future : list) {
                LDMTaskReturn ldmTaskReturn = future.get();
                if (!ldmTaskReturn.getSuccess()) {
                    Slice slice = runningWaitingSlicePool.consumeBySliceId(ldmTaskReturn.getSliceId());
                    slice.setException(Slice.SliceException.LDM_TASK_START_CALL_TIMEOUT);
                    slice.setStatus(Slice.SliceStatus.TERMINATED);
                } else {
                    Slice slice = runningWaitingSlicePool.consumeBySliceId(ldmTaskReturn.getSliceId());
                    slice.setStatus(Slice.SliceStatus.RUNNING);
                    logger.info("submit slice to RUNNING: sliceId=" + slice.getId());
                    runningSlicePool.submit(slice);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Thread Exception", e);
        }
    }

    @Scheduled(fixedRate = 500)
    public void checkSliceEndTime() {
        ZonedDateTime now = ZonedDateTime.now();
        List<Slice> list = runningSlicePool.consumeSliceEndTimeBefore(now);
        for (Slice slice : list) {
            logger.info("submit slice to TERMINATABLE: sliceId=" + slice.getId() + ", endTime=" + slice.getEndTime());
            slice.setStatus(Slice.SliceStatus.TERMINATABLE);
            terminatableSlicePool.submit(slice);
        }
    }


    @Scheduled(fixedRate = 500)
    public void terminateSlice() {
        List<Slice> list = terminatableSlicePool.consumeAllSlice();
        //logger.debug("terminatableSlicePool.consumeAllSlice()");
        for (Slice slice : list) {
            List<Integer> switchesIDs = slice.getSwitchIdList();
            List<Integer> vmIDs = slice.getVmIdList();
            List<SwitchToSwitchTunnel> switchToSwitchTunnels = slice.getSwitchToSwitchTunnelList();
            List<SwitchToVMTunnel> switchToVMTunnels = slice.getSwitchToVMTunnelList();

            LDMStopTask ldmStopTask = new LDMStopTask(slice);
            Future<LDMTaskReturn> futureTask = ldmService.submit(ldmStopTask);
            logger.info("submit slice stopping task to ldmConnector: sliceId=" + slice.getId());
            ldmTaskTerminatedFuturePool.submit(futureTask);

            slice.setStatus(Slice.SliceStatus.TERMINATED_WAITING);
            logger.info("submit slice to TERMINATED_WAITING: sliceId=" + slice.getId() + " beginTime=" + slice.getBeginTime());
            terminatedWaitingSlicePool.submit(slice);
        }
    }


    @Scheduled(fixedRate = 500)
    public void checkSliceTerminated() {
        try {
            List<Future<LDMTaskReturn>> list = ldmTaskTerminatedFuturePool.consumeAllFinishedTask();
            for (Future<LDMTaskReturn> future : list) {
                if (!future.isDone()) continue;
                LDMTaskReturn ldmTaskReturn = future.get();
                if (!ldmTaskReturn.getSuccess()) {
                    Slice slice = terminatedWaitingSlicePool.consumeBySliceId(ldmTaskReturn.getSliceId());
                    slice.setException(Slice.SliceException.LDM_TASK_STOP_CALL_TIMEOUT);
                    slice.setStatus(Slice.SliceStatus.TERMINATED);
                } else {
                    Slice slice = terminatedWaitingSlicePool.consumeBySliceId(ldmTaskReturn.getSliceId());
                    logger.info("submit slice to TERMINATED: sliceId=" + slice.getId());
                    slice.setStatus(Slice.SliceStatus.TERMINATED);
                    terminatedSlicePool.submit(slice);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Thread Exception", e);
        }
    }
}
