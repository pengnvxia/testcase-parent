service sshd start
source /etc/profile
java -jar /opt/{{ name }}-0.0.1-SNAPSHOT.jar &
tail -f /etc/hosts
