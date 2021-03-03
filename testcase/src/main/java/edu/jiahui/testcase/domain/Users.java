package edu.jiahui.testcase.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String token;

    private Date deletedAt;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

}
