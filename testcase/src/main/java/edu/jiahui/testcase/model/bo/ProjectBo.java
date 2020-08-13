package edu.jiahui.testcase.model.bo;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectBo {
    private Integer id;

    private String projectName;

    private String devAddress;

    private String prodAddress;

    private String description;

    private Date updatedAt;

    private String updatedBy;
}
