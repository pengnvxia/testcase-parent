package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.TestcaseConfig;
import edu.jiahui.testcase.domain.request.ConfigListReq;
import edu.jiahui.testcase.domain.request.SearchConfigReq;
import edu.jiahui.testcase.domain.response.SearchConfigRes;


import java.util.List;

public interface TestcaseConfigMapper {

    List<TestcaseConfig> select();

    TestcaseConfig selectByPrimaryKey(Integer id);

    List<TestcaseConfig> selectByCondition(SearchConfigReq req);

    List<TestcaseConfig> selectByIds(ConfigListReq req);

    Integer insert(TestcaseConfig testcaseConfig);

    Integer selectByConfigName(String configName,Integer id);

    void update(TestcaseConfig testcaseConfig);

    void delete(Integer id);

}
