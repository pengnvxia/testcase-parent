package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.TestcaseConfigDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestcaseConfigDetailMapper {

    TestcaseConfigDetail selectByPrimaryKey(Integer id);

    List<TestcaseConfigDetail> selectByConfigId(Integer configId);

    void insert(@Param("testcaseConfigDetails") List<TestcaseConfigDetail> testcaseConfigDetails);

    Integer insertOne(TestcaseConfigDetail testcaseConfigDetail);

    void update(TestcaseConfigDetail testcaseConfigDetail);

    void deleteNotIn(@Param("ids") List<Integer> ids,Integer configId);

    void deleteByConfigId(Integer configId);

}
