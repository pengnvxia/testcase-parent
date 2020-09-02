package edu.jiahui.testcase.domain.response;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TestcaseRes {

    private Integer id;

    private String testcaseName;

    private Integer envId;

    private String configIds;

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
    public static class Response {

        private Integer id;

        private String name;

        private String type;

        private String comparator;

        private String expectedValue;
    }
}
