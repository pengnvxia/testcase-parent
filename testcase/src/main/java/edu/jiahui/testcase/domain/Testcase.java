package edu.jiahui.testcase.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Testcase {
    private Integer id;

    private String testcaseName;

    private Integer envId;

    private String configIds;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

    private Integer testcaseGroupId;

    private Integer interfaceId;

    private String method;

    private String url;
}
