package com.tatar.repofinder.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tatar.repofinder.R
import com.tatar.repofinder.data.model.Repo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.repository_list_item.*

class RepoAdapter(private val picasso: Picasso, private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<RepoAdapter.RepositoryViewHolder>() {

    private var repoList: List<Repo>

    init {
        repoList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repository_list_item, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return repoList.size
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(repoList[position], itemClickListener)
    }

    fun setRepositoryListItems(repoList: List<Repo>) {
        this.repoList = repoList
        notifyDataSetChanged()
    }

    inner class RepositoryViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(repo: Repo, itemClickListener: ItemClickListener) = with(itemView) {
            repo_name_tv.text = repo.name
            picasso.load(repo.ownerAvatarUrl).into(repo_owner_avatar_iv)
            repo_description_tv.text = if (repo.description == null) "--" else repo.description
            repo_owner_name_tv.text = repo.ownerName
            repo_fork_count_tv.text = repo.forkCount.toString()
            repo_primary_language_tv.text = if (repo.primaryLanguage == null) "--" else repo.primaryLanguage

            setOnClickListener { itemClickListener.onItemClick(repo) }
        }
    }

    interface ItemClickListener {
        fun onItemClick(repo: Repo)
    }
}