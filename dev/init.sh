service sshd start
source /etc/profile
cd /opt
java -Xmx1536m -Xms1536m -Xmn1024m -jar ./{{ name }}-0.0.1-SNAPSHOT.jar &
tail -f /etc/hosts
