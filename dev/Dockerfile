FROM 172.19.22.1/java-initial/java:v1
RUN sed -e 's|^mirrorlist=|#mirrorlist=|g' \
        -e 's|^#baseurl=http://mirror.centos.org/centos/|baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-vault/|g' \
        -i.bak \
        /etc/yum.repos.d/CentOS-*.repo
ARG variable=testcase_dev
ENV CLUSTER_NAME=$variable
RUN cd /opt/
ADD application* /opt/
ADD testcase* /opt/
ADD init.sh /opt/
ADD create_yaml.py /opt/
ADD run_test.py /opt/
ADD health_check.sh /opt/
RUN yum install python3 -y
RUN pip3 install httprunner==1.5.8
HEALTHCHECK --interval=10s --timeout=3s --retries=5 CMD /bin/bash /opt/health_check.sh
CMD ["/bin/bash","/opt/init.sh"]
