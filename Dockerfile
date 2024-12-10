FROM tomcat:11.0.1-jre21-temurin

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY build/libs/movie-review-project-1.0.war /usr/local/tomcat/webapps/ROOT.war

COPY src/main/resources/META-INF/context.xml /usr/local/tomcat/conf/context.xml

EXPOSE 8080

CMD ["catalina.sh", "run"]
