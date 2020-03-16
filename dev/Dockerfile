FROM 172.19.22.1/java-initial/java:v1
RUN cd /opt/
ADD application* /opt/
ADD {{ name }}* /opt/
ADD init.sh /opt/
ADD health_check.sh /opt/
HEALTHCHECK --interval=10s --timeout=3s --retries=5 CMD /bin/bash /opt/health_check.sh
CMD ["/bin/bash","/opt/init.sh"]
