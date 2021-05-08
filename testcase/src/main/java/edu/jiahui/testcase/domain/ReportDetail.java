package edu.jiahui.testcase.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDetail {

    private Integer id;

    private Integer reportId;

    private String result;

    private String url;

    private String method;

    private String statusCode;

    private String requestHeaders;

    private String requestBody;

    private String response;

    private String createdBy;

    private String validators;

    private String updatedBy;

    private Date createdAt;

    private Date updatedAt;

    private String exception;

    private String attachment;

}
