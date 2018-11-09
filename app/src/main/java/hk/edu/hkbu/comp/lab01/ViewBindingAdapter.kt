package hk.edu.hkbu.comp.lab01

import android.databinding.BindingAdapter
import android.webkit.WebView


@BindingAdapter("html")
fun WebView.bindHtml(html:String) {
    this.loadData(html, "text/html", "null")
}