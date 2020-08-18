package edu.jiahui.testcase.domain.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class InterfaceRes {

    private List<InterfaceInfo> interfaceInfos;

    @Data
    public static class InterfaceInfo{
        private Integer id;
        private String name;
        private String url;
        private String updatedBy;
        private Date updatedAt;
        List<TestcaseInfo> testcaseInfos;
    }

    @Data
    public static class TestcaseInfo{
        private Integer caseId;
        private String caseName;
        private Integer caseEnvId;
        private String caseUpdatedBy;
        private Date caseUpdatedAt;
    }

}
