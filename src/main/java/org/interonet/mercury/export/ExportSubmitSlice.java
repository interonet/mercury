package org.interonet.mercury.export;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class ExportSubmitSlice {
    private Integer switchesNum;
    private Integer vmsNum;
    private ZonedDateTime beginTime;
    private ZonedDateTime endTime;
    private Map<String, String> topology;
    private Map<String, String> controllerConf;
    private Map<String, String> switchConf;
    private Map<String, Map> customSwitchConf;

    public ExportSubmitSlice() {
        switchesNum = 1;
        vmsNum = 2;
        beginTime = ZonedDateTime.now().plusSeconds(30);
        endTime = ZonedDateTime.now().plusSeconds(60);

        topology = new HashMap<>();
        topology.put("h0:0", "s0:0");
        topology.put("h1:0", "s0:1");

        switchConf = new HashMap<>();
        switchConf.put("s0", "custom");

        controllerConf = new HashMap<>();
        controllerConf.put("ip", "202.117.15.79");
        controllerConf.put("port", "6633");

        customSwitchConf = new HashMap<>();

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

    public Map<String, String> getControllerConf() {
        return controllerConf;
    }

    public void setControllerConf(Map<String, String> controllerConf) {
        this.controllerConf = controllerConf;
    }
}
