package com.joeys.expriment.ptrRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.joeys.expriment.R

/**
 * @param retry 当加载更多失败时 state==STATE_RETRY ，点击retry按钮触发回调
 */
class LoadMoreBinder(
        val retry: () -> Unit
) : ItemViewBinder<LoadMoreState, LoadMoreBinder.VH>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH {
        return VH(inflater.inflate(R.layout.item_search_loadmore_binder, parent, false))
    }


    override fun onBindViewHolder(holder: VH, item: LoadMoreState) {
        if (item.state == LoadMoreState.STATE_IDLE ||
                item.state == LoadMoreState.STATE_LOADING) {
            showLoading(holder)
        } else if (item.state == LoadMoreState.STATE_RETRY) {
            holder.mRoot.setOnClickListener {
                item.state = LoadMoreState.STATE_LOADING
                showLoading(holder)
                retry.invoke()
            }
            showRetry(holder)
        } else if (item.state == LoadMoreState.STATE_NO_MORE) {
            showNoMore(holder)
        }
    }

    private fun showRetry(holder: VH) {
        holder.mProgressBar.setGone()
        holder.mTvRetry.setVisible()
        holder.mTvRetry.setText("retry")
    }

    private fun showNoMore(holder: VH) {
        holder.mProgressBar.setGone()
        holder.mTvRetry.setVisible()
        holder.mTvRetry.setText("no_more")
    }

    private fun showLoading(holder: VH) {
        holder.mRoot.setOnClickListener(null)
        holder.mTvRetry.setGone()
        holder.mProgressBar.setVisible()
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val mProgressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        val mTvRetry = view.findViewById<TextView>(R.id.tv_retry)
        val mRoot = view.findViewById<RelativeLayout>(R.id.root)

    }

}

data class LoadMoreState(var state: Int = STATE_IDLE) {
    companion object {
        /**
         * 初始状态
         */
        const val STATE_IDLE = 0
        /**
         * 加载更多中
         */
        const val STATE_LOADING = 1
        /**
         * 需要点击重试
         */
        const val STATE_RETRY = -1
        /**
         * 没有更多了
         */
        const val STATE_NO_MORE = -2
    }
}
