package org.interonet.mercury.service;

import org.interonet.mercury.domain.ldm.LDMCalls;
import org.interonet.mercury.domain.ldm.task.LDMStartTask;
import org.interonet.mercury.domain.ldm.task.LDMStopTask;
import org.interonet.mercury.domain.ldm.task.LDMTaskReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class LDMService {
    Logger logger = LoggerFactory.getLogger(LDMService.class);

    @Autowired
    private LDMCalls ldmCalls;

    @Async
    public Future<LDMTaskReturn> submit(LDMStartTask ldmStartTask) {
        LDMTaskReturn ldmTaskReturn = new LDMTaskReturn(ldmStartTask.getSliceId());
        try {
            ldmCalls.createTunnelSW2SW(ldmStartTask.getSwitchToSwitchTunnels());
            ldmCalls.createTunnelSW2VM(ldmStartTask.getSwitchToVMTunnels());
            ldmCalls.powerOnVM(ldmStartTask.getHostIdList());
            ldmTaskReturn.setSuccess(true);

            /*
            *
            * Comment these lines for the fuck box to fix the power system.
            * by Samuel, Dec, 14
            *
            * */

            //for (Integer switchID : switchesIDs) {
            //    ldmService.powerOnSwitch(switchID);
            //}


            /*
            *
            * Comment these lines for the fuck onetswitch30's eth0 driver in u-boot-meshsr.
            * By Samuel, Dec, 14
            *
            * */

            //for (Map.Entry<String, Integer> entry : userSW2domSW.entrySet()) {
            //    Thread t = new Thread(() -> {
            //        try {
            //            String userSW = entry.getKey();//s0
            //            String userSWConf = slice.switchConf.get(userSW);
            //            Integer domSW = entry.getValue();//s5
            //            if (userSWConf == null || userSW == null) {
            //                logger.error("Error to map:" + "userSWConf=" + userSWConf + ", userSW=" + userSW);
            //                throw new Exception("Error to map");
            //            }
            //            switch (userSWConf) {
            //                case "OF1.0":
            //                    ldmService.addSwitchConf("OF1.0", domSW, slice.controllerIP, slice.controllerPort);
            //                    break;
            //                case "OF1.3":
            //                    ldmService.addSwitchConf("OF1.3", domSW, slice.controllerIP, slice.controllerPort);
            //                    break;
            //                default://custom
            //                    Map<String, String> customSwitchConf = slice.customSwitchConf.get(userSW);
            //                    if (customSwitchConf == null) {
            //                        logger.error("There should be #" + userSW + " , But this number is not in the custom switch conf.");
            //                        throw new Exception("Incomplete Custom Switch Configuration");
            //                    }
            //
            //                    /*
            //                    *   customSwitchConf should be like this.
            //                    *
            //                    *  {
            //                    *     "root-fs": "http://202.117.15.79/ons_bak/backup.tar.xz",
            //                    *     "system-bit": "http://202.117.15.79/ons_bak/system.bit",
            //                    *     "uImage": "http://202.117.15.79/ons_bak/uImage",
            //                    *     "device-tree": "http://202.117.15.79/ons_bak/devicetree.dtb"
            //                    *  }
            //                    *
            //                    * */
            //
            //                    String rootFsUrl = customSwitchConf.get("root-fs");
            //                    String systemBitUrl = customSwitchConf.get("system-bit");
            //                    String uImageUrl = customSwitchConf.get("uImage");
            //                    String deviceTreeUrl = customSwitchConf.get("device-tree");
            //
            //                    //Url Validation.
            //                    String urlRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
            //                    if (!rootFsUrl.matches(urlRegex) ||
            //                            !systemBitUrl.matches(urlRegex) ||
            //                            !uImageUrl.matches(uImageUrl) ||
            //                            !deviceTreeUrl.matches(urlRegex)) {
            //                        logger.error("rootFsUrl = " + rootFsUrl);
            //                        logger.error("systemBitUrl = " + systemBitUrl);
            //                        logger.error("uImageUrl = " + uImageUrl);
            //                        logger.error("deviceTreeUrl = " + deviceTreeUrl);
            //                        throw new Exception("Wrong URL");
            //                    }
            //
            //                    //RootFS
            //                    String rootFsFileName = System.currentTimeMillis() + "-rootfs.tar.xz";
            //                    String rootFsFilePath = "/var/www/html/SwitchConfStore/" + rootFsFileName;
            //                    FileUtils.copyURLToFile(new URL(rootFsUrl), new File(rootFsFilePath));
            //                    String rootFsUrlFilePath = "http://202.117.15.79/SwitchConfStore/" + rootFsFileName;
            //
            //                    //system-bit
            //                    String bootGenUtilWorkingDir = configurationCenter.getConf("BootgenUtilWorkingDir");
            //                    FileUtils.copyURLToFile(new URL(systemBitUrl), new File(bootGenUtilWorkingDir + "onetswitch_top.bit"));
            //                    //Generate actual boot-bin using bootgen utils.
            //                    Runtime.getRuntime().exec("rm boot.bin", null, new File(bootGenUtilWorkingDir)).waitFor();
            //                    Runtime.getRuntime().exec("bootgen -w -image output.bif -o boot.bin", null, new File(bootGenUtilWorkingDir)).waitFor();
            //                    if (!new File(bootGenUtilWorkingDir + "boot.bin").exists()) {
            //                        logger.error("bootgen build error");
            //                        Runtime.getRuntime().exec("rm boot.bin", null, new File(bootGenUtilWorkingDir)).waitFor();
            //                        throw new Exception("bootgen build error.");
            //                    }
            //                    String bootBinFileName = System.currentTimeMillis() + "-boot.bin";
            //                    String bootBinFilePath = "/var/www/html/SwitchConfStore/" + bootBinFileName;
            //                    FileUtils.copyFile(new File(bootGenUtilWorkingDir + "boot.bin"), new File(bootBinFilePath));
            //                    String bootBinUrlFilePath = "http://202.117.15.79/SwitchConfStore/" + bootBinFileName;
            //
            //                    //uImage
            //                    String uImageFileName = System.currentTimeMillis() + "-uImage";
            //                    String uImageFilePath = "/var/www/html/SwitchConfStore/" + uImageFileName;
            //                    FileUtils.copyURLToFile(new URL(uImageUrl), new File(uImageFilePath));
            //                    String uImageUrlFilePath = "http://202.117.15.79/SwitchConfStore/" + uImageFileName;
            //
            //                    //Device Tree
            //                    String deviceTreeFileName = System.currentTimeMillis() + "-devicetree.dtb";
            //                    String deviceTreeFilePath = "/var/www/html/SwitchConfStore/" + deviceTreeFileName;
            //                    FileUtils.copyURLToFile(new URL(deviceTreeUrl), new File(deviceTreeFilePath));
            //                    String deviceTreeUrlFilePath = "http://202.117.15.79/SwitchConfStore/" + deviceTreeFileName;
            //
            //                    Map<String, String> customSwitchConfGDM = new HashMap<>();
            //                    customSwitchConfGDM.put("root-fs", rootFsUrlFilePath);
            //                    customSwitchConfGDM.put("boot-bin", bootBinUrlFilePath);
            //                    customSwitchConfGDM.put("uImage", uImageUrlFilePath);
            //                    customSwitchConfGDM.put("device-tree", deviceTreeUrlFilePath);
            //
            //                    ldmService.addSwitchConf(customSwitchConfGDM, domSW, slice.controllerIP, slice.controllerPort);
            //
            //                    break;
            //            }
            //        } catch (Throwable e) {
            //            e.printStackTrace();
            //        }
            //    });
            //    t.start();
            //    threadsAddConf.submit(t);
            //}
            //
            //for (Thread thread : threadsAddConf) {
            //    thread.join();
            //}


        } catch (Throwable throwable) {
            ldmTaskReturn.setSuccess(false);
            ldmTaskReturn.setThrowable(throwable);
            logger.error("ldmService RPC Call Exception", throwable);
        }
        return new AsyncResult<>(ldmTaskReturn);
    }

    @Async
    public Future<LDMTaskReturn> submit(LDMStopTask ldmStopTask) {
        LDMTaskReturn ldmTaskReturn = new LDMTaskReturn(ldmStopTask.getSliceId());
        try {
            ldmCalls.deleteTunnelSW2SW(ldmStopTask.getSwitchToSwitchTunnels());
            ldmCalls.deleteTunnelSW2VM(ldmStopTask.getSwitchToVMTunnels());
            ldmCalls.powerOffVM(ldmStopTask.getHostIdList());
            ldmTaskReturn.setSuccess(true);
        } catch (Throwable throwable) {
            ldmTaskReturn.setSuccess(false);
            ldmTaskReturn.setThrowable(throwable);
            logger.error("ldmService RPC Call Exception", throwable);
        }

        return new AsyncResult<>(ldmTaskReturn);
    }
}
