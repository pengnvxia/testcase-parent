package edu.jiahui.testcase.domain;

import lombok.Data;

import java.util.Date;


@Data
public class Testcase {
    private Integer id;

    private Integer projectId;

    private String testcaseName;

    private Integer envId;

    private String configIds;

    private String requestType;

    private String path;

    private String headers;

    private String description;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;


}
