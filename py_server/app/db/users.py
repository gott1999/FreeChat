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
select uid from `user_u_data` where uname=%s and upassword=%s;
'''

sql_get_user = '''
select `user_u_data`.`uid` as uid,
       `user_b_data`.`id` as id,
       `user_b_data`.`display_name` as displayName,
       `user_b_data`.`icon` as icon
from `user_u_data` left join `user_b_data` on `user_u_data`.uid=`user_b_data`.`uid`
where `user_u_data`.`uid`=%s;
'''

sql_count = '''
select count(uid) as count from `user_u_data` where uname=%s;
'''

sql_register = '''
insert into `user_u_data`(uname,upassword) values(%s,%s);
'''

sql_register2 = '''
insert into `user_b_data`(uid,id,display_name) values(%s,%s,%s);
'''

sql_getContact = '''
SELECT tar_uid as uid, nickname, `user_b_data`.`icon` as icon
FROM `user_relationship` left join`user_b_data` on `user_relationship`.tar_uid=`user_b_data`.`uid`
where `user_relationship`.src_uid=%s;
'''


def login(username, password):
    """
    db operation - login
    :param username: username
    :param password: password
    :return: [{user_unique_data}]
    """
    conn = connect(**properties)
    with conn.cursor(cursor=cursors.DictCursor) as cursor:
        cursor.execute(sql_login, (username, password))
        res = cursor.fetchall()
        cursor.close()
    conn.close()
    return res


def register(username, password):
    """
    db operation - register
    :param username: username
    :param password: password
    :return: 0:fail 1:success
    """
    conn = connect(**properties)
    res = 0
    with conn.cursor(cursor=cursors.DictCursor) as cursor:
        cursor.execute(sql_count, username)
        rt = cursor.fetchone()
        if rt["count"] == 0:
            cursor.execute(sql_register, (username, password))
            conn.commit()

            cursor.execute(sql_login, (username, password))
            rt = cursor.fetchone()
            if rt is not None:
                cursor.execute(sql_register2, (rt["uid"], username, username))
                conn.commit()
                res = 1
        cursor.close()
    conn.close()
    return res


def getContact(uid):
    """
    db operation - get Contact
    :param uid: src_uid
    :return: [{Contact}...]
    """
    conn = connect(**properties)
    with conn.cursor(cursor=cursors.DictCursor) as cursor:
        cursor.execute(sql_getContact, uid)
        res = cursor.fetchall()
        cursor.close()
    conn.close()
    return res


def getUserBase(uid):
    """
    db operation - get user base data
    :param uid: uid
    :return: [{user_base_data}]
    """
    conn = connect(**properties)
    with conn.cursor(cursor=cursors.DictCursor) as cursor:
        cursor.execute(sql_get_user, uid)
        res = cursor.fetchall()
        cursor.close()
    conn.close()
    return res
