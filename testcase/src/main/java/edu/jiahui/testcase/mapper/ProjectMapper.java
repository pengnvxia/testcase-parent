package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.Project;
import java.util.List;

public interface ProjectMapper {
    Project selectByPrimaryKey(Integer id);
    List<Project> selectAll();
    void updateByPrimaryKey(Project project);
    void delete(Integer id);
    void insert(Project project);
    Integer projectNameExit(Integer id, String projectName);
}
