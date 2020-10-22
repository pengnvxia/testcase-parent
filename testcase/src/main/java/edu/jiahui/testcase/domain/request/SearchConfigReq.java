package edu.jiahui.testcase.domain.request;

import lombok.Data;

@Data
public class SearchConfigReq {

    private String configName;

    private Integer projectId;

    private Integer envId;

    private String lastUpdatedBy;
}
