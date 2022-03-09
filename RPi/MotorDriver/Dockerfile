FROM ros:melodic-ros-base

# install ros tutorials packages
RUN apt-get update && apt-get install -y \
    tmux \
    && echo "source /catkin_ws/devel/setup.bash" >> /root/.bashrc

# RUN apt-get install python-smbus -y

COPY ./compile.sh /
RUN chmod +x /compile.sh
ENTRYPOINT /compile.sh

################################################################

FROM ros:melodic-ros-base

RUN apt-get update
RUN apt-get install python-smbus -y

COPY ./catkin_ws /catkin_ws
COPY ./autostart.sh /
ENTRYPOINT /autostart.sh

# ENTRYPOINT /bin/bash