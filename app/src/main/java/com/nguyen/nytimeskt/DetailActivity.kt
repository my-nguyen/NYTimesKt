package com.nguyen.nytimeskt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import com.nguyen.nytimeskt.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    companion object {
        val EXTRA_ARTICLE_OBJECT = "ARTICLE_OBJECT"

        fun newIntent(context: Context, article: Article) : Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_ARTICLE_OBJECT, article)
            return intent
        }
    }

    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var article = intent.getSerializableExtra(EXTRA_ARTICLE_OBJECT) as Article
        // set up to open WebView and not a browser
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        binding.webView.loadUrl(article.webUrl)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity, menu)
        val shareItem = menu?.findItem(R.id.action_share)
        val shareActionProvider = MenuItemCompat.getActionProvider(shareItem) as ShareActionProvider
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, binding.webView.url)
        shareActionProvider.setShareIntent(shareIntent)

        return super.onCreateOptionsMenu(menu)
    }
}
