package com.tatar.repofinder.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tatar.repofinder.R
import com.tatar.repofinder.data.model.Subscriber
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.subscriber_list_item.*

class SubscriberAdapter(private val picasso: Picasso) :
    RecyclerView.Adapter<SubscriberAdapter.SubscriberViewHolder>() {

    private var subscribers: List<Subscriber> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subscriber_list_item, parent, false)
        return SubscriberViewHolder(view)
    }

    override fun getItemCount(): Int {
        return subscribers.size
    }

    override fun onBindViewHolder(holder: SubscriberViewHolder, position: Int) {
        holder.bind(subscribers[position])
    }

    fun setSubscribers(subscribers: List<Subscriber>) {
        this.subscribers = subscribers
        notifyDataSetChanged()
    }

    inner class SubscriberViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(subscriber: Subscriber) = with(itemView) {
            picasso.load(subscriber.avatarUrl).into(subscriber_avatar_iv)
            subscriber_name_tv.text = subscriber.name
            subscriber_bio_tv.text = if (subscriber.bio.isNullOrBlank()) BIO_NOT_AVAILABLE_TEXT else subscriber.bio
        }
    }

    companion object {
        private const val BIO_NOT_AVAILABLE_TEXT = "Subscriber's bio is not available"
    }
}