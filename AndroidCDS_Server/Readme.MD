# Installation
`pip install pytesseract`
`sudo apt install tesseract-ocr`
`sudo apt install libtesseract-dev`

运行环境：
Ubuntu 18.04

依赖的第三方库：
sudo pip3 install pytesseract
sudo apt install tesseract-ocr
sudo apt install libtesseract-dev
sudo apt install tesseract-ocr-chi-sim


opencv和contrib安装教程:
1.查找 opencv相关库或者链接；
dpkg -l | grep -i opencv

2.卸载过程
sudo apt remove libopencv-dev
sudo apt remove libopencv-dev
sudo apt autoremove
dpkg -l | grep -i opencv
sudo apt remove python-opencv
dpkg -l | grep -i opencv
sudo apt remove libopencv-*
dpkg -l | grep -i opencv

3.安装过程(参考的链接：https://blog.csdn.net/jindunwan7388/article/details/80397700)
4.另安装 python的安装模块
sudo apt-get install python-matplotlib


a
1. 编译
 g++ homography.cpp -o homography `pkg-config --cflags --libs opencv` -lpthread
 ./homography 

./homography ./Images/ee.png
 
g++ 1.cpp -o 1 -lpthread




