package edu.jiahui.testcase.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interfaces {
    private Integer id;

    private String name;

    private String url;

    private String method;

    private Long priority;

    private Integer status;

    private Integer creatorId;

    private Integer lockerId;

    private Integer moduleId;

    private Integer repositoryId;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private Integer envId;

    private String description;

    private String updatedBy;

    private String createdBy;

}
