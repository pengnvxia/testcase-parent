service sshd start
source /etc/profile
cd /opt
java -Xmx3072m -Xms3072m -Xmn2048m -jar ./{{ name }}-1.0.0-RELEASE.jar &
tail -f /etc/hosts
