package edu.jiahui.testcase.domain.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SearchModuleReq {

    @NotNull(message = "repositoryId不能为空")
    private Integer repositoryId;

    @NotNull(message = "envId不能为空")
    private Integer envId;
}
