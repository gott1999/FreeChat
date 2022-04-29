from pymysql import connect
from pymysql import cursors


class UrchatConnector:

    properties = {
        'host': '127.0.0.1',
        'user': 'root',
        'password': '123456',
        'port': 3306,
        'db': 'urchat_users',
        'charset': 'utf8'
    }

    def __init__(self):
        self.conn = connect(**UrchatConnector.properties)
        self.cursor = self.conn.cursor(cursor=cursors.DictCursor)

    def __del__(self):
        self.cursor.close()
        self.conn.close()

    def select(self, sql):
        self.cursor.execute(sql)
        return self.cursor.fetchall()

    def update(self, sql):
        try:
            result = self.cursor.execute(sql)
            self.conn.commit()
            if result == 1:
                return True
            else:
                return False
        except Exception as e:
            print('Update Failed: %s' % e)
            return False
