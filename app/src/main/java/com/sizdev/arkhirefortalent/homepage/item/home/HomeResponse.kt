package com.sizdev.arkhirefortalent.homepage.item.home

import com.google.gson.annotations.SerializedName


data class HomeResponse (val success: String, val message: String, val data: talent) {
    data class talent(@SerializedName("account_name") val accountName: String)
}