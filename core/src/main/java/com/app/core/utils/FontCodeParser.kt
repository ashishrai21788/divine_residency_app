package com.app.core.utils

import com.app.core.extensions.isNotNullOrEmpty
import com.app.core.extensions.loge

object FontCodeParser {
    val fontRawCodes = ".appynative-block-user:before {\n" +
            "  content: \"\\61\";\n" +
            "}\n" +
            ".appynative-report:before {\n" +
            "  content: \"\\62\";\n" +
            "}\n" +
            ".appynative-send:before {\n" +
            "  content: \"\\63\";\n" +
            "}\n" +
            ".appynative-tag:before {\n" +
            "  content: \"\\64\";\n" +
            "}\n" +
            ".appynative-coupon:before {\n" +
            "  content: \"\\66\";\n" +
            "}\n" +
            ".appynative-coupon-1:before {\n" +
            "  content: \"\\67\";\n" +
            "}\n" +
            ".appynative-font-change:before {\n" +
            "  content: \"\\68\";\n" +
            "}\n" +
            ".appynative-loyalti-card:before {\n" +
            "  content: \"\\69\";\n" +
            "}\n" +
            ".appynative-my-orders:before {\n" +
            "  content: \"\\6a\";\n" +
            "}\n" +
            ".appynative-share:before {\n" +
            "  content: \"\\6b\";\n" +
            "}\n" +
            ".appynative-update-listing:before {\n" +
            "  content: \"\\6c\";\n" +
            "}\n" +
            ".appynative-check-in:before {\n" +
            "  content: \"\\65\";\n" +
            "}\n" +
            ".appynative-user:before {\n" +
            "  content: \"\\6d\";\n" +
            "}\n" +
            ".appynative-share-1:before {\n" +
            "  content: \"\\6e\";\n" +
            "}\n" +
            ".appynative-enquiry:before {\n" +
            "  content: \"\\6f\";\n" +
            "}\n" +
            ".appynative-camera-500x500:before {\n" +
            "  content: \"\\70\";\n" +
            "}\n" +
            ".appynative-web-lock:before {\n" +
            "  content: \"\\71\";\n" +
            "}\n" +
            ".appynative-solid-0:before {\n" +
            "  content: \"\\72\";\n" +
            "}\n" +
            ".appynative-solid-1:before {\n" +
            "  content: \"\\73\";\n" +
            "}\n" +
            ".appynative-solid-2:before {\n" +
            "  content: \"\\74\";\n" +
            "}\n" +
            ".appynative-solid-3:before {\n" +
            "  content: \"\\75\";\n" +
            "}\n" +
            ".appynative-stroke-0:before {\n" +
            "  content: \"\\76\";\n" +
            "}\n" +
            ".appynative-stroke-1:before {\n" +
            "  content: \"\\77\";\n" +
            "}\n" +
            ".appynative-stroke-2:before {\n" +
            "  content: \"\\78\";\n" +
            "}\n" +
            ".appynative-stroke-3:before {\n" +
            "  content: \"\\79\";\n" +
            "}\n" +
            ".appynative-solid-0-1:before {\n" +
            "  content: \"\\7a\";\n" +
            "}\n" +
            ".appynative-stroke-0-1:before {\n" +
            "  content: \"\\41\";\n" +
            "}\n" +
            ".appynative-beginner:before {\n" +
            "  content: \"\\42\";\n" +
            "}\n" +
            ".appynative-bmr:before {\n" +
            "  content: \"\\43\";\n" +
            "}\n" +
            ".appynative-diet:before {\n" +
            "  content: \"\\44\";\n" +
            "}\n" +
            ".appynative-edit-goal:before {\n" +
            "  content: \"\\45\";\n" +
            "}\n" +
            ".appynative-exercise:before {\n" +
            "  content: \"\\46\";\n" +
            "}\n" +
            ".appynative-pause:before {\n" +
            "  content: \"\\47\";\n" +
            "}\n" +
            ".appynative-record:before {\n" +
            "  content: \"\\48\";\n" +
            "}\n" +
            ".appynative-repeat:before {\n" +
            "  content: \"\\49\";\n" +
            "}\n" +
            ".appynative-rest:before {\n" +
            "  content: \"\\4a\";\n" +
            "}\n" +
            ".appynative-stop:before {\n" +
            "  content: \"\\4b\";\n" +
            "}\n" +
            ".appynative-workout:before {\n" +
            "  content: \"\\4c\";\n" +
            "}\n" +
            ".appynative-realestate-favorite:before {\n" +
            "  content: \"\\4d\";\n" +
            "}\n" +
            ".appynative-realestate-filter:before {\n" +
            "  content: \"\\4e\";\n" +
            "}\n" +
            ".appynative-realestate-share:before {\n" +
            "  content: \"\\4f\";\n" +
            "}\n" +
            ".appynative-realestate-sort:before {\n" +
            "  content: \"\\50\";\n" +
            "}\n" +
            ".appynative-no-driver:before {\n" +
            "  content: \"\\51\";\n" +
            "}\n" +
            ".appynative-arrow-map:before {\n" +
            "  content: \"\\52\";\n" +
            "}\n" +
            ".appynative-assignment:before {\n" +
            "  content: \"\\53\";\n" +
            "}\n" +
            ".appynative-classwork:before {\n" +
            "  content: \"\\54\";\n" +
            "}\n" +
            ".appynative-icon-awesome-book:before {\n" +
            "  content: \"\\56\";\n" +
            "}\n" +
            ".appynative-material:before {\n" +
            "  content: \"\\57\";\n" +
            "}\n" +
            ".appynative-people:before {\n" +
            "  content: \"\\59\";\n" +
            "}\n" +
            ".appynative-stream:before {\n" +
            "  content: \"\\5a\";\n" +
            "}\n" +
            ".appynative-filter:before {\n" +
            "  content: \"\\55\";\n" +
            "}\n" +
            ".appynative-message:before {\n" +
            "  content: \"\\58\";\n" +
            "}\n" +
            ".appynative-topic-value:before {\n" +
            "  content: \"\\30\";\n" +
            "}\n" +
            ".appynative-awesome-shield-alt:before {\n" +
            "  content: \"\\31\";\n" +
            "}\n" +
            ".appynative-clock:before {\n" +
            "  content: \"\\32\";\n" +
            "}\n" +
            ".appynative-metro-language:before {\n" +
            "  content: \"\\33\";\n" +
            "}\n" +
            ".appynative-ticket:before {\n" +
            "  content: \"\\34\";\n" +
            "}\n" +
            ".appynative-add-listings:before {\n" +
            "  content: \"\\35\";\n" +
            "}\n" +
            ".appynative-calendar:before {\n" +
            "  content: \"\\36\";\n" +
            "}\n" +
            ".appynative-camera:before {\n" +
            "  content: \"\\37\";\n" +
            "}\n" +
            ".appynative-delete:before {\n" +
            "  content: \"\\38\";\n" +
            "}\n" +
            ".appynative-document:before {\n" +
            "  content: \"\\39\";\n" +
            "}\n" +
            ".appynative-edit:before {\n" +
            "  content: \"\\21\";\n" +
            "}\n" +
            ".appynative-filter-1:before {\n" +
            "  content: \"\\22\";\n" +
            "}\n" +
            ".appynative-log-out:before {\n" +
            "  content: \"\\23\";\n" +
            "}\n" +
            ".appynative-offer:before {\n" +
            "  content: \"\\24\";\n" +
            "}\n" +
            ".appynative-person:before {\n" +
            "  content: \"\\25\";\n" +
            "}\n" +
            ".appynative-place:before {\n" +
            "  content: \"\\26\";\n" +
            "}\n" +
            ".appynative-place-nearby:before {\n" +
            "  content: \"\\27\";\n" +
            "}\n" +
            ".appynative-reset:before {\n" +
            "  content: \"\\28\";\n" +
            "}\n" +
            ".appynative-user-1:before {\n" +
            "  content: \"\\29\";\n" +
            "}\n" +
            ".appynative-update:before {\n" +
            "  content: \"\\2a\";\n" +
            "}\n" +
            ".appynative-audio-back:before {\n" +
            "  content: \"\\2b\";\n" +
            "}\n" +
            ".appynative-audio-link:before {\n" +
            "  content: \"\\2c\";\n" +
            "}\n" +
            ".appynative-audio-next:before {\n" +
            "  content: \"\\2d\";\n" +
            "}\n" +
            ".appynative-audio-pause:before {\n" +
            "  content: \"\\2e\";\n" +
            "}\n" +
            ".appynative-audio-play:before {\n" +
            "  content: \"\\2f\";\n" +
            "}\n" +
            ".appynative-audio-suffle:before {\n" +
            "  content: \"\\3a\";\n" +
            "}\n" +
            ".appynative-delete-1:before {\n" +
            "  content: \"\\3b\";\n" +
            "}\n" +
            ".appynative-bed-e3:before {\n" +
            "  content: \"\\3c\";\n" +
            "}\n" +
            ".appynative-auction:before {\n" +
            "  content: \"\\3d\";\n" +
            "}\n" +
            ".appynative-feature:before {\n" +
            "  content: \"\\3e\";\n" +
            "}\n" +
            ".appynative-nodata:before {\n" +
            "  content: \"\\3f\";\n" +
            "}\n" +
            ".appynative-uservoice:before {\n" +
            "  content: \"\\40\";\n" +
            "}"


    fun parse() {
        val builder = StringBuilder()
        fontRawCodes.split("\n").forEach { item ->
            loge("code", item)
            val step1Break = item.split(":before")
            if (step1Break.size >= 2) {
                val fontResName = step1Break[0].replace(".", "").replace("-", "_")
                val fontValueStep1 = step1Break[1].split(";")
                if (fontValueStep1.size >= 2) {
                    val fontResValue = fontValueStep1[0].split(":").lastOrNull()?.trim()?.replace("'\\", "")?.replace("'", "")
                    if (fontResValue.isNotNullOrEmpty()) {
                        val finalValue = String.format("<string name=\"$fontResName\">&#x%s;</string>", fontResValue)
                        builder.append(finalValue).append("\n")
                    }
                }
            }
        }
        loge("allFontName", builder.toString())
    }
}