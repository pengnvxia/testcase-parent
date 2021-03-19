package edu.jiahui.testcase.service;

import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.threadlocal.ParameterThreadLocal;
import edu.jiahui.testcase.constants.BaseConstans;
import edu.jiahui.testcase.domain.Interfaces;
import edu.jiahui.testcase.domain.Modules;
import edu.jiahui.testcase.domain.Project;
import edu.jiahui.testcase.domain.Testcase;
import edu.jiahui.testcase.domain.request.ModuleReq;
import edu.jiahui.testcase.domain.request.ProjectReq;
import edu.jiahui.testcase.domain.request.SearchInterfaceReq;
import edu.jiahui.testcase.domain.request.SearchModuleReq;
import edu.jiahui.testcase.domain.response.*;
import edu.jiahui.testcase.mapper.*;
import edu.jiahui.testcase.model.bo.ProjectBo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ModulesMapper modulesMapper;

    @Resource
    private InterfacesMapper interfacesMapper;

    @Resource
    private TestcaseMapper testcaseMapper;

    @Resource
    private PropertiesMapper propertiesMapper;


    public ProjectRes listOne(Integer id){
        Project project= projectMapper.selectByPrimaryKey(id);
        if(project==null){
            throw (new ClientException(BaseConstans.BUSI_CODE.PROJECT_NOT_EXIT.getCode(),BaseConstans.BUSI_CODE.PROJECT_NOT_EXIT.getMsg()));
        }else {
            ProjectRes projectRes= new ProjectRes();
            projectRes.setId(project.getId());
            projectRes.setProjectName(project.getProjectName());
            projectRes.setDevAddress(project.getDevAddress());
            projectRes.setProdAddress(project.getProdAddress());
            projectRes.setDescription(project.getDescription());
            return projectRes;

        }
    }

    public List<ProjectBo> list(){
        List<Project> projectList = projectMapper.selectAll();
        List<ProjectBo> rsp = new ArrayList<>();
        if(projectList.size()>0){
            for(Project project:projectList){
                ProjectBo probo = new ProjectBo();
                probo.setId(project.getId());
                probo.setProjectName(project.getProjectName());
                probo.setDevAddress(project.getDevAddress());
                probo.setProdAddress(project.getProdAddress());
                probo.setDescription(project.getDescription());
                probo.setUpdatedBy(project.getUpdatedName());
                probo.setUpdatedAt(project.getUpdatedAt());
                rsp.add(probo);
            }
        }
        return rsp;
    }


    public void addProject(ProjectReq req){
        if(projectExit(null,req.getProjectName())){
            throw (new ClientException(BaseConstans.BUSI_CODE.PROJECT_NAME_EXIT.getCode(),BaseConstans.BUSI_CODE.PROJECT_NAME_EXIT.getMsg()));
        }
        Project project=Project.builder()
                .projectName(req.getProjectName())
                .devAddress(req.getDevAddress())
                .prodAddress(req.getProdAddress())
                .description(req.getDescription())
                .createdBy(Integer.parseInt(ParameterThreadLocal.getUid()))
                .updatedBy(Integer.parseInt(ParameterThreadLocal.getUid()))
                .build();
        projectMapper.insert(project);
    }

    public void updateProject(Integer id,ProjectReq req){
        Project project = projectMapper.selectByPrimaryKey(id);
        if(project==null){
            throw (new ClientException(BaseConstans.BUSI_CODE.PROJECT_NOT_EXIT.getCode(),BaseConstans.BUSI_CODE.PROJECT_NOT_EXIT.getMsg()));
        }
        if(projectExit(id,req.getProjectName())){
            throw (new ClientException(BaseConstans.BUSI_CODE.PROJECT_NAME_EXIT.getCode(),BaseConstans.BUSI_CODE.PROJECT_NAME_EXIT.getMsg()));
        }
        project.setProjectName(req.getProjectName());
        project.setDevAddress(req.getDevAddress());
        project.setProdAddress(req.getProdAddress());
        project.setDescription(req.getDescription());
        project.setCreatedBy(Integer.parseInt(ParameterThreadLocal.getUid()));
        project.setUpdatedBy(Integer.parseInt(ParameterThreadLocal.getUid()));
        projectMapper.updateByPrimaryKey(project);
    }

    public void addModule(ModuleReq req){
        if(moduleExit(req.getName(),req.getRepositoryId())){
            throw (new ClientException(BaseConstans.BUSI_CODE.MODULE_NAME_EXIT.getCode(),BaseConstans.BUSI_CODE.MODULE_NAME_EXIT.getMsg()));
        }else {
            Modules modules = new Modules();
            modules.setName(req.getName());
            modules.setDescription(req.getDescription());
            modules.setEnvId(req.getEnvId());
            modules.setRepositoryId(req.getRepositoryId());
            modulesMapper.insert(modules);
        }
    }

    public List<ModuleRes.Module> moduleList(SearchModuleReq req){
        List<Modules> modules = modulesMapper.selectModules(req.getEnvId(),req.getRepositoryId());
//        ModuleRes res=new ModuleRes();
        List<ModuleRes.Module> moduleList= new ArrayList<>();
        if(modules.size()>0){
            for (Modules mo: modules){
                ModuleRes.Module module=new ModuleRes.Module();
                module.setId(mo.getId());
                module.setModuleName(mo.getName());
                moduleList.add(module);
            }
        }
//        res.setModules(moduleList);
        return moduleList;
    }

    public void deleteInterface(Integer id){
//        接口下有用例则不允许删除
        if(testcaseMapper.selectByInterfaceId(id).size()>0){
            throw (new ClientException(BaseConstans.BUSI_CODE.CASE_EXIT.getCode(),BaseConstans.BUSI_CODE.CASE_EXIT.getMsg()));
        }else {
            if(interfacesMapper.selectByPrimaryKey(id)==null){
                throw (new ClientException(BaseConstans.BUSI_CODE.INTERFACE_NOT_EXIT.getCode(),BaseConstans.BUSI_CODE.INTERFACE_NOT_EXIT.getMsg()));
            }else {
                propertiesMapper.deleteByInterfaceId(id);
                interfacesMapper.deleteByPrimaryKey(id);
            }

        }

    }

    public List<InterfaceRes.InterfaceInfo> interfaceList(SearchInterfaceReq req){
        List<Interfaces> interfaces=interfacesMapper.selectById(req.getEnvId(),req.getRepositoryId(),req.getModuleId());
        List<InterfaceRes.InterfaceInfo> interfaceInfoList=new ArrayList<>();
        if(interfaces.size()>0){
            for(Interfaces inter:interfaces){
                InterfaceRes.InterfaceInfo interfaceInfo=InterfaceRes.InterfaceInfo.builder()
                        .id(inter.getId())
                        .name(inter.getName())
                        .url(inter.getUrl())
                        .updatedBy(inter.getUpdatedBy())
                        .updatedAt(inter.getUpdatedAt())
                        .build();
                List<InterfaceRes.TestcaseInfo> testcaseList= testcaseMapper.selectByInterfaceId(inter.getId());

//                List<InterfaceRes.TestcaseInfo> testcaseInfoList= new ArrayList<>();
//                if(testcaseList.size()>0){
//                    for(Testcase testcase:testcaseList){
//                        InterfaceRes.TestcaseInfo testcaseInfo= InterfaceRes.TestcaseInfo.builder()
//                                .caseId(testcase.getId())
//                                .caseName(testcase.getTestcaseName())
//                                .caseEnvId(testcase.getEnvId())
//                                .caseUpdatedBy(testcase.getUpdatedBy())
//                                .caseUpdatedAt(testcase.getUpdatedAt())
//                                .build();
//                        testcaseInfoList.add(testcaseInfo);
//                    }
//
//                }
//                interfaceInfo.setTestcaseInfos(testcaseInfoList);
                interfaceInfo.setTestcaseInfos(testcaseList);
                interfaceInfoList.add(interfaceInfo);
            }
        }
        return interfaceInfoList;
    }


    public boolean moduleExit(String name, Integer id){
        return modulesMapper.moduleNameExit(name, id)>0;
    }

    public void deleteProject(Integer id){
        projectMapper.delete(id);
    }

    public boolean projectExit(Integer id, String projectName){
        return projectMapper.projectNameExit(id,projectName)>0;
    }

    public List<ProjectListRes.Project> projectList(){
        List<Project> projectList=projectMapper.selectByEnvId();
        List<ProjectListRes.Project> projects=new ArrayList<>();
        for(Project pj: projectList){
            ProjectListRes.Project project=ProjectListRes.Project.builder()
                    .id(pj.getId())
                    .projectName(pj.getProjectName())
                    .build();
                    projects.add(project);
        }
        return projects;
    }

}
