from xmcext import XMCamera
import os, time

image_dir = os.path.dirname(os.path.realpath(__file__)) + "/Temp"  # 路径不要修改
cp = XMCamera("10.41.0.199", 34567, "admin", "ludian@blq", image_dir)


def serachdevicebyIP():
    cp.PrintInfo()
    cp.login()
    #cp.close()
    cp.SearchDevice()


if __name__ == '__main__':
    serachdevicebyIP()