package com.brsv44n.some_courier.data

import com.brsv44n.some_courier.data.remote.dto.LoginResultDto
import com.brsv44n.some_courier.data.remote.dto.OrderDto
import com.brsv44n.some_courier.data.remote.dto.RemoteSettingsDto
import com.brsv44n.some_courier.data.remote.dto.RouteResponseDto
import com.brsv44n.some_courier.data.remote.dto.SingleOrderDto
import com.brsv44n.some_courier.data.remote.dto.UserDto
import com.brsv44n.some_courier.core.data.remote.PagedListPageSizeResponseDto
import kotlinx.serialization.json.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

@Suppress("ComplexInterface")
interface ApiService {
    @POST("delivery-app/courier/login")
    suspend fun login(
        @Body body: JsonObject,
    ): LoginResultDto

    @POST("delivery-app/courier/logout")
    suspend fun logout()

    @POST("delivery-app/courier/update_push_token")
    suspend fun updatePushToken(
        @Body body: JsonObject,
    )

    @GET("delivery-app/courier/settings")
    suspend fun settings(): RemoteSettingsDto

    @GET("delivery-app/courier/user")
    suspend fun getUser(): UserDto

    @GET("delivery-app/order/courier/list")
    suspend fun getOrdersHistory(
        @Query("type") type: String,
        @Query("page") page: Int,
    ): PagedListPageSizeResponseDto<OrderDto>

    @GET("delivery-app/order")
    suspend fun getOrder(
        @Query("orderId") orderId: Long
    ): SingleOrderDto

    @PATCH("delivery-app/courier/location")
    suspend fun sendLocation(
        @Body body: JsonObject,
    )

    @POST("delivery-app/order/status")
    suspend fun changeOrderStatus(
        @Body body: JsonObject,
    ): OrderDto

    @POST("delivery-app/order/has_trouble")
    suspend fun markOrderAsWithProblem(
        @Body body: JsonObject,
    ): OrderDto

    @GET("delivery-app/route/courier")
    suspend fun getCurrentRoute(): RouteResponseDto

    @POST("delivery-app/route/finish")
    suspend fun closeRoute()

    @GET("events")
    suspend fun openSSE()

}
