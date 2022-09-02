FROM ros_base_python_3

RUN pip3 install pantilthat

RUN apt-get install -y python3-smbus

COPY ./autostart.sh /
RUN chmod +x /autostart.sh
ENTRYPOINT /autostart.sh