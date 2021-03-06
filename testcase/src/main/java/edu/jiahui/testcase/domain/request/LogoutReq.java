package edu.jiahui.testcase.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LogoutReq {

    @NotNull(message = "id不能为空")
    private Integer id;
}
