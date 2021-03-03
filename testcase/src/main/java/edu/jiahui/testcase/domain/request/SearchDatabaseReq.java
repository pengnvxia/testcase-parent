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
public class SearchDatabaseReq extends BaseBo {

    private Integer envId;

    private String dbName;
}
