## TianYan_AndroidCDS

中科天演子模块CDS模块

AndroidCDS系统部署手册
====
一、	硬件安装
-------
AndroidCDS所需要的硬件有，Android手机，LpmsB手环，服务器，雄迈IPC相机。
说明，Android手机，服务器，雄迈IPC相机必须处于同一个网段内。LpmsB手环通过手机蓝牙进行配对处理。雄迈IPC相机通过POE交换机与服务器和Android手机组成局域网。
　　1、	雄迈IPC的布线安装。
雄迈IPC相机通过POE交换机与服务器相连。如下图所示。![](https://i.imgur.com/LJRorkY.jpg)
　　　　POE交换机最右侧的RJ45网口为输入口，左侧的四个RJ45网口为交换机口。通过上一级路由器或者普通交换机接入一根线到这个POE交换机的右侧RJ45网口，左侧4个RJ45网口接雄迈IPC相机。![](https://i.imgur.com/f3AopgF.jpg)
　　2、	雄迈IPC的配置。
　　　　　a)	运行“DeviceManage.exe”这个软件。点击“搜索设备”按钮，搜索可用的雄迈IPC相机。![](https://i.imgur.com/I4IPsqp.png)![](https://i.imgur.com/jl98VYb.png)
　　　　　b)	选中已搜索到的IPC相机，在右侧可以修改其IP地址。
　　　　　　　最重要的操作是，将所选择的IPC相机的“IP自适应”功能关闭。否则它会自动更改IP地址。![](https://i.imgur.com/O91R5ej.png)
二、	服务端
-------
　　1、	Nvidia显卡驱动
　　　　　a)	如果是新系统就安装g++，gcc，cmake
　　　　　　　　　　```sudo apt-get install g++ gcc cmake```
　　　　　b)	如果系统内已经用apt方式安装了Nvidia显卡驱动，先卸载
　　　　　　　　　　```sudo apt-get purge nvidia*```
　　　　　c)	禁用自带的nouveau驱动
　　　　　　　　　　```sudo gedit /etc/modprobe.d/blacklist-nouveau.conf```
　　　　　　　将以下内容粘贴入刚才编辑的文件内
　　　　　　　　　　```blacklist nouveau options nouveau modeset=0```
　　　　　　　更新：
　　　　　　　　　　```sudo update-initramfs -u```
　　　　　　　重启系统，通过命令
　　　　　　　　　　```lsmod | grep nouveau```
　　　　　　　查看是否禁用成功。如果没有输出则禁用成功
　　　　　d)	进入命令行
　　　　　　　按```Ctrl+Alt+F1```进入命令行模式（不只是F1；F2，F3，F4都可以）
　　　　　　　依次输入以下命令
　　　　　　　　　　```sudo service gdm stop```
　　　　　　　　　　```sudo service gdm3 stop```
　　　　　　　进入“NVIDIA-Linux-x86_64-410.78.run”下载目录
　　　　　　　　　　```sudo ./NVIDIA-Linux-x86_64-410.78.run```
　　　　　　　根据提示安装显卡驱动即可，安装完成后输入```nvidia-smi```查看驱动是否正确安装。如果正确安装之后再依次输入以下命令（都是在命令行模式下输入，如果跳出命令行模式，请重新进入命令行模式），输入完成后，重启系统即可
　　　　　　　　　　```sudo service gdm3 start```
　　　　　　　　　　```sudo service gdm start```
　　2、	Cuda（9.2）
　　　　　a)	进入Cuda下载目录
　　　　　　　　　　```sudo ./cuda_9.2.148_396.37_linux.run```
　　　　　　　这里会出现More（0%），意思是你要阅读这些协议，连续按空格键可跳过，按照提示输入accept，第一个安装的时候，是安装图形驱动的，选择n，不安装英伟达驱动。有一个是否需要链接到“```/usr/local/cuda```”选择n，不进行链接。除了这两个，其他的全选y，并建议选择默认安装路径，即“```/usr/local/cuda-9.2```”其他版本也可。
　　　　　b)	环境变量的配置
　　　　　　　用gedit命令打开bashrc文件
　　　　　　　　　　```sudo gedit ~./bashrc```
　　　　　　　将以下内容，粘贴至文本最后
　　　　　　　　　　```#cuda 9.2```
　　　　　　　　　　```export PATH=/usr/local/cuda-9.2/bin/${PATH:+:$PATH}```
　　　　　　　　　　```export LD_LIBRARY_PATH=/usr/local/cuda-9.2/lib64/${LD_LIBRARY_PATH:+:$LD_LIBRARY_PATH}```
　　　　　　　　　　```export LIBRARY_PATH=$LIBRARY_PATH:/usr/local/cuda-9.2/lib64 ```
　　　　　　　保存后，更新文件
　　　　　　　　　　```source ~/.bashrc```
　　　　　c)	查看一下cuda是否正确安装
　　　　　　　　　　```nvcc -V```
　　3、	Cudnn（7.3.1）
　　　　　a)	先在官网https://developer.nvidia.com/rdp/cudnn-archive上下载最新的CuDnn文件,例如cudnn-9.2-linux-x64-v7.3.1，并解压
　　　　　b)	进入解压后的目录后，执行如下命令：
　　　　　　　　　　```cd cuda/include```
　　　　　　　　　　```sudo cp cudnn.h /usr/local/cuda-9.2/include```       \#复制头文件
　　　　　c)	再将进入lib64目录下的动态文件进行复制和链接
　　　　　　　　　　```cd ../lib64```
　　　　　　　　　　```sudo cp lib* /usr/local/cuda-9.2/lib64/```  \#复制动态链接库
　　　　　　　　　　```cd /usr/local/ cuda-9.2/lib64/```
　　　　　　　　　　```sudo rm -rf libcudnn.so libcudnn.so.7```  \#删除原有动态文件
　　　　　　　　　　```sudo ln -s libcudnn.so.7.3.1 libcudnn.so.7```  \#生成软衔接
　　　　　　　　　　```sudo ln -s libcudnn.so.7 libcudnn.so```     \#生成软链接
　　　　　　　　　　```sudo ldconfig```    \#使配置生效
　　　　　d)	查看cudnn是否正确安装
　　　　　　　　　　```cat /usr/local/cuda/include/cudnn.h | grep CUDNN_MAJOR -A 2```
　　　　　e)	注意：在使用CUDA的过程中，常会遇到以下问题：
　　　　　　　　　　```Error: libcudart.so.9.0: cannot open shared object file: No such file or directory```
　　　　　　　　　　// 或者
　　　　　　　　　　```Error: libcusolver.so.9.0: cannot open shared object file: No such file or 　　　　　　　　　　direcctory```
　　　　　　　　　　// 或者
　　　　　　　　　　```Error: libcublas.so.9.0: cannot open shared object file: No such file or directory```
　　　　　　　大部分情况下是环境没有配好，运行以下命令
　　　　　　　　　　```sudo cp /usr/local/cuda-9.2/lib64/libcudart.so.9.2 /usr/local/lib/libcudart.so.9.2 && sudo ldconfig && cp /usr/local/cuda-9.2/lib64/libcublas.so.9.2 /usr/local/lib/libcublas.so.9.2 && sudo ldconfig && cp /usr/local/cuda-9.2/lib64/libcurand.so.9.2 /usr/local/lib/libcurand.so.9.2 && sudo ldconfig```
　　　　　　　一般情况下，这种解法可以搞定问题的；如果还是报错libcusolver.so.9.0不存在，下面是算是一种解法：
　　　　　　　　　　```sudo ldconfig /usr/local/cuda/lib64```
　　4、	OpenCV-3.4.1
　　　　　a)	安装官方给的opencv依赖包
　　　　　　　　　　```sudo apt-get install build-essential cmake git libgtk2.0-dev pkg-config libavcodec-dev libavformat-dev libswscale-dev python-dev python-numpy libtbb2 libtbb-dev libjpeg-dev libpng-dev libtiff-dev libdc1394-22-dev cmake-qt-gui```
　　　　　b)	将OpenCV3.4.1和OpenCV_contrib-3.4.1解压（提取）
　　　　　　　双击进入解压出来的OpenCV3.4.1文件夹，右键打开终端，然后依次输入（不要忘了第三行的最后的空格和两个点）
　　　　　　　　　　```mkdir build```
　　　　　　　　　　```cd build```
　　　　　　　　　　```cmake-gui .. ```
　　　　　c)	然后会弹出CMake的图形化界面，在上方的两个路径里面，选择好代码所在文件夹的路径和要安装的路径，点击左下方的Configure按钮，选择Unix Makefiles，选择Use default native compilers（默认），然后点击Finish，需要下载一些文件，要等待一段时间。然后CMake即载入默认配置
　　　　　d)	这里需要对两个地方进行修改：
　　　　　　　搜索```CMAKE_BUILD_TYPE```值处输入```RELEASE```，其他保持不变（如果已经存在就不必修改）。
　　　　　　　在```OPENCV_EXTRA_MODULES_PATH```处，选择输入目录（单击这一行后方空白“…”即可选中），然后选择opencv_contrib-3.4.1文件夹中的modules文件夹，注意，不是只选中opencv_contrib-3.4.1文件夹就好了，需要选中里面的modules文件夹！
　　　　　e)	点击Generate生成配置文件，这一步应该比较快就完成了。
　　　　　　　接着，在build目录下打开终端，输入
　　　　　　　　　　```make```
　　　　　　　　　　```sudo make install```
　　　　　　　当你执行完上面两行命令的时候，并看到100%的时候，恭喜你！安装成功
　　　　　f)	配置环境变量
　　　　　　　安装成功后还需要设置opencv的环境变量。打开文件：
　　　　　　　　　　```sudo gedit /etc/ld.so.conf.d/opencv.conf```
　　　　　　　将以下内容添加到最后：
　　　　　　　　　　```/usr/local/lib```
　　　　　　　接下来配置库：
　　　　　　　　　　```sudo ldconfig```
　　　　　　　更改环境变量：
　　　　　　　　　　```sudo gedit /etc/bash.bashrc```
　　　　　　　在文件后添加：
　　　　　　　　　　```PKG_CONFIG_PATH=$PKG_CONFIG_PATH:/usr/local/lib/pkgconfig ```
　　　　　　　　　　```export PKG_CONFIG_PATH```
　　　　　　　保存退出，在运行下面的例程之前，需要重新开启终端来使配置生效。
　　　　　　　到此，安装和配置的整个过程都完成了！
　　　　　g)	测试
　　　　　　　在终端运行命令
　　　　　　　　　　```pkg-config --cflags --libs opencv ```
　　　　　　　出现下面信息：
　　　　　　　　　　```-I/usr/local/include/opencv -I/usr/local/include -L/usr/local/lib -lopencv_shape -lopencv_stitching -lopencv_objdetect -lopencv_superres -lopencv_videostab -lopencv_calib3d -lopencv_features2d -lopencv_highgui -lopencv_videoio -lopencv_imgcodecs -lopencv_video -lopencv_photo -lopencv_ml -lopencv_imgproc -lopencv_flann -lopencv_core ```
　　5、	python和python3
　　　　　a)	Ubuntu系统安装完成后，自带python和python3，但是没有集成pip和pip3
　　　　　　　　　　```sudo apt-get install python-pip python3-pip```
　　　　　　　安装完成后，升级一下
　　　　　　　　　　```python -m pip install --upgrade pip```
　　　　　　　　　　```python3 -m pip install --upgrade pip```
　　　　　b)	升级完成后出现```ImportError: cannot import name main```
　　　　　　　解决：pip文件在/usr/bin目录下，cd进去。分别修改pip和pip3，都是同样的修改方式。
　　　　　　　　　　```cd /usr/bin/```
　　　　　　　　　　```sudo gedit pip```
　　　　　　　和
　　　　　　　　　　```sudo gedit pip3```
　　　　　　　把下面的三行
　　　　　　　　　　```from pip import main```
　　　　　　　　　　```if __name__ == '__main__': ```
　　　　　　　　　　```　　sys.exit(main())```
　　　　　　　换成下面的三行
　　　　　　　　　　```from pip import __main__```
　　　　　　　　　　```if __name__ == '__main__': ```
　　　　　　　　　　```　　sys.exit(__main__._main())```
　　　　　　　然后问题就解决了。
　　　　　c)	python换源
　　　　　　　默认pip是使用python官方的源，但是由于国外官方源经常被墙，导致不可用，我们可以使用国内的python镜像源，从而解决python安装不上库的烦恼。
　　　　　　　　　　```cd ~```
　　　　　　　　　　```mkdir .pip```
　　　　　　　　　　```cd ~/.pip/```
　　　　　　　　　　```gedit pip.conf```
　　　　　　　将以下文本粘贴入文本内。保存即可。
　　　　　　　　　　``` [global] ```
　　　　　　　　　　``` index-url = https://pypi.tuna.tsinghua.edu.cn/simple```
　　　　　d)	安装一些其他的AndroidCDS所需要的python库
　　　　　　　　　　```sudo pip3 install flask```
　　　　　　　　　　```sudo pip3 install opencv-python```
　　6、	OpenFace
　　　　　a)	安装依赖
　　　　　　　　　　```sudo apt-get install git cmake libboost-dev libboost-python-dev python-opencv luarocks```
　　　　　b)	下载OpenFace代码
　　　　　　　　　　```git clone https://github.com/cmusatyalab/openface.git```
　　　　　c)	下载完成后，开始安装OpenFace的依赖库
　　　　　　　　　　```cd openface```
　　　　　　　　　　```sudo pip install -r requirements.txt```
　　　　　　　　　　```sudo pip install dlib```
　　　　　　　　　　```sudo pip install matplotlib```
　　　　　　　安装python3版的
　　　　　　　　　　```sudo pip3 install -r requirements.txt```
　　　　　　　　　　```sudo pip3 install dlib```
　　　　　　　　　　```sudo pip3 install matplotlib```
　　　　　　　如果python3版的第一步“requirements.txt”内的库没有安装成功，就打开文档然后单独安装里面的每一个库
　　　　　d)	安装 TORCH—科学计算框架，支持机器学习算法　
　　　　　　　　　　```git clone https://github.com/torch/distro.git ~/torch --recursive```
　　　　　　　　　　```cd torch```
　　　　　　　　　　```vim install-deps```
　　　　　　　修改```install-deps```的第178行，将“```python-software-properties```”替换为“```software-properties-common```”修改完成后，就可以执行下面的命令了
　　　　　　　　　　```bash install-deps```
　　　　　　　提示安装完torch的依赖包之后，首先禁用cuda的浮点运算符。原因是cuda和torch的头文件都提供了相同的重载运算符，编译器不知道用哪一个。输入下面shell命令禁止使用cuda的头文件编译torch
　　　　　　　　　　```export TORCH_NVCC_FLAGS="-D__CUDA_NO_HALF_OPERATORS__"```
　　　　　　　然后再开始安装Torch
　　　　　　　　　　```./install.sh```
　　　　　　　提示输入yes的时候就说明torch已经正确安装完成，输入yes打回车即可。然后将torch加入环境变量就完成torch的安装了。
　　　　　　　　　　```source ~/.bashrc```
　　　　　e)	安装依赖的 LUA库
　　　　　　　　　　```luarocks install dpnn```
　　　　　　　下面的为选装，有些函数或方法可能会用到
　　　　　　　　　　```luarocks install image```
　　　　　　　　　　```luarocks install nn```
　　　　　　　　　　```luarocks install graphicsmagick```
　　　　　　　　　　```luarocks install torchx```
　　　　　　　　　　```luarocks install csvigo```
　　　　　f)	编译安装OpenFace代码
　　　　　　　　　　```python setup.py build```
　　　　　　　　　　```sudo python setup.py install```
　　　　　　　编译安装python3版的OpenFace代码
　　　　　　　　　　```python3 setup.py build```
　　　　　　　　　　```sudo python3 setup.py install```
　　　　　g)	验证是否安装完成
　　　　　　　打开一个终端，输入```python```进入命令行版的python，输入
　　　　　　　　　　```import openface```
　　　　　　　如果没有错误，就证明已正确安装
　　　　　　　打开一个终端，输入```python3```进入命令行版的python3，输入
　　　　　　　　　　```import openface```
　　　　　　　如果没有错误，就证明已正确安装
　　　　　h)	下载预训练后的数据
　　　　　　　　　　```sh models/get-models.sh```
　　　　　　　　　　```wget https://storage.cmusatyalab.org/openface-models/nn4.v1.t7 -O models/openface/nn4.v1.t7```
　　　　　　　可以用compare.py(demo文件夹中)给出的示例检测两张脸的相近程度。
　　　　　　　　　　```./demos/compare.py images/examples/{lennon*,clapton*}```
　　7、	MySQL（5.7.23）
　　　　　a)	下载“```mysql-5.7.23-linux-glibc2.12-x86_64.tar.gz```”到```/home/humanmotion/```目录下，解压到当前文件夹。
　　　　　　　　　　```tar zxvf mysql-5.7.23-linux-glibc2.12-x86_64.tar.gz```
　　　　　　　　　　```sudo mv mysql-5.7.23-linux-glibc2.12-x86_64 /usr/local```
　　　　　　　　　　```sudo ln -s /usr/local/mysql-5.7.23-linux-glibc2.12-x86_64/ /usr/local/mysql```
　　　　　b)	添加至环境变量内
　　　　　　　　　　```gedit ~/.bashrc```
　　　　　　　将以下内容添加至文末
　　　　　　　　　　```export PATH=$PATH:/usr/local/mysql/bin```
　　　　　　　保存并退出，然后更新一下系统
　　　　　　　　　　```source ~/.bashrc```
　　　　　c)	安装依赖库
　　　　　　　　　　```sudo apt-get install libaio1```
　　　　　d)	配置权限
　　　　　　　　　　　　\#添加用户组
　　　　　　　　　　```sudo groupadd mysql```
　　　　　　　　　　　　\#添加用户，这个用户是不能登录的
　　　　　　　　　　```sudo useradd -r -g mysql -s /bin/false mysql```
　　　　　　　　　　　　\#进入文件目录，mysql是链接
　　　　　　　　　　```cd /usr/local/mysql```
　　　　　　　　　　　　\#新建文件夹
　　　　　　　　　　```sudo mkdir mysql-files```
　　　　　　　　　　　　\#修改文件夹的权限 	
　　　　　　　　　　```sudo chmod 750 mysql-files```
　　　　　　　　　　```sudo chown -R mysql . ```
　　　　　　　　　　```sudo chgrp -R mysql . ```
　　　　　e)	安装初始化，注意：此部最后一行会有一个初始化密码，用于root账号的首次登录
　　　　　　　　　　```sudo bin/mysqld --initialize --user=mysql```
　　　　　f)	生成证书
　　　　　　　　　　```sudo bin/mysql_ssl_rsa_setup```
　　　　　g)	把权限修改回来
　　　　　　　　　　```sudo chown -R root . ```
　　　　　　　　　　```sudo chown -R mysql data mysql-files```
　　　　　h)	启动在后台
　　　　　　　　　　```sudo bin/mysqld_safe --user=mysql```
　　　　　i)	登录测试并修改root密码
　　　　　　　　　　```mysql -uroot -p```
　　　　　　　　　　提示：密码在安装初始化时最后一行的信息，里面有括号和特殊字符。
　　　　　　　　　　\#修改root密码，每一个分号直接回车
　　　　　　　　　　```SET PASSWORD = PASSWORD('新密码'); ```
　　　　　　　　　　比如    ```SET PASSWORD = PASSWORD('humanmotion'); ```
　　　　　　　　　　```ALTER USER 'root'@'localhost' PASSWORD EXPIRE NEVER;```
　　　　　　　　　　```flush privileges; ```
　　　　　　　　　　\#增加一个'root'@'%'账号实现远程登录
　　　　　　　　　　```grant all privileges on *.* to 'root'@'%' identified by '新密码' with grant option; ```
　　　　　　　　　　比如    ```grant all privileges on *.* to 'root'@'%' identified by 'humanmotion' with grant option; ```
　　　　　j)	由于还未找到Ubuntu18.04手动添加开机自启动的方式，mysql服务现在是开机之后需要手动重启的。以下是　　每次开机启动系统后的手动启动mysql服务的方式。
　　　　　　　依次打以下命令即可重启mysql服务。
　　　　　　　　　　```cd /usr/local/mysql```
　　　　　　　　　　```sudo bin/mysqld --initialize --user=mysql```
　　　　　　　　　　```sudo bin/mysqld_safe --user=mysql```
　　　　　　　也可将这三条命令放入一个shell文件内，每次开机之后运行这个shell文件即可。操作如下。
　　　　　　　　　　```cd ~```
　　　　　　　　　　```gedit mysql.sh```
　　　　　　　将那三条命令放入文本内保存并退出，之后给刚创建的shell文本赋予可执行权限。
　　　　　　　　　　```chmod +x mysql.sh```
　　　　　　　下次开机之后，直接```./mysql.sh```即可。
　　　　　k)	Linux下手动导入sql文件。
　　　　　　　启动mysql之后，创建对应的数据库，并对它进行操作。
　　　　　　　　　　```mysql -uroot -p```
　　　　　　　输入密码后首先创建一个同名数据库。
　　　　　　　　　　```create database 你的数据库名; ```
　　　　　　　　　　比如   ```create database workdatabase; ```
　　　　　　　　　　```use workdatabase; ```
　　　　　　　　　　```set names utf8; ```
　　　　　　　　　　再通过source命令将数据库条目导入到数据库内。
　　　　　　　　　　```source 你的数据库文件路径; ```
　　　　　　　　　　比如   ```source /home/humanmotion/workdatabase.sql; ```
　　　　　l)	安装SQLClient（python3支持的）
　　　　　　　安装依赖库
　　　　　　　　　　```sudo apt-get install libmysqlclient-dev```
　　　　　　　　　　```sudo pip3 install mysqlclient```
　　　　　　　　　　在python3中```import MySQLdb```没有错误就说明正确安装了
　　8、	谷歌文字识别Tesseract
　　　　　　　　　　```sudo pip3 install pytesseract```
　　　　　　　　　　```sudo apt-get install tesseract-ocr libtesseract-dev tesseract-ocr-chi-sim```
　　9、	雄迈“xmcext.so”库文件的编译和生成
　　　　　　　进入项目的```OtherTools\XM_IPC\PyCExt\```目录下
　　　　　　　　　　```mkdir build```  如果有build文件夹就不用再次创建
　　　　　　　　　　```cd build```
　　　　　　　　　　```rm -rf *```
　　　　　　　　　　```cmake .. ```
　　　　　　　　　　```make```
　　　　　　　make之后会在```OtherTools\XM_IPC\PyCExt\```目录下生成```xmcext.so```库文件，就可以用了
　　10、	AndroidCDS的服务端运行时需要修改“```lpms_data_server.py```”文件内的本机IP地址![](https://i.imgur.com/3hGcehC.png)
　　11、	运行AndroidCDS服务端
　　　　　　　在“```AndroidCDS_Server```”文件夹下，右单击，打开终端。
　　　　　　　或者```cd TianYan_AndroidCDS/AndroidCDS_Server```
　　　　　　　　　　```python3 robot_server.py ```
　　　　　　　　　　```python3 lpms_data_server.py```
三、	客户端
-------
　　1、	Android Studio
　　　　　　　下载“```android-studio-ide-173.4907809-linux.zip```”到“```/home/humanmotion/```”目录下，并解压到当前目录
　　　　　　　　　　```cd android-studio-ide-173.4907809-linux```
　　　　　　　　　　```mv android-studio ~ /android-studio```
　　　　　　　下载“```AndroidSDK```” 到“```/home/humanmotion/```”目录下，并解压到当前目录
　　　　　　　　　　```cd ~/android-studio/bin```
　　　　　　　　　　```./studio.sh```
　　　　　　　开始配置AndroidStudio，当配置到AndroidSDK时，设置其SDK路径为刚才“```AndroidSDK```”解压出来的路径
　　2、	下载“```opencv_contrib-3.4.1.zip```” 到“```/home/humanmotion/```”目录下，，并解压到当前目录
　　3、	选择“```Open an existing Android Studio project```”，打开“```AndroidCDS_Client```”。修改以下两处位置的内容。
　　　　　a)	运行前首先要引入“openCVLibrary341”进入项目里；
　　　　　　　　　　```File->New->Import Module```
　　　　　　　选择刚才解压的“```opencv_contrib-3.4.1```”目录里的```sdk/java```文件夹。
　　　　　b)	修改与科大智能机器人通信的URL地址以及与后台服务端的通信URL地址```AndroidCDS_Client\app\src\main\java\ac\ict\humanmotion\abracadabra\Interface```下的```URLAPI.java```文件内的```dataurl```和```roboturl```，```dataurl```为“AndroidCDS”服务端的IP地址，```roboturl```为与科大智能机器人通信的地址![](https://i.imgur.com/35IDP6X.png)
　　　　　　　修改```OpenCV-Android-SDK```配置文件，将```OpenCV-Android-SDK```解压到一个目录内
　　　　　　　修改```AndroidCDS_Client\app```下的“```CMakeLists.txt```”文件内的“```include_directories```”和“```set_target_properties```”的值为解压的OpenCV-Android-SDK的路径 ![](https://i.imgur.com/kgOLLOc.png)
　　　　　　　修改```AndroidCDS_Client\app```下的```build.gradle```文件内的“```jniLibs.srcdirs```”的值为解压的```OpenCV-Android-SDK```的路径 ![](https://i.imgur.com/pKJEmeZ.png)
　　4、	点击![](https://i.imgur.com/oaWf65p.png)　重新gradle一下项目，就可以识别为App项目了。然后插入Android手机，点击![](https://i.imgur.com/X8Mgnat.png)　运行至手机即可。
