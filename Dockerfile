FROM maven:3.5.2-jdk-8

ENV PATH="/usr/lib/postgresql/9.6/bin:${PATH}"

RUN apt-get update && \
    apt-get install -y python-pip postgresql

COPY src /src
COPY pom.xml /pom.xml
COPY target /target

RUN mvn install -U

ADD http://ephemeralpg.org/code/ephemeralpg-2.5.tar.gz /src

RUN tar -xzf /src/ephemeralpg-2.5.tar.gz -C /src && rm /src/ephemeralpg-2.5.tar.gz
RUN pg_tmp=$(find /src -maxdepth 2 -type d -name '*ephemeralpg*') && echo $pg_tmp && make install -C $pg_tmp

RUN chown -R postgres:postgres /src
RUN chmod 777 /src
RUN chmod 777 /target

# Switch USER to non-root to run BasicTest
USER postgres

CMD [ "sh", "-c", "java -jar /target/medium-test-postgresql-java-1-jar-with-dependencies.jar" ]
