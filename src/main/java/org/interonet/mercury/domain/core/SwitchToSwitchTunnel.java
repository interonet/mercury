package org.interonet.mercury.domain.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwitchToSwitchTunnel {
    private int switchId;
    private int switchIdPortNum;
    private int peerSwitchId;
    private int peerSwitchIdPortNum;

    public SwitchToSwitchTunnel() {
    }

    public SwitchToSwitchTunnel(int switchId, int switchIdPortNum, int peerSwitchId, int peerSwitchIdPortNum) {
        this.switchId = switchId;
        this.switchIdPortNum = switchIdPortNum;
        this.peerSwitchId = peerSwitchId;
        this.peerSwitchIdPortNum = peerSwitchIdPortNum;
    }

    public static List<SwitchToSwitchTunnel> getswswTunnel(Map<String, String> topology, Map<String, Integer> userSW2domSw, Map<String, Integer> userVM2domVM) {
        List<SwitchToSwitchTunnel> switchToSwitchTunnels = new ArrayList<>();

        for (Map.Entry<String, String> entry : topology.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            String userID = key.split(":")[0]; //"s0", "h0"
            String userPeerID = value.split(":")[0]; //"s0", "h0"

            if (userID.substring(0, 1).equals("h") || userPeerID.substring(0, 1).equals("h"))
                continue;

            //FIXME if userId is null, and the whole program will be crashed.
            int domIDint = userSW2domSw.get(userID);
            int domPeerIDint = userSW2domSw.get(userPeerID);

            int userSwitchPort = Integer.parseInt(key.split(":")[1]); //0
            int userPeerSwitchPort = Integer.parseInt(value.split(":")[1]); //1


            SwitchToSwitchTunnel tunnel = new SwitchToSwitchTunnel(domIDint, userSwitchPort, domPeerIDint, userPeerSwitchPort);
            switchToSwitchTunnels.add(tunnel);
        }
        return switchToSwitchTunnels;
    }

    @Override
    public String toString() {
        return "SwitchToSwitchTunnel{" +
                "switchId=" + switchId +
                ", switchIdPortNum=" + switchIdPortNum +
                ", peerSwitchId=" + peerSwitchId +
                ", peerSwitchIdPortNum=" + peerSwitchIdPortNum +
                '}';
    }

    public int getSwitchId() {
        return switchId;
    }

    public void setSwitchId(int switchId) {
        this.switchId = switchId;
    }

    public int getSwitchIdPortNum() {
        return switchIdPortNum;
    }

    public void setSwitchIdPortNum(int switchIdPortNum) {
        this.switchIdPortNum = switchIdPortNum;
    }

    public int getPeerSwitchId() {
        return peerSwitchId;
    }

    public void setPeerSwitchId(int peerSwitchId) {
        this.peerSwitchId = peerSwitchId;
    }

    public int getPeerSwitchIdPortNum() {
        return peerSwitchIdPortNum;
    }

    public void setPeerSwitchIdPortNum(int peerSwitchIdPortNum) {
        this.peerSwitchIdPortNum = peerSwitchIdPortNum;
    }
}
