FROM ros:melodic-ros-base

RUN apt-get update

RUN apt-get install -y python-catkin-tools python3-dev python3-pip python3-catkin-pkg-modules python3-numpy python3-yaml ros-melodic-cv-bridge \
    python3-opencv

RUN pip3 install rospkg catkin_pkg

# RUN cd /usr/lib/aarch64-linux-gnu && sudo ln -s libboost_python3.a libboost_python36.a && sudo ln -s libboost_python3.so libboost_python36.so

COPY ./ros_entrypoint.sh /
RUN chmod +x /ros_entrypoint.sh
ENTRYPOINT ["/ros_entrypoint.sh"]