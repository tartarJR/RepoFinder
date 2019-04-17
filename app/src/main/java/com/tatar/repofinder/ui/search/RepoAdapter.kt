package com.tatar.repofinder.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tatar.repofinder.R
import com.tatar.repofinder.data.model.Repo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.repo_list_item.*

class RepoAdapter(private val picasso: Picasso, private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    private var repos: List<Repo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_list_item, parent, false)
        return RepoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(repos[position], itemClickListener)
    }

    fun setRepos(repoList: List<Repo>) {
        this.repos = repoList
        notifyDataSetChanged()
    }

    inner class RepoViewHolder(override val containerView: View) :
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