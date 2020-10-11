package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.Interfaces;
import java.util.List;

public interface InterfacesMapper {
    Interfaces selectByPrimaryKey(Integer id);
    List<Interfaces> selectById(Integer envId,Integer repositoryId,Integer moduleId);
    void deleteByPrimaryKey(Integer id);
    void insert(Interfaces interfaces);
}
