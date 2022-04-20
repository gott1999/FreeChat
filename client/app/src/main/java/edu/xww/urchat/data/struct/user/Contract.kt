package edu.xww.urchat.data.struct.user

import edu.xww.urchat.data.struct.system.CommonRecyclerViewItem

data class Contract(
    val uId: String,
    val name: String,
    override val icon: String,
    override val displayName: String,
    override val tag: String,
    val contractType: ContractType = ContractType.NORMAL
) : CommonRecyclerViewItem(icon, displayName,tag), java.io.Serializable {

    enum class ContractType {
        NORMAL, SYSTEM, FOLD
    }

    companion object {
        fun buildNormal(
            uid: String,
            name: String,
            icon: String,
            displayName: String,
            tag: String
        ): Contract =
            Contract(uid, name, icon, displayName, tag, ContractType.NORMAL)

        fun buildSystem(
            uid: String,
            name: String,
            icon: String,
            displayName: String,
            tag: String
        ): Contract =
            Contract(uid, name, icon, displayName, tag, ContractType.SYSTEM)

        fun buildFold(
            uid: String,
            name: String,
            icon: String,
            displayName: String,
            tag: String
        ): Contract =
            Contract(uid, name, icon, displayName, tag, ContractType.FOLD)
    }

    public val typeId = contractType.ordinal

    public val typeName = contractType.name

}
