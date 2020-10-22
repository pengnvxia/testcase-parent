package edu.jiahui.testcase.controller;

import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.domain.request.AddInterfaceInfoReq;
import edu.jiahui.testcase.domain.request.CreateTestcaseReq;
import edu.jiahui.testcase.domain.request.RunTestcaseReq;
import edu.jiahui.testcase.domain.request.TestcaseReq;
import edu.jiahui.testcase.domain.response.TestcaseRes;
import edu.jiahui.testcase.service.PropertiesService;
import edu.jiahui.testcase.service.TestcaseService;
import jnr.ffi.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/")
@Slf4j
public class TestcaseController {


    @Autowired
    private TestcaseService testcaseService;

    @Autowired
    private PropertiesService propertiesService;

    @RequestMapping(method = RequestMethod.POST, value= "/add/{id}")
    public ResultCode addTestcase(@PathVariable("id")Integer id, @RequestBody @Valid TestcaseReq req){
        try{
            testcaseService.addTestcase(id,req);
        }catch (Exception e){

            log.error("添加测试用例异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return  ResultCode.getSuccessReturn(null, "添加成功！", null);
    }

    @RequestMapping(method = RequestMethod.GET, value= "/{id}" )
    public ResultCode interfaceInfo(@PathVariable("id")Integer id){
        TestcaseRes testcaseRes= new TestcaseRes();
        try{
            testcaseRes=propertiesService.InterfaceInfo(id);
        }catch (Exception e){
            log.error("查询用例信息:{}",e.getMessage());
            return ResultCode.getFailure(null, "服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null, null, testcaseRes);
    }

    @RequestMapping(method = RequestMethod.DELETE, value= "/delete/{id}")
    public ResultCode deleteTestcase(@PathVariable("id")Integer id){
        try{
            testcaseService.deleteTestcase(id);
        }catch (Exception e){
            log.error("删除用例失败{}",e.getMessage());
            return ResultCode.getFailure(null, "服务器繁忙，请稍后重试!");
        }
        return ResultCode.getSuccessReturn(null,"删除成功！",null);
    }

    @RequestMapping(method = RequestMethod.GET, value= "/case/{id}")
    public ResultCode testcaseInfo(@PathVariable("id")Integer id){
        TestcaseRes testcaseRes = new TestcaseRes();
        try{
            testcaseRes = testcaseService.testcaseInfo(id);
        }catch (Exception e){
            log.error("查询失败{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试!");
        }
        return ResultCode.getSuccessReturn(null,null,testcaseRes);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit/{id}")
    public ResultCode updateTestcase(@PathVariable("id")Integer id,@RequestBody @Valid TestcaseReq req){
        try{
            testcaseService.updateTestcase(id,req);
        }catch (Exception e){
            log.error("更新失败{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试!");
        }
        return ResultCode.getSuccessReturn(null,"更新成功！",null);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/run")
    public ResultCode runTestcase(@RequestBody @Valid RunTestcaseReq req){
        try{
            testcaseService.runTestcaseList(req);
        }catch (Exception e){
            log.error("运行用例失败{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试!");
        }
        return ResultCode.getSuccessReturn(null,null,"操作成功！");
    }

//    @RequestMapping(method = RequestMethod.GET,value = "/docker/{imageName}")
//    public ResultCode createContainer(@PathVariable("imageName")String imageName){
//        String containerId = null;
//        try{
//            containerId = testcaseService.createDb(imageName);
//        }catch (Exception e){
//            return ResultCode.getFailure(null,e.getMessage());
//        }
//        return ResultCode.getSuccessReturn(null,null,containerId);
//    }


    @RequestMapping(method = RequestMethod.GET,value = "/initDatabase/{id}")
    public ResultCode initDatabase(@PathVariable("id")Integer id ){
        List<Integer> ids = new ArrayList();
        ids.add(1);
        try {
            testcaseService.initDatabase(ids);
        }catch (Exception e){
            return ResultCode.getFailure(null,e.getMessage());
        }
        return ResultCode.getSuccessReturn(null,null,"操作成功！");
    }

    @RequestMapping(method = RequestMethod.POST,value = "/addInterface")
    public ResultCode addInterface(@RequestBody @Valid AddInterfaceInfoReq req){
        try {
            propertiesService.addInterfaceInfo(req);
        }catch (Exception e){
            return ResultCode.getFailure(null,e.getMessage());
        }
        return ResultCode.getSuccessReturn(null,null,"添加成功！");
    }

//    @RequestMapping(method = RequestMethod.POST, value = "/create/testcase")
//    public ResultCode<List<String>> createTestcase(@RequestBody @Valid CreateTestcaseReq req){
//        try{
//            testcaseService.createTestcase(req);
//        }catch (Exception e){
//            return ResultCode.getFailure("-1","创建用例失败");
//        }
//
//        return ResultCode.getSuccessReturn(null, "创建用例成功", null);
//
//    }
//
//    @RequestMapping(method =RequestMethod.POST, value = "/run/testcase")
//    public ResultCode<Object> runTestcase(@RequestBody List<Integer> req ){
//        String result=null;
//        if(req.size()==0){
//            return ResultCode.getFailure("-1","没有用例需要运行");
//        }
//        try{
//            result = testcaseService.runTestcase(req);
//        }catch (Exception e){
//            return ResultCode.getFailure("-2","用例执行失败");
//        }
//
//
//        if(result != "0"){
//            return ResultCode.getFailure("-2","用例执行失败");
//        }else {
//            return ResultCode.getSuccessReturn(null, "运行用例成功", null);
//        }
//    }
}
