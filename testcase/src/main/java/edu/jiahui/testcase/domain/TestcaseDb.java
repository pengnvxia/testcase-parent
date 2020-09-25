package edu.jiahui.testcase.domain;

import lombok.Data;

import java.util.Date;

@Data
public class TestcaseDb {
    private Integer id;

    private Integer testcaseId;

    private String dbIds;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;
}
