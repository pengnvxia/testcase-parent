package edu.jiahui.testcase.domain.request;

import edu.jiahui.testcase.model.bo.BaseBo;
import lombok.Data;

@Data
public class SearchConfigReq extends BaseBo {

    private String configName;

    private String projectName;

    private Integer envId;

    private String lastUpdatedBy;
}
