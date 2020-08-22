package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.Testcase;

import java.util.List;

public interface TestcaseMapper {
    Testcase selectByPrimaryKey(Integer id);
    List<Testcase> selectByInterfaceId(Integer interfaceId);
    void insert(Testcase testcase);
}
