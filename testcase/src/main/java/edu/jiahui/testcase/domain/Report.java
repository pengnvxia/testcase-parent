package edu.jiahui.testcase.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Report {
    private Integer id;

    private Integer testcaseId;

    private Integer groupId;

    private String createdBy;

    private String updatedBy;

    private Date createdAt;

    private Date updatedAt;

    private String content;
}
