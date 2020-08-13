package edu.jiahui.testcase.service;

import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.testcase.constants.BaseConstans;
import edu.jiahui.testcase.domain.Project;
import edu.jiahui.testcase.domain.request.ProjectReq;
import edu.jiahui.testcase.mapper.ProjectMapper;
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
        Project project = new Project();
        project.setProjectName(req.getProjectName());
        project.setDevAddress(req.getDevAddress());
        project.setProdAddress(req.getProdAddress());
        project.setDescription(req.getDescription());
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
        project.setUpdatedAt(new Date());
        projectMapper.updateByPrimaryKey(project);
    }

    public void deleteProject(Integer id){
        projectMapper.delete(id);
    }

    public boolean projectExit(Integer id, String projectName){
        return projectMapper.projectNameExit(id,projectName)>0;
    }

}
