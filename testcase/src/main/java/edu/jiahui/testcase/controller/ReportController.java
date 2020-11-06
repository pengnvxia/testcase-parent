package edu.jiahui.testcase.controller;

import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.domain.response.ReportRes;
import edu.jiahui.testcase.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping(method = RequestMethod.GET,value = "/list/{id}")
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

}
