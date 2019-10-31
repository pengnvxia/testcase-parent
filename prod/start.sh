#替换文件
sed -i "s#spring.profiles.active=local#spring.profiles.active=prod#g" /{{ name }}/application.properties
#进入目录
cd /{{ name }}
#构建镜像
docker build -t {{ name }}-prod:$1 ./
#删除文件
rm -rf /{{ name }}/*