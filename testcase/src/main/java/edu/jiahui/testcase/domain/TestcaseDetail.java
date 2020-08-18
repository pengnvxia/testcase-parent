package edu.jiahui.testcase.domain;

import lombok.Data;

import java.util.Date;

@Data
public class TestcaseDetail {
    private Integer id;

    private String scope;

    private String type;

    private String name;

    private Integer propertiesId;

    private String value;

    private Integer databaseId;

    private Integer testcaseId;

    private Date createdAt;

    private Date updatedAt;

    private Integer createdBy;

    private Integer updatedBy;

}
