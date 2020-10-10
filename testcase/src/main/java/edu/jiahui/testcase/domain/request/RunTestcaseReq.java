package edu.jiahui.testcase.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class RunTestcaseReq {

    private Integer envId;

    private Integer projectId;

    private List<Integer> testcaseIds;

    private Boolean flag;
}
