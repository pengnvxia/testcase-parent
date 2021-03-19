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
public class SearchConfigRes {

    private List<Configs> configsList;

    private Long total;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Configs{

        private Integer id;

        private String configName;

        private Integer projectId;

        private String projectName;

        private String updatedBy;

        private Date updatedAt;

        private String description;

        private Integer envId;

    }



}
