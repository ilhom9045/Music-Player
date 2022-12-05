package tj.ilhom.musicappplayer.module.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.module.main.callback.OnMusicAdapterItemClickListener
import tj.ilhom.musicappplayer.module.main.model.MusicItemDTO

class MusicAdapter : RecyclerView.Adapter<MusicAdapter.ViewHolder>(), Filterable {

    private val items = ArrayList<MusicItemDTO>()
    private var dataList = ArrayList<MusicItemDTO>()

    var musicAdapterItemClickListener: OnMusicAdapterItemClickListener? = null

    fun addData(newItems: List<MusicItemDTO>) {
        items.clear()
        dataList.clear()
        dataList.addAll(newItems)
        items.addAll(newItems)
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.music_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

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

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<MusicItemDTO>()

                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(dataList)
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
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

                val result = FilterResults()
                result.values = filteredList
                return result
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                items.clear()
                items.addAll(results?.values as ArrayList<MusicItemDTO>)
                notifyDataSetChanged()
            }

        }
    }
}
