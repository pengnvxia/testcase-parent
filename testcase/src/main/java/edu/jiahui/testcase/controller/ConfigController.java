package edu.jiahui.testcase.controller;


import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.domain.request.ConfigListReq;
import edu.jiahui.testcase.domain.request.ConfigReq;
import edu.jiahui.testcase.domain.request.SearchConfigReq;
import edu.jiahui.testcase.domain.response.ConfigRes;
import edu.jiahui.testcase.domain.response.SearchConfigRes;
import edu.jiahui.testcase.model.bo.TestcaseConfigBo;
import edu.jiahui.testcase.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/config")
@Slf4j
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultCode<List<TestcaseConfigBo>> getConfig() {

        return ResultCode.getSuccessReturn(null, null, configService.list());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search/list")
    public ResultCode<SearchConfigRes> searchConfig(@RequestBody @Valid SearchConfigReq req){
        SearchConfigRes res = null;
        try{
            res = configService.searchConfig(req);
        }catch (Exception e){
            log.error("查询配置项失败{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null, null, res);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/config/list")
    public ResultCode<List<SearchConfigRes.Configs>> configList(@RequestBody @Valid ConfigListReq req){
        List<SearchConfigRes.Configs> res= new ArrayList<>();
        try{
            res=configService.configsList(req);
        }catch (Exception e){
            log.error("查询配置项失败{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,null,res);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultCode createConfig(@RequestBody @Valid ConfigReq req){
        try {
            configService.createConfig(req);
        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(),c.getMessage());
        }catch (Exception e){
            log.error("添加配置项失败{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,null,"添加成功！");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit/{id}")
    public ResultCode updateConfig(@RequestBody @Valid ConfigReq req, @PathVariable("id")Integer id){
        try{
            configService.updateConfig(req,id);
        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(),c.getMessage());
        }catch (Exception e){
            log.error("更新配置项失败{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,null,"更新成功！");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResultCode configDetail(@PathVariable("id")Integer id){
        ConfigRes res=null;
        try{
            res = configService.configDetail(id);
        }catch (Exception e){
            log.error("查询配置详情失败{}",e.getMessage());
            return  ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,null,res);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    public ResultCode deleteConfig(@PathVariable("id")Integer id){
        try{
            configService.deleteConfig(id);
        }catch (Exception e){
            log.error("删除配置项失败{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,null,"删除成功！");
    }
}
