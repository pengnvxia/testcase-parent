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
public class Project {
    private Integer id;

    private String projectName;

    private String devAddress;

    private String prodAddress;

    private String description;

    private Date createdAt;

    private Integer createdBy;

    private Date updatedAt;

    private Integer updatedBy;

    private String createdName;

    private String updatedName;

}
