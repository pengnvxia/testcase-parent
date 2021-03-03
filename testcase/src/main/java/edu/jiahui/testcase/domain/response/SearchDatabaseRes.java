package edu.jiahui.testcase.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDatabaseRes {

    private List<Databases> DatabaseList;

    private Long total;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Databases{

        private Integer id;

        private String dbName;

        private String host;

        private Integer port;

        private String username;

        private Integer envId;

        private String updatedBy;

        private Date updatedAt;
    }
}
