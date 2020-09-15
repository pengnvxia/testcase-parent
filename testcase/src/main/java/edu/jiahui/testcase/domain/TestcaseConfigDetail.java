package edu.jiahui.testcase.domain;

import lombok.Data;

import java.util.Date;

@Data
public class TestcaseConfigDetail {
    private Integer id;

    private String name;

    private String type;

    private Integer value;

    private Integer configId;

    private String createdBy;

    private String updatedBy;

    private Date createdAt;

    private Date updatedAt;

}
