package ru.netology.nework.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nework.R
import ru.netology.nework.databinding.CardPostBinding
import ru.netology.nework.dto.Post
import ru.netology.nework.enumeration.AttachmentType
import ru.netology.nework.util.Converters
import ru.netology.nework.view.loadCircleCrop

interface PostOnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
}

class PostsAdapter(
    private val onInteractionListener: PostOnInteractionListener,
) : PagingDataAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        // FIXME: students will do in HW
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: PostOnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = Converters.convertDateTime(post.published)
            content.text = post.content
            if (post.authorAvatar.isNullOrEmpty()) {
                avatar.setImageResource(R.drawable.baseline_account_circle_24)
            } else {
                post.authorAvatar.let {
                    avatar.loadCircleCrop(it)
                }
            }
            like.isChecked = post.likedByMe
            like.text = "${post.likedOwnerIds.size}"
            mentioned.isChecked = post.mentionedMe
            mentioned.text = "${post.mentionIds.size}"

            if (post.link != null) {
                link.visibility = View.VISIBLE
                link.text = post.link
            } else link.visibility = View.GONE

            if (post.coords != null) {
                coordinates.visibility = View.VISIBLE
                coordinates.text = "${post.coords.lat}:${post.coords.long}"
            } else {
                coordinates.visibility = View.GONE
            }

            menu.visibility = if (post.ownedByMe) View.VISIBLE else View.INVISIBLE

            val attachment = post.attachment
            if (attachment != null) {
                when (attachment.type) {
                    AttachmentType.IMAGE -> {
                        attachmentImageView.visibility = View.VISIBLE
                        Glide.with(attachmentImageView)
                            .load(post.attachment.url)
                            .placeholder(R.drawable.baseline_downloading_24)
                            .error(R.drawable.baseline_broken_image_24)
                            .timeout(10_000)
                            .into(attachmentImageView)
                    }

                    AttachmentType.AUDIO -> {
                        //TODO Дописать аудио проигрователь
                        audioPlayerContainer.visibility = View.VISIBLE
                    }

                    else -> {
                        //TODO Дописать видео проигрыватель
                        videoPlayerContainer.visibility = View.VISIBLE
                    }
                }
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    // TODO: if we don't have other options, just remove dots
                    menu.setGroupVisible(R.id.owned, post.ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            mentioned.setOnClickListener {
                onInteractionListener.onShare(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
