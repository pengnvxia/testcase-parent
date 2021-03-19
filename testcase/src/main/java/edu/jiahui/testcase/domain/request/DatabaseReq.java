package edu.jiahui.testcase.domain.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseReq {

    private Integer id;

    @NotBlank(message = "dbName不能为空")
    private String dbName;

    @NotBlank(message = "host不能为空")
    private String host;

    @NotNull(message = "port不能为空")
    private Integer port;

    @NotBlank(message = "username不能为空")
    private String username;

    @NotBlank(message = "password不能为空")
    private String password;

    @NotNull(message = "envId不能为空")
    private Integer envId;

    private String createTableSql;

    private String insertSql;

    private String createdBy;

    private String updatedBy;
}
