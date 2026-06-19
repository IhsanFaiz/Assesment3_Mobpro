    package com.ihsanfaiz0048.asessment3.ui.screen

    import android.graphics.Bitmap
    import android.util.Log
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.ihsanfaiz0048.asessment3.model.OrderWithMenu
    import com.ihsanfaiz0048.asessment3.model.ReviewRequest
    import com.ihsanfaiz0048.asessment3.network.ApiStatus
    import com.ihsanfaiz0048.asessment3.network.MenuApi
    import com.ihsanfaiz0048.asessment3.network.OrderApi
    import com.ihsanfaiz0048.asessment3.network.ReviewApi
    import com.ihsanfaiz0048.asessment3.supabase.Constants
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext
    import okhttp3.MediaType.Companion.toMediaTypeOrNull
    import okhttp3.MultipartBody
    import okhttp3.RequestBody.Companion.toRequestBody
    import java.io.ByteArrayOutputStream
    import java.util.UUID

    class HistoryDetailViewModel : ViewModel() {

        private val _errorMessage = MutableStateFlow<String?>(null)
        val errorMessage: StateFlow<String?> = _errorMessage

        private val _status = MutableStateFlow(ApiStatus.LOADING)
        val status: StateFlow<ApiStatus> = _status

        suspend fun getDetailHistory(userId: String, id: Int): OrderWithMenu? {
            return withContext(Dispatchers.IO) {
                try {
                    val orders = OrderApi.services.getOrderById(Constants.SUPABASE_KEY, userId, "eq.$id")
                    val order = orders.firstOrNull() ?: return@withContext null
                    val menus = MenuApi.services.getMenuById(Constants.SUPABASE_KEY, userId, "eq.${order.idMenu}")
                    val menu = menus.firstOrNull() ?: return@withContext null
                    _status.value = ApiStatus.SUCCESS
                    OrderWithMenu(order, menu)
                } catch (e: Exception) {
                    null
                }
            }
        }

        fun deleteOrder(userId: String, id: Int){
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    OrderApi.services.deleteOrder(Constants.SUPABASE_KEY, userId, "eq.$id")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private val _uploadStatus = MutableStateFlow<ApiStatus>(ApiStatus.IDLE)
        val uploadStatus: StateFlow<ApiStatus> = _uploadStatus.asStateFlow()

        fun insertReview (review: String, star: Int, menuId: Int, userId: String, bitmap: Bitmap?, orderId: Int){
            viewModelScope.launch(Dispatchers.IO) {
                _uploadStatus.value = ApiStatus.LOADING
                try {
                    var finalImageUrl: String? = null

                    if (bitmap != null) {
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                        val byteArray = baos.toByteArray()

                        val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())

                        val uniqueFileName = "review_${UUID.randomUUID()}.jpg"
                        val bucketName = "review-image"

                        ReviewApi.services.uploadImage(
                            apiKey = Constants.SUPABASE_KEY,
                            authorization = "Bearer ${Constants.SUPABASE_KEY}",
                            contentType = "image/jpeg",
                            bucket = bucketName,
                            imageName = uniqueFileName,
                            file = requestBody
                        )

                        finalImageUrl = "${Constants.SUPABASE_URL}storage/v1/object/public/$bucketName/$uniqueFileName"
                    }
                    val review = ReviewRequest(
                        review = review,
                        star = star,
                        imageUrl = finalImageUrl,
                        menuId = menuId,
                        email = userId,
                        orderId = orderId
                    )
                    ReviewApi.services.insertReview(
                        Constants.SUPABASE_KEY,
                        "Bearer ${Constants.SUPABASE_KEY}",
                        review
                    )
                    _uploadStatus.value = ApiStatus.SUCCESS
                }catch (e: Exception){
                    e.printStackTrace()
                    Log.e("REVIEW", e.message ?: "Unknown error")
                    _uploadStatus.value = ApiStatus.FAILED
                }
            }
        }

        suspend fun checkIfOrderReviewed(orderId: Int, userId: String): Boolean {
            return try {
                val response = ReviewApi.services.checkReviewByOrder(
                    Constants.SUPABASE_KEY,
                    userId,
                    "eq.$orderId"
                )
                response.isNotEmpty()
            } catch (e: Exception) {
                Log.e("ViewModel", "Gagal cek status review: ${e.message}")
                false
            }
        }
    }