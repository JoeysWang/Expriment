package com.joeys.expriment.scaleRecycler;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ScalableRecyclerView extends RecyclerView implements  GestureProxy.OnGestureListener {

    protected static final int TOUCH_CENTER = 0;

    protected static final int TOUCH_TOP = 1;//At LinearLayoutManager.VERTICAL is valid

    protected static final int TOUCH_LEFT = 2;//At LinearLayoutManager.HORIZONTAL is valid

    protected static final int TOUCH_RIGHT = 3;//At LinearLayoutManager.HORIZONTAL is valid

    protected static final int TOUCH_BOTTOM = 4;//At LinearLayoutManager.VERTICAL is valid

    protected static final float TOUCH_SCROLL_RATIO = 0.95f;

    private static final int SINGLE_TAP = 1;

    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();

    private static final int DOUBLE_TAP_MIN_TIME = 40;

    private int mDoubleTapSlopSquare;

    protected int mTouchRegion;

    private Handler mHandler;

    private float mTouchDownX;

    private float mTouchDownY;

    private boolean mStillDown;

    private boolean mDoubleTap;

    private boolean mPointerDown = false;

    private boolean mDeferConfirmSingleTap;

    private boolean mAlwaysInBiggerTapRegion;

    private MotionEvent mCurrentDownEvent;

    private MotionEvent mPreviousUpEvent;

    private GestureProxy mGestureProxy;

    private OnTouchCartoonCenterListener mCenterListener;

    private OnInterceptMoveEventListener interceptMoveEventListener;
    protected WindowManager mWindowManager;

    protected int mScreenWidth;

    protected int mScreenHeight;

    public ScalableRecyclerView(Context context) {
        super(context);
        init(context);

    }


    public ScalableRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public ScalableRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);

    }

    private void init(Context context) {
        mGestureProxy = new GestureProxy(this);
        mGestureProxy.setOnGestureListener(this);
        mDoubleTapSlopSquare = (int) (mGestureProxy.mTouchSlop * mGestureProxy.mTouchSlop);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == SINGLE_TAP) {
                    if (mPointerDown || mStillDown || mDoubleTap
                            || !mAlwaysInBiggerTapRegion) {
                        mDoubleTap = false;
                        return;
                    }
                    singleTapConfirmed();
                }
            }
        };
    }

    private DisplayMetrics obtainScreenMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        }
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public void fineTouchRegion(float x, float y) {
        DisplayMetrics metrics = obtainScreenMetrics();
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        final int tempDisX = mScreenWidth / 4;
        final int tempDisY = mScreenHeight / 4;
        boolean boolX = x > tempDisX && x < (mScreenWidth - tempDisX);
        boolean boolY = y > tempDisY && y < (mScreenHeight - tempDisY);
        if (boolX && boolY) {
            mTouchRegion = TOUCH_CENTER;
        } else {
            if (getManagerOrientation() == LinearLayoutManager.HORIZONTAL) {
                if (x > mScreenWidth / 2) {
                    mTouchRegion = TOUCH_RIGHT;
                } else {
                    mTouchRegion = TOUCH_LEFT;
                }
            } else {
                if (y > mScreenHeight / 2) {
                    mTouchRegion = TOUCH_BOTTOM;
                } else {
                    mTouchRegion = TOUCH_TOP;
                }
            }
        }
    }

    protected boolean singleTapConfirmed() {
        if (mTouchRegion == TOUCH_CENTER) {
            if (mCenterListener != null) {
                return mCenterListener.onCenter(this);
            }
        } else {
            mDeferConfirmSingleTap = true;
        }
        if (getManagerOrientation() == LinearLayoutManager.VERTICAL) {
            final int scrollHeight = (int) (mScreenHeight * TOUCH_SCROLL_RATIO);
            switch (mTouchRegion) {
                case TOUCH_TOP:
                    smoothScrollBy(0, -scrollHeight);
                    break;
                case TOUCH_BOTTOM:
                    smoothScrollBy(0, scrollHeight);
                    break;
                default:
                    break;
            }
        } else {

            switch (mTouchRegion) {
                case TOUCH_LEFT:

                    smoothScrollBy(-mScreenWidth, 0);
                    break;
                case TOUCH_RIGHT:
                    smoothScrollBy(mScreenWidth, 0);
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    public LinearLayoutManager getCartoonLayoutManager() {
        return (LinearLayoutManager) getLayoutManager();
    }

    public void resetOriginalState(int orientation) {
        if (orientation == LinearLayoutManager.VERTICAL) {
            if (mGestureProxy != null) {
                mGestureProxy.resetViewOriginalState();
            }
        }
    }

    public void changeGestureProxyAttribute() {
        if (mGestureProxy == null) {
            return;
        }
        if (getManagerOrientation() == LinearLayoutManager.VERTICAL) {
            mGestureProxy.setZoomEnabled(true);
            mGestureProxy.setDisableMode(GestureProxy.DISABLE_SCROLL_Y);
        } else {
            mGestureProxy.setZoomEnabled(false);
            mGestureProxy.setDisableMode(GestureProxy.DISABLE_SCROLL_X);
        }
    }

    public int getManagerOrientation() {
        LinearLayoutManager manager = getCartoonLayoutManager();
        if (manager == null) {
            return -1;
        }
        return manager.getOrientation();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mGestureProxy.updateBaseRect(getLeft(), getTop(), getRight(), getBottom());
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        c.concat(mGestureProxy.mCurrMatrix);
    }

    public void moveToPosition(int index, int dy) {
        RecyclerView.LayoutManager manager = getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) manager;
            llm.scrollToPositionWithOffset(index, dy + 1);
            llm.setStackFromEnd(true);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (getLayoutManager() == null) {
            return super.dispatchTouchEvent(e);
        }
        final int action = e.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                final float x = e.getX();
                final float y = e.getY();
                if (interceptMoveEventListener != null && canChildScroll(this, false, 0, 0, x, y)) {
                    requestDisallowInterceptTouchEvent(true);
                    return super.dispatchTouchEvent(e);
                }
                mStillDown = true;
                mDeferConfirmSingleTap = false;
                mAlwaysInBiggerTapRegion = true;
                fineTouchRegion(e.getRawX(), e.getRawY());
                mTouchDownX = x;
                mTouchDownY = y;
                boolean hasMessage = mHandler.hasMessages(SINGLE_TAP);
                if (hasMessage) mHandler.removeMessages(SINGLE_TAP);
                if (hasMessage && isConsideredDoubleTap(mCurrentDownEvent, mPreviousUpEvent, e)) {
                    mDoubleTap = true;
                }
                if (mCurrentDownEvent != null) {
                    mCurrentDownEvent.recycle();
                }
                mCurrentDownEvent = MotionEvent.obtain(e);
                mHandler.sendEmptyMessageDelayed(SINGLE_TAP, DOUBLE_TAP_TIMEOUT);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mPointerDown = true;

                break;
            case MotionEvent.ACTION_MOVE:
                final int deltaX = (int) (e.getX() - mTouchDownX);
                final int deltaY = (int) (e.getY() - mTouchDownY);
                if ((deltaX * deltaX) + (deltaY * deltaY) > mDoubleTapSlopSquare) {
                    mAlwaysInBiggerTapRegion = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mStillDown = false;
                mPointerDown = false;
                if (mPreviousUpEvent != null) {
                    mPreviousUpEvent.recycle();
                }
                mPreviousUpEvent = MotionEvent.obtain(e);
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                        fling(0, 0);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(e);
    }

    private String TAG = "tagivRecycler";

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onInterceptTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, "onInterceptTouchEvent ACTION_POINTER_DOWN");
                requestDisallowInterceptTouchEvent(true);
                return false;

            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onInterceptTouchEvent ACTION_MOVE");
                break;
        }


        if (getLayoutManager() == null) {
            return super.onInterceptTouchEvent(e);
        }
        final float x = e.getX();
        final float y = e.getY();
        if (interceptMoveEventListener != null && canChildScroll(this, false, 0, 0, x, y)) {
            requestDisallowInterceptTouchEvent(true);
            return false;
        }
        if (getManagerOrientation() == LinearLayoutManager.VERTICAL) {
            return true;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, "onTouchEvent ACTION_POINTER_DOWN");
                return false;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent ACTION_MOVE");
                break;
        }
        boolean s = super.onTouchEvent(e);
        if (e.getAction() == MotionEvent.ACTION_UP) {
            if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                fling(0, 0);
            }
        }
        return mGestureProxy.onTouchEvent(e) || s;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
//        if (getManagerOrientation() == LinearLayoutManager.HORIZONTAL) {
//            CartoonLayoutManager cal = getCartoonLayoutManager();
//            if (mDeferConfirmSingleTap) {
//                cal.setClick(true);
//                cal.setTouchRegion(mTouchRegion);
//            }
//            int index = cal.computeScrollToItemIndex(velocityX, velocityY);
//            smoothScrollToPosition(index);
//            return true;
//        }
        return super.fling(velocityX, velocityY);
    }

    @Override
    public void onDown(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public void onDoubleTap(MotionEvent e) {
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public void onDrag(float dx, float dy) {
        invalidate();
    }

    @Override
    public void onFling(float velocityX, float velocityY) {
        fling(-(int) velocityX, -(int) velocityY);
    }

    @Override
    public void onScale(float scaleFactor, float focusX, float focusY) {
        invalidate();
    }

    private boolean isConsideredDoubleTap(MotionEvent firstDown, MotionEvent firstUp,
                                          MotionEvent secondDown) {
        if (firstDown == null || firstUp == null) {
            return false;
        }

        if (!mAlwaysInBiggerTapRegion) {
            return false;
        }

        final long deltaTime = secondDown.getEventTime() - firstUp.getEventTime();
        if (deltaTime > DOUBLE_TAP_TIMEOUT || deltaTime < DOUBLE_TAP_MIN_TIME) {
            return false;
        }

        int deltaX = (int) firstDown.getX() - (int) secondDown.getX();
        int deltaY = (int) firstDown.getY() - (int) secondDown.getY();
        return (deltaX * deltaX + deltaY * deltaY < 100 * 100);
    }

    protected boolean canChildScroll(View v, boolean checkV, float dx, float dy, float focusX, float focusY) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;

            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                final View child = group.getChildAt(i);

                final int childLeft = child.getLeft() + supportGetTranslationX(child);
                final int childRight = child.getRight() + supportGetTranslationX(child);
                final int childTop = child.getTop() + supportGetTranslationY(child);
                final int childBottom = child.getBottom() + supportGetTranslationY(child);

                if (focusX >= childLeft && focusX < childRight && focusY >= childTop && focusY < childBottom
                        && canChildScroll(child, true, dx, dy, focusX - childLeft, focusY - childTop)) {
                    return true;
                }
            }
        }

        return checkV && interceptMoveEventListener.isDraggable(v, dx, dy, focusX, focusY);
    }

    private int supportGetTranslationY(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return (int) v.getTranslationY();
        }

        return 0;
    }

    private int supportGetTranslationX(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return (int) v.getTranslationX();
        }

        return 0;
    }

    public boolean checkTouchSlop(float dx, float dy) {
        if (mGestureProxy != null) {
            return mGestureProxy.checkSlop(dx, dy);
        }
        return false;
    }

    public boolean checkTouchVerticalSlop(float dx, float dy) {
        if (mGestureProxy != null) {
            return mGestureProxy.checkVerticalSlop(dx, dy);
        }
        return false;
    }

    public boolean checkTouchHorizontalSlop(float dx, float dy) {
        if (mGestureProxy != null) {
            return mGestureProxy.checkHorizontalSlop(dx, dy);
        }
        return false;
    }

    public void setOnTouchCartoonCenterListener(OnTouchCartoonCenterListener centerListener) {
        this.mCenterListener = centerListener;
    }

    public void setInterceptMoveEventListener(OnInterceptMoveEventListener interceptMoveEventListener) {
        this.interceptMoveEventListener = interceptMoveEventListener;
    }

    /* on touch in CartoonReaderView center region listener */
    public interface OnTouchCartoonCenterListener {
        boolean onCenter(View v);

    }

    /* check child touch */
    public interface OnInterceptMoveEventListener {

        boolean isDraggable(View v, float dx, float dy, float x, float y);
    }
}
