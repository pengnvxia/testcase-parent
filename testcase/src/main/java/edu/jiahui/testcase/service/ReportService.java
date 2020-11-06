package edu.jiahui.testcase.service;


import com.alibaba.fastjson.JSON;
import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.testcase.domain.Report;
import edu.jiahui.testcase.domain.response.ReportRes;
import edu.jiahui.testcase.mapper.ReportMapper;
import org.springframework.stereotype.Service;
import edu.jiahui.testcase.constants.BaseConstans;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {


    @Resource
    private ReportMapper reportMapper;

    public List<ReportRes.Report> reportList(Integer id){
        List<Report> reportList = reportMapper.selectByTestcaseId(id);
        List<ReportRes.Report> res= new ArrayList<>();
        for(Report rp: reportList){
            ReportRes.Report report= ReportRes.Report.builder()
                    .id(rp.getId())
                    .createdAt(rp.getCreatedAt())
                    .createdBy(rp.getCreatedBy())
                    .result(rp.getResult())
                    .reportLink(rp.getReportLink())
                    .reportHtml(JSON.parseObject(rp.getContent()).get("reports").toString())
                    .build();
            res.add(report);
        }
        return res;
    }
}
