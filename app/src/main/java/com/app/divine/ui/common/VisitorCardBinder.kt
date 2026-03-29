package com.app.divine.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.divine.api.dto.VillaVisitorLogDto
import com.app.divine.databinding.ItemPendingVisitorBinding

object VisitorCardBinder {

    fun inflateAndBind(
        parent: ViewGroup,
        log: VillaVisitorLogDto,
        onApprove: () -> Unit,
        onReject: () -> Unit,
    ): ItemPendingVisitorBinding {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPendingVisitorBinding.inflate(inflater, parent, false)
        binding.visitorName.text = log.name ?: "Visitor"
        binding.visitorAvatarInitial.text =
            (log.name ?: "V").trim().firstOrNull()?.uppercaseChar()?.toString() ?: "V"
        binding.visitorGateTime.text = buildString {
            log.gateId?.let { append("Gate $it") }
            log.createdAt?.let {
                if (isNotEmpty()) append(" • ")
                append(it)
            }
            if (isEmpty()) log.purpose?.let { append(it) }
            if (isEmpty()) log.phone?.let { append(it) }
        }.ifEmpty { "—" }
        binding.visitorApprove.setOnClickListener { onApprove() }
        binding.visitorReject.setOnClickListener { onReject() }
        return binding
    }
}
