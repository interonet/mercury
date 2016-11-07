package org.interonet.mercury.service;

import org.interonet.mercury.config.MercuryTokenYamlConfig;
import org.interonet.mercury.domain.core.Switch;
import org.interonet.mercury.domain.core.SwitchMapper;
import org.interonet.mercury.domain.core.VirtualMachine;
import org.interonet.mercury.domain.core.VmMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);

    @Autowired
    MercuryTokenYamlConfig mercuryTokenYamlConfig;
    @Autowired
    private VmMapper vmMapper;
    @Autowired
    private SwitchMapper switchMapper;

    public List<Switch> getSwitchList(List<Integer> idList) {
        StringBuilder idListStr = new StringBuilder();
        for (int id : idList) {
            idListStr.append(id).append(",");
        }

        List<Switch> ret = switchMapper.selectByIdList(idListStr.toString());
        if (ret == null) return new ArrayList<>();
        return ret;
    }

    public List<VirtualMachine> getVMList(List<Integer> idList) {
        StringBuilder idListStr = new StringBuilder();
        for (int id : idList) {
            idListStr.append(id).append(",");
        }

        List<VirtualMachine> ret = vmMapper.selectByIdList(idListStr.toString());
        if (ret == null) return new ArrayList<>();
        return ret;
    }

    public Switch getSwitchById(Integer switchId) throws Exception {
        return switchMapper.selectById(switchId);
    }

    public VirtualMachine getVMById(Integer vmId) throws Exception {
        return vmMapper.selectById(vmId);
    }

    public Long getTokenExpirePeriod() {
        return mercuryTokenYamlConfig.getExpireMins();
    }

    public Integer getSwitchNumber() {
        Integer switchNumber = switchMapper.countAllSwitch();
        if (switchNumber == null) return 0;
        return switchNumber;
    }

    public Integer getVmNumber() {
        Integer vmNumber = vmMapper.countAllVm();
        if (vmNumber == null) return 0;
        return vmNumber;
    }
}
