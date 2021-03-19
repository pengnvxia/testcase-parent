package edu.jiahui.testcase.domain.request;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ModuleReq {

    private Integer id;

    @NotNull(message = "name不能为空")
    private String name;

    private Long priority;

    @NotNull(message = "repositoryId不能为空")
    private Integer repositoryId;

    @NotNull(message = "envId不能为空")
    private Integer envId;

    private String description;

    private Integer createdBy;

    private Integer updatedBy;
}
