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

    private String comparator;

    private String expectedValue;

    private Integer parentId;

    private Integer arrayIndex;

}
