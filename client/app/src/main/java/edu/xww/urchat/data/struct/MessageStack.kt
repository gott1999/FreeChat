package edu.xww.urchat.data.struct

class MessageStack {

    /**
     * 栈元素的结构
     */
    private class MessageStackNode {
        var id: String?
        var box: MessageBox?
        var prev: MessageStackNode?
        var next: MessageStackNode?

        constructor() {
            id = null
            box = null
            prev = null
            next = null
        }

        constructor(_box: MessageBox) {
            id = _box.messageId
            box = _box
            prev = null
            next = null
        }

        public fun update(newBox: MessageBox) {
            box = newBox
        }

        public fun clear() {
            id = null
            box = null
            prev = null
            next = null
        }
    }

    /**
     * 到栈底的编号 to id
     */
    private var indexToId = HashMap<Int, String>()

    /**
     * id to 到栈底的编号
     */
    private var idToIndex = HashMap<String, Int>()

    /**
     * 消息id 到 消息节点
     */
    private var idToNode = HashMap<String, MessageStackNode>()

    /**
     * size
     */
    private var size: Int = 0

    /**
     * 哨兵  头为栈底 尾为栈顶
     */
    private var head = MessageStackNode()
    private var tail = MessageStackNode()

    /**
     * 初始化头尾哨兵
     */
    init {
        head.next = tail
        head.prev = tail
        tail.prev = head
        tail.next = head
    }

    /**
     * get Size
     */
    public fun size(): Int {
        return size
    }

    /**
     * 根据是栈中第几个(0 开始) 获取这个相对于栈底的距离 size - no - 1
     * 再根据到栈底的距离 获取id
     * 根据id 拿节点
     */
    public fun get(no: Int): MessageBox? {
        val id = indexToId[size - no - 1]
        val node = idToNode[id]
        if (null != node) {
            return node.box
        }
        return null
    }

    public fun getById(id: String): MessageBox? {
        return idToNode[id]?.box
    }

    /**
     * 添加节点
     */
    public fun push(box: MessageBox) {
        val node = MessageStackNode(box)
        val id = box.messageId
        indexToId[size] = id
        idToIndex[id] = size
        idToNode[id] = node
        push(node)
        ++size
    }

    /**
     * 批量添加
     */
    public fun pushAll(boxes: Array<MessageBox>) {
        for (box in boxes) push(box)
    }

    /**
     * 压入栈
     */
    private fun push(node: MessageStackNode) {
        node.next = tail
        node.prev = tail.prev
        tail.prev?.next = node
        tail.prev = node
    }

    /**
     * 删除数据结构中一个节点
     */
    public fun remove(messageId: String) {
        val node = idToNode[messageId] ?: return
        size--
        val next = node.next
        node.prev?.next = next
        node.next?.prev = node.prev
    }

    /**
     * 更新数据结构中的内容
     */
    public fun update(messageBox: MessageBox): Boolean {
        val node = idToNode[messageBox.messageId]
        if (null != node) {
            node.update(messageBox)
            moveToTail(node)
        } else {
            push(messageBox)
        }

        return true
    }

    /**
     * 移动到栈顶
     * TODO 更新集合的映射顺序关系
     */
    private fun moveToTail(node: MessageStackNode): Boolean {
        val next = node.next
        node.prev?.next = next
        node.next?.prev = node.prev


        node.next = tail
        node.prev = tail.prev
        tail.prev?.next = node
        tail.prev = node

        return true
    }


    public fun clear() {
        indexToId.clear()
        idToIndex.clear()
        idToNode.clear()

        var curr: MessageStackNode? = head.next
        var next: MessageStackNode?
        while (null != curr) {
            next = curr.next
            curr.clear()
            curr = next
        }

        head.clear()
        tail.clear()
    }

}

