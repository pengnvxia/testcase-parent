#删除之前存活的镜像
docker rm -f `docker ps -a |grep {{ name }}-prod|awk '{print $1}'`
time=`date +%F-%H-%M-%S`
#替换文件
sed -i "s#spring.profiles.active=local#spring.profiles.active=prod#g" /{{ name }}/application.properties
#进入目录
cd /{{ name }}
#构建镜像
docker build -t {{ name }}-prod:$time ./
#运行镜像
docker run -d -p 8080:8080 -v /etc/localtime:/etc/localtime:ro -v /data/{{ name }}/:/opt/log/ -m 3276M {{ name }}-prod:$time
#删除文件
rm -rf /{{ name }}/*