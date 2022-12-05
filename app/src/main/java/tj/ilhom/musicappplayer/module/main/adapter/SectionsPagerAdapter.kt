package tj.ilhom.musicappplayer.module.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import tj.ilhom.musicappplayer.module.main.model.TabItem

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    val items = ArrayList<TabItem>()

    fun setData(newItems: ArrayList<TabItem>) {
        val oldSize = ArrayList(items).size
        items.addAll(newItems)
        notifyItemRangeInserted(oldSize, itemCount)
    }

    override fun getItemCount() = items.size

    override fun createFragment(position: Int): Fragment {
        return items[position].fragment
    }

}