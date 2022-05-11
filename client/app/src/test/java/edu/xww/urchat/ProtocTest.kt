package edu.xww.urchat

import org.junit.Test
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class ProtocTest {

    @Test
    fun pTest() {
        val fin = FileInputStream("D:\\workspace\\UrChat\\py_server\\upload\\img\\1.jpeg")
        val out = FileOutputStream("D:\\workspace\\UrChat\\py_server\\upload\\img\\43d33bd179f2472f81fafc2684ed5083!400x400.jpeg")
        val byte = fin.readBytes()


        val a = Base64.getEncoder().encode(byte)

        out.write(a)
    }
}