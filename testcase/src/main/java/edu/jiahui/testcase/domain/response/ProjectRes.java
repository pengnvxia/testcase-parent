package edu.jiahui.testcase.domain.response;

import lombok.Data;



@Data
public class ProjectRes {

    private Integer id;

    private String projectName;

    private String devAddress;

    private String prodAddress;

    private String description;

}
