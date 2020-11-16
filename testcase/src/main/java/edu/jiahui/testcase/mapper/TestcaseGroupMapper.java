package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.TestcaseGroup;
import edu.jiahui.testcase.domain.request.SearchGroupReq;
import edu.jiahui.testcase.domain.response.SearchGroupRes;
import java.util.List;


public interface TestcaseGroupMapper {
    TestcaseGroup selectByPrimaryKey(Integer id);
    List<SearchGroupRes.Group> select(SearchGroupReq req);
    Integer exitName(String groupName,Integer id);
    Integer insert(TestcaseGroup testcaseGroup);
    void update(TestcaseGroup testcaseGroup);
    void delete(Integer id);
}
