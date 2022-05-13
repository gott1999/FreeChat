package edu.xww.urchat.data.loader

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBLoader {

    class DBOpenHelper(context: Context) : SQLiteOpenHelper(context, name, null, version) {
        companion object {
            private const val name = "db.user.message"
            private var version = 1
            // private var version = 2
        }

        override fun onCreate(database: SQLiteDatabase?) {
            val sql =
                """create table message(id integer primary key autoincrement, uid varchar(255), type int,message varchar(255), time Long)"""
            database?.execSQL(sql)
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        }

    }




}