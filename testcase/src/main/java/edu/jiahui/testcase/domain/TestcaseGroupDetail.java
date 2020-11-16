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
public class TestcaseGroupDetail {

    private Integer id;

    private Integer groupId;

    private String scope;

    private String type;

    private String name;

    private String value;

    private Integer databaseId;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

}
