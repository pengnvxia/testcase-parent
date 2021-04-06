package edu.jiahui.testcase.controller;

import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.domain.response.ReportRes;
import edu.jiahui.testcase.service.ReportService;
import feign.QueryMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping(method = RequestMethod.GET,value = "/list/{id}")
    // @RequestParam("group") Integer group
    public ResultCode list(@PathVariable("id")Integer id){
        List<ReportRes.Report> res= new ArrayList<>();
        try{
            res=reportService.reportList(id);
        }catch (Exception e){
            log.error("查询报告列表异常:{}",e.getMessage());
            return ResultCode.getFailure(null,e.getMessage());
        }
        return ResultCode.getSuccessReturn(null,null,res);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/group/list/{id}")
    public ResultCode groupList(@PathVariable("id")Integer id){
        List<ReportRes.Report> res= new ArrayList<>();
        try{
            res=reportService.groupReportList(id);
        }catch (Exception e){
            log.error("查询报告列表异常:{}",e.getMessage());
            return ResultCode.getFailure(null,e.getMessage());
        }
        return ResultCode.getSuccessReturn(null,null,res);
    }
}
