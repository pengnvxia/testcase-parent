package edu.jiahui.testcase.domain.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConfigRes {

    private String configName;

    private Integer projectId;

    private Integer envId;

    private String description;

    private List<Variables> variablesList;


    @Data
    @Builder
    public static class Variables{

        private Integer variableId;

        private String name;

        private String type;

        private String value;
    }


}
