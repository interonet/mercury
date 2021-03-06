package org.interonet.mercury.domain.core;

import org.interonet.mercury.domain.core.datetime.SliceDuration;
import org.interonet.mercury.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

@Component
public class TimeTable {
    private static Logger logger = LoggerFactory.getLogger(TimeTable.class);

    @Autowired
    ConfigService configService;

    private Integer totalSwitchesNumber;
    private Integer totalVmsNumber;

    private Map<Integer, TreeSet<SliceDuration>> switchTimeTable;
    private Map<Integer, TreeSet<SliceDuration>> vmTimeTable;

    @PostConstruct
    public void init() {
        totalSwitchesNumber = configService.getSwitchNumber();
        totalVmsNumber = configService.getVmNumber();

        switchTimeTable = new HashMap<>();
        for (int i = 1; i <= totalSwitchesNumber; i++) {
            TreeSet<SliceDuration> swTimeLine = new TreeSet<>();
            switchTimeTable.put(i, swTimeLine);
        }
        vmTimeTable = new HashMap<>();
        for (int i = 1; i <= totalVmsNumber; i++) {
            TreeSet<SliceDuration> vmTimeLine = new TreeSet<>();
            vmTimeTable.put(i, vmTimeLine);
        }
    }

    /*
       resourceRequest={"switch":10,"vm":10}
    */
    synchronized public Map<String, List<Integer>> tryToReserve(String sliceId, Map<String, Integer> resourceRequest, ZonedDateTime reqBegin, ZonedDateTime reqEnd) throws Exception {
        ZonedDateTime nowTime = ZonedDateTime.now();
        if (reqBegin.isBefore(nowTime)) {
            logger.error("begin time is early than now time, failed");
            logger.error("beginTime = " + reqBegin);
            logger.error("nowTime = " + nowTime);
            throw new Exception("resourceRequest = [" + resourceRequest + "], reqBegin = [" + reqBegin + "], reqEnd = [" + reqEnd + "], now= [" + nowTime + "]");
        }

        Integer requestSwitchNum = null;
        Integer requestVMNum = null;
        for (Map.Entry<String, Integer> entry : resourceRequest.entrySet()) {
            String resourceName = entry.getKey(); //switch or vm
            if (resourceName.equals("switch")) requestSwitchNum = entry.getValue();
            if (resourceName.equals("vm")) requestVMNum = entry.getValue();
        }
        if (requestSwitchNum == null || requestVMNum == null)
            throw new Exception("resourceRequest = [" + resourceRequest + "], reqBegin = [" + reqBegin + "], reqEnd = [" + reqEnd + "]");

        /*Try to find available switches and vms.*/
        Set<Integer> availableSwitch = new HashSet<>();
        for (Map.Entry<Integer, TreeSet<SliceDuration>> entry : switchTimeTable.entrySet()) {
            int switchId = entry.getKey();
            TreeSet<SliceDuration> switchTimeLine = entry.getValue();
            if (!switchTimeLine.contains(new SliceDuration(sliceId, reqBegin, reqEnd)))
                availableSwitch.add(switchId);
        }
        Set<Integer> availableVM = new HashSet<>();
        for (Map.Entry<Integer, TreeSet<SliceDuration>> entry : vmTimeTable.entrySet()) {
            int vmId = entry.getKey();
            TreeSet<SliceDuration> vmTimeLine = entry.getValue();
            if (!vmTimeLine.contains(new SliceDuration(sliceId, reqBegin, reqEnd)))
                availableVM.add(vmId);
        }


        List<Integer> reservedSwitch = new ArrayList<>();
        List<Integer> reservedVMs = new ArrayList<>();

        if (availableSwitch.size() < requestSwitchNum || availableVM.size() < requestVMNum) {
            Map<String, List<Integer>> resourceMap = new HashMap<>(resourceRequest.size());
            resourceMap.put("switch", reservedSwitch);
            resourceMap.put("vm", reservedVMs);
            return resourceMap;
        }

        /*Insert into time table actually */
        for (Integer switchId : availableSwitch) {
            TreeSet<SliceDuration> switchTimeLine = switchTimeTable.get(switchId);
            if (switchTimeLine.add(new SliceDuration(sliceId, reqBegin, reqEnd)))
                reservedSwitch.add(switchId);
            if (reservedSwitch.size() == requestSwitchNum) break;
        }

        for (Integer vmId : availableVM) {
            TreeSet<SliceDuration> vmTimeLine = vmTimeTable.get(vmId);
            if (vmTimeLine.add(new SliceDuration(sliceId, reqBegin, reqEnd)))
                reservedVMs.add(vmId);
            if (reservedVMs.size() == requestVMNum) break;
        }

        Map<String, List<Integer>> resourceMap = new HashMap<>(resourceRequest.size());
        resourceMap.put("switch", reservedSwitch);
        resourceMap.put("vm", reservedVMs);
        return resourceMap;

    }

    synchronized public Map<Integer, TreeSet<SliceDuration>> getSwitchTimeTableSnapShot() {
        return new HashMap<>(switchTimeTable);
    }

    synchronized public Map<Integer, TreeSet<SliceDuration>> getVmTimeTableSnapShot() {
        return new HashMap<>(vmTimeTable);
    }

}
