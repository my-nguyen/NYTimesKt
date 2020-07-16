package com.nguyen.nytimeskt

import com.google.gson.annotations.SerializedName

data class Json(@SerializedName("response") val response: Response)

data class Response(@SerializedName("docs") val docs: List<Doc>)

data class Doc(@SerializedName("web_url") val webUrl: String,
               @SerializedName("multimedia") val multimedia: List<Multimedium>,
               @SerializedName("headline") val headline: Headline
)

data class Multimedium(@SerializedName("subtype") val subtype: String,
                       @SerializedName("url") val url: String
)

data class Headline(@SerializedName("main") val main: String)
