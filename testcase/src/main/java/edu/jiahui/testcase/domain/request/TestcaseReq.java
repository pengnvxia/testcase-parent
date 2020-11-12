package edu.jiahui.testcase.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class TestcaseReq {

    @NotBlank(message = "testcaseName不能为空")
    private String testcaseName;

    @NotBlank(message = "path不能为空")
    private String path;

    @NotNull(message = "envId不能为空")
    private Integer envId;

    private String method;

    private List<Integer> configIds;

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

        @NotBlank(message = "variable name不能为空")
        private String name;

        @NotBlank(message = "variable type不能为空")
        private String type;

        @NotBlank(message = "variable value不能为空")
        private String value;

        private Integer databaseId;
    }

    @Data
    public static class Parameter {

        private Integer id;

        @NotBlank(message = "parameter keyName不能为空")
        private String keyName;

        @NotBlank(message = "parameter value不能为空")
        private String value;
    }

    @Data
    public static class Setuphook {

        private Integer id;

        @NotBlank(message = "setuphook sql不能为空")
        private String sql;

        @NotNull(message = "setuphook databaseId不能为空")
        private Integer databaseId;
    }

    @Data
    public static class ReqHeader {

        private Integer id;

        @NotBlank(message = "reqHeader keyName不能为空")
        private String keyName;

        @NotBlank(message = "reqHeader value不能为空")
        private String value;
    }

    @Data
    public static class ReqParam {

        private Integer id;

        @NotBlank(message = "reqParam keyName不能为空")
        private String keyName;

        @NotBlank(message = "reqParam value不能为空")
        private String value;
    }

    @Data
    public static class Response {

        private Integer id;

        private Integer indexValue;

        @NotBlank(message = "response name不能为空")
        private String name;

        @NotBlank(message = "response type不能为空")
        private String type;

        @NotBlank(message = "response comparator不能为空")
        private String comparator;

        @NotBlank(message = "response expectedValue不能为空")
        private String expectedValue;

        private List<Response> children;
    }
}
