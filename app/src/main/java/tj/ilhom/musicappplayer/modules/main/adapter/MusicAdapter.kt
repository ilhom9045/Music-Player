package tj.ilhom.musicappplayer.modules.main.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.modules.main.callback.OnMusicAdapterItemClickListener
import tj.ilhom.musicappplayer.modules.main.model.MusicItemDTO

class MusicAdapter() :
    ListAdapter<MusicItemDTO, MusicAdapter.ViewHolder>(MusicDiffUtil()) {

    private val items = ArrayList<MusicItemDTO>()
    private var dataList = ArrayList<MusicItemDTO>()

    var musicAdapterItemClickListener: OnMusicAdapterItemClickListener? = null

    fun addData(newItems: List<MusicItemDTO>) {
        items.clear()
        dataList.clear()
        dataList.addAll(newItems)
        items.addAll(newItems)
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.music_item, parent, false)
        )
    }

    override fun getItemCount() = currentList.size

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var music_name: TextView = v.findViewById(R.id.music_name)
        private var music_duration: TextView = v.findViewById(R.id.music_duration)
        private var cardView: CardView = v.findViewById(R.id.cardView)

        @SuppressLint("SetTextI18n")
        fun bind(musicItemDTO: MusicItemDTO) {
            music_duration.text = musicItemDTO.duration
            music_name.text = musicItemDTO.name
            cardView.setOnClickListener {
                musicAdapterItemClickListener?.onItemClicked(musicItemDTO)
            }
        }
    }

    private class MusicDiffUtil : DiffUtil.ItemCallback<MusicItemDTO>() {

        override fun areItemsTheSame(oldItem: MusicItemDTO, newItem: MusicItemDTO): Boolean {
            return oldItem.path == newItem.path
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: MusicItemDTO, newItem: MusicItemDTO): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    fun filter(constraint: String) {
        val filteredList = ArrayList<MusicItemDTO>()

        if (constraint.isEmpty()) {
            filteredList.addAll(dataList)
        } else {
            val filterPattern = constraint.lowercase().trim()
            for (i in dataList) {
                if (i.name.trim().lowercase().contains(filterPattern) || i.artist?.trim()
                        ?.lowercase()?.contentEquals(
                            filterPattern
                        ) == true
                ) {
                    filteredList.add(i)
                }
            }
        }

        submitList(filteredList)
    }
}
