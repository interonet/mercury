package org.interonet.mercury.domain.ldm;

import org.interonet.mercury.domain.core.SwitchToSwitchTunnel;
import org.interonet.mercury.domain.core.SwitchToVMTunnel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LDMCalls {
    private final static Logger logger = LoggerFactory.getLogger(LDMCalls.class);

    public LDMCalls() {
    }

    public String createTunnelSW2SW(List<SwitchToSwitchTunnel> switchToSwitchTunnels) {
        logger.info("LDM --> createTunnelSW2SW(switchToSwitchTunnels=)" + switchToSwitchTunnels);
//        if (!DEBUG) {
//            ldmService.createSwitchToSwitchTunnel(switchToSwitchTunnels);
//        }
        return "Success";
    }

    public String createTunnelSW2VM(List<SwitchToVMTunnel> switchToVMTunnelList) {
        logger.info("LDM --> createTunnelSW2VM(switchToSwitchTunnels=)" + switchToVMTunnelList);
//        if (!DEBUG) {
//            ldmService.createSwitchToVMTunnel(switchToVMTunnelList);
//        }
        return "Success";
    }

    public String powerOnVM(List<Integer> vmIDList) throws Throwable {
        logger.info("LDM --> powerOnVM(vmIDList=" + vmIDList + ")");
//        if (!DEBUG) {
//            ldmService.powerOnVM(vmIDList);
//        }
        return "Success";
    }

    public String deleteTunnelSW2SW(List<SwitchToSwitchTunnel> switchToSwitchTunnels) {
        logger.info("LDM --> deleteTunnelSW2SW(switchToSwitchTunnels=" + switchToSwitchTunnels + ")");
//        if (!DEBUG) {
//            ldmService.deleteSwitchToSwitchTunnel(switchToSwitchTunnels);
//        }
        return "Success";
    }

    public String deleteTunnelSW2VM(List<SwitchToVMTunnel> switchToVMTunnelList) {
        logger.info("LDM --> deleteTunnelSW2VM(switchToVMTunnelList=" + switchToVMTunnelList + ")");
//        if (!DEBUG) {
//            ldmService.deleteSwitchToVMTunnel(switchToVMTunnelList);
//        }
        return "Success";
    }

    public String powerOffVM(List<Integer> vmIdList) throws Throwable {
        logger.info("LDM --> powerOffVM(vmIdList=" + vmIdList + ")");
//        if (!DEBUG) {
//            ldmService.powerOffVM(vmIdList);
//        }
        return "Success";
    }
}
