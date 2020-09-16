package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.Report;

public interface ReportMapper {
    Report selectByPrimaryKey(Integer id);
    void insert(Report report);
}
