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
public class TestcaseConfigDetail {

    private Integer id;

    private String name;

    private String type;

    private String value;

    private Integer configId;

    private String createdBy;

    private String updatedBy;

    private Date createdAt;

    private Date updatedAt;

}
