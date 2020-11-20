package edu.jiahui.testcase.domain.request;


import edu.jiahui.testcase.model.bo.BaseBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchGroupReq extends BaseBo {

    private String groupName;

    private String projectName;

    private Integer envId;

    private String lastUpdatedBy;

}
