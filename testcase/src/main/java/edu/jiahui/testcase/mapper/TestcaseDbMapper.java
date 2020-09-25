package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.TestcaseDb;

public interface TestcaseDbMapper {
    TestcaseDb selectByPrimaryKey(Integer id);
    String selectByTestcaseId(Integer testcaseId);
}
