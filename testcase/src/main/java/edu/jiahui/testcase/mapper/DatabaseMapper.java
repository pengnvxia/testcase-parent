package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.DatabaseWithBLOBs;

public interface DatabaseMapper {
    DatabaseWithBLOBs selectByPrimaryKey(Integer id);
}