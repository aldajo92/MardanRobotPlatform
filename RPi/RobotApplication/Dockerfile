FROM ros:melodic-ros-base

RUN echo "source /catkin_ws/devel/setup.bash" >> /root/.bashrc

COPY ./autostart.sh /
RUN chmod +x /autostart.sh
ENTRYPOINT /autostart.sh
# ENTRYPOINT /bin/bash