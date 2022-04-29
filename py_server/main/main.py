from app.db.UrchatDb import UrchatConnector

conn = UrchatConnector()
sql = '''
SELECT 
a.uid as id, a.uname as name, a.upassword as pwd, b.display_name as display_name 
FROM 
`user_u_data` a LEFT JOIN `user_b_data` b 
ON a.uid = b.uid
'''
for i in conn.select(sql):
    print(i)
