# MardanBot Platform #

## Rpi configurations (still editing)
- [Access Point configuration](https://www.raspberrypi.com/documentation/computers/configuration.html#setting-up-a-routed-wireless-access-point) (Follow the **Setting up a Bridged Wireless Access Point** section)
- [Docker for Rpi](https://dev.to/elalemanyo/how-to-install-docker-and-docker-compose-on-raspberry-pi-1mo)

## Computer Tools:
- Visual Studio Code:
  - SSH Plugin
- Git
- Docker

# Android App
## Steps for Windows Users: ##
Open PowerShell and type the following commands inside:
- To get the JetsonBotApp project:
```
cd ~
git clone https://github.com/aldajo92/UNAL_TDG_AutonomousVehiclePlatform_AndroidApp.git
cd UNAL_TDG_AutonomousVehiclePlatform_AndroidApp
```

Finally open Project with Android Studio

# ROS And NodeJS
## Steps Windows Users: ##

Open PowerShell and type the following commands inside:
- To get the ROS_NodeJS project:
```
cd ~
git clone https://github.com/aldajo92/ROS_NodeJS.git
cd ROS_NodeJS
```
Open this project with Visual Studio Code
- Check the computer ip:
```
ipconfig
```

- Building the container:
```
cd ~/ROS_NodeJS
docker build -t ros_nodejs ${PWD}
```

- Running the container:
```
docker run -it -p 5170:5170 --name=ros_nodejs --volume ${PWD}/catkin_ws:/catkin_ws --volume ${PWD}/nodejs_ros_server:/nodejs_ros_server --rm ros_nodejs
```

# Diagrams for illustration #
## Computers:
![](./.media/MardanDiagram.jpeg)

## Embedded Computers:
![](./.media/MardanDiagram2.jpeg)
