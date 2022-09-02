#!/usr/bin/env python3

import rospy
from geometry_msgs.msg import Point

import math
import time
import pantilthat

class PanTiltNode:

    def __init__(self):
        self._rate = rospy.Rate(1)

        self.subscriber = rospy.Subscriber(
            'pan_tilt/values',
            Point,
            queue_size=1,
            callback=self.values_received
        )
    
    def values_received(self, point):
        pantilthat.pan((point.x * 50) - 12)
        pantilthat.tilt((point.y * 50) - 24)


if __name__ == '__main__':
    rospy.init_node('pan_tilt')
    pantilthat.pan(-12)
    pantilthat.tilt(-24)

    navigation = PanTiltNode()

    rospy.spin()

# print("hello world from python3")

# counter = 0
# while counter < 1000:
#     # Get the time in seconds
#     t = time.time()

#     # G enerate an angle using a sine wave (-1 to 1) multiplied by 90 (-90 to 90)
#     a = math.sin(t * 2) * 90
    
#     # Cast a to int for v0.0.2
#     a = int(a)

#     pantilthat.pan(a)
#     pantilthat.tilt(a)

#     # Two decimal places is quite enough!
#     # print(round(a,2))

#     # Sleep for a bit so we're not hammering the HAT with updates
#     time.sleep(0.005)
#     counter = counter + 1