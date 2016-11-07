package org.interonet.mercury.domain.core;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SwitchMapper {
    @Select("SELECT * FROM switch")
    Switch selectAll();

    @Select("SELECT COUNT(id) FROM switch")
    Integer countAllSwitch();

    @Select("select distinct * from switch where FIND_IN_SET( switch.id, #{idListStr}) <> 0")
    List<Switch> selectByIdList(@Param("idListStr") String idListStr);

    @Select("SELECT * FROM switch where id=#{switchId}")
    Switch selectById(@Param("switchId") Integer switchId);
}
