package edu.xww.urchat.data.struct

data class Contract(
    val m_Id: String,
    val m_Name: String,
    val m_Icon: String,
    val m_DisplaceName: String,
    val m_ContractType: ContractType = ContractType.NORMAL
) : java.io.Serializable {

    enum class ContractType {
        NORMAL, SYSTEM, FOLD
    }

    companion object {
        fun buildNormal(id: String, name: String, icon: String, displayName: String): Contract =
            Contract(id, name, displayName, icon, ContractType.NORMAL)

        fun buildSystem(id: String, name: String, icon: String, displayName: String): Contract =
            Contract(id, name, displayName, icon, ContractType.SYSTEM)

        fun buildFold(id: String, name: String, icon: String, displayName: String): Contract =
            Contract(id, name, displayName, icon, ContractType.FOLD)
    }

    public val typeId = m_ContractType.ordinal

    public val typeName = m_ContractType.name

}
