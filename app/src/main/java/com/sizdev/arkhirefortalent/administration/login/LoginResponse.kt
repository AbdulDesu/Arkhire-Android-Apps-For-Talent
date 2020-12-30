package com.sizdev.arkhirefortalent.administration.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(val success: Boolean, val message: String, val data: Data? ) {
    data class Data(
            @SerializedName("accountID") val userId: String?,
            @SerializedName("account_name") val userEmail: String?,
            @SerializedName("account_email") val userLevel: String?,
            val privilege: String?,
            val token: String?
    )
}