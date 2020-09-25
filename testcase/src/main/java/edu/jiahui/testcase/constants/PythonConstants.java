package edu.jiahui.testcase.constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PythonConstants {

    @Value("${python.create.yaml}")
    public String pythonCreateYaml;

    @Value("${python.run.test}")
    public String pythonRunTest;
}
