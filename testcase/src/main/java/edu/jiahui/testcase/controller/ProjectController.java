package edu.jiahui.testcase.controller;

import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.domain.request.ProjectReq;
import edu.jiahui.testcase.model.bo.ProjectBo;
import edu.jiahui.testcase.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/project")
@Slf4j
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultCode<List<ProjectBo>> list(){
        return ResultCode.getSuccessReturn(null, null, projectService.list());
    }

    @RequestMapping(method = RequestMethod.POST, value= "/add")
    public ResultCode add(@RequestBody @Valid ProjectReq req){
        try{
            projectService.addProject(req);
        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(),c.getMessage());
        }catch (Exception e){
            log.error("添加项目异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return  ResultCode.getSuccessReturn(null, "添加成功！", null);
    }

    @RequestMapping(method = RequestMethod.POST, value= "/edit/{id}")
    public ResultCode update(@PathVariable("id")Integer id,@RequestBody @Valid ProjectReq req){
        try{
            projectService.updateProject(id,req);

        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(),c.getMessage());
        }catch (Exception e){
            log.error("编辑项目异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return  ResultCode.getSuccessReturn(null, "更新成功！", null);
    }

    @RequestMapping(method = RequestMethod.DELETE, value= "/delete/{id}")
    public ResultCode delete(@PathVariable Integer id){
        try{
            projectService.deleteProject(id);
        }catch (Exception e){
            log.error("编辑项目异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return  ResultCode.getSuccessReturn(null, "删除成功！", null);
    }

}
