from flask import Flask
import json
import pandas as pd
import urllib
from flask import request, redirect, url_for
app = Flask(__name__)
UPLOAD_FOLDER = './Images/'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


@app.route('/robotReceive', methods=['POST'])
def robotReceive():
    rea = request.get_data().decode('utf8')
    print(rea)
    return '{"result": "success", "value": "权限匹配不成功！！"}'


if __name__ == '__main__':
    app.run(port=56789, host='127.0.0.1')
