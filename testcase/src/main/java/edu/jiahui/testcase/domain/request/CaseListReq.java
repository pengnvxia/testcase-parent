package edu.jiahui.testcase.domain.request;

import lombok.Data;

@Data
public class CaseListReq {

    private Integer projectId;

    private Integer moduleId;

    private Integer envId;

}
