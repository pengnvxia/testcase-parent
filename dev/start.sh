rm -rf /opt/docker/application*
rm -rf /opt/docker/{{ name }}-0.0.1-SNAPSHOT.jar
cd /{{ name }}
mv application* /opt/docker/
mv {{ name }}-0.0.1-SNAPSHOT.jar /opt/docker
sed -i "s#spring.profiles.active=local#spring.profiles.active=dev#g" /opt/docker/application.properties
time=`date +%F-%H-%M-%S`
cd /opt/docker/
docker build -t {{ name }}-dev:$time ./
docker rm -f `docker ps -a -q`
docker run -d -p 8080:8080 {{ name }}-dev:$time
