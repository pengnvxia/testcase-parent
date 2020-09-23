package edu.jiahui.testcase.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class Database {
    private Integer id;

    private String dbName;

    private String host;

    private Integer port;

    private String username;

    private String password;

    private Integer envId;

    private String createdBy;

    private String updatedBy;

    private Date createdAt;

    private Date updatedAt;

}
