package com.joeys.expriment.ptrRecyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.joeys.expriment.ptrRecyclerView.LoadMoreState


fun RecyclerView.loadmoreListener(onLoadmore: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            var lastPosition = -1
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    lastPosition = layoutManager.findLastVisibleItemPosition()
                } else
                    return

                val itemCount = recyclerView.layoutManager?.itemCount ?: 0
                if (itemCount <= 0)
                    return

                if (lastPosition == itemCount - 1) {
                    if (recyclerView.adapter is MultiTypeAdapter) {
                        val adapter = recyclerView.adapter as MultiTypeAdapter
                        val lastItem = adapter.items[lastPosition]
                        if (lastItem is LoadMoreState && lastItem.state == LoadMoreState.STATE_IDLE) {
                            lastItem.state = LoadMoreState.STATE_LOADING
                            adapter.notifyItemChanged(lastPosition)
                            onLoadmore.invoke()
                        }
                    }
                }
            }
        }
    })

}

fun TextView.measureWord(): Float {
    return paint.measureText("a")
}

inline fun View.setGone() {
    visibility = View.GONE
}

inline fun View.setVisible() {
    visibility = View.VISIBLE
}

inline fun View.setInvisible() {
    visibility = View.INVISIBLE
}