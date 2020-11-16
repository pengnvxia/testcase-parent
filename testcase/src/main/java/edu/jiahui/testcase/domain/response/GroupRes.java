package edu.jiahui.testcase.domain.response;


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
public class GroupRes {

    private Integer id;

    private String groupName;

    private String configIds;

    private String testcaseIds;

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

        private String name;

        private String type;

        private String value;

        private Integer databaseId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Parameter {

        private Integer id;

        private String name;

        private String value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Setuphook {

        private Integer id;

        private String sql;

        private Integer databaseId;
    }

}

