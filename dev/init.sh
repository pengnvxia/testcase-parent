service sshd start
source /etc/profile
cd /opt
java -jar ./{{ name }}-0.0.1-SNAPSHOT.jar &
tail -f /etc/hosts
