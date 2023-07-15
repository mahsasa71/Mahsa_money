package ir.dunijet.Mahsamoney.ApiManager.Models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class NewsData(
    @SerializedName("Data")
    val `data`: List<Data?>?,
    @SerializedName("HasWarning")
    val hasWarning: Boolean?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("RateLimit")
    val rateLimit: RateLimit?,
    @SerializedName("Type")
    val type: Int?
) : Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("body")
        val body: String?,
        @SerializedName("categories")
        val categories: String?,
        @SerializedName("downvotes")
        val downvotes: String?,
        @SerializedName("guid")
        val guid: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("imageurl")
        val imageurl: String?,
        @SerializedName("lang")
        val lang: String?,
        @SerializedName("published_on")
        val publishedOn: Int?,
        @SerializedName("source")
        val source: String?,
        @SerializedName("source_info")
        val sourceInfo: SourceInfo?,
        @SerializedName("tags")
        val tags: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("upvotes")
        val upvotes: String?,
        @SerializedName("url")
        val url: String?
    ) : Parcelable {
        @Parcelize
        data class SourceInfo(
            @SerializedName("img")
            val img: String?,
            @SerializedName("lang")
            val lang: String?,
            @SerializedName("name")
            val name: String?
        ) : Parcelable
    }

    @Parcelize
    class RateLimit : Parcelable
}