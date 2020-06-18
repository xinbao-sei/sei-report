# Docker for java sei-report

# Base image oracle jdk8
FROM frolvlad/alpine-java:latest

# Author
LABEL maintainer="brianhsiung@outlook.com"

# Environment 此处需要根据实际情况修改APP_NAME
ENV JAVA_OPTS="" APP_NAME="sei-report"

# Timezone
RUN rm -rf /etc/localtime && ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# Application
ADD /build/libs/$APP_NAME.jar $APP_NAME.jar

# Port
EXPOSE 8080

# Launch the application
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar $APP_NAME.jar --server.servlet.context-path=/$APP_NAME --server.port=8080"]