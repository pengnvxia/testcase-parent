package edu.jiahui.testcase.domain.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTestcaseGroupReq {

    private Integer id;

    @NotBlank(message = "groupName")
    private String groupName;

    private String configIds;

    @NotBlank(message = "testcaseIds")
    private String testcaseIds;

    @NotNull(message = "envId")
    private Integer envId;

    private Integer projectId;

    private List<Variable> variables;

    private List<Parameter> parameters;

    private List<Setuphook> setuphooks;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Variable{

        private Integer id;

        @NotBlank(message = "name")
        private String name;

        @NotBlank(message = "type")
        private String type;

        @NotBlank(message = "value")
        private String value;

        private Integer databaseId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Parameter {

        private Integer id;

        @NotBlank(message = "name")
        private String name;

        @NotBlank(message = "value")
        private String value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Setuphook {

        private Integer id;

        @NotBlank(message = "sql")
        private String sql;

        @NotNull(message = "databaseId")
        private Integer databaseId;
    }

}
