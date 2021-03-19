package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.Testcase;
import edu.jiahui.testcase.domain.request.CaseListReq;
import edu.jiahui.testcase.domain.response.InterfaceRes;

import java.util.List;

public interface TestcaseMapper {
    Testcase selectByPrimaryKey(Integer id);
    List<InterfaceRes.TestcaseInfo> selectByInterfaceId(Integer interfaceId);
    Integer insert(Testcase testcase);
    void deleteByPrimaryKey(Integer id);
    void updateByPrimaryKey(Testcase testcase);
    List<Testcase> selectByIds(CaseListReq req);
}
