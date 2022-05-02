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
select uid from user_u_data where uname=%s and upassword=%s
'''

sql_count = '''
select count(*) from user_u_data where uname=%s
'''

sql_register = '''
insert into user_u_data(username,passwd) values(%s,%s)
'''

sql_getContact = '''
select 
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
    with conn.cursor(cursor=cursors.DictCursor) as cursor:
        cursor.execute(sql_register, (username, password))
        conn.commit()
        cursor.close()
    conn.close()



