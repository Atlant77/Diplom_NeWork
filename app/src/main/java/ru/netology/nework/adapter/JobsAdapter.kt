package ru.netology.nework.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.databinding.CardJobBinding
import ru.netology.nework.dto.Job
import ru.netology.nework.util.Converters

interface JobOnInteractionListener {
    fun onLinkClick(url: String) {}
    fun onRemoveJob(job: Job) {}
}

class JobAdapter(private val onInteractionListener: JobOnInteractionListener) :
    ListAdapter<Job, JobViewHolder>(JobDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = CardJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding, onInteractionListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)
        holder.bind(job)
    }
}

class JobViewHolder(
    private val binding: CardJobBinding,
    private val listener: JobOnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(job: Job) {
        binding.apply {
            company.text = job.name
            position.text = job.position
            jobStart.text = Converters.convertDateTime(job.start.toString())
            jobFinish.text = Converters.convertDateTime(job.finish.toString())
            link.text = job.link
            link.setOnClickListener {
                listener.onLinkClick(link.text.toString())
            }
        }
    }
}

class JobDiffCallback : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }
}