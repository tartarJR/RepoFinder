package com.tatar.repofinder.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tatar.repofinder.R
import com.tatar.repofinder.data.model.Repository
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.repository_list_item.*

class RepositoryAdapter(private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    private var repositoryList: List<Repository>

    init {
        repositoryList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.repository_list_item, parent, false)
        return RepositoryViewHolder(v)
    }

    override fun getItemCount(): Int {
        return repositoryList.size
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(repositoryList[position], itemClickListener)
    }

    fun setRepositoryListItems(repositoryList: List<Repository>) {
        this.repositoryList = repositoryList
        notifyDataSetChanged()
    }

    inner class RepositoryViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(repository: Repository, itemClickListener: ItemClickListener) = with(itemView) {
            repo_name_tv.text = repository.name
            Picasso.get().load(repository.ownerAvatarUrl).into(repo_owner_avatar_iv) // TODO inject picasso
            repo_description_tv.text = repository.description
            repo_owner_name_tv.text = repository.ownerName
            repo_fork_count_tv.text = repository.forkCount.toString()
            repo_primary_language_tv.text = repository.primaryLanguage

            setOnClickListener { itemClickListener.onItemClick(repository) }
        }
    }

    interface ItemClickListener {
        fun onItemClick(repository: Repository)
    }
}