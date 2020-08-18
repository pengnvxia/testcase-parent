package edu.jiahui.testcase.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Modules {
    private Integer id;

    private String name;

    private Long priority;

    private Integer creatorId;

    private Integer repositoryId;

    private Integer envId;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private String description;

}
