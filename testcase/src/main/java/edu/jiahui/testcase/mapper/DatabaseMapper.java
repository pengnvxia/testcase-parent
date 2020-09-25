package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.DatabaseWithBLOBs;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DatabaseMapper {
    DatabaseWithBLOBs selectByPrimaryKey(Integer id);
    List<DatabaseWithBLOBs> selectByIds(@Param("ids") List<Integer> ids);
}
