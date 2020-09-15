package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.TestcaseConfigDetail;
import java.util.List;

public interface TestcaseConfigDetailMapper {
    TestcaseConfigDetail selectByPrimaryKey(Integer id);
    List<TestcaseConfigDetail> selectByConfigId(Integer configId);
}
