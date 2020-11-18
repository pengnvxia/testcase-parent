package edu.jiahui.testcase.controller;


import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.domain.request.CreateTestcaseGroupReq;
import edu.jiahui.testcase.domain.response.GroupRes;
import edu.jiahui.testcase.domain.response.SearchGroupRes;
import edu.jiahui.testcase.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import edu.jiahui.testcase.domain.request.SearchGroupReq;

import java.util.*;

@RestController
@RequestMapping("/group")
@Slf4j
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(method = RequestMethod.POST, value = "/search/list")
    public ResultCode searchList(@RequestBody @Valid SearchGroupReq req){
        List<SearchGroupRes.Group> rg= new ArrayList<>();
        try{
            rg=groupService.searchGroupList(req);
        }catch (Exception e){
            log.error("查询用例组异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,null,rg);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public ResultCode createGroup(@RequestBody @Valid CreateTestcaseGroupReq req){
        try {
            groupService.createTestcaseGroup(req);
        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(),c.getMessage());
        }catch (Exception e){
            log.error("创建用例组失败:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,"添加成功！",null);
    }

    @RequestMapping(method = RequestMethod.POST, value="/edit")
    public ResultCode editGroup(@RequestBody @Valid CreateTestcaseGroupReq req){
        try {
            groupService.editTestcaseGroup(req);
        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(),c.getMessage());
        }catch (Exception e){
            log.error("更新用例组失败:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,"更新成功！",null);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResultCode groupDetail(@PathVariable("id") Integer id){
        GroupRes res = new GroupRes();
        try{
            res = groupService.groupDetail(id);
        }catch (Exception e){
            log.error("查询用例组详情失败:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,null,res);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/run")
    public ResultCode runGroup(@RequestBody @Valid List<Integer> req){
        try {
            groupService.runGroups(req);
        }catch (Exception e){
            log.error("运行用例失败:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,"运行结束，请在报告列表查看结果！",null);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    public ResultCode deleteGroup(@PathVariable("id") Integer id){
        try{
            groupService.deleteGroup(id);
        }catch (Exception e){
            log.error("删除用例组失败:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,"删除成功！",null);

    }

}
