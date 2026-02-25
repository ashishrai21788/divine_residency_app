package com.app.core.utils

import android.text.InputFilter
import android.text.Spanned


class CoreInputLengthFilter(val max: Int, var enableDot: Boolean = false) : InputFilter {

    override fun filter(
        source: CharSequence, start: Int, end: Int, dest: Spanned,
        dstart: Int, dend: Int
    ): CharSequence? {
        var keep = max - (dest.length - (dend - dstart))
        when {
            keep <= 0 -> {
                return ""
            }
            keep >= end - start -> {
                return null // keep original
            }
            else -> {
                keep += start
                if (Character.isHighSurrogate(source[keep - 1])) {
                    --keep
                    if (keep == start) {
                        return ""
                    }
                }
                if (keep > 2 && enableDot) {
                    return source.subSequence(start, keep).replaceRange(keep - 2, keep, "..")
                }
                return source.subSequence(start, keep)
            }
        }
    }
}