package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.Report;

import java.util.List;

public interface ReportMapper {
    Report selectByPrimaryKey(Integer id);
    List<Report> selectByTestcaseId(Integer id);
    void insert(Report report);
}
