#!/usr/bin/env python2

import cv2
import os
import numpy as np
import time
import pdb
import Transformer_Table

np.set_printoptions(precision=2)

from multiprocessing import Process, Manager, Lock
lock = Lock()
queue = Manager().Queue()

mydict = Manager().dict()
mydict["now_push_ip"] = "255.255.255.255"
IPC_Path = None
nowFatherPath = os.path.dirname(os.path.realpath(__file__))
#nowFatherPath = os.path.dirname(os.path.realpath(__file__)) + "/datas/"


def camera_detect(q, l, address,path):
    global IPC_Path
   # image_dir = os.path.dirname(os.path.realpath(__file__)) + "/" + address  # 路径不要修改
    image_dir = os.path.dirname(os.path.realpath(__file__)) + "/datas/" + path


    if not os.path.exists(image_dir):
        os.makedirs(image_dir)

    from xmcext import XMCamera
    cp = XMCamera(address, 34567, "admin", "", image_dir)
    cp.PrintInfo()
    cp.login()

    cp.open()
    time.sleep(2)
    pre_frame = None
    nowSavingName = None
    img_frequency = 0

    while True:
        # pdb.set_trace()
        frame = np.asarray(cp.queryframe('array')).reshape(1080, 1920, 3)

        gray_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        gray_frame = cv2.resize(gray_frame, (500, 500))
        gray_frame = cv2.GaussianBlur(gray_frame, (21, 21), 0)

        if pre_frame is None:
            pre_frame = gray_frame
        else:
            img_delta = cv2.absdiff(pre_frame, gray_frame)
            thresh = cv2.threshold(img_delta, 25, 255, cv2.THRESH_BINARY)[1]

            # pdb.set_trace()
            if nowSavingName is None:
                print(os.path.dirname(os.path.realpath(__file__)))
                nowSavingName = time.strftime('%Y%m%d%H%M%S', time.localtime(time.time()))
                os.system("mkdir ./datas/{}/{}".format(path,'IPC_Catch'))
            cv2.imwrite(
                './datas/{}/{}/IPC_Pic.jpg'.format( path,'IPC_Catch'), frame)
            IPC_Path = image_dir + "/IPC_Catch/IPC_Pic.jpg"
            print(IPC_Path)
            if len([x for x in os.listdir(image_dir + "/IPC_Catch")]) > 0:
                img_frequency = 1
            break
            pre_frame = gray_frame
        if img_frequency == 1:
            break
    cp.close()


def catch_PIC(transformer,path):
    # print("请输入柜门号：")
    # transformer = input()
    camera_IP = Transformer_Table.getTransformIP(transformer)
    camera_detect(queue, lock, camera_IP,path)

def getRep(imgPath):
    import time

    start = time.time()

    import argparse
    import cv2
    import itertools
    import os

    import numpy as np
    np.set_printoptions(precision=2)

    import openface

    fileDir = os.path.dirname(os.path.realpath(__file__))
    modelDir = os.path.join(fileDir, '', 'models')
    dlibModelDir = os.path.join(modelDir, 'dlib')
    openfaceModelDir = os.path.join(modelDir, 'openface')

    parser = argparse.ArgumentParser()

    #原compare.py文件执行方式为：在控制台下敲入‘./compare {图片1，图片2}'
    #parser.add_argument('imgs', type=str, nargs='+', help="Input images.")
    parser.add_argument('--dlibFacePredictor', type=str, help="Path to dlib's face predictor.",
                        default=os.path.join(dlibModelDir, "shape_predictor_68_face_landmarks.dat"))
    parser.add_argument('--networkModel', type=str, help="Path to Torch network model.",
                        default=os.path.join(openfaceModelDir, 'nn4.small2.v1.t7'))
    parser.add_argument('--imgDim', type=int,
                        help="Default image dimension.", default=96)
    parser.add_argument('--verbose', action='store_true')

    args = parser.parse_args()

    if args.verbose:
        print("Argument parsing and loading libraries took {} seconds.".format(
            time.time() - start))

    start = time.time()
    align = openface.AlignDlib(args.dlibFacePredictor)
    net = openface.TorchNeuralNet(args.networkModel, args.imgDim)
    if args.verbose:
        print("Loading the dlib and OpenFace models took {} seconds.".format(
            time.time() - start))

    if args.verbose:
        print("Processing {}.".format(imgPath))
    bgrImg = cv2.imread(imgPath)
    if bgrImg is None:
        raise Exception("Unable to load image: {}".format(imgPath))
    rgbImg = cv2.cvtColor(bgrImg, cv2.COLOR_BGR2RGB)

    if args.verbose:
        print("  + Original size: {}".format(rgbImg.shape))

    start = time.time()
    bb = align.getLargestFaceBoundingBox(rgbImg)
    if bb is None:
        raise Exception("Unable to find a face: {}".format(imgPath))
    if args.verbose:
        print("  + Face detection took {} seconds.".format(time.time() - start))

    start = time.time()
    alignedFace = align.align(args.imgDim, rgbImg, bb,
                              landmarkIndices=openface.AlignDlib.OUTER_EYES_AND_NOSE)
    if alignedFace is None:
        raise Exception("Unable to align image: {}".format(imgPath))
    if args.verbose:
        print("  + Face alignment took {} seconds.".format(time.time() - start))

    start = time.time()
    rep = net.forward(alignedFace)
    if args.verbose:
        print("  + OpenFace forward pass took {} seconds.".format(time.time() - start))
        print("Representation:")
        print(rep)
        print("-----\n")
    return rep

# for (img1, img2) in itertools.combinations(args.imgs, 2):
#     d = getRep(img1) - getRep(img2)
#     compare_Values = ("%.3f")% np.dot(d, d)
#     save_Values_FileName = fileDir + '/values.txt'
#     f = open(save_Values_FileName, 'w')
#     f.write(compare_Values + '\n')
#     nowtimelocal = time.localtime(time.time())
#     f.write(time.strftime('%Y-%m-%d %H:%M:%S', nowtimelocal))
#     f.close()
#     print(np.dot(d, d))
#     print("Comparing {} with {}.".format(img1, img2))
#
#     print(
#         "  + Squared l2 distance between representations: {:0.3f}".format(np.dot(d, d)))


def compare(transformer,path):
    global nowFatherPath
    catch_PIC(transformer,path)
    # for address in ["10.41.0.168", "10.41.0.199"]:
    #     Process(target=camera_detect, args=(queue, lock, address, )).start()
    #
    # while True:
    #     pass
    #print(IPC_Path)
    #d = getRep(nowFatherPath + '/a.jpg') - getRep(IPC_Path)
    #print(nowFatherPath + '/datas/' + path + '/RESULT.jpg')
    # /home/humanmotion/Program_Codes/All_Project/20181013_New/OCR/datas/20181015100150/RESULT.jpg
    try:
        d = getRep(nowFatherPath + '/datas/' + path + '/RESULT.jpg') - getRep(IPC_Path)
    except Exception as err:
        print(err)
        return False
    compare_Values = ("%.3f") % np.dot(d, d)#两张照片比对的区别值

    #print(np.dot(d, d))
    #print(compare_Values)
    if(float(compare_Values) <= 0.77):
        print("d:   " + str(np.dot(d, d)))
        print("compare_Values:   " + str(compare_Values))
        print('两张照片是一个人！！！！')
        return True
    else:
        print("d:   " + str(np.dot(d, d)))
        print("compare_Values:   " + str(compare_Values))
        print('两张照片不是一个人！！！')
        return False

