FROM centos_java_sshd:v2
RUN cd /opt/
ADD application* /opt/
ADD {{ name }}* /opt/
ADD init.sh /opt/
CMD ["/bin/bash","/opt/init.sh"]
