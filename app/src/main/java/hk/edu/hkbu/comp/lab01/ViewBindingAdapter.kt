package hk.edu.hkbu.comp.lab01

import android.databinding.BindingAdapter
import android.webkit.WebView
import android.widget.ImageView
import com.squareup.picasso.Picasso


@BindingAdapter("html")
fun WebView.bindHtml(html:String) {
    this.loadData(html, "text/html", "null")
}

@BindingAdapter("imageUrl")
fun ImageView.bindImageUrl(url:String) {
    if (url.isNullOrBlank()) return
    Picasso.get().load(url).into(this)
}