#FROM 172.19.22.1/java-initial/java:v1
FROM 15056332824/java:latest
ARG variable=testcase_dev
ENV CLUSTER_NAME=$variable
RUN cd /opt/
ADD application* /opt/
ADD testcase* /opt/
ADD init.sh /opt/
ADD create_yaml.py /opt/
ADD run_test.py /opt/
ADD health_check.sh /opt/
RUN yum install python3==3.7.7 -y
RUN pip3 install httprunner==1.5.8
HEALTHCHECK --interval=10s --timeout=3s --retries=5 CMD /bin/bash /opt/health_check.sh
CMD ["/bin/bash","/opt/init.sh"]
