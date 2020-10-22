package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.TestcaseConfig;


import java.util.List;

public interface TestcaseConfigMapper {

    List<TestcaseConfig> select();

    TestcaseConfig selectByPrimaryKey(Integer id);

    List<TestcaseConfig> selectByCondition(TestcaseConfig testcaseConfig);

    Integer insert(TestcaseConfig testcaseConfig);

    Integer selectByConfigName(String configName,Integer id);

    void update(TestcaseConfig testcaseConfig);

    void delete(Integer id);

}
