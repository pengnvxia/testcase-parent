package edu.jiahui.testcase.controller;

import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.model.bo.DbBo;
import edu.jiahui.testcase.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/db")
public class DbController {

    @Autowired
    private DbService dbservice;

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultCode<List<DbBo>> getDb(HttpServletResponse response) {
//        解决跨域问题
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Cache-Control","no-cache");
//        解决跨域问题
        return ResultCode.getSuccessReturn(null, null, dbservice.list());
    }

}

