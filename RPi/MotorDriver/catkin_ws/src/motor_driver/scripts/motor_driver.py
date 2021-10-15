#!/usr/bin/python

import rospy
from geometry_msgs.msg import Twist

import zumo_lib as motors

class MotorController:
    def __init__(self):
        self.sub = rospy.Subscriber("motors/motor_twist", Twist, self.callback)
        self.vel_max = 200
        motors.init_i2c_bus()

    
    def callback(self, twist):
        l_val = (twist.linear.x + twist.angular.z)/2
        r_val = (twist.linear.x - twist.angular.z)/2

        motors.send_speed(l_val, r_val)


def main():
    rospy.init_node('motor_driver')
    obc = MotorController()
    try:
        rospy.spin()
    except KeyboardInterrupt:
        print("Shutting down")


if __name__ == '__main__':
    main()
