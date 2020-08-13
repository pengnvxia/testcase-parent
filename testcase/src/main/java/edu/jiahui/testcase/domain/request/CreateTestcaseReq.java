package edu.jiahui.testcase.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class CreateTestcaseReq {

    @NotNull(message = "projectId不能为空")
    private Integer projectId;

    @NotBlank(message = "testcaseName")
    private String testcaseName;

    @NotNull(message = "envId")
    private Integer envId;

    private List<Integer> configIds;

    private Map<String,Object> variables;

    private List<Map<String,Object>> sqlSentence;

    private List<Map<String,Object>> parameters;

    private List<Map<String,Object>> setupHooks;

    @NotBlank(message = "requestType")
    private String requestType;

    @NotBlank(message = "path")
    private String path;

    private Map<String,Object> headers;

    private Map<String,Object> params;

    private Map<String,Object> body;

    private Map<String,Object> extractVariables;

    private List<Map<String,Object>> validate;

    private String description;


}
