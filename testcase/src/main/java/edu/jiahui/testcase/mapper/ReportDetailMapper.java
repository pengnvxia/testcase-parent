package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.ReportDetail;
import java.util.*;

public interface ReportDetailMapper {
    ReportDetail selectByPrimaryKey(Integer id);
    void insert(ReportDetail reportDetail);
    List<ReportDetail> selectByReportId(Integer reportId);
}
