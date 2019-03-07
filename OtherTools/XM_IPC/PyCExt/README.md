# How to Use XMCamera with Python

# 编译生成xmcext.so
具体方法请见Pycext文件夹
也可以使用本目录下的pycext.so文件（python3 编译）

# 部署方式

文件结构如下所示 如果缺少so文件可能会发生107错误
.
├── CMakeLists.txt
├── libxmnetsdk.so
├── netsdk.h
├── pywrapper.cpp
├── PyXMCamera.cpp
└── README.md

# 简单使用
        python3
        from xmcext import XMCamera
        cp = XMCamera("IP",PORT,"USERNAME","PWD","DIR")
        cp.PrintInfo()

# 通过cv2连续显示H264-stream中图像

        from xmcext import XMCamera
        import os, time, cv2
        image_dir = os.path.dirname(os.path.realpath(__file__))+ "/Temp" # 路径不要修改
        cp = XMCamera("192.168.0.4",34567,"admin","",image_dir)

        cp.PrintInfo()
        cp.login()
        cp.open() # SDK preview start

        time.sleep(1)

        i = 0
        while i<200:
        cv2.imshow('stream',cv2.imread(cp.GetDetentionFrame()))
        cv2.waitKey(1)
        i = i + 1

        cv2.destroyAllWindows()

        time.sleep(2)

        print ("-------------switch-------------")

        # use native C++

        cp.StratParsingStream() # c++ native opencv preview start

        time.sleep(10)

        cp.StopParsingStream() # c++ native opencv preview stop
        cp.close() # SDK preview stop && free memory

# 其他接口
		{"GetIP", (PyCFunction)CXMCamera_GetIP, METH_NOARGS, "Get the IP address of instance."},
		{"GetPort", (PyCFunction)CXMCamera_GetPort, METH_NOARGS, "Get the port number of instance."},
		{"GetUserName", (PyCFunction)CXMCamera_GetUserName, METH_NOARGS, "Get the user name of isntance."},
		{"GetPassword", (PyCFunction)CXMCamera_GetPassword, METH_NOARGS, "Get the password of instance."},
		{"GetParameters", (PyCFunction)CXMCamera_GetParameters, METH_NOARGS, "Get all the properties of isntance."},
		{"SetParameters", (PyCFunction)CXMCamera_SetParameters, METH_VARARGS, "Set the properties of instance."},
		{"PrintInfo", (PyCFunction)CXMCamera_PrintInfo, METH_NOARGS, "Print all information of instance."},

		{"login", (PyCFunction)CXMCamera_Login, METH_NOARGS, "login the camera"},
		{"open", (PyCFunction)CXMCamera_Open, METH_NOARGS, "open the camera"},
		{"isopen", (PyCFunction)CXMCamera_IsOpen, METH_NOARGS, "is the camera opened."},
		{"close", (PyCFunction)CXMCamera_Close, METH_NOARGS, "close the camera."},
		{"read", (PyCFunction)CXMCamera_Read, METH_NOARGS, "query frame from camera."},
		{"queryframe", (PyCFunction)CXMCamera_QueryFrame, METH_VARARGS, "query frame from camera."},

		{"StratParsingStream", (PyCFunction)CXMCamera_StratParsingStreamWithFFmpeg, METH_NOARGS, "Strat Parsing Stream With FFmpeg."},
		{"StopParsingStream", (PyCFunction)CXMCamera_StopParsingStreamWithFFmpeg, METH_NOARGS, "Stop Parsing Stream With FFmpeg."},
		{"GetDetentionFrame", (PyCFunction)CXMCamera_GetDetentionFrame, METH_NOARGS, "Get Detention Frame (1500ms From Current)."},
		{"GetCurrentionFrame", (PyCFunction)CXMCamera_GetCurrentionFrame, METH_NOARGS, "Get Currention Frame (100ms From Current)."},