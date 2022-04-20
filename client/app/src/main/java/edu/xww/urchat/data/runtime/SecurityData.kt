package edu.xww.urchat.data.runtime

import edu.xww.urchat.data.struct.security.Keychain

object SecurityData {

    fun clear(){
        sessionKeychain = null
    }

    var sessionKeychain: Keychain? = null


}