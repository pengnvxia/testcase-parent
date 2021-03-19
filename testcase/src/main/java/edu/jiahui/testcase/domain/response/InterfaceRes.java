package edu.jiahui.testcase.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterfaceRes {

    private List<InterfaceInfo> interfaceInfos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterfaceInfo{
        private Integer id;
        private String name;
        private String url;
        private String updatedBy;
        private Date updatedAt;
        List<TestcaseInfo> testcaseInfos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestcaseInfo{
        private Integer caseId;
        private String caseName;
        private Integer caseEnvId;
        private String caseUpdatedBy;
        private Date caseUpdatedAt;
    }

}
