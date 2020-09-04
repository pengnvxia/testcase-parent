package edu.jiahui.testcase.service;

import edu.jiahui.testcase.domain.Testcase;
import edu.jiahui.testcase.domain.TestcaseDetail;
import edu.jiahui.testcase.domain.request.TestcaseReq;
import edu.jiahui.testcase.domain.response.TestcaseRes;
import edu.jiahui.testcase.mapper.TestcaseDetailMapper;
import edu.jiahui.testcase.mapper.TestcaseMapper;
import jnr.ffi.annotations.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestcaseService {

    @Resource
    private TestcaseMapper testcaseMapper;

    @Resource
    private TestcaseDetailMapper testcaseDetailMapper;

    public void addTestcase(Integer id,TestcaseReq req){
        Testcase testcase = new Testcase();
        testcase.setTestcaseName(req.getTestcaseName());
        testcase.setEnvId(req.getEnvId());
        testcase.setConfigIds(req.getConfigIds());
        testcase.setInterfaceId(id);
        testcaseMapper.insert(testcase);
        Integer testcaseId = testcase.getId();
        List<TestcaseDetail> testcaseDetailList = new ArrayList<>();

        if(req.getVariables()!=null && req.getVariables().size()>0){
            for(TestcaseReq.Variable variable : req.getVariables()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setName(variable.getName());
                testcaseDetail.setType(variable.getType());
                testcaseDetail.setValue(variable.getValue());
                testcaseDetail.setDatabaseId(variable.getDatabaseId());
                testcaseDetail.setScope("variables");
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailList.add(testcaseDetail);
            }
        }

        if(req.getParameters()!=null && req.getParameters().size()>0){
            for(TestcaseReq.Parameter parameter : req.getParameters()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setName(parameter.getKeyName());
                testcaseDetail.setValue(parameter.getValue());
                testcaseDetail.setScope("parameters");
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailList.add(testcaseDetail);
            }
        }
        if(req.getSetuphooks()!=null && req.getSetuphooks().size()>0){
            for(TestcaseReq.Setuphook setuphook: req.getSetuphooks()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setValue(setuphook.getSql());
                testcaseDetail.setDatabaseId(setuphook.getDatabaseId());
                testcaseDetail.setScope("setupHooks");
                testcaseDetail.setType("sql");
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailList.add(testcaseDetail);
            }
        }
        if(req.getReqHeaders()!=null && req.getReqHeaders().size()>0){
            for(TestcaseReq.ReqHeader reqHeader:req.getReqHeaders()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setName(reqHeader.getKeyName());
                testcaseDetail.setValue(reqHeader.getValue());
                testcaseDetail.setScope("reqHeaders");
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailList.add(testcaseDetail);
            }
        }
        if(req.getReqParams()!=null && req.getReqParams().size()>0){
            for(TestcaseReq.ReqParam reqParam:req.getReqParams()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setName(reqParam.getKeyName());
                testcaseDetail.setValue(reqParam.getValue());
                testcaseDetail.setScope("reqParams");
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailList.add(testcaseDetail);
            }
        }
        if(req.getResponses()!=null && req.getResponses().size()>0){
            for(TestcaseReq.Response response:req.getResponses()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setName(response.getName());
                testcaseDetail.setType(response.getType());
                testcaseDetail.setComparator(response.getComparator());
                testcaseDetail.setExpectedValue(response.getExpectedValue());
                testcaseDetail.setScope("response");
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailList.add(testcaseDetail);
            }
        }
        if(req.getRequestBody()!=null){
            String aa = req.getRequestBody().getClass().toString();
            TestcaseDetail testcaseDetail = new TestcaseDetail();
            testcaseDetail.setValue(req.getRequestBody().toString());
            testcaseDetail.setScope("requestBody");
            testcaseDetail.setTestcaseId(testcaseId);
            testcaseDetailList.add(testcaseDetail);
        }

        testcaseDetailMapper.insert(testcaseDetailList);
    }

    public TestcaseRes testcaseInfo(Integer testcaseId){

        TestcaseRes testcaseRes = new TestcaseRes();

        Testcase testcase=testcaseMapper.selectByPrimaryKey(testcaseId);
        testcaseRes.setEnvId(testcase.getEnvId());
        testcaseRes.setConfigIds(testcase.getConfigIds());
        testcaseRes.setTestcaseName(testcase.getTestcaseName());
        List<TestcaseDetail> testcaseDetailList = testcaseDetailMapper.selectByTestcaseId(testcaseId);
        if(testcaseDetailList.size()>0){
            List<TestcaseRes.Variable> variableList = new ArrayList<>();
            List<TestcaseRes.Parameter> parameterList = new ArrayList<>();
            List<TestcaseRes.Setuphook> setuphookList = new ArrayList<>();
            List<TestcaseRes.ReqHeader> reqHeaderList = new ArrayList<>();
            List<TestcaseRes.ReqParam> reqParamList = new ArrayList<>();
            List<TestcaseRes.Response> responseList = new ArrayList<>();
            for(TestcaseDetail testcaseDetail:testcaseDetailList){
                String scope=testcaseDetail.getScope();
                switch (scope){
                    case "variables":
                        TestcaseRes.Variable variable= new TestcaseRes.Variable();
                        variable.setId(testcaseDetail.getId());
                        variable.setName(testcaseDetail.getName());
                        variable.setValue(testcaseDetail.getValue());
                        variable.setType(testcaseDetail.getType());
                        variable.setDatabaseId(testcaseDetail.getDatabaseId());
                        variableList.add(variable);
                        break;
                    case "parameters":
                        TestcaseRes.Parameter parameter = new TestcaseRes.Parameter();
                        parameter.setId(testcaseDetail.getId());
                        parameter.setKeyName(testcaseDetail.getName());
                        parameter.setValue(testcaseDetail.getValue());
                        parameterList.add(parameter);
                        break;
                    case "setupHooks":
                        TestcaseRes.Setuphook setuphook = new TestcaseRes.Setuphook();
                        setuphook.setId(testcaseDetail.getId());
                        setuphook.setSql(testcaseDetail.getValue());
                        setuphook.setDatabaseId(testcaseDetail.getDatabaseId());
                        setuphookList.add(setuphook);
                        break;
                    case "reqHeaders":
                        TestcaseRes.ReqHeader reqHeader = new TestcaseRes.ReqHeader();
                        reqHeader.setId(testcaseDetail.getId());
                        reqHeader.setKeyName(testcaseDetail.getName());
                        reqHeader.setValue(testcaseDetail.getValue());
                        reqHeaderList.add(reqHeader);
                        break;
                    case "reqParams":
                        TestcaseRes.ReqParam reqParam = new TestcaseRes.ReqParam();
                        reqParam.setId(testcaseDetail.getId());
                        reqParam.setKeyName(testcaseDetail.getName());
                        reqParam.setValue(testcaseDetail.getValue());
                        reqParamList.add(reqParam);
                        break;
                    case "response":
                        TestcaseRes.Response  response = new TestcaseRes.Response();
                        response.setId(testcaseDetail.getId());
                        response.setName(testcaseDetail.getName());
                        response.setType(testcaseDetail.getType());
                        response.setComparator(testcaseDetail.getComparator());
                        response.setExpectedValue(testcaseDetail.getExpectedValue());
                        responseList.add(response);
                        break;
                    case "requestBody":
                        Map reqBodyMap = new HashMap();
                        reqBodyMap.put("id",testcaseDetail.getId());
                        reqBodyMap.put("requestBody",testcaseDetail.getValue());
                        testcaseRes.setReqBody(reqBodyMap);
                }
            }
            testcaseRes.setVariables(variableList);
            testcaseRes.setParameters(parameterList);
            testcaseRes.setSetuphooks(setuphookList);
            testcaseRes.setReqHeaders(reqHeaderList);
            testcaseRes.setReqParams(reqParamList);
            testcaseRes.setResponses(responseList);
        }
        return testcaseRes;

    }

    public void updateTestcase(Integer id,TestcaseReq req){
        Testcase testcase= new Testcase();
        testcase.setTestcaseName(req.getTestcaseName());
        testcase.setEnvId(req.getEnvId());
        testcase.setConfigIds(req.getConfigIds());
        testcase.setId(id);
        testcaseMapper.updateByPrimaryKey(testcase);
        if(req.getVariables()!=null && req.getVariables().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.Variable variable:req.getVariables()){
                TestcaseDetail testcaseDetail= new TestcaseDetail();
                testcaseDetail.setScope("variables");
                testcaseDetail.setName(variable.getName());
                testcaseDetail.setValue(variable.getValue());
                testcaseDetail.setType(variable.getType());
                testcaseDetail.setDatabaseId(variable.getDatabaseId());
                if(variable.getId()!=null){
                    testcaseDetail.setId(variable.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(variable.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,"variables");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,"variables");
        }

        if(req.getParameters()!=null && req.getParameters().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.Parameter parameter:req.getParameters()){
                TestcaseDetail testcaseDetail= new TestcaseDetail();
                testcaseDetail.setScope("parameters");
                testcaseDetail.setName(parameter.getKeyName());
                testcaseDetail.setValue(parameter.getValue());
                if(parameter.getId()!=null){
                    testcaseDetail.setId(parameter.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(parameter.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,"parameters");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,"parameters");
        }

        if(req.getSetuphooks()!=null && req.getSetuphooks().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.Setuphook setuphook:req.getSetuphooks()){
                TestcaseDetail testcaseDetail= new TestcaseDetail();
                testcaseDetail.setScope("setupHooks");
                testcaseDetail.setValue(setuphook.getSql());
                testcaseDetail.setDatabaseId(setuphook.getDatabaseId());
                if(setuphook.getId()!=null){
                    testcaseDetail.setId(setuphook.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(setuphook.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,"setupHooks");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,"setupHooks");
        }

        if(req.getReqHeaders()!=null && req.getReqHeaders().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.ReqHeader reqHeader:req.getReqHeaders()){
                TestcaseDetail testcaseDetail= new TestcaseDetail();
                testcaseDetail.setScope("reqHeaders");
                testcaseDetail.setName(reqHeader.getKeyName());
                testcaseDetail.setValue(reqHeader.getValue());
                if(reqHeader.getId()!=null){
                    testcaseDetail.setId(reqHeader.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(reqHeader.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,"reqHeaders");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,"reqHeaders");
        }

        if(req.getReqParams()!=null && req.getReqParams().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.ReqParam reqParam:req.getReqParams()){
                TestcaseDetail testcaseDetail= new TestcaseDetail();
                testcaseDetail.setScope("reqParams");
                testcaseDetail.setName(reqParam.getKeyName());
                testcaseDetail.setValue(reqParam.getValue());
                if(reqParam.getId()!=null){
                    testcaseDetail.setId(reqParam.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(reqParam.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,"reqParams");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,"reqParams");
        }

        if(req.getResponses()!=null && req.getResponses().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.Response response:req.getResponses()){
                TestcaseDetail testcaseDetail= new TestcaseDetail();
                testcaseDetail.setScope("response");
                testcaseDetail.setName(response.getName());
                testcaseDetail.setType(response.getType());
                testcaseDetail.setExpectedValue(response.getExpectedValue());
                testcaseDetail.setComparator(response.getComparator());
                if(response.getId()!=null){
                    testcaseDetail.setId(response.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(response.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,"response");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,"response");
        }

        if(req.getReqBody()!=null){
            TestcaseDetail testcaseDetail= new TestcaseDetail();
            testcaseDetail.setValue(req.getReqBody().get("requestBody").toString());
            testcaseDetail.setScope("requestBody");
            if(req.getReqBody().containsKey("id")){
                testcaseDetail.setId(Integer.parseInt(req.getReqBody().get("id").toString()));
                testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
            }else {
                testcaseDetail.setTestcaseId(id);
                testcaseDetailMapper.insertOne(testcaseDetail);
            }
        }
    }

    public void deleteTestcase(Integer id){
        testcaseDetailMapper.deleteByTestcaseId(id);
        testcaseMapper.deleteByPrimaryKey(id);
    }

//    @Resource
//    private TestcaseMapper testcaseMapper;


//    public void createTestcase(CreateTestcaseReq req){
//        TestcaseWithBLOBs testcaseWithBLOBs = new TestcaseWithBLOBs();
//        testcaseWithBLOBs.setProjectId(req.getProjectId());
//        testcaseWithBLOBs.setBody(JSON.toJSONString(req.getBody()));
//        testcaseWithBLOBs.setExtractVariables(JSON.toJSONString(req.getExtractVariables()));
//        testcaseWithBLOBs.setParameters(JSON.toJSONString(req.getParameters()));
//        testcaseWithBLOBs.setParams(JSON.toJSONString(req.getParams()));
//        testcaseWithBLOBs.setSetupHooks(JSON.toJSONString(req.getSetupHooks()));
//        testcaseWithBLOBs.setSqlSentence(JSON.toJSONString(req.getSqlSentence()));
//        testcaseWithBLOBs.setValidate(JSON.toJSONString(req.getValidate()));
//        testcaseWithBLOBs.setVariables(JSON.toJSONString(req.getVariables()));
//        testcaseWithBLOBs.setConfigIds(JSON.toJSONString(req.getConfigIds()));
//        testcaseWithBLOBs.setDescription(req.getDescription());
//        testcaseWithBLOBs.setEnvId(req.getEnvId());
//        testcaseWithBLOBs.setHeaders(JSON.toJSONString(req.getHeaders()));
//        testcaseWithBLOBs.setPath(req.getPath());
//        testcaseWithBLOBs.setTestcaseName(req.getTestcaseName());
//        testcaseWithBLOBs.setRequestType(req.getRequestType());
//        testcaseMapper.insert(testcaseWithBLOBs);
//    }
//
//
////    运行单个用例
//    public String runTestcase(List<Integer> req){
//        String result = "-1";
//        for(Integer testcaseId : req){
//            String project= "test_paper_paper"; //临时参数
//            String patha = "paper_user_login"; //临时参数
//
//            List<Object> testcase_list = JSON.parseArray("[{\"test\": {\"request\": " +
//                    "{\"url\": \"/paper/user/login\", \"method\": \"POST\", \"json\": " +
//                    "{\"username\": \"testQuiz2\", \"password\": \"e10adc3949ba59abbe56e057f20f883e\"}}, " +
//                    "\"name\": \"case_paper_login\", \"extract\": [{\"token\": \"content.retval\"}]}}]");//临时参数
//            try {
//                String[] args = new String[]{"python3", "/Users/pengyishuang/Desktop/mypro01/create_yaml.py", project, patha, JSON.toJSONString(testcase_list)};
//                Process proc = Runtime.getRuntime().exec(args);
//
//                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//                if (String.valueOf(in.readLine()).equals("ok")) {
//                    result = "0";
//                }
//                in.close();
//                proc.waitFor();
//            }catch (IOException e){
//                e.printStackTrace();
//            }catch(InterruptedException e){
//                e.printStackTrace();
//            }
//        }
//
//        return result;
//
//    }
}
