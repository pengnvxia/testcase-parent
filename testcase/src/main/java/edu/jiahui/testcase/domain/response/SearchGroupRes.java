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
public class SearchGroupRes {

    private List<Group> groupList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Group {

        private Integer id;

        private String groupName;

        private Integer projectId;

        private String projectName;

        private Integer envId;

        private String updatedBy;

        private Date updatedAt;

        private String description;
    }
}
