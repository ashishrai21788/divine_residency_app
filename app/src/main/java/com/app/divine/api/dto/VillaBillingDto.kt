package com.app.divine.api.dto

import com.google.gson.annotations.SerializedName

/** My bills / payments (resident) */
data class VillaBillDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("villa_id") val villaId: String? = null,
    @SerializedName("month") val month: Int? = null,
    @SerializedName("year") val year: Int? = null,
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("due_date") val dueDate: String? = null
)

data class VillaPaymentDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("villa_id") val villaId: String? = null,
    @SerializedName("amount_paid") val amountPaid: Double? = null,
    @SerializedName("payment_mode") val paymentMode: String? = null,
    @SerializedName("transaction_reference") val transactionReference: String? = null,
    @SerializedName("paid_at") val paidAt: String? = null
)
