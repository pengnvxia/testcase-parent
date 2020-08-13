package edu.jiahui.testcase.service;

import edu.jiahui.testcase.domain.Database;
import edu.jiahui.testcase.domain.TestcaseConfig;
import edu.jiahui.testcase.mapper.TestcaseConfigMapper;
import edu.jiahui.testcase.model.bo.DbBo;
import edu.jiahui.testcase.model.bo.TestcaseConfigBo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigService {


    @Resource
    private TestcaseConfigMapper testcaseConfigMapper;

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


}
