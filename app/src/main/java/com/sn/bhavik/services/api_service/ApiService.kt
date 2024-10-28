package com.sn.bhavik.network

import com.sn.bhavik.model.OtpRequest
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName
import retrofit2.http.Header
import retrofit2.http.Headers

data class Data(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("contact_number") var contactNumber: String? = null,
    @SerializedName("dob") var dob: String? = null,
    @SerializedName("bio") var bio: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("latitude") var latitude: Int? = null,
    @SerializedName("longitude") var longitude: Int? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("fitness_level") var fitnessLevel: String? = null,
    @SerializedName("interests") var interests: List<String>? = null,
    @SerializedName("profile_photo") var profilePhoto: String? = null,
    @SerializedName("registered_at") var registeredAt: String? = null,
    @SerializedName("total_followers") var totalFollowers: Int? = null,
    @SerializedName("total_followings") var totalFollowings: Int? = null,
    @SerializedName("is_following") var isFollowing: Int? = null,
    @SerializedName("is_blocked") var isBlocked: Int? = null
)

data class Meta(
    @SerializedName("message") var message: String? = null
)

data class ApiResponse(
    @SerializedName("data") var data: Data? = Data(),
    @SerializedName("meta") var meta: Meta? = Meta()
)

data class ProfileUpdateRequest(
    val fullName: String,
    val email: String,
    val dob: String
)
data class ProfileUpdateRequestF2(
    val username: String,
    val bio: String,
    val gender: String,

)
data class ProfileUpdateRequestF3(
    @SerializedName("fitness_level") val fitnessLevel: String,
    val interests: List<String>
)


interface ApiService {
    @Headers("Content-Type: application/json")

    @POST("send-otp")
    fun requestOtp(@Body request: OtpRequest): Call<ApiResponse>

    @POST("verify-otp")
    fun verifyOtp(@Body request: OtpRequest): Call<ApiResponse>

    @POST("resend-otp")
    fun resendOtp(@Body request: OtpRequest): Call<ApiResponse>

    @Headers("Accept: application/json")
    @POST("edit-profile")
    fun updateProfile(@Header("Authorization") token: String, @Body data : JsonObject) : Call<ApiResponse>

}
