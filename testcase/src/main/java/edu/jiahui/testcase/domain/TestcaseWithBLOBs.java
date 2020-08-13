package edu.jiahui.testcase.domain;

import lombok.Data;

@Data
public class TestcaseWithBLOBs extends Testcase {
    private String variables;

    private String sqlSentence;

    private String parameters;

    private String setupHooks;

    private String params;

    private String body;

    private String extractVariables;

    private String validate;

}
