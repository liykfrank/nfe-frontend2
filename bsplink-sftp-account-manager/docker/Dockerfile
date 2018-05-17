# Based on the SSH CentosOS 7 Dockerfile:
# https://github.com/CentOS/CentOS-Dockerfiles/tree/master/ssh/centos7

FROM centos:centos7

RUN yum -y update; yum clean all
RUN yum -y install openssh-server passwd java-1.8.0-openjdk; yum clean all

RUN mkdir /var/run/sshd
RUN ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key -N '' 

# Installation of sftp account manager scripts and initialization.
ADD ./shell ./sftp-am-scripts
RUN chmod 755 ./sftp-am-scripts/install.bash
RUN ./sftp-am-scripts/install.bash -y
RUN rm -r ./sftp-am-scripts

ADD ./start.bash ./start.bash
ADD ./bsplink-sftp-account-manager*jar ./

ENV APP_FAKE_PASSWORD_INITIALIZER=PhrW7XN9
ENTRYPOINT ["/start.bash"]
