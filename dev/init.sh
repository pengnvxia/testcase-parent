service sshd start
cd /opt
java -Xmx1536m -Xms1536m -Xmn1024m -jar ./testcase-0.0.1-SNAPSHOT.jar &
tail -f /etc/hosts
