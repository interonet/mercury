package org.interonet.mercury.domain.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwitchToVMTunnel {
    private int switchId;
    private int switchPort;
    private int vmId;
    private int vmPort;

    public SwitchToVMTunnel() {
    }

    public SwitchToVMTunnel(int switchId, int switchPort, int vmId, int vmPort) {
        this.switchId = switchId;
        this.switchPort = switchPort;
        this.vmId = vmId;
        this.vmPort = vmPort;
    }

    public static List<SwitchToVMTunnel> getswvmTunnel(Map<String, String> topology, Map<String, Integer> userSW2domSW, Map<String, Integer> userVM2domVM) {
        List<SwitchToVMTunnel> switchToVMTunnels = new ArrayList<>();

        for (Map.Entry<String, String> entry : topology.entrySet()) {
            // entry : h0:1---> s1:0
            // entry : s0:1---> h1:0

            String key = entry.getKey();
            String value = entry.getValue();

            String userID = key.split(":")[0]; //"s0", "h0"
            String userPeerID = value.split(":")[0]; //"s0", "h0"

            // Ignore Switch to Switch Link
            if (userID.substring(0, 1).equals("s") && userPeerID.substring(0, 1).equals("s"))
                continue;

            String userVmId;
            String vmPort;
            String userSwitchId;
            String switchPort;
            if (key.substring(0, 1).equals("h")) {
                // key = "h0:1"
                userVmId = key.split(":")[0];
                vmPort = key.split(":")[1];
                //value = "s1:0"
                userSwitchId = value.split(":")[0];
                switchPort = value.split(":")[1];
            } else {
                //key = "s0:1"
                userSwitchId = key.split(":")[0];
                switchPort = key.split(":")[1];
                //value = "s1:0"
                userVmId = value.split(":")[0];
                vmPort = value.split(":")[1];
            }
            // Find the true Id.
            try {
                int domVmId = userVM2domVM.get(userVmId);
                int domVmPort = Integer.parseInt(vmPort);
                int domSwitchId = userSW2domSW.get(userSwitchId);
                int domSwitchPort = Integer.parseInt(switchPort);
                SwitchToVMTunnel tunnel = new SwitchToVMTunnel(domSwitchId, domSwitchPort, domVmId, domVmPort);
                switchToVMTunnels.add(tunnel);

            } catch (NullPointerException e) {
                return switchToVMTunnels;
            }
        }
        return switchToVMTunnels;
    }

    @Override
    public String toString() {
        return "SwitchToVMTunnel{" +
                "switchId=" + switchId +
                ", switchPort=" + switchPort +
                ", vmId=" + vmId +
                ", vmPort=" + vmPort +
                '}';
    }

    public int getSwitchId() {
        return switchId;
    }

    public void setSwitchId(int switchId) {
        this.switchId = switchId;
    }

    public int getSwitchPort() {
        return switchPort;
    }

    public void setSwitchPort(int switchPort) {
        this.switchPort = switchPort;
    }

    public int getVmId() {
        return vmId;
    }

    public void setVmId(int vmId) {
        this.vmId = vmId;
    }

    public int getVmPort() {
        return vmPort;
    }

    public void setVmPort(int vmPort) {
        this.vmPort = vmPort;
    }
}
