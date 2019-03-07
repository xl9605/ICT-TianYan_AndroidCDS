# ICT-XMNet-xmcamera
## 雄迈SDK下载地址（XM-SDK Download Url）
This is the [download page](https://download.xm030.cn/d/MDAwMDA3MzM= "Title") .
注意：这个SDK对linux的支持较差 提供的说明文档和demo不够详细

## SDK使用方法 （How to use the SDK）
> 0.   定义`video.cpp`文件所在的文件夹为根目录。
> 1.   将SDK压缩包中的lib文件夹下的所有内容拷贝到根目录中。
> 2.   将SDK压缩包中的incCn文件夹下的`HCNetSDK.h`头文件拷贝到根目录中。
> 3.   (可选)将`LinuxPlayM4.h`和`PlayM4.h`拷贝到根目录中。

## 目录结构（Files-Tree）
> 0.   Demo_HK_PY - 封装后的python调用demo 通过CV2对解析后的H264图像序列进行显示
> 1.   PyCExt - 接口封装源文件
> 2.   目录下其他文件 - 用于测试雄迈API

## 使用前的准备（Pre-Works）
> 0.   安装FFMPEG
> 1.   安装OPENCV
> 2.   安装Python（可选）

## 注意事项 （Cautious）
> 0.   使用FFMPEG对视频帧进行解码时需要进行适当偏移 具体参见回调函数中的处理
> 1.   不要在回调函数中执行耗时操作 否则会导致解码出现问题
> 2.   使用FFMPEG解码回调数据时尽量使用旧版接口进行处理

##XM_IPC
> 0.   雄迈摄像头IP被自动改变问题，由于官方未提供接口，所以未实现。
雄迈摄像头
重新编译
> 1.   如果有xmcext.so文件就直接make
如果没有，就在PyCExt目录下
rm CMakeCache.txt#这个是删除CMakeCache.txt
cmake .
make一下就可以
在PyXMCamera.cpp里写C++代码，636行“static PyMethodDef CXMCamera_MethodMembers[] =”写python和C++函数关系
官方API开发手册：“http://docs-open.xmeye.net/#/”
“SheBeiWangLuoSDK.zip”在Linux下解压缩是出现乱码，在windows下解压没有问题
> 2.   相关SDK查看时可能出现乱码，是因为文本文件保存时的字符集不是UTF-8导致，将其用win系统下的记事本（notepad）重新保存为“UTF-8”即可