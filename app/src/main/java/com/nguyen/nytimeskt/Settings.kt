package com.nguyen.nytimeskt

import java.io.Serializable
import javax.inject.Inject

class Settings @Inject constructor() : Serializable {
    var beginDate: Date? = null
    var sortOrder: String? = null
    var arts: Boolean = false
    var fashionStyle: Boolean = false
    var sports: Boolean = false

    fun getBeginDate() : String? {
        return if (beginDate == null) null else beginDate.toString()
    }

    fun getOrder() : String? {
        // convert the first letter from uppercase to lowercase, so that "Oldest" becomes "oldest",
        // "Newest" becomes "newest"
        return if (sortOrder == null) null else (sortOrder!!.substring(0, 1).toLowerCase() + sortOrder!!.substring(1))
    }

    fun getFilterQuery() : String? {
        if (!arts && !fashionStyle && !sports) {
            return null
        } else {
            val values = mutableListOf<String>()
            if (arts) {
                values.add("\"Arts\"")
            }
            if (fashionStyle) {
                values.add("\"Fashion & Style\"")
            }
            if (sports) {
                values.add("\"Sports\"")
            }

            val builder = StringBuilder()
            builder.append("news_desk:(")
            builder.append(values.get(0))
            for (i in 1 until values.size) {
                builder.append(" ").append(values.get(i))
            }
            builder.append(")")
            return builder.toString()
        }
    }
}
