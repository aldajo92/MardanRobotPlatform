#!/bin/bash
set -e

# setup ros environment
source "/opt/ros/$ROS_DISTRO/setup.bash"

# INSTALL_FOLDER=/python3_ws/install
# if !([ -d "$INSTALL_FOLDER" ]); then
#     cd /python3_ws \
#         && catkin clean --y \
#         && catkin init \
#         && catkin config --install -DPYTHON_EXECUTABLE=/usr/bin/python3 -DPYTHON_INCLUDE_DIR=/usr/include/python3.6m -DPYTHON_LIBRARY=/usr/lib/aarch64-linux-gnu/libpython3.6m.so -DBOOST_ROOT=/usr/include/boost/
#         && catkin config --install -DPYTHON_EXECUTABLE=/usr/bin/python3 -DPYTHON_INCLUDE_DIR=/usr/include/python3.6m -DBOOST_ROOT=/usr/include/boost/
#         # && catkin config --install

#     # Build
#     cd /python3_ws/ && catkin build cv_bridge
# fi

# # Extend environment with new package
# source /python3_ws/install/setup.bash --extend

# # cd /catkin_ws/ && catkin_make
# source /python3_ws/devel/setup.bash
exec "$@"