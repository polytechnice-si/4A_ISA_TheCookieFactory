FROM tomee:8-jdk-7.0.1-plus
MAINTAINER Sébastien Mosser (mosser@i3s.unice.fr)

# Working inside the local TomEE system
WORKDIR /usr/local/tomee/

# Loading the executable server inside the image
COPY ./tcf-backend.war ./webapps/.

# Creating the tomee/eemot user to access to the web admin console
COPY ./resources/tomcat-user.xml ./conf/tomcat-users.xml

# Allow one to access to the manager from outside the container
COPY ./resources/manager-context.xml ./webapps/manager/META-INF/context.xml

# Configure the link to the external partners
COPY ./resources/bank.properties ./WEB-INF/classes/bank.properties
RUN ["jar", "uvf", "./webapps/tcf-backend.war", "./WEB-INF/classes/bank.properties"]

HEALTHCHECK --interval=5s CMD curl --fail http://localhost:8080/ || exit 1

# exposing the 8080 port to support external connections
EXPOSE 8080
