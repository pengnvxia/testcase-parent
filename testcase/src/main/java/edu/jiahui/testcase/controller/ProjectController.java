package edu.jiahui.testcase.controller;

import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.domain.request.ModuleReq;
import edu.jiahui.testcase.domain.request.SearchInterfaceReq;
import edu.jiahui.testcase.domain.request.SearchModuleReq;
import edu.jiahui.testcase.domain.request.ProjectReq;
import edu.jiahui.testcase.domain.response.InterfaceRes;
import edu.jiahui.testcase.domain.response.ModuleRes;
import edu.jiahui.testcase.domain.response.ProjectListRes;
import edu.jiahui.testcase.domain.response.ProjectRes;
import edu.jiahui.testcase.model.bo.ProjectBo;
import edu.jiahui.testcase.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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

    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public ResultCode listOne(@PathVariable("id")Integer id){
        ProjectRes projectRes= new ProjectRes();
        try{
            projectRes = projectService.listOne(id);
        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(),c.getMessage());
        }catch (Exception e){
            log.error("查询项目异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,null,projectRes);
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

    @RequestMapping(method = RequestMethod.POST, value = "/module/add")
    public ResultCode addModule(@RequestBody @Valid ModuleReq req){
        try{
            projectService.addModule(req);
        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(), c.getMessage());
        }catch (Exception e){
            log.error("插入模块异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,"添加成功！",null);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/interface/delete/{id}")
    public ResultCode deleteInerface(@PathVariable Integer id){
        try{
            projectService.deleteInterface(id);
        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(),c.getMessage());
        }catch (Exception e){
            log.error("删除接口异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,"删除成功",null);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/module/list")
    public ResultCode moduleList(@RequestBody @Valid SearchModuleReq req){
        List<ModuleRes.Module> moduleRes = new ArrayList<>();
        try{
            moduleRes = projectService.moduleList(req);
        }catch (Exception e){
            log.error("查询模块列表:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,null,moduleRes);

    }

    @RequestMapping(method = RequestMethod.POST, value= "/interface/list")
    public ResultCode interfaceList(@RequestBody @Valid SearchInterfaceReq req){
        List<InterfaceRes.InterfaceInfo> interfaceRes = new ArrayList<>();
        try{
            interfaceRes = projectService.interfaceList(req);
        } catch (Exception e){
            log.error("查询接口列表:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,null,interfaceRes);

    }

    @RequestMapping(method = RequestMethod.GET, value="/little/list")
    public ResultCode projectList(){
        List<ProjectListRes.Project> projects= new ArrayList<>();
        try{
            projects=projectService.projectList();
        }catch (Exception e){
            log.error("查询项目列表接口失败{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,null,projects);
    }

}
