package edu.jiahui.testcase.utils;
//import com.spotify.docker.client.*;
//import com.spotify.docker.client.messages.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class DockerUtil {

//    public void pullImage() throws DockerCertificateException {
//        DockerClient docker = DefaultDockerClient.fromEnv().build();
//    }

//    public String createContainer(String imageName) throws DockerCertificateException, DockerException, InterruptedException {
//        DockerClient docker = DefaultDockerClient.fromEnv().build();
//        // Bind container ports to host ports
//        String[] ports = {"11"};
//        Map<String, List<PortBinding>> portBindings = new HashMap<>();
//        for (String port : ports) {
//            List<PortBinding> hostPorts = new ArrayList<>();
//            hostPorts.add(PortBinding.of("0.0.0.0", port));
//            portBindings.put(port, hostPorts);
//        }
//        HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();
//        // Create container with exposed ports
//        ContainerConfig containerConfig = ContainerConfig.builder()
//                .hostConfig(hostConfig)
//                .image(imageName).exposedPorts(ports)
//                .cmd("sh", "-c", "while :; do sleep 1; done")
//                .build();
//        ContainerCreation creation = docker.createContainer(containerConfig);
//        String id = creation.id();
//        return id;
//    }





}
