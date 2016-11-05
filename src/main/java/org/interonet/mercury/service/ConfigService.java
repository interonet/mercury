package org.interonet.mercury.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.interonet.mercury.domain.core.Switch;
import org.interonet.mercury.domain.core.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigService {
    private Logger logger = LoggerFactory.getLogger(ConfigService.class);

    private Map<String, String> globalConfiguration = new HashMap<>();
    private Map<Integer, VirtualMachine> vmDB = new HashMap<>();

    private Map<Integer, Switch> switchDB = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public ConfigService() {
//        if (System.getenv().get("INTERONET_HOME") == null) {
//            System.out.println("INTERONET_HOME Environment Variable Check Error.");
//            System.exit(1);
//        }
//        initGlobalConf();
//        initSwitchDB();
//        initVMDB();
    }

    private void initGlobalConf() {
//        logger.info("reading conf file from " + System.getenv().get("INTERONET_HOME") + "/mercury/conf/conf.json");
//        File confFile = new File(System.getenv().get("INTERONET_HOME") + "/mercury/conf/conf.json");
//        try {
//            Map<String, String> map = objectMapper.readValue(confFile, Map.class);
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                globalConfiguration.put(entry.getKey(), entry.getValue());
//            }
//        } catch (Exception e) {
//            logger.error("initSwitchDB Error", e);
//        }
    }

    private void initSwitchDB() {
//        logger.info("reading swDB file from " + globalConfiguration.get("switchDB"));
//        File switchDBFile = new File(globalConfiguration.get("switchDB"));
//        try {
//            Map<String, Map<String, String>> switches = objectMapper.readValue(switchDBFile, Map.class);
//            for (Map.Entry<String, Map<String, String>> entry : switches.entrySet()) {
//                Integer id = Integer.parseInt(entry.getKey());
//                Map<String, String> s = entry.getValue();
//                String name = s.get("name");
//                String url = s.get("url");
//                switchDB.put(id, new Switch(id, name, url));
//            }
//        } catch (Exception e) {
//            logger.error("initSwitchDB Error", e);
//        }
    }

    private void initVMDB() {
//        logger.info("reading vmDB file from " + globalConfiguration.get("vmDB"));
//        File vmDBFile = new File(globalConfiguration.get("vmDB"));
//
//        try {
//            Map<String, Map<String, String>> vms = objectMapper.readValue(vmDBFile, Map.class);
//            for (Map.Entry<String, Map<String, String>> entry : vms.entrySet()) {
//                Integer id = Integer.parseInt(entry.getKey());
//                Map<String, String> s = entry.getValue();
//                String name = s.get("name");
//                String url = s.get("url");
//                vmDB.put(id, new VirtualMachine(id, name, url));
//            }
//        } catch (Exception e) {
//            logger.error("initSwitchDB Error", e);
//        }
    }

    public String getConf(String key) {
        return globalConfiguration.get(key);
    }

    public List<Switch> getSwitchList(List<Integer> idList) {
        //TODO
        return null;
    }

    public List<VirtualMachine> getVMList(List<Integer> idList) {
        //TODO
        return null;
    }

    public Switch getSwitchById(Integer domSwitchId) throws Exception {
//        if (domSwitchId == null) return null;
//        return switchDB.get(domSwitchId);
        return null;
    }

    public VirtualMachine getVMById(Integer domVMId) throws Exception {
//        if (domVMId == null) return null;
//        return vmDB.get(domVMId);
        return null;
    }

    public Long getTokenExpirePeriod() {
        //Minus
        return (long) 20;
    }
}
