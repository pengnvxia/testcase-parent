package edu.jiahui.testcase.domain.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ProjectReq {

    private Integer id;

    @NotNull(message = "projectName不能为空")
    private String projectName;

    private String devAddress;

    private String prodAddress;

    private String description;

    private Date createdAt;

    private String createdBy;

    private Date updatedAt;

    private String updatedBy;
}
