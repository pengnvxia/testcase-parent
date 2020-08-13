package edu.jiahui.testcase.controller;


import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.model.bo.TestcaseConfigBo;
import edu.jiahui.testcase.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultCode<List<TestcaseConfigBo>> getConfig() {

        return ResultCode.getSuccessReturn(null, null, configService.list());
    }

}
