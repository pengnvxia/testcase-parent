FROM 172.19.22.1/java-initial/java:v1
RUN cd /opt/
ADD application* /opt/
ADD {{ name }}* /opt/
ADD init.sh /opt/
CMD ["/bin/bash","/opt/init.sh"]
