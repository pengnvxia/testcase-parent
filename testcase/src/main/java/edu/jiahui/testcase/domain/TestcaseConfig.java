package edu.jiahui.testcase.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestcaseConfig {

    private Integer id;

    private String configName;

    private Integer projectId;

    private Integer envId;

    private String description;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

}
