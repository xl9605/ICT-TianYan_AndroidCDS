import subprocess
from flask import Flask
from flask import request, redirect, url_for

import os

import pandas as pd
import numpy as np

import json

main = "./homography"
try:
    import Image
except ImportError:
    from PIL import Image
import pytesseract

from werkzeug.utils import secure_filename

import cv2

UPLOAD_FOLDER = './Images/'
ALLOWED_EXTENSIONS = set(['pdf', 'png', 'jpg', 'jpeg', 'gif'])

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

from sklearn.externals import joblib
clf = joblib.load("train_model.m")


@app.route('/save/<string:label>', methods=['POST'])
def save(label):
    res_table = pd.read_json(request.form.get('result', '[]'))
    # print (res_table)

    final_table = pd.DataFrame()

    for i in range(0, len(res_table)-20):
        temp_table = (pd.DataFrame(res_table[i:i+20].values.reshape([1, 120])))
        final_table = pd.concat([final_table, temp_table])

    final_table['label'] = label

    final_table.to_csv("train.csv", header=False, index=False, mode='a')

    print(final_table)

    return request.form.get('result', 'BAD')


@app.route('/predict', methods=['POST'])
def predict():

    # return request.form.get('content', '')

    test_data = pd.read_json(request.form.get(
        'content', '[]')).values.reshape([1, 120])

    # print (clf.predict(test_data)[0])
    print (test_data)
    # if test_data.em:
    #     return "BAD"
    # else:
    return clf.predict(test_data)[0]

@app.route('/robot', methods=['POST'])
def robot():
    robotcommond=request.path
    #jsonData=robotcommond.json()
    #robotcommond.decode('utf-8')
    #jsonData = json.loads(request.body)
    #robotcommond = pd.read_json(request.body)

    #robotcommand = pd.read_json(request.form.get(
        #'robot', '[]')).values.reshape([1, 120])
    re=request.json
    res=re.get('cmd')
    ress=re.get('device_code')
    resss=re.get('time')
    #res_table = request.form.get(requestbody)
    #data=json.loads(re.text)
    print (re)
    print (res)
    print (ress)
    print (resss)
    data={"result":"success","value":"操作成功！"}
    jsondata=json.dumps(data)
    print (jsondata)
    #print (robotcommond.text)
    return jsondata

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS
def insert_operation(op,str1,str2):
    if str1:
        if str2:
            str2=1
	    #print("success")
        #else:
	    #print("empty")
    #插入数据库
    return op  
    
@app.route('/ocr', methods=['POST'])
def ocr_result():
    if request.method == 'POST':
        # check if the post request has the file part
        if 'file' not in request.files:
            return 'No file part'
        file = request.files['file']
        # if user does not select file, browser also
        # submit a empty part without filename
        if file.filename == '':
            return 'No selected file'
        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            m_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
            file.save(m_path)
            if os.path.exists(main):
                rc= subprocess.getoutput("./homography ./Images/img.jpg")
                #print(rc)
                rc1=pytesseract.image_to_string(Image.open("./Images/1.png"), lang='chi_sim')
                print(rc1)#1.单位
                #insert_db(rc1)
                rc2=pytesseract.image_to_string(Image.open("./Images/2.png"), lang='chi_sim')
                print(rc2)#2.编号
                rc3=pytesseract.image_to_string(Image.open("./Images/3.png"), lang='chi_sim')
                print(rc3)#3.发令人
                rc4=pytesseract.image_to_string(Image.open("./Images/4.png"), lang='chi_sim')
                print(rc4)#4.受令人
                rc5=pytesseract.image_to_string(Image.open("./Images/5.png"), lang='chi_sim')
                print(rc5)#5.发令时间
                rc6=pytesseract.image_to_string(Image.open("./Images/6.png"), lang='chi_sim')
                print(rc6)#6.操作开始时间
                rc7=pytesseract.image_to_string(Image.open("./Images/7.png"), lang='chi_sim')
                print(rc7)#7.操作结束时间
                rc8=pytesseract.image_to_string(Image.open("./Images/8.png"), lang='chi_sim')
                print(rc8)#8.监护下操作
                rc9=pytesseract.image_to_string(Image.open("./Images/9.png"), lang='chi_sim')
                print(rc9)#9.单人操作
                rc10=pytesseract.image_to_string(Image.open("./Images/10.png"), lang='chi_sim')
                print(rc10)#10.检修人员操作
                rc11=pytesseract.image_to_string(Image.open("./Images/11.png"), lang='chi_sim')
                print(rc11)#11.操作任务
                rc12=pytesseract.image_to_string(Image.open("./Images/12.png"), lang='chi_sim')
                print(rc12)#12.1-操作项目
                rc13=pytesseract.image_to_string(Image.open("./Images/13.png"), lang='chi_sim')
                print(rc13)#13.1-是否完成
                insert_operation(1,rc12,rc13)
                rc14=pytesseract.image_to_string(Image.open("./Images/14.png"), lang='chi_sim')
                print(rc14)#14.2-操作项目
                rc15=pytesseract.image_to_string(Image.open("./Images/15.png"), lang='chi_sim')
                print(rc15)#15.2-是否完成
                insert_operation(2,rc14,rc15)
                rc16=pytesseract.image_to_string(Image.open("./Images/16.png"), lang='chi_sim')
                print(rc16)#16.3-操作项目
                rc17=pytesseract.image_to_string(Image.open("./Images/17.png"), lang='chi_sim')
                print(rc17)#17.3-是否完成
                insert_operation(3,rc16,rc17)
                rc18=pytesseract.image_to_string(Image.open("./Images/18.png"), lang='chi_sim')
                print(rc18)#18.4-操作项目
                rc19=pytesseract.image_to_string(Image.open("./Images/19.png"), lang='chi_sim')
                print(rc19)#19.4-是否完成
                insert_operation(4,rc18,rc19)
                rc20=pytesseract.image_to_string(Image.open("./Images/20.png"), lang='chi_sim')
                print(rc20)#20.5-操作项目
                rc21=pytesseract.image_to_string(Image.open("./Images/21.png"), lang='chi_sim')
                print(rc21)#21.5-是否完成
                insert_operation(5,rc20,rc21)
                rc22=pytesseract.image_to_string(Image.open("./Images/22.png"), lang='chi_sim')
                print(rc22)#22.6-操作项目
                rc23=pytesseract.image_to_string(Image.open("./Images/23.png"), lang='chi_sim')
                print(rc23)#23.6-是否完成
                insert_operation(6,rc22,rc23)
                rc24=pytesseract.image_to_string(Image.open("./Images/24.png"), lang='chi_sim')
                print(rc24)#24.7-操作项目
                rc25=pytesseract.image_to_string(Image.open("./Images/25.png"), lang='chi_sim')
                print(rc25)#25.7-是否完成
                insert_operation(7,rc24,rc5)
                rc26=pytesseract.image_to_string(Image.open("./Images/26.png"), lang='chi_sim')
                print(rc26)#26.8-操作项目
                rc27=pytesseract.image_to_string(Image.open("./Images/27.png"), lang='chi_sim')
                print(rc27)#27.8-是否完成
                insert_operation(8,rc26,rc27)
                rc28=pytesseract.image_to_string(Image.open("./Images/28.png"), lang='chi_sim')
                print(rc28)#28.9-操作项目
                rc29=pytesseract.image_to_string(Image.open("./Images/29.png"), lang='chi_sim')
                print(rc29)#29.9-是否完成
                insert_operation(9,rc28,rc29)
                rc30=pytesseract.image_to_string(Image.open("./Images/30.png"), lang='chi_sim')
                print(rc30)#30.10-操作项目
                rc31=pytesseract.image_to_string(Image.open("./Images/31.png"), lang='chi_sim')
                print(rc31)#31.10-是否完成
                insert_operation(10,rc30,rc31)
                rc32=pytesseract.image_to_string(Image.open("./Images/32.png"), lang='chi_sim')
                print(rc32)#32.11-操作项目
                rc33=pytesseract.image_to_string(Image.open("./Images/33.png"), lang='chi_sim')
                print(rc33)#33.11-是否完成
                insert_operation(11,rc32,rc33)
                rc34=pytesseract.image_to_string(Image.open("./Images/34.png"), lang='chi_sim')
                print(rc34)#34.12-操作项目
                rc35=pytesseract.image_to_string(Image.open("./Images/35.png"), lang='chi_sim')
                print(rc35)#35.12-是否完成
                insert_operation(12,rc34,rc35)
                rc36=pytesseract.image_to_string(Image.open("./Images/36.png"), lang='chi_sim')
                print(rc36)#36.13-操作项目
                rc37=pytesseract.image_to_string(Image.open("./Images/37.png"), lang='chi_sim')
                print(rc37)#37.13-是否完成
                insert_operation(13,rc36,rc37)
                rc38=pytesseract.image_to_string(Image.open("./Images/38.png"), lang='chi_sim')
                print(rc38)#38.14-操作项目
                rc39=pytesseract.image_to_string(Image.open("./Images/39.png"), lang='chi_sim')
                print(rc39)#39.14-是否完成
                insert_operation(14,rc38,rc39)
                rc40=pytesseract.image_to_string(Image.open("./Images/40.png"), lang='chi_sim')
                print(rc40)#40.15-操作项目
                rc41=pytesseract.image_to_string(Image.open("./Images/41.png"), lang='chi_sim')
                print(rc41)#41.15-是否完成
                rc42=pytesseract.image_to_string(Image.open("./Images/42.png"), lang='chi_sim')
                insert_operation(15,rc40,rc41)
                print(rc42)#42.备注
                rc43=pytesseract.image_to_string(Image.open("./Images/43.png"), lang='chi_sim')
                print(rc43)#43.操作人
                rc44=pytesseract.image_to_string(Image.open("./Images/44.png"), lang='chi_sim')
                print(rc44)#44.监护人
                rc45=pytesseract.image_to_string(Image.open("./Images/45.png"), lang='chi_sim')
                print(rc45)#45.值班负责人（值长）
                return pytesseract.image_to_string(Image.open(m_path), lang='chi_sim')
            
    return 'NOT FOUND'


@app.route('/face', methods=['POST'])
def face_upload():
    if request.method == 'POST':
        # check if the post request has the file part
        if 'file' not in request.files:
            return 'No file part'
        file = request.files['file']
        # if user does not select file, browser also
        # submit a empty part without filename
        if file.filename == '':
            return 'No selected file'
        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            m_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
            file.save(m_path)
            return "FACE SAVED"
    return 'NOT FOUND'


if __name__ == '__main__':
    app.run(port=23456, host='192.168.0.107')
