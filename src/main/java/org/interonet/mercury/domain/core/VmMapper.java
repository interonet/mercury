package org.interonet.mercury.domain.core;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VmMapper {
    @Select("SELECT * FROM vm")
    VirtualMachine selectAll();

    @Select("SELECT COUNT(id) FROM vm")
    Integer countAllVm();

    @Select("select distinct * from vm where FIND_IN_SET( vm.id, #{idListStr}) <> 0")
    List<VirtualMachine> selectByIdList(@Param("idListStr") String idListStr);

    @Select("SELECT * FROM vm where id=#{vmId}")
    VirtualMachine selectById(@Param("vmId") Integer vmId);
}
