package edu.jiahui.testcase.domain.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestcaseRes {

    private Integer id;

    private String testcaseName;

    private String path;

    private Integer envId;

    private String method;

    private List<Integer> configIds;

//    private String configIds;

    private String requestBody;

    private List<Variable> variables;

    private List<Parameter> parameters;

    private List<Setuphook> setuphooks;

    private List<ReqHeader> reqHeaders;

    private List<ReqParam> reqParams;

    private List<Response> responses;

    private Map reqBody;


    @Data
    public static class Variable {

        private Integer id;

        private String name;

        private String type;

        private String value;

        private Integer databaseId;
    }

    @Data
    public static class Parameter {

        private Integer id;

        private String keyName;

        private String value;
    }

    @Data
    public static class Setuphook {

        private Integer id;

        private String sql;

        private Integer databaseId;
    }

    @Data
    public static class ReqHeader {

        private Integer id;

        private String keyName;

        private String value;
    }

    @Data
    public static class ReqParam {

        private Integer id;

        private String keyName;

        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private Integer id;

        private Integer indexValue;

        private String name;

        private String type;

        private String comparator;

        private String expectedValue;

        private List<Response> children;
    }
}
