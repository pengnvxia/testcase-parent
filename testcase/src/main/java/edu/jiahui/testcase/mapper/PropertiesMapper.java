package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.PropertiesWithBLOBs;

public interface PropertiesMapper {
    PropertiesWithBLOBs selectByPrimaryKey(Integer id);
    void deleteByInterfaceId(Integer interfaceId);
}
