package co.muslimummah.android.widget.ptrRecyclerView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.muslimummah.android.R
import co.muslimummah.android.module.search.itemViews.LoadMoreState
import co.muslimummah.android.module.search.itemViews.LoadMoreBinder
import com.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.collections.forEachWithIndex
import timber.log.Timber

class PtrRecyclerView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    val recyclerView: RecyclerView
    val swipeRefreshLayout: SwipeRefreshLayout

    private var loadMoreElement = LoadMoreState()

    var isRefreshing = false
        set(value) {
            field = value
            swipeRefreshLayout.isRefreshing = value
        }

    /**
     * 是否还可以翻页加载更多
     */
    var hasMore = false
        set(value) {
            field = value
            if (field)
                setLoadMoreState(LoadMoreState.STATE_IDLE)
            else
                setLoadMoreState(LoadMoreState.STATE_NO_MORE)
        }

    /**
     * 下拉刷新监听
     */
    private var onRefreshListener: (() -> Unit)? = null

    /**
     * 下拉刷新监听
     */
    fun setOnRefreshListener(value: (() -> Unit)?): PtrRecyclerView {
        onRefreshListener = value
        if (value == null) {
            swipeRefreshLayout.isRefreshing = false
            swipeRefreshLayout.isEnabled = false
        } else {
            swipeRefreshLayout.isEnabled = true
            swipeRefreshLayout.setOnRefreshListener {
                onRefreshListener?.invoke()
            }
        }
        return this
    }


    /**
     * 加载更多监听
     */
    private var onLoadMoreListener: (() -> Unit)? = null

    /**
     * 加载更多监听
     */
    fun setOnLoadMoreListener(value: (() -> Unit)?): PtrRecyclerView {
        onLoadMoreListener = value
        return this
    }


    /**
     * 加载更多重试监听
     */
    private var onRetryListener: (() -> Unit)? = null

    /**
     * 加载更多重试监听
     */
    fun setOnRetryListener(value: (() -> Unit)?): PtrRecyclerView {
        onRetryListener = value
        return this
    }

    /**
     *  @param enableLoadMore 是否使用加载更多
     */
    fun setAdapter(adapter: MultiTypeAdapter, enableLoadMore: Boolean = true): PtrRecyclerView {
        recyclerView.adapter = adapter
        if (enableLoadMore)
            adapter.register(LoadMoreBinder {
                setLoadMoreState(LoadMoreState.STATE_LOADING)
                onRetryListener?.invoke()
            })

        return this
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager): PtrRecyclerView {
        recyclerView.layoutManager = layoutManager
        return this
    }

    /**
     * 设置加载更多的状态[LoadMoreState.Companion]
     *
     */
    fun setLoadMoreState(state: Int) {
        val adapter = recyclerView.adapter
        if (adapter is MultiTypeAdapter) {
            if (adapter.items.contains(loadMoreElement)) {
                loadMoreElement.state = state
                adapter.notifyDataSetChanged()
            } else
                addLoadMoreFooter(state)
        }
    }


    /**
     * 获取现在底部状态
     */
    fun getLoadMoreState() = loadMoreElement.state

    /**
     *  添加/修改底部item状态、 如果要修改，请使用[setLoadMoreState]
     *
     */
    private fun addLoadMoreFooter(state: Int = LoadMoreState.STATE_IDLE) {
        val adapter = recyclerView.adapter
        if (adapter is MultiTypeAdapter) {
            val items = adapter.items.toMutableList().apply {
                add(loadMoreElement
                        .apply {
                            this.state = state
                        })
            }
            adapter.items = items
            adapter.notifyDataSetChanged()
        }
    }

    private fun removeLoadMoreFooter() {
        val adapter = recyclerView.adapter
        if (adapter is MultiTypeAdapter) {
            var footIndex = -1
            adapter.items.forEachWithIndex { index, item ->
                if (item is LoadMoreState) {
                    footIndex = index
                    return@forEachWithIndex
                }
            }
            if (footIndex > -1) {
                val items = adapter.items.toMutableList().apply { remove(footIndex) }
                adapter.items = items
                adapter.notifyItemRemoved(footIndex)
            }
        }
    }

    init {
        val view = View.inflate(context, R.layout.ptr_recyclerview, null)
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.loadmoreListener {
            if (onLoadMoreListener == null)
                Timber.tag(this.javaClass.simpleName).e("未设置 { PtrRecyclerView.onLoadMoreListener } ")
            onLoadMoreListener?.invoke()
        }
        addView(view)
    }

}