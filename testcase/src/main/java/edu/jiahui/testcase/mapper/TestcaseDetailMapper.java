package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.TestcaseDetail;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface TestcaseDetailMapper {
    TestcaseDetail selectByPrimaryKey(Integer id);
    void insert(@Param("list") List<TestcaseDetail> list);
    TestcaseDetail selectByInterfaceId(Integer interfaceId);
    void deleteByTestcaseId(Integer testcaseId);
    List<TestcaseDetail> selectByTestcaseId(Integer testcaseId);
    void updateByPrimaryKey(TestcaseDetail testcaseDetail);
    void insertOne(TestcaseDetail testcaseDetail);
    void deleteNotIn(@Param("ids") List<Integer> ids,Integer testcaseId,Integer parentId,String scope);
    List<TestcaseDetail> selectByParentId(Integer parentId);
}
