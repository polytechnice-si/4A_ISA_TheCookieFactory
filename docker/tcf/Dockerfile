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
ENV bank_host=localhost
ENV bank_port=9090

# Copy the startup script
COPY ./resources/start-tcf.sh .
RUN ["chmod", "u+x", "./start-tcf.sh"]

# Creating an health check verification to check TomEE status
HEALTHCHECK --interval=5s CMD curl --fail http://localhost:8080/ || exit 1

# exposing the 8080 port to support external connections
EXPOSE 8080

# Starting the service
ENTRYPOINT ["./start-tcf.sh"]
