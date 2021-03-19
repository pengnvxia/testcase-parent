package edu.jiahui.testcase.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ConfigReq {

    @NotBlank(message = "configName")
    private String configName;

    @NotNull(message = "projectId")
    private Integer projectId;


    @NotNull(message = "envId")
    private Integer envId;

    private String description;

    private List<Variables> variablesList;

    private Integer createdBy;

    private Integer updatedBy;

    @Data
    public static class Variables{

        private Integer variableId;

        @NotBlank(message = "name")
        private String name;


        @NotBlank(message = "type")
        private String type;


        @NotBlank(message = "value")
        private String value;
    }
}
