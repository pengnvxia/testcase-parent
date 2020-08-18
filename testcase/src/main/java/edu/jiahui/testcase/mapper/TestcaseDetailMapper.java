package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.TestcaseDetail;

public interface TestcaseDetailMapper {
    TestcaseDetail selectByPrimaryKey(Integer id);
}