# -*- coding: UTF-8 -*-

from pymysql import connect
from pymysql import cursors


properties = {
    'host': '127.0.0.1',
    'user': 'root',
    'password': '123456',
    'port': 3306,
    'db': 'urchat_users',
    'charset': 'utf8'
}

sql_login = '''
select uid from `user_u_data` where uname=%s and upassword=%s
'''

sql_get_user = '''
select `user_u_data`.uid 
from `user_u_data` left join `user_b_data` on `user_u_data`.uid=`user_b_data`.uid 
where uid=%s
'''

sql_count = '''
select count(uid) as count from `user_u_data` where uname=%s
'''

sql_register = '''
insert into `user_u_data`(username,passwd) values(%s,%s)
'''

sql_getContact = '''
SELECT tar_uid FROM `user_relationship` where src_uid=%s
'''


def login(username, password):
    conn = connect(**properties)
    with conn.cursor(cursor=cursors.DictCursor) as cursor:
        cursor.execute(sql_login, (username, password))
        res = cursor.fetchall()
        cursor.close()
    conn.close()
    return res


def register(username, password):
    conn = connect(**properties)
    res = 0
    with conn.cursor(cursor=cursors.DictCursor) as cursor:
        if cursor.execute(sql_count, username) == 0:
            res = cursor.execute(sql_register, (username, password))
            conn.commit()
        cursor.close()
    conn.close()
    return res


def getContact(uid):
    conn = connect(**properties)
    with conn.cursor(cursor=cursors.DictCursor) as cursor:
        cursor.execute(sql_getContact, uid)
        res = cursor.fetchall()
        cursor.close()
    conn.close()
    return res


def getUserBase(uid):
    conn = connect(**properties)
    with conn.cursor(cursor=cursors.DictCursor) as cursor:
        cursor.execute(sql_get_user, uid)
        res = cursor.fetchall()
        cursor.close()
    conn.close()
    return res
