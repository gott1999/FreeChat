package edu.xww.urchat.data.struct.user

import java.util.concurrent.locks.ReentrantLock

class MessageList {

    private val lock = ReentrantLock()

    val runtime = HashMap<String, ArrayList<Message>>()

    /**
     * Operator get. If there is no "uid" to ArrayList<Message> it will create a new one.
     */
    operator fun get(uid: String): ArrayList<Message> {
        if (!runtime.containsKey(uid)) runtime[uid] = arrayListOf()
        return runtime[uid]!!
    }

    /**
     * clear
     */
    fun clear() {
        runtime.clear()
    }

    /**
     * Operator set. If there is no "uid" to ArrayList<Message> it will create a new one.
     */
    operator fun set(uid: String, value: ArrayList<Message>) {
        synchronized(lock) {
            if (runtime.containsKey(uid)) runtime[uid]!!.addAll(value)
            else runtime[uid] = value
            runtime[uid]!!.sortBy{ it.time }
        }
    }

    /**
     * Operator set. If there is no "uid" to ArrayList<Message> it will create a new one.
     */
    operator fun set(uid: String, value: Message) {
        synchronized(lock) {
            if (!runtime.containsKey(uid)) runtime[uid] = arrayListOf(value)
            else runtime[uid]!!.add(value)
            runtime[uid]!!.sortBy{ it.time }
        }
    }

}