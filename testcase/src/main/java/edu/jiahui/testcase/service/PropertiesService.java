package edu.jiahui.testcase.service;

import com.alibaba.fastjson.JSONObject;
import edu.jiahui.testcase.domain.Interfaces;
import edu.jiahui.testcase.domain.Properties;
import edu.jiahui.testcase.domain.request.AddInterfaceInfoReq;
import edu.jiahui.testcase.domain.request.TestcaseReq;
import edu.jiahui.testcase.domain.response.InterfaceRes;
import edu.jiahui.testcase.domain.response.TestcaseRes;
import edu.jiahui.testcase.mapper.InterfacesMapper;
import edu.jiahui.testcase.mapper.PropertiesMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PropertiesService {

    @Resource
    private InterfacesMapper interfacesMapper;

    @Resource
    private PropertiesMapper propertiesMapper;

    public TestcaseRes InterfaceInfo(Integer interfaceId){
        Integer envId = interfacesMapper.selectByPrimaryKey(interfaceId).getEnvId();
        List<Properties> propertiesList = propertiesMapper.selectByInterfaceId(interfaceId);
        TestcaseRes testcaseRes = new TestcaseRes();
        List<TestcaseRes.Response> responseList = new ArrayList<>();
        List<TestcaseRes.ReqHeader> reqHeaderList = new ArrayList<>();
        List<TestcaseRes.ReqParam> reqParamList = new ArrayList<>();
        JSONObject requestBodyJson = new JSONObject();

        for(Properties properties:propertiesList){
            if(properties.getScope().equals("response")) {
                TestcaseRes.Response response = new TestcaseRes.Response();
                response.setName(properties.getName());
                response.setType(properties.getType());
                responseList.add(response);
            }else{
                if(properties.getPos()==1){
                    TestcaseRes.ReqHeader reqHeader = new TestcaseRes.ReqHeader();
                    reqHeader.setKeyName(properties.getName());
                    reqHeader.setValue(properties.getValue());
                    reqHeaderList.add(reqHeader);
                }else if(properties.getPos()==2){
                    TestcaseRes.ReqParam reqParam = new TestcaseRes.ReqParam();
                    reqParam.setKeyName(properties.getName());
                    reqParam.setValue(properties.getValue());
                    reqParamList.add(reqParam);
                }else {
                    requestBodyJson.put(properties.getName(),properties.getValue());
                }
            }
        }
        testcaseRes.setRequestBody(requestBodyJson.toString());
        testcaseRes.setEnvId(envId);
        testcaseRes.setReqHeaders(reqHeaderList);
        testcaseRes.setReqParams(reqParamList);
        testcaseRes.setResponses(responseList);
        return testcaseRes;
    }

    public void addInterfaceInfo(AddInterfaceInfoReq req){
        Interfaces interfaces= new Interfaces();
        interfaces.setName(req.getInterfaceName());
        interfaces.setUrl(req.getInterfaceAddress());
        interfaces.setMethod(req.getInterfaceMethod());
        interfaces.setDescription(req.getDescription());
        interfaces.setRepositoryId(req.getProjectId());
        interfaces.setModuleId(req.getModuleId());
        interfaces.setEnvId(req.getEnvId());
        interfacesMapper.insert(interfaces);
//        Interfaces interfaces=Interfaces.builder().name(req.getInterfaceName()).
//                url(req.getInterfaceAddress()).method(req.getInterfaceMethod()).
//                description(req.getDescription()).repositoryId(req.getProjectId()).moduleId(req.getModuleId()).build();

    }
}
