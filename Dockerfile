FROM openjdk:11-jre
COPY target/ux-bcp-service-payment-*SNAPSHOT.jar /opt/ux-bcp-service-payment.jar
ENTRYPOINT ["java","-jar","/opt/ux-bcp-service-payment.jar"]