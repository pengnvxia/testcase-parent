package edu.jiahui.testcase.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class UserReq {

    @NotNull(message = "fullname不能为空")
    private String fullname;

    @NotBlank(message = "password不能为空")
    private String password;

    private String email;

    private String token;


}
