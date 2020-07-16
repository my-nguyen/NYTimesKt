package com.nguyen.nytimeskt

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.nytimeskt.databinding.ItemArticleBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ArticleAdapter(val articles: List<Article>, val context: Context) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val article = articles.get(layoutPosition)
            val intent = DetailActivity.newIntent(context, article)
            context.startActivity(intent)
        }

        fun bind(article: Article) {
            if (!TextUtils.isEmpty(article.thumbNail)) {
                Picasso.get()
                    .load(article.thumbNail)
                    .fit()
                    .into(binding.articleImage)
            }
            binding.articleTitle.setText(article.headline)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        // binding = DataBindingUtil.inflate(inflater, R.layout.item_article, parent, false)
        val binding = ItemArticleBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles.get(position)
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return articles.size
    }
}
