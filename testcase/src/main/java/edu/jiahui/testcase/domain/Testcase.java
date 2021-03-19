package edu.jiahui.testcase.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Testcase {
    private Integer id;

    private String testcaseName;

    private String url;

    private Integer envId;

    private String configIds;

    private Date createdAt;

    private Date updatedAt;

    private Integer createdBy;

    private Integer updatedBy;

    private Integer testcaseGroupId;

    private Integer interfaceId;

    private String method;

}
