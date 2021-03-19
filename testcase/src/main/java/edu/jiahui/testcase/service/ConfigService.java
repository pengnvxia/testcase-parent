package edu.jiahui.testcase.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.threadlocal.ParameterThreadLocal;
import edu.jiahui.testcase.constants.BaseConstans;
import edu.jiahui.testcase.domain.TestcaseConfig;
import edu.jiahui.testcase.domain.TestcaseConfigDetail;
import edu.jiahui.testcase.domain.request.ConfigListReq;
import edu.jiahui.testcase.domain.request.ConfigReq;
import edu.jiahui.testcase.domain.request.SearchConfigReq;
import edu.jiahui.testcase.domain.response.ConfigRes;
import edu.jiahui.testcase.domain.response.SearchConfigRes;
import edu.jiahui.testcase.mapper.ProjectMapper;
import edu.jiahui.testcase.mapper.TestcaseConfigDetailMapper;
import edu.jiahui.testcase.mapper.TestcaseConfigMapper;
import edu.jiahui.testcase.model.bo.TestcaseConfigBo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigService {


    @Resource
    private TestcaseConfigMapper testcaseConfigMapper;

    @Resource
    private TestcaseConfigDetailMapper testcaseConfigDetailMapper;

    @Resource
    private ProjectMapper projectMapper;

    public List<TestcaseConfigBo> list(){
        List<TestcaseConfig> testcaseConfigs = testcaseConfigMapper.select();
        List<TestcaseConfigBo> rsp = new ArrayList<TestcaseConfigBo>();
        if(testcaseConfigs.size()>0){
            for(TestcaseConfig testcaseConfig : testcaseConfigs){
                TestcaseConfigBo tc = new TestcaseConfigBo();
                tc.setId(testcaseConfig.getId());
                tc.setConfigName(testcaseConfig.getConfigName());
                rsp.add(tc);
            }
        }
        return rsp;
    }

    public SearchConfigRes searchConfig(SearchConfigReq req){
        SearchConfigRes res = new SearchConfigRes();
        PageHelper.startPage(req.currentifPage(),req.sizeif());
//        TestcaseConfig testcaseConfig= TestcaseConfig.builder()
//                .configName(req.getConfigName()).projectId(req.getProjectId())
//                .updatedBy(req.getLastUpdatedBy()).envId(req.getEnvId()).build();
        List<TestcaseConfig> testcaseConfigList = testcaseConfigMapper.selectByCondition(req);
        PageInfo<TestcaseConfig> pageInfo= new PageInfo<TestcaseConfig>(testcaseConfigList);
        List<SearchConfigRes.Configs> searchConfigResList = new ArrayList<>();
        for(TestcaseConfig tc: testcaseConfigList){
            SearchConfigRes.Configs configs= SearchConfigRes.Configs.builder().id(tc.getId())
                    .configName(tc.getConfigName())
                    .projectId(tc.getProjectId())
                    .projectName(projectMapper.selectByPrimaryKey(tc.getProjectId()).getProjectName())
                    .updatedBy(tc.getUpdatedBy())
                    .updatedAt(tc.getUpdatedAt())
                    .description(tc.getDescription())
                    .envId(tc.getEnvId())
                    .build();
            searchConfigResList.add(configs);
        }
        res.setConfigsList(searchConfigResList);
        res.setTotal(pageInfo.getTotal());
        return res;
    }

    public List<SearchConfigRes.Configs> configsList(ConfigListReq req){
        List<TestcaseConfig> testcaseConfigs= testcaseConfigMapper.selectByIds(req);
        List<SearchConfigRes.Configs> configsList = new ArrayList<>();
        for(TestcaseConfig tc: testcaseConfigs){
            SearchConfigRes.Configs sc= SearchConfigRes.Configs.builder().id(tc.getId()).configName(tc.getConfigName()).build();
            configsList.add(sc);
        }
        return configsList;
    }


    public void createConfig(ConfigReq req){
        //判断名称是否存在
        if(testcaseConfigMapper.selectByConfigName(req.getConfigName(),null)>0){
            throw (new ClientException(BaseConstans.BUSI_CODE.CONFIG_NAME_EXIT.getCode(),BaseConstans.BUSI_CODE.CONFIG_NAME_EXIT.getMsg()));
        }
        TestcaseConfig testcaseConfig= TestcaseConfig.builder()
                .configName(req.getConfigName())
                .projectId(req.getProjectId())
                .envId(req.getEnvId())
                .description(req.getDescription())
                .createdBy(ParameterThreadLocal.getUid())
                .updatedBy(ParameterThreadLocal.getUid())
                .build();
        testcaseConfigMapper.insert(testcaseConfig);
        List<ConfigReq.Variables> variablesList=req.getVariablesList();
        List<TestcaseConfigDetail> testcaseConfigDetailList = new ArrayList<>();
        for(ConfigReq.Variables vr:variablesList){
            TestcaseConfigDetail testcaseConfigDetail = TestcaseConfigDetail.builder()
                    .name(vr.getName())
                    .type(vr.getType())
                    .value(vr.getValue())
                    .configId(testcaseConfig.getId())
                    .updatedBy(ParameterThreadLocal.getUid())
                    .build();
            testcaseConfigDetailList.add(testcaseConfigDetail);
        }
        testcaseConfigDetailMapper.insert(testcaseConfigDetailList);
    }

    public void updateConfig(ConfigReq req,Integer id){
        //判断名称是否存在
        if(testcaseConfigMapper.selectByConfigName(req.getConfigName(),id)>0){
            throw (new ClientException(BaseConstans.BUSI_CODE.CONFIG_NAME_EXIT.getCode(),BaseConstans.BUSI_CODE.CONFIG_NAME_EXIT.getMsg()));
        }
        TestcaseConfig testcaseConfig=TestcaseConfig.builder()
                .configName(req.getConfigName())
                .projectId(req.getProjectId())
                .envId(req.getEnvId())
                .updatedBy(ParameterThreadLocal.getUid())
                .id(id)
                .description(req.getDescription()).build();
        //更新config中内容
        testcaseConfigMapper.update(testcaseConfig);
        List<Integer> ids = new ArrayList<>();
        for(ConfigReq.Variables vr: req.getVariablesList()){
            TestcaseConfigDetail testcaseConfigDetail= TestcaseConfigDetail.builder()
                    .name(vr.getName())
                    .type(vr.getType())
                    .value(vr.getValue())
                    .id(vr.getVariableId())
                    .configId(id)
                    .id(vr.getVariableId())
                    .updatedBy(ParameterThreadLocal.getUid())
                    .build();
            //新增或者更新config_detail中的内容
            if(testcaseConfigDetail.getId()==null){
                testcaseConfigDetailMapper.insertOne(testcaseConfigDetail);
            }else {
                testcaseConfigDetailMapper.update(testcaseConfigDetail);
            }
            ids.add(testcaseConfigDetail.getId());
        }
        //删除多余的内容
        if(ids.size()>0){
            testcaseConfigDetailMapper.deleteNotIn(ids,id);
        }
    }

    public ConfigRes configDetail(Integer id){
        //拼接variables部分
        List<ConfigRes.Variables> variablesList=new ArrayList<>();
        List<TestcaseConfigDetail> testcaseConfigDetailList = testcaseConfigDetailMapper.selectByConfigId(id);
        for(TestcaseConfigDetail tc: testcaseConfigDetailList){
            ConfigRes.Variables variables=ConfigRes.Variables.builder()
                    .variableId(tc.getId())
                    .name(tc.getName())
                    .type(tc.getType())
                    .value(tc.getValue())
                    .build();
            variablesList.add(variables);
        }
        TestcaseConfig testcaseConfig=testcaseConfigMapper.selectByPrimaryKey(id);
        ConfigRes res= ConfigRes.builder()
                .configName(testcaseConfig.getConfigName())
                .envId(testcaseConfig.getEnvId())
                .projectId(testcaseConfig.getProjectId())
                .description(testcaseConfig.getDescription())
                .variablesList(variablesList).build();
        return res;
    }

    public void deleteConfig(Integer id){
        //配置项被引用不允许删除
        //...
        testcaseConfigDetailMapper.deleteByConfigId(id);
        testcaseConfigMapper.delete(id);
    }

}
