FROM wharf.gaikai.org/cgei-base/jdk11-minimal:2019022801
# run as nobody user
USER 65534
COPY target/data-producer.jar /tmp/data-producer.jar

CMD [ \
  "java", \
  "-jar", \
  "-Xmx512m", \
  "/tmp/data-producer.jar" \
]

