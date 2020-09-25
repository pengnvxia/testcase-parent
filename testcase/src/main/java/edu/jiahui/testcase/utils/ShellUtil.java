package edu.jiahui.testcase.utils;

import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
public class ShellUtil {
    public static Integer executeShellReturnexitValue(String shellCommand) {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        Process pid = null;
        try {
            stringBuffer.append("准备执行Shell命令 ").append(shellCommand)
                    .append(" \r\n");

//            String[] cmd = {"/bin/sh", "-c", shellCommand};
            pid = Runtime.getRuntime().exec(shellCommand);
            log.info("执行脚本结束");
            if (pid != null) {
                pid.waitFor();
            } else {
                stringBuffer.append("没有pid\r\n");
            }
        } catch (Exception e) {

        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error("关闭流异常", e);
                }
            }
            if (pid != null) {
                while (pid.isAlive()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("Waiting For Exit。。。");
                }
                log.info("PID EXIT: " + pid.exitValue());
            }
        }
        log.warn(stringBuffer.toString());
        return pid.exitValue();
    }
}
