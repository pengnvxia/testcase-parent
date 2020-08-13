package edu.jiahui.testcase.domain;

import lombok.Data;

import java.util.Date;


@Data
public class TestcaseConfig {

    private Integer id;
    private String configName;
    private Integer projectId;
    private Integer envId;
    private String desc;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

}
