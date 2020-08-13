package edu.jiahui.testcase.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Users {
    private Integer id;

    private String fullname;

    private String password;

    private String email;

    private String token;

    private Date deletedAt;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

}
