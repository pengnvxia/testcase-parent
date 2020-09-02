package edu.jiahui.testcase.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Properties {
    private Integer id;

    private String scope;

    private String type;

    private Integer pos;

    private String name;

    private String rule;

    private Integer parentId;

    private Long priority;

    private Integer interfaceId;

    private Integer creatorId;

    private Integer moduleId;

    private Integer repositoryId;

    private Boolean required;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private String value;

    private String description;
}
