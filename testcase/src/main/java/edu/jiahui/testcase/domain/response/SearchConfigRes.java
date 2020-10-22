package edu.jiahui.testcase.domain.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SearchConfigRes {

    private Integer id;

    private String configName;

    private Integer projectId;

    private String projectName;

    private String updateBy;

    private Date updatedAt;

    private String description;

    private Integer envId;

}
