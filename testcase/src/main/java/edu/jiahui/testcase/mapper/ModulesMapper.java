package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.Modules;

import java.util.List;

public interface ModulesMapper {
    Modules selectByPrimaryKey(Integer id);
    Integer moduleNameExit(String name, Integer id);
    void insert(Modules modules);
    List<Modules> selectModules(Integer envId, Integer repositoryId);
}
