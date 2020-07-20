package com.nguyen.nytimeskt

import java.io.Serializable

class Article(doc: Doc) : Serializable {
    var webUrl = doc.webUrl
    val headline = doc.headline.main
    var thumbNail: String = ""

    init {
        for ((subtype, url) in doc.multimedia) {
            if (subtype == "thumbnail") {
                thumbNail = "http://www.nytimes.com/$url"
                break
            }
        }
    }
}
