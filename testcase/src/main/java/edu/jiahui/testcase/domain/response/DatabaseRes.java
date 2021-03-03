package edu.jiahui.testcase.domain.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseRes {

    private Integer id;

    private String dbName;

    private String host;

    private Integer port;

    private String username;

    private String password;

    private Integer envId;

    private String createTableSql;

    private String insertSql;

    private String updatedBy;

    private Date updatedAt;
}
