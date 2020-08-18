//package edu.jiahui.testcase.service;
//
//import com.alibaba.fastjson.JSON;
//import edu.jiahui.testcase.domain.request.CreateTestcaseReq;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.List;
//
//@Service
//public class TestcaseService {
//
//    @Resource
//    private TestcaseMapper testcaseMapper;
//
//
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
//}
