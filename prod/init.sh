service sshd start
source /etc/profile
java -jar /opt/{{ name }}-1.0.0-RELEASE.jar &
tail -f /etc/hosts
