//package edu.jiahui.testcase.controller;
//
//import edu.jiahui.framework.util.ResultCode;
//import edu.jiahui.testcase.domain.request.CreateTestcaseReq;
//import edu.jiahui.testcase.service.TestcaseService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import java.util.List;
//
//import javax.validation.Valid;
//
//@RestController
//@RequestMapping("/")
//public class TestcaseController {
//
//
//    @Autowired
//    private TestcaseService testcaseService;
//
//
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
//}
