package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.TestcaseGroupDetail;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface TestcaseGroupDetailMapper {
    TestcaseGroupDetail selectByPrimaryKey(Integer id);
    void insert(@Param("testcaseGroupDetails")List<TestcaseGroupDetail> testcaseGroupDetails);
    Integer insertOne(TestcaseGroupDetail testcaseGroupDetail);
    void update(TestcaseGroupDetail testcaseGroupDetail);
    void deleteNotIn(@Param("ids") List<Integer> ids,Integer groupId);
    List<TestcaseGroupDetail> selectByGroupId(Integer groupId);
    void delete(Integer groupId);
}
