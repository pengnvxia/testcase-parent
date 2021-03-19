package edu.jiahui.testcase.domain.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddInterfaceInfoReq {

    @NotBlank(message = "interfaceName")
    private String interfaceName;

    @NotBlank(message = "interfaceAddress")
    private String interfaceAddress;

    @NotBlank(message = "interfaceMethod")
    private String interfaceMethod;

    private String description;

    @NotNull(message = "projectId")
    private Integer projectId;

    @NotNull(message = "moduleId")
    private Integer moduleId;

    private Integer envId;

}
