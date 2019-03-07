cmake_minimum_required(VERSION 3.4.1)

#me
include_directories(/home/humanmotion/lcysoftware/opencv-3.4.1-android-sdk/OpenCV-android-sdk/sdk/native/jni/include)
#徐亮
#include_directories(/home/humanmotion/Program_Files/OpenCV-Android-SDK/sdk/native/jni/include)
#include_directories(F:/OpenCV-Android-SDK/sdk/native/jni/include)
#7700k+1080
#include_directories(/home/humanmotion/lcypro/software/opencv-3.4.1-android-sdk/OpenCV-android-sdk/sdk/native/jni/include)
#华硕
#include_directories(F:/Envirorment/OpenCV-Android-SDK/sdk/native/jni/include)
#孙旭
#include_directories(/home/humanmotion/Downloads/opencv-3.4.1-android-sdk/OpenCV-android-sdk/sdk/native/jni/include)
#刘彩玉
#include_directories(D:/opencv-3.4.1-android-sdk/OpenCV-android-sdk/sdk/native/jni/include)
add_library( lib_opencv SHARED IMPORTED )

#me
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION /home/humanmotion/lcysoftware/opencv-3.4.1-android-sdk/OpenCV-android-sdk/sdk/native/libs/${ANDROID_ABI}/libopencv_java3.so)
#徐亮
#set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION /home/humanmotion/Program_Files/OpenCV-Android-SDK/sdk/native/libs/${ANDROID_ABI}/libopencv_java3.so)
#set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION F:/OpenCV-Android-SDK/sdk/native/libs/${ANDROID_ABI}/libopencv_java3.so)
#7700k+1080
#set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION /home/humanmotion/lcypro/software/opencv-3.4.1-android-sdk/OpenCV-android-sdk/sdk/native/libs/${ANDROID_ABI}/libopencv_java3.so)
#华硕
#set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION F:/Envirorment/OpenCV-Android-SDK/sdk/native/libs/${ANDROID_ABI}/libopencv_java3.so)
#孙旭
#set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION /home/humanmotion/Downloads/opencv-3.4.1-android-sdk/OpenCV-android-sdk/sdk/native/libs/${ANDROID_ABI}/libopencv_java3.so)
#刘彩玉
#set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION D:/opencv-3.4.1-android-sdk/OpenCV-android-sdk/sdk/native/libs/${ANDROID_ABI}/libopencv_java3.so)
add_library( native-lib
             SHARED
             src/main/cpp/native-lib.cpp )

find_library( log-lib
              log )

target_link_libraries( native-lib
                       lib_opencv
                       jnigraphics
                       ${log-lib} )
