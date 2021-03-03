package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.DatabaseWithBLOBs;
import edu.jiahui.testcase.domain.request.DatabaseReq;
import edu.jiahui.testcase.domain.request.SearchDatabaseReq;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DatabaseMapper {
    DatabaseWithBLOBs selectByPrimaryKey(Integer id);
    List<DatabaseWithBLOBs> selectByIds(@Param("ids") List<Integer> ids);
    void insert(DatabaseReq req);
    Integer selectByDbnameEnvid(DatabaseReq req);
    Integer selectByDbnameEnvidAndId(DatabaseReq req);
    void update(DatabaseReq req);
    List<DatabaseWithBLOBs> select(SearchDatabaseReq req);
    void delete(Integer id);
}
