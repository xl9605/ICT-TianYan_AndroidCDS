import subprocess
from flask import Flask
from flask import request, redirect, url_for
import MySQLdb
import os

import pandas as pd
import numpy as np

import json
import time,re
main = "./homography"
main_tableId=""
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

global conn #全局变量 conn指链接的数据库
global cur


def database_connect():
    global conn
    global cur
    conn = MySQLdb.connect(host='10.41.0.241', user='root', passwd='humanmotion', db='workdatabase', charset='utf8')
    cur = conn.cursor()


def database_disconnect():
    global cur
    cur.close()
    conn.commit()
    conn.close()

#工单存入数据库
def database_insert(r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17):
    global cur
    sqlinsertworktableinsert = 'INSERT INTO `worktable` VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)'
    # '工单编号','单位','发令人','受令人','操作开始时间','操作结束时间','发令时间','监护下操作','单人操作',
    # '检修人操作','操作任务','备注','操作人','监护人','值班负责人（值长）','工单图片路径','人脸照片路径'
    insertwt = cur.execute(sqlinsertworktableinsert, (r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17))
    return insertwt    
# if (insertwt == 1):
    #     sqlinsertoperationinsert = "INSERT INTO `operation` (worktableID,operationorder,operationdetail,isfinished) VALUES (%s,%s,%s,%s)"
    #     # '操作总顺序','工单号','操作顺序','操作项目','是否完成（就是对勾）'
    #     insertop = cur.execute(sqlinsertoperationinsert, ('00008', '9', 'sdfdsfdsfsd', '1'))
    #     # print(insertop)
    # else:
    #     print('插入worktable数据库失败！！')
    #sqlselect = 'select * from operation'
    #reCount = cur.execute(sqlselect)
    #print(reCount)
    #reData = cur.fetchmany(reCount)
    #for ii in reData:
    #    print(ii)

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

def string_time(str):
    if str:
       #取出年月日时分秒
       result = re.split("年|月|日|时|分",str.replace(" ",""))
       year=result[0]
       mothon=result[1]
       day=result[2]
       hour=result[3]
       mininute=result[4]
       if year:
          nowtimelocal = time.localtime(time.time())
          time.strftime('%Y-%m-%d %H:%M:%S', nowtimelocal)
          if year.isdigit()!=1:                    
             year=time.strftime("%Y",nowtimelocal);
          if mothon.isdigit()!=1:
             mothon=time.strftime("%m",nowtimelocal)
          if day.isdigit()!=1:
             day=time.strftime("%d",nowtimelocal)
          if hour.isdigit()!=1:
             hour=time.strftime("%H",nowtimelocal)
          if mininute.isdigit()!=1:
             mininute=time.strftime("%M",nowtimelocal)
          return year+"-"+mothon+"-"+day+" "+hour+":"+mininute+":00" 
    return "null"  

def string_int(str):
    if str:
       return 1
    else:
       return 0  

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


@app.route('/ocr', methods=['POST'])
def ocr_result():
    nowtimes= time.localtime(time.time())
    print("0-------"+time.strftime('%Y-%m-%d %H:%M:%S', nowtimes))
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
            nowtime2 = time.localtime(time.time())
            print("1-------"+time.strftime('%Y-%m-%d %H:%M:%S', nowtime2))
            if os.path.exists(main):
                rc= subprocess.getoutput("./homography ./Images/ee.png")
                nowtime1 = time.localtime(time.time())
                print("2-------"+time.strftime('%Y-%m-%d %H:%M:%S', nowtime1))
                #print(rc)
                #path="./Images/1.png"
                rc2=pytesseract.image_to_string(Image.open("./Images/2.png"), lang='chi_sim')
                print(rc2)#2.编号
                nowtime4 = time.localtime(time.time())
                print("4-------"+time.strftime('%Y-%m-%d %H:%M:%S',nowtime4))
          
                main_tableId=rc2
                if rc2:
                    rc1=pytesseract.image_to_string(Image.open("./Images/1.png"), lang='chi_sim')
                    print(rc1)#1.单位
                    nowtime5 = time.localtime(time.time())
                    print("5-------"+time.strftime('%Y-%m-%d %H:%M:%S',nowtime5))
                    rc3=pytesseract.image_to_string(Image.open("./Images/3.png"), lang='chi_sim')
                    print(rc3)#3.发令人
                    nowtime6 = time.localtime(time.time())
                    print("6-------"+time.strftime('%Y-%m-%d %H:%M:%S',nowtime6))
                    rc4=pytesseract.image_to_string(Image.open("./Images/4.png"), lang='chi_sim')
                    print(rc4)#4.受令人
                    
          
                    rc5=pytesseract.image_to_string(Image.open("./Images/5.png"), lang='chi_sim')
                    print(rc5)#5.发令时间
                    rc5=string_time(rc5)
                    print(rc5)
                    rc6=pytesseract.image_to_string(Image.open("./Images/6.png"), lang='chi_sim')
                    print(rc6)#6.操作开始时间
                    rc6=string_time(rc6)
                    print(rc6)
                    rc7=pytesseract.image_to_string(Image.open("./Images/7.png"), lang='chi_sim')
                    print(rc7)#7.操作结束时间
                    rc7=string_time(rc7)
                    print(rc7)
                    
                    rc8=pytesseract.image_to_string(Image.open("./Images/8.png"), lang='chi_sim')
                    print(rc8)#8.监护下操作
                    rc8=string_int(rc8)
                    print(rc8)
                    rc9=pytesseract.image_to_string(Image.open("./Images/9.png"), lang='chi_sim')
                    print(rc9)#9.单人操作
                    rc9=string_int(rc9)
                    print(rc9)
                    rc10=pytesseract.image_to_string(Image.open("./Images/10.png"), lang='chi_sim')
                    print(rc10)#10.检修人员操作
                    rc10=string_int(rc10)
                    print(rc10)

                    rc11=pytesseract.image_to_string(Image.open("./Images/11.png"), lang='chi_sim')
                    print(rc11)#11.操作任务

                    rc42=pytesseract.image_to_string(Image.open("./Images/42.png"), lang='chi_sim')  
                    print(rc42)#42.备注
                    rc43=pytesseract.image_to_string(Image.open("./Images/43.png"), lang='chi_sim')
                    print(rc43)#43.操作人
                    rc44=pytesseract.image_to_string(Image.open("./Images/44.png"), lang='chi_sim')
                    print(rc44)#44.监护人
                    rc45=pytesseract.image_to_string(Image.open("./Images/45.png"), lang='chi_sim')
                    print(rc45)#45.值班负责人（值长）
                    
                    nowtime33 = time.localtime(time.time())
                    print("333-------"+time.strftime('%Y-%m-%d %H:%M:%S', nowtime33))    
                    #数据库连接
                    database_connect()
                    flag=database_insert(rc2,rc1,rc3,rc4,rc6,rc7,rc5,rc8,rc9,rc10,rc11,rc42,rc43,rc44,rc45,"./Images/","./Images/")
                    database_disconnect()
                    nowtime7 = time.localtime(time.time())
                    print("7-------"+time.strftime('%Y-%m-%d %H:%M:%S',nowtime7))
                    if flag==1:
                        i=1;
                        while i<=15:
                            nu=i*2+10
                            #c1="./Images/"+str(nu)+".png"
                            rc12=pytesseract.image_to_string(Image.open("./Images/"+str(nu)+".png"), lang='chi_sim')
                            print(rc12)#12.1-操作项目
                            if rc12:
                                nu=nu+1
                                rc13=pytesseract.image_to_string(Image.open("./Images/"+str(nu)+".png"), lang='chi_sim')
                                print(rc13)#13.1-是否完成
                                if rc13:
                                    str2=1
                                else:
                                    str2=0    
                               
                                print(i)
                                print(main_tableId)
                                print(rc12)
                                print(str2)
                                #插入数据库("sql:"insert into operation values(ids,op,str1,str2)"")
                                database_connect()
                                global cur
                                sql = "INSERT INTO `operation` (worktableID,operationorder,operationdetail,isfinished) VALUES (%s,%s,%s,%s)"
                                insertop = cur.execute(sql, (main_tableId,i,rc12, str2)) 
                                database_disconnect()
                                #insert_operation(main_tableId,i,rc12,rc13)
                            else:
                                break
                            i=i+1

                        #rc14=pytesseract.image_to_string(Image.open("./Images/14.png"), lang='chi_sim')
                        #print(rc14)#14.2-操作项目
                        
                        nowtime3 = time.localtime(time.time())
                        print("3-------"+time.strftime('%Y-%m-%d %H:%M:%S', nowtime3))                 
                        return "sucess"
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
