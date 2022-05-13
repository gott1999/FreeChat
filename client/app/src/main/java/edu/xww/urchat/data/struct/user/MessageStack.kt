package edu.xww.urchat.data.struct.user

import edu.xww.urchat.network.proto.UserMessageOuterClass
import java.util.concurrent.locks.ReentrantLock

class MessageStack {
    private val lock = ReentrantLock()

    class MessageInfo(var uid: String, var msg: String = "", var time: Long = 0)

    private class LinkedNode {
        var index = 0
        var info: MessageInfo?
        var prev: LinkedNode?
        var next: LinkedNode?

        constructor() {
            info = null
            prev = null
            next = null
        }

        constructor(_info: MessageInfo) {
            info = _info
            prev = null
            next = null
        }

        fun clear() {
            info = null
            prev = null
            next = null
        }
    }

    private var indexToNode = HashMap<Int, LinkedNode>()

    private var uidToNode = HashMap<String, LinkedNode>()

    var size: Int = 0
        private set

    private var head = LinkedNode()
    private var tail = LinkedNode()

    init {
        head.next = tail
        head.prev = tail
        tail.prev = head
        tail.next = head
    }

    operator fun get(no: Int): MessageInfo? {
        return indexToNode[size - no - 1]?.info
    }

    operator fun get(uid: String): MessageInfo? {
        return uidToNode[uid]?.info
    }

    operator fun set(src: String, msg: UserMessageOuterClass.UserMessage) {
        push(src, msg.message, msg.millisecondTimestamp)
    }

    operator fun set(src: String, msg: String) {
        push(src, msg, System.currentTimeMillis())
    }

    fun push(uid: String, msg: String, time: Long) {
        if (uidToNode[uid] == null) {
            synchronized(lock) {
                val node = LinkedNode(MessageInfo(uid, msg, time))
                node.index = size

                uidToNode[uid] = node
                indexToNode[size] = node

                node.next = tail
                node.prev = tail.prev
                tail.prev?.next = node
                tail.prev = node

                ++size
            }
        } else {
            if (uidToNode[uid]?.info?.time!! > time) return
            remove(uid)
            push(uid, msg, time)
        }

    }

    fun remove(uid: String) {
        synchronized(lock) {
            val node = uidToNode[uid] ?: return

            uidToNode.remove(uid)
            indexToNode.remove(node.index)

            var curr = node.next

            node.prev?.next = node.next
            node.next?.prev = node.prev
            node.clear()

            while (curr != tail && curr != null) {
                val index = uidToNode[uid]!!.index

                indexToNode.remove(index)
                indexToNode[index - 1] = curr
                curr.index = index - 1

                curr = curr.next
            }

            --size
        }
    }

    fun clear() {
        indexToNode.clear()
        uidToNode.clear()

        var curr: LinkedNode? = head.next
        var next: LinkedNode?
        while (null != curr) {
            next = curr.next
            curr.clear()
            curr = next
        }

        head.clear()
        tail.clear()
    }

}

