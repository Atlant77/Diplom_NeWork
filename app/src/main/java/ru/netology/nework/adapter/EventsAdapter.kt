package ru.netology.nework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nework.R
import ru.netology.nework.databinding.CardEventBinding
import ru.netology.nework.dto.Event
import ru.netology.nework.enumeration.AttachmentType
import ru.netology.nework.view.loadCircleCrop

interface EventOnInteractionListener {
    fun onLike(event: Event) {}
    fun onEdit(event: Event) {}
    fun onRemove(event: Event) {}
    fun onShare(event: Event) {}
}

class EventsAdapter(
    private val onInteractionListener: EventOnInteractionListener,
) : PagingDataAdapter<Event, EventViewHolder>(EventDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        // FIXME: students will do in HW
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}

class EventViewHolder(
    private val binding: CardEventBinding,
    private val onInteractionListener: EventOnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: Event) {
        binding.apply {
            author.text = event.author
            published.text = event.published
            content.text = event.content
            event.authorAvatar?.let { avatar.loadCircleCrop(it) }
            like.isChecked = event.likedByMe
            like.text = "${event.likeOwnerIds.size}"
            participates.isChecked = event.participatedByMe
            participates.text = "${event.participantsIds.size}"

            if (event.link != null) {
                link.visibility = View.VISIBLE
                link.text = event.link
            } else link.visibility = View.GONE

            if (event.coords != null) {
                coordinates.visibility = View.VISIBLE
                coordinates.text = event.coords.toString()
            } else {
                coordinates.visibility = View.GONE
            }

            menu.visibility = if (event.ownedByMe) View.VISIBLE else View.INVISIBLE

            val attachment = event.attachment
            if (attachment != null) {
                when (attachment.type) {
                    AttachmentType.IMAGE -> {
                        attachmentImageView.visibility = View.VISIBLE
                        Glide.with(attachmentImageView)
                            .load(event.attachment.url)
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
                    menu.setGroupVisible(R.id.owned, event.ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(event)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.onEdit(event)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                onInteractionListener.onLike(event)
            }

            participates.setOnClickListener {
                onInteractionListener.onShare(event)
            }
        }
    }
}

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}
