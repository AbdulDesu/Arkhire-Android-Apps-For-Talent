package com.sizdev.arkhirefortalent.homepage.item.home.project.highlightproject

import com.google.gson.annotations.SerializedName

data class HighLightProjectResponse(val success: Boolean, val message: String, val data: List<HighLightProjectResponse.NewProject>) {
    data class NewProject(@SerializedName("contributorID") val contributorID: String,
                          @SerializedName("participator_owner") val talentID: String,
                          @SerializedName("company_name") val companyName: String,
                          @SerializedName("company_image") val companyImage: String,
                          @SerializedName("project_tittle") val projectTittle: String,
                          @SerializedName("project_desc") val projectDesc: String,
                          @SerializedName("project_duration") val projectDuration: String,
                          @SerializedName("project_sallary") val projectSalary: String,
                          @SerializedName("project_image") val projectImage: String,
                          @SerializedName("offeringID") val offeringID: String,
                          @SerializedName("hiring_status") val hiringStatus: String,
                          @SerializedName("offered_salary") val offeredSalary: String,
                          @SerializedName("reply_message") val replyMsg: String)
}