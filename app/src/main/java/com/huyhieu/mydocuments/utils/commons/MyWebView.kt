package com.huyhieu.mydocuments.utils.commons

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

class MyWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : WebView(context, attrs, defStyleAttr) {
    init {
        setUpWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setUpWebView() {
        this.settings.apply {
            /*loadWithOverviewMode = true
            useWideViewPort = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING*/
            javaScriptEnabled = true
            allowContentAccess = true
        }
    }

    fun setDataToWebView(content: String?) {
        this.loadDataWithBaseURL("", getHtmlData(content.toString()), "text/html", "utf-8", "")
    }

    private fun getHtmlData(bodyHTML: String): String {
        val head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>"
        return "<html>$head<body>$bodyHTML</body></html>"
    }
}

