FROM java:8-jdk

WORKDIR /code
ADD . /code

RUN ./gradlew build

RUN mv build/dist/edustor-telegram.jar .

CMD java -jar edustor-telegram.jar