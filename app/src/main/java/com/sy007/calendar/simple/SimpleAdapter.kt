package com.sy007.calendar.simple

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sy007.calendar.simple.R
import com.sy007.calendar.simple.databinding.ItemSimpleBinding

/**
 * Created by sy007 on 5/8/22.
 */
data class SimpleData(val title: String, val des: String, val tag: String)

class SimpleAdapter(private val data: List<SimpleData>, private val itemClickListener: View.OnClickListener) : RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder>() {

    private val mItemDecoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = 30
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(mItemDecoration)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView.removeItemDecoration(mItemDecoration)
    }

    class SimpleViewHolder(val binding: ItemSimpleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return SimpleViewHolder(ItemSimpleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        holder.itemView.tag = data[position].tag
        holder.itemView.apply {
            tag = data[position].tag
            setOnClickListener(itemClickListener)
        }
        holder.binding.apply {
            tvItemTitle.text = data[position].title
            tvItemDes.text = data[position].des
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}