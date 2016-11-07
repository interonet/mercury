package org.interonet.mercury.domain.core;

import org.interonet.mercury.export.ExportSubmitSlice;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class Slice {
    private Integer switchesNum;
    private Integer vmsNum;
    private ZonedDateTime beginTime;
    private ZonedDateTime endTime;
    private Map<String, String> topology;
    private Map<String, String> controllerConf;
    private Map<String, String> switchConf;
    private Map<String, Map> customSwitchConf;

    private String id;
    private SliceStatus status = SliceStatus.NEW;
    private String username;
    private List<Integer> switchIdList;
    private List<Switch> switchList;
    private List<Integer> vmIdList;
    private List<VirtualMachine> vmList;
    private List<SwitchToSwitchTunnel> switchToSwitchTunnelList;
    private List<SwitchToVMTunnel> switchToVMTunnelList;
    private Map<String, Integer> userSW2domSW;
    private Map<String, Integer> userVM2domVM;
    private SliceException exception = SliceException.NONE;

    public Slice() {
    }

    public Slice(ExportSubmitSlice exportSubmitSlice) {
        this.switchesNum = exportSubmitSlice.getSwitchesNum();
        this.vmsNum = exportSubmitSlice.getVmsNum();
        this.beginTime = exportSubmitSlice.getBeginTime();
        this.endTime = exportSubmitSlice.getEndTime();
        this.topology = exportSubmitSlice.getTopology();
        this.controllerConf = exportSubmitSlice.getControllerConf();
        this.switchConf = exportSubmitSlice.getSwitchConf();
        this.customSwitchConf = exportSubmitSlice.getCustomSwitchConf();
    }

    public Integer getSwitchesNum() {
        return switchesNum;
    }

    public void setSwitchesNum(Integer switchesNum) {
        this.switchesNum = switchesNum;
    }

    public Integer getVmsNum() {
        return vmsNum;
    }

    public void setVmsNum(Integer vmsNum) {
        this.vmsNum = vmsNum;
    }

    public ZonedDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(ZonedDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Map<String, String> getTopology() {
        return topology;
    }

    public void setTopology(Map<String, String> topology) {
        this.topology = topology;
    }

    public Map<String, String> getControllerConf() {
        return controllerConf;
    }

    public void setControllerConf(Map<String, String> controllerConf) {
        this.controllerConf = controllerConf;
    }

    public Map<String, String> getSwitchConf() {
        return switchConf;
    }

    public void setSwitchConf(Map<String, String> switchConf) {
        this.switchConf = switchConf;
    }

    public Map<String, Map> getCustomSwitchConf() {
        return customSwitchConf;
    }

    public void setCustomSwitchConf(Map<String, Map> customSwitchConf) {
        this.customSwitchConf = customSwitchConf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SliceStatus getStatus() {
        return status;
    }

    public void setStatus(SliceStatus status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Integer> getSwitchIdList() {
        return switchIdList;
    }

    public void setSwitchIdList(List<Integer> switchIdList) {
        this.switchIdList = switchIdList;
    }

    public List<Switch> getSwitchList() {
        return switchList;
    }

    public void setSwitchList(List<Switch> switchList) {
        this.switchList = switchList;
    }

    public List<Integer> getVmIdList() {
        return vmIdList;
    }

    public void setVmIdList(List<Integer> vmIdList) {
        this.vmIdList = vmIdList;
    }

    public List<VirtualMachine> getVmList() {
        return vmList;
    }

    public void setVmList(List<VirtualMachine> vmList) {
        this.vmList = vmList;
    }

    public List<SwitchToSwitchTunnel> getSwitchToSwitchTunnelList() {
        return switchToSwitchTunnelList;
    }

    public void setSwitchToSwitchTunnelList(List<SwitchToSwitchTunnel> switchToSwitchTunnelList) {
        this.switchToSwitchTunnelList = switchToSwitchTunnelList;
    }

    public List<SwitchToVMTunnel> getSwitchToVMTunnelList() {
        return switchToVMTunnelList;
    }

    public void setSwitchToVMTunnelList(List<SwitchToVMTunnel> switchToVMTunnelList) {
        this.switchToVMTunnelList = switchToVMTunnelList;
    }

    public Map<String, Integer> getUserSW2domSW() {
        return userSW2domSW;
    }

    public void setUserSW2domSW(Map<String, Integer> userSW2domSW) {
        this.userSW2domSW = userSW2domSW;
    }

    public Map<String, Integer> getUserVM2domVM() {
        return userVM2domVM;
    }

    public void setUserVM2domVM(Map<String, Integer> userVM2domVM) {
        this.userVM2domVM = userVM2domVM;
    }

    public SliceException getException() {
        return exception;
    }

    public void setException(SliceException exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "Slice{" +
                "switchesNum=" + switchesNum +
                ", vmsNum=" + vmsNum +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", topology=" + topology +
                ", controllerConf=" + controllerConf +
                ", switchConf=" + switchConf +
                ", customSwitchConf=" + customSwitchConf +
                ", id='" + id + '\'' +
                ", status=" + status +
                ", username='" + username + '\'' +
                ", switchIdList=" + switchIdList +
                ", switchList=" + switchList +
                ", vmIdList=" + vmIdList +
                ", vmList=" + vmList +
                ", switchToSwitchTunnelList=" + switchToSwitchTunnelList +
                ", switchToVMTunnelList=" + switchToVMTunnelList +
                ", userSW2domSW=" + userSW2domSW +
                ", userVM2domVM=" + userVM2domVM +
                ", exception=" + exception +
                '}';
    }

    public String getControllerIP() {
        return controllerConf.get("ip");
    }

    public int getControllerPort() {
        return Integer.parseInt(controllerConf.get("port"));
    }

    public enum SliceStatus {
        NEW,
        TIME_WAITING,
        RUNNABLE,
        RUNNING_WAITING,
        RUNNING,
        TERMINATED_WAITING,
        TERMINATABLE,
        TERMINATED
    }

    public enum SliceException {
        NONE,
        LDM_TASK_START_CALL_TIMEOUT, //vmThreadPool.awaitTermination(10, TimeUnit.MINUTES)
        LDM_TASK_STOP_CALL_TIMEOUT,//vmThreadPool.awaitTermination(10, TimeUnit.MINUTES)
        WRONG_TOPOLOGY_FORMAT, //parse topology error when start. // TODO: 4/22/16 it can be done when submit.
        USER_OPERATION // user force to stop a slice.
    }
}
