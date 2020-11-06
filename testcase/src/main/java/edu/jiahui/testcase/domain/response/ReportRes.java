package edu.jiahui.testcase.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRes {

    private List<Report> reportList;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Report{

        private Integer id;

        private Date createdAt;

        private String createdBy;

        private Integer result;

        private String reportLink;

        private String reportHtml;
    }
}
