import MySQLdb


global conn #全局变量 conn指链接的数据库
global cur


def database_connect():
    global conn
    global cur
    conn = MySQLdb.connect(host='127.0.0.1', user='root', passwd='humanmotion', db='workdatabase', charset='utf8')
    cur = conn.cursor()


def database_disconnect():
    global cur
    cur.close()
    conn.commit()
    conn.close()


if __name__ == '__main__':
    database_connect()

    sqlinsertworktableinsert= 'INSERT INTO `worktable` VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)'
    # '工单编号','单位','发令人','受令人','操作开始时间','操作结束时间','发令时间','监护下操作','单人操作',
    # '检修人操作','操作任务','备注','操作人','监护人','值班负责人（值长）','工单图片路径','人脸照片路径'
    insertwt=cur.execute(sqlinsertworktableinsert,('00004','s','s','s','2018-05-22 09:30:41',
                                                   '2018-05-22 09:30:41', '2018-05-22 09:30:41','1','1','0','s','s','s','s','s','s','s'))

    if(insertwt == 1):
        sqlinsertoperationinsert="INSERT INTO `operation` (worktableID,operationorder,operationdetail,isfinished) VALUES (%s,%s,%s,%s)"
        # '操作总顺序','工单号','操作顺序','操作项目','是否完成（就是对勾）'
        insertop=cur.execute(sqlinsertoperationinsert,('00004','9','sdfdsfdsfsd','1'))
        #print(insertop)
    else:
        print('插入worktable数据库失败！！')
    sqlselect = 'select * from operation'
    reCount = cur.execute(sqlselect)

    print(reCount)
    reData = cur.fetchmany(reCount)

    for ii in reData:
        print(ii)
    database_disconnect()


