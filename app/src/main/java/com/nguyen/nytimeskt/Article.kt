package com.nguyen.nytimeskt

import java.io.Serializable

class Article(doc: Doc) : Serializable {
    var webUrl: String
    val headline: String
    var thumbNail: String = ""

    init {
        webUrl = doc.webUrl
        headline = doc.headline.main
        for ((subtype, url) in doc.multimedia) {
            if (subtype.equals("thumbnail")) {
                thumbNail = "http://www.nytimes.com/$url"
                break
            }
        }
    }
}
