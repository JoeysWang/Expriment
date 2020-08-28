package com.joeys.expriment.scaleRecycler;


import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import com.joeys.expriment.scaleRecycler.ScalableRecyclerView;

import java.lang.ref.WeakReference;

public class GestureProxy implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {

    public static final float MIN_ZOOM = 1.0f;

    public static final float MAX_ZOOM = 2.0f;

    public static final int DISABLE_NONE = -1;

    public static final int DISABLE_SCROLL_X = 0;

    public static final int DISABLE_SCROLL_Y = 1;

    public static final int DISABLE_SCROLL_XY = 2;

    private static final int DEFAULT_ZOOM_DURATION = 300;

    final Matrix mBaseMatrix = new Matrix();

    final Matrix mCurrMatrix = new Matrix();

    final Matrix mRealMatrix = new Matrix();

    private final RectF mBaseRect = new RectF();

    private final RectF mRealRect = new RectF();

    private final float[] mMatrixValues = new float[9];

    private int mDisableMode = DISABLE_NONE;

    private int mZoomDuration = DEFAULT_ZOOM_DURATION;

    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public final float mTouchSlop;

    private boolean mScaling = false;

    private boolean mDragging = false;

    private boolean mZoomEnabled = true;

    private boolean mTouchDisable = false;

    private WeakReference<View> mTarget;

    private FlingRunnable mFlingRunnable;

    private GestureDetector mGestureDetector;

    private ScaleGestureDetector mScaleGestureDetector;

    private OnGestureListener mListener;

    public void setOnGestureListener(OnGestureListener listener) {
        mListener = listener;
    }

    public GestureProxy(View target) {
        this(target, false);
    }

    public GestureProxy(View target, boolean inTouch) {
        Context ctx = target.getContext();
        ViewConfiguration configuration = ViewConfiguration.get(ctx);
        mTarget = new WeakReference<>(target);
        mTouchSlop = configuration.getScaledTouchSlop();
        mGestureDetector = new GestureDetector(ctx, this);
        mScaleGestureDetector = new ScaleGestureDetector(ctx, this);
        if (inTouch) {
            target.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    boolean b = mGestureDetector.onTouchEvent(event);
                    boolean b1 = mScaleGestureDetector.onTouchEvent(event);
                    return b || b1;
                }
            });
        }
    }

    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public boolean isZoomEnabled() {
        return mZoomEnabled;
    }

    public void setZoomEnabled(boolean zoomEnabled) {
        this.mZoomEnabled = zoomEnabled;
    }

    public boolean isTouchDisable() {
        return mTouchDisable;
    }

    public void setTouchDisable(boolean touchDisable) {
        this.mTouchDisable = touchDisable;
    }

    public int getDisableMode() {
        return mDisableMode;
    }

    public void setDisableMode(int disableMode) {
        this.mDisableMode = disableMode;
    }

    public float getScale() {
        return (float) Math.sqrt((float) Math.pow(getValue(mCurrMatrix, Matrix.MSCALE_X), 2)
                + (float) Math.pow(getValue(mCurrMatrix, Matrix.MSKEW_Y), 2));
    }

    public boolean checkSlop(float dx, float dy) {
        return Math.abs(dx) > mTouchSlop || Math.abs(dy) > mTouchSlop;
    }

    public boolean checkVerticalSlop(float dx, float dy) {
        final float temp = Math.abs(dy);
        return temp > mTouchSlop && temp > Math.abs(dx);
    }

    public boolean checkHorizontalSlop(float dx, float dy) {
        final float temp = Math.abs(dx);
        return temp > mTouchSlop && temp > Math.abs(dy);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (isListener()) {
            return mListener.onSingleTapConfirmed(e);
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (mZoomEnabled) {
            View view = getTarget();
            if (null == view) {
                return false;
            }
            final float crisis = (MAX_ZOOM - MIN_ZOOM) / 2.0f + MIN_ZOOM;
            final float currScale = getScale();
            float dstScale;
            if (currScale > crisis) {
                dstScale = MIN_ZOOM;
            } else {
                dstScale = MAX_ZOOM;
            }
            postRunnable(view, new ZoomRunnable(e.getX(), e.getY(), currScale, dstScale));
        }
        if (isListener()) {
            mListener.onDoubleTap(e);
        }
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        cancelFling();
        mScaling = false;
        mDragging = false;
        if (isListener()) {
            mListener.onDown(e);
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mScaling) {
            return true;
        }
        if (!mDragging) {
            // Use Pythagoras to see if drag length is larger than touch slop
            mDragging = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY)) >= mTouchSlop && getScale() > MIN_ZOOM;
        }

        if (mDragging) {
            translate(- distanceX, - distanceY);
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (isListener()) {
            mListener.onLongPress(e);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        View view = getTarget();
        boolean isFling = getScale() > MIN_ZOOM;
        boolean isCartoonReader = view instanceof ScalableRecyclerView;
        if (null != view && isFling) {
            mFlingRunnable = new FlingRunnable(view.getContext());
            mFlingRunnable.fling((int) mBaseRect.width(),
                    (int) mBaseRect.height(), - (int) velocityX, isCartoonReader ? 0 : - (int) velocityY);
            postRunnable(view, mFlingRunnable);
        }
        if (isListener() && isFling) {
            mListener.onFling(isCartoonReader ? 0 : velocityX, velocityY);
        }
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
            return false;
        }
        float focusX = detector.getFocusX();
        float focusY = detector.getFocusY();
        scale(scaleFactor, focusX, focusY);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        final float currScale = getScale();
        if (currScale < MIN_ZOOM) {
            postRunnable(getTarget(), new ZoomRunnable(detector.getFocusX(), detector.getFocusY(), currScale, MIN_ZOOM));
        } else {
            mScaling = false;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (mTouchDisable) {
            return false;
        }
        boolean scaleBool = false;
        boolean ret = mGestureDetector.onTouchEvent(ev);
        if (mZoomEnabled) {
            scaleBool = mScaleGestureDetector.onTouchEvent(ev);
        }
        return ret || scaleBool;
    }

    public void updateBaseRect(RectF rectF) {
        updateBaseRect(rectF.left, rectF.top, rectF.right, rectF.bottom);
    }

    public void updateBaseRect(float left, float top, float right, float bottom) {
        mBaseRect.set(left, top, right, bottom);
    }

    public void resetViewOriginalState() {
        final float currScale = getScale();
        if (currScale != MIN_ZOOM) {
            scale(MIN_ZOOM / currScale, 0, 0);
        }
    }

    private boolean isListener() {
        return mListener != null;
    }

    private RectF getRealRect() {
        mRealMatrix.set(mBaseMatrix);
        mRealMatrix.postConcat(mCurrMatrix);
        mRealRect.set(0.f, 0.f, mBaseRect.width(), mBaseRect.height());
        mRealMatrix.mapRect(mRealRect);
        return mRealRect;
    }

    private float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    private int getViewWidth(View view) {
        if (view == null) {
            return 0;
        }
        return view.getWidth() - view.getPaddingLeft() - view.getPaddingRight();
    }

    private int getViewHeight(View view) {
        if (view == null) {
            return 0;
        }
        return view.getHeight() - view.getPaddingTop() - view.getPaddingBottom();
    }

    private void scale(float scaleFactor, float px, float py) {
        mScaling = true;
        mCurrMatrix.postScale(scaleFactor, scaleFactor, px, py);
        checkMatrixBounds();
        if (isListener()) {
            mListener.onScale(scaleFactor, px, py);
        }
    }

    private void translate(float dx, float dy) {
        switch (mDisableMode) {
            case DISABLE_SCROLL_X:
                mCurrMatrix.postTranslate(0.f, dy);
                break;
            case DISABLE_SCROLL_Y:
                mCurrMatrix.postTranslate(dx, 0.f);
                break;
            case DISABLE_SCROLL_XY:
                break;
            default:
                mCurrMatrix.postTranslate(dx, dy);
        }
        checkMatrixBounds();
        if (isListener()) {
            mListener.onDrag(dx, dy);
        }
    }

    private boolean checkMatrixBounds() {
        final View view = getTarget();
        if (null == view) {
            return false;
        }

        final RectF rect = getRealRect();
        if (null == rect) {
            return false;
        }

        final float height = rect.height(), width = rect.width();
        float deltaX = 0, deltaY = 0;

        final int viewHeight = getViewHeight(view);
        if (height <= viewHeight) {
            deltaY = (viewHeight - height) / 2.f - rect.top;
        } else if (rect.top > 0) {
            deltaY = -rect.top;
        } else if (rect.bottom < viewHeight) {
            deltaY = viewHeight - rect.bottom;
        }

        final int viewWidth = getViewWidth(view);
        if (width <= viewWidth) {
            deltaX = (viewWidth - width) / 2.f - rect.left;
        } else if (rect.left > 0) {
            deltaX = -rect.left;
        } else if (rect.right < viewWidth) {
            deltaX = viewWidth - rect.right;
        }

        // Finally actually translate the matrix
        mCurrMatrix.postTranslate(deltaX, deltaY);
        return true;
    }

    private View getTarget() {
        View view = null;

        if (mTarget != null) {
            view = mTarget.get();
        }

        return view;
    }

    private void postRunnable(View view, Runnable runnable) {
        if (null == view) {
            return;
        }
        view.post(runnable);
    }

    private void cancelFling() {
        if (null != mFlingRunnable) {
            mFlingRunnable.cancelFling();
            mFlingRunnable = null;
        }
        View view = getTarget();
        if (view instanceof ScalableRecyclerView) {
            ScalableRecyclerView readerView = (ScalableRecyclerView) view;
            readerView.stopScroll();
        }
    }

    class ZoomRunnable implements Runnable {

        private long startTime;

        private float focusX, focusY;

        private float startScale, endScale;

        public ZoomRunnable(float focusX, float focusY, float startScale, float endScale) {
            this.startTime = System.currentTimeMillis();
            this.focusX = focusX;
            this.focusY = focusY;
            this.startScale = startScale;
            this.endScale = endScale;
        }

        @Override
        public void run() {
            View view = getTarget();
            if (null == view) {
                mScaling = false;
                return;
            }

            float t = interpolate();
            float scale = startScale + t * (endScale - startScale);
            float scaleFactor = scale / getScale();

            scale(scaleFactor, focusX, focusY);

            // We haven't hit our target scale yet, so post ourselves again
            if (t < 1f) {
                postRunnable(view, this);
            } else {
                mScaling = false;
            }
        }

        private float interpolate() {
            float t = 1f * (System.currentTimeMillis() - startTime) / mZoomDuration;
            t = Math.min(1f, t);
            t = mInterpolator.getInterpolation(t);
            return t;
        }
    }

    class FlingRunnable implements Runnable {

        private int mCurrentX, mCurrentY;

        private OverScroller mScroller;

        public FlingRunnable(Context ctx) {
            mScroller = new OverScroller(ctx);
        }

        public void cancelFling() {
            mScroller.forceFinished(true);
        }

        public void fling(int viewWidth, int viewHeight, int velocityX, int velocityY) {
            final RectF rect = getRealRect();
            if (null == rect) {
                return;
            }

            final int startX = Math.round(-rect.left);
            final int minX, maxX, minY, maxY;

            if (mDisableMode == DISABLE_SCROLL_X) {
                if (velocityX > 0.f) {
                    minX = 0;
                    maxX = velocityX;
                } else {
                    minX = maxX = startX;
                }
            } else {
                if (viewWidth < rect.width()) {
                    minX = 0;
                    maxX = Math.round(rect.width() - viewWidth);
                } else {
                    minX = maxX = startX;
                }
            }

            final int startY = Math.round(-rect.top);
            if (mDisableMode == DISABLE_SCROLL_Y) {
                if (velocityY > 0.f) {
                    minY = 0;
                    maxY = velocityY;
                } else {
                    minY = maxY = startY;
                }
            } else {
                if (viewHeight < rect.height()) {
                    minY = 0;
                    maxY = Math.round(rect.height() - viewHeight);
                } else {
                    minY = maxY = startY;
                }
            }

            mCurrentX = startX;
            mCurrentY = startY;

            // If we actually can move, fling the scroller
            if (startX != maxX || startY != maxY) {
                mScroller.fling(startX, startY, velocityX, velocityY, minX,
                        maxX, minY, maxY, 0, 0);
            }
        }

        @Override
        public void run() {
            if (mScroller.isFinished()) {
                return; // remaining post that should not be handled
            }

            View view = getTarget();
            if (null != view && mScroller.computeScrollOffset()) {

                final int newX = mScroller.getCurrX();
                final int newY = mScroller.getCurrY();

                translate(mCurrentX - newX, mCurrentY - newY);

                mCurrentX = newX;
                mCurrentY = newY;

                postRunnable(view, this);
            }
        }
    }

    public interface OnGestureListener {

        void onDown(MotionEvent e);

        boolean onSingleTapConfirmed(MotionEvent e);

        void onDoubleTap(MotionEvent e);

        void onLongPress(MotionEvent e);

        void onDrag(float dx, float dy);

        void onFling(float velocityX, float velocityY);

        void onScale(float scaleFactor, float focusX, float focusY);
    }
}
