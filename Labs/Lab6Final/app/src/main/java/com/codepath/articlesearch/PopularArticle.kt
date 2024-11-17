package com.codepath.articlesearch

import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Keep
@Serializable
data class PopularArticlesResponse(
    @SerialName("results")
    val results: List<PopularArticle>?
)

@Keep
@Serializable
data class PopularArticle(
    @SerialName("url")
    val url: String?,

    @SerialName("published_date")
    val publishedDate: String?,

    @SerialName("byline")
    val byline: String?,

    @SerialName("title")
    val title: String?,

    @SerialName("abstract")
    val abstract: String?,

    @SerialName("media")
    val media: List<PopularMedia>?
)

@Keep
@Serializable
data class PopularMedia(
    @SerialName("media-metadata")
    val mediaMetadata: List<MediaMetadata>?
)

@Keep
@Serializable
data class MediaMetadata(
    @SerialName("url")
    val url: String?
)
