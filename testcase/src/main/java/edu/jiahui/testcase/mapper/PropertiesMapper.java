package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.Properties;

import java.util.List;

public interface PropertiesMapper {
    Properties selectByPrimaryKey(Integer id);
    void deleteByInterfaceId(Integer interfaceId);
    List<Properties> selectByInterfaceId(Integer interfaceId);
}
