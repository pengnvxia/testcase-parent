package edu.jiahui.testcase.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class TestcaseReq {

    private String testcaseName;

    private Integer envId;

    private String configIds;

    private Object requestBody;

    private List<Variable> variables;

    private List<Parameter> parameters;

    private List<Setuphook> setuphooks;

    private List<ReqHeader> reqHeaders;

    private List<ReqParam> reqParams;

    private List<Response> responses;

    @Data
    public static class Variable {
        private String name;
        private String type;
        private String value;
        private Integer databaseId;
    }

    @Data
    public static class Parameter {
        private String keyName;
        private String value;
    }

    @Data
    public static class Setuphook {
        private String sql;
        private Integer databaseId;
    }

    @Data
    public static class ReqHeader {
        private String keyName;
        private String value;
    }

    @Data
    public static class ReqParam {
        private String keyName;
        private String value;
    }

    @Data
    public static class Response {
        private String name;
        private String type;
        private String comparator;
        private String expectedValue;
    }
}
