from flask import Flask
from flask import request, redirect, url_for

import os

import pandas as pd
import numpy as np

try:
    import Image
except ImportError:
    from PIL import Image
import pytesseract

from werkzeug.utils import secure_filename

import cv2

UPLOAD_FOLDER = './AndroidFaceImages/'
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
    
    final_table = pd.DataFrame()
    temp_table = (pd.DataFrame(test_data))
    final_table = pd.concat([final_table, temp_table])
    final_table['label'] = clf.predict(test_data)[0]
    final_table.to_csv("lpms.csv", header=False, index=False, mode='a')
    # print (clf.predict(test_data)[0])
    print (test_data)
    # if test_data.em:
    #     return "BAD"
    # else:
    return clf.predict(test_data)[0]


def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

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
