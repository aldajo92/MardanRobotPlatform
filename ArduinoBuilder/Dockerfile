FROM ubuntu:xenial

RUN apt-get update && apt-get install -y \
    git \
    arduino-mk

RUN apt install wget zip -y

RUN mkdir /arduinoLibraries

RUN cd /arduinoLibraries/ && wget https://github.com/pololu/zumo-shield-arduino-library/archive/refs/tags/2.0.0.zip

RUN cd /arduinoLibraries/ && unzip 2.0.0.zip && mv zumo-shield-arduino-library-2.0.0 ZumoShield

CMD ["bash"]