package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.TestcaseWithBLOBs;

public interface TestcaseMapper {
    TestcaseWithBLOBs selectByPrimaryKey(Integer id);
    void insert(TestcaseWithBLOBs testcase);
}
