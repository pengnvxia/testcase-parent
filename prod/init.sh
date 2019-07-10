service sshd start
source /etc/profile
cd /opt
java -jar ./{{ name }}-1.0.0-RELEASE.jar &
tail -f /etc/hosts
