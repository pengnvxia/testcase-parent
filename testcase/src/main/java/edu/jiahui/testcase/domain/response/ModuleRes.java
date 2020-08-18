package edu.jiahui.testcase.domain.response;

import lombok.Data;
import java.util.List;
@Data
public class ModuleRes {
//    private List<Module> modules;

    @Data
    public static class Module{
        private Integer id;
        private String moduleName;
    }
}
