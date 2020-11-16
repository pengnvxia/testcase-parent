package edu.jiahui.testcase.domain.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchGroupReq {

    private String groupName;

    private String projectName;

    private Integer envId;

    private String lastUpdatedBy;

}
