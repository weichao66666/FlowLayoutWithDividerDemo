package io.weichao.flowlayoutwithdividerdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.weichao.flowlayoutwithdividerdemo.util.ViewUtil;

/**
 * Created by chao.wei on 2018/1/26.
 */
public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";

    private FlowLayoutAdapter mFlowLayoutAdapter;

    private List<Line> lineList = new ArrayList<>();
    private int totalWidth;
    private int totalHeight;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(FlowLayoutAdapter adapter) {
        mFlowLayoutAdapter = adapter;
    }

    // 测量 child 的宽、高、margin，该方法有可能调用多次
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 把之前缓存数据全部清空
        if (mFlowLayoutAdapter == null) {
            return;
        }

        removeAllViews();
        lineList.clear();
        Line line = null;
        int usedWidth = 0;

        List<View> children = mFlowLayoutAdapter.getChildren();
        for (View child : children) {
            addView(child);
        }

        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();// 去掉 padding，实际可用的宽度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();// 去掉 padding，实际可用的高度
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);// 获取父容器为 child 设置的宽的测量模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);// 获取父容器为 child 设置的高的测量模式
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode);

        // 测量每个 child
        int index = 0;
        int childCount = getChildCount();
        boolean isLast = false;
        int dividerViewWidth = mFlowLayoutAdapter.getDividerViewWidth();
        int dividerViewRightMargin = mFlowLayoutAdapter.getDividerViewRightMargin();
        int dividerViewLeftMargin = mFlowLayoutAdapter.getDividerViewLeftMargin();
        int rowMargin = mFlowLayoutAdapter.getRowMargin();
        while (index < childCount && !isLast) {
            if (index == childCount - 1) {
                isLast = true;
            }
            View child = getChildAt(index);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            int j = 0;
            if (line == null) {
                line = new Line();
                usedWidth = 0;
                View dividerView = mFlowLayoutAdapter.getDividerView();
                addView(dividerView, index);
                line.addChild(dividerView, dividerViewWidth);
                usedWidth += dividerViewWidth;
                Log.d(TAG, "usedWidth: " + dividerViewWidth + "--" + usedWidth);
                j++;
            }
            usedWidth += dividerViewRightMargin;
            Log.d(TAG, "usedWidth: " + dividerViewRightMargin + "--" + usedWidth);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);// 测量 child
            int childWidth = child.getMeasuredWidth();
            usedWidth += childWidth;
            Log.d(TAG, "usedWidth: " + childWidth + "--" + usedWidth);
            usedWidth += dividerViewLeftMargin;
            Log.d(TAG, "usedWidth: " + dividerViewLeftMargin + "--" + usedWidth);
            usedWidth += dividerViewWidth;
            Log.d(TAG, "usedWidth: " + dividerViewWidth + "--" + usedWidth);
            if (usedWidth <= widthSize) {
                line.addChild(child, childWidth);
                View dividerView = mFlowLayoutAdapter.getDividerView();
                addView(dividerView, index + j + 1);
                line.addChild(dividerView, dividerViewWidth);
                j++;
            } else {
                lineList.add(line);// 把之前的行添加到集合中
                line = new Line();
                usedWidth = 0;
                View dividerView = mFlowLayoutAdapter.getDividerView();
                addView(dividerView, index);
                line.addChild(dividerView, dividerViewWidth);
                usedWidth += dividerViewWidth;
                Log.d(TAG, "usedWidth: " + dividerViewWidth + "--" + usedWidth);
                usedWidth += dividerViewRightMargin;
                Log.d(TAG, "usedWidth: " + dividerViewRightMargin + "--" + usedWidth);
                j++;
                line.addChild(child, childWidth);
                usedWidth += childWidth;
                Log.d(TAG, "usedWidth: " + childWidth + "--" + usedWidth);
                usedWidth += dividerViewLeftMargin;
                Log.d(TAG, "usedWidth: " + dividerViewLeftMargin + "--" + usedWidth);
                View dividerView1 = mFlowLayoutAdapter.getDividerView();
                addView(dividerView1, index + j + 1);
                line.addChild(dividerView1, dividerViewWidth);
                usedWidth += dividerViewWidth;
                Log.d(TAG, "usedWidth: " + dividerViewWidth + "--" + usedWidth);
                j++;
            }
            index += j;
            index++;
            childCount += j;
        }

        if (!lineList.contains(line)) {
            lineList.add(line);// 把最后一行添加到集合中
        }

        int totalWidth = widthSize + getPaddingLeft() + getPaddingRight();
        this.totalWidth = totalWidth;
        Log.d(TAG, "totalWidth: " + totalWidth);

        int totalHeight = 0;
        int lineListSize = lineList.size();
        for (int i = 0; i < lineListSize; i++) {
            totalHeight += lineList.get(i).getHeight();
        }
        totalHeight += (lineListSize - 1) * rowMargin;
        totalHeight += getPaddingTop();
        totalHeight += getPaddingBottom();
        this.totalHeight = totalHeight;
        Log.d(TAG, "totalHeight: " + totalHeight);

        setMeasuredDimension(totalWidth, totalHeight);
    }

    // 分配每个 child 的位置
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        left = getPaddingLeft();
        top = getPaddingTop();// 注意：不是 top += getPaddingTop();
        int rowMargin = mFlowLayoutAdapter.getRowMargin();
        for (int i = 0, size = lineList.size(); i < size; i++) {
            Line line = lineList.get(i);
            line.layout(left, top); // 分配行的位置，然后再交给每个行去分配 child 的位置
            top += line.getHeight();
            top += rowMargin;
        }
    }

    private class Line {
        private int width;
        private int height;
        private List<View> childList = new ArrayList<>();

        private void addChild(View child, int width) {
            childList.add(child);
            this.width += width;
            // 让当前行高度等于最高的 child 的高度
            int childMeasuredHeight = child.getMeasuredHeight();
            if (height < childMeasuredHeight) {
                height = childMeasuredHeight;
            }
        }

        // 分配行里面每个 child 的位置
        public void layout(int left, int top) {
            if (childList == null || childList.size() == 0) {
                return;
            }

            int gravityMode = mFlowLayoutAdapter.getGravityMode();
            int dividerViewWidth = mFlowLayoutAdapter.getDividerViewWidth();
            int dividerViewHeight = mFlowLayoutAdapter.getDividerViewHeight();
            int childListSize = childList.size();
            Log.d(TAG, "width: " + width);
            int marginsWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - width;
            Log.d(TAG, "marginsWidth: " + marginsWidth);
            int marginWidth = marginsWidth / (childListSize - 1);
            int minMargin = mFlowLayoutAdapter.getDividerViewMinMargin();
            if (gravityMode == FlowLayoutAdapter.GRAVITY_MODE_LEFT) {
                if (marginWidth < minMargin) {
                    marginWidth = minMargin;
                }
            }
            Log.d(TAG, "marginWidth: " + marginWidth);
            int minRestWidth = minMargin + dividerViewWidth;
            Log.d(TAG, "minRestWidth: " + minRestWidth);
            int offset = marginsWidth % (childListSize - 1);
            Log.d(TAG, "offset: " + offset);
            int leftOffset = offset >> 1;
            Log.d(TAG, "leftOffset: " + leftOffset);
            int rightOffset = offset - leftOffset;
            Log.d(TAG, "rightOffset: " + rightOffset);

            for (int i = 0; i < childListSize; i++) {
                View child = childList.get(i);
                // 分配 child 的位置
                int realTop;
                int right;
                int bottom;
                if (i % 2 == 0) {// dividerView
                    if (gravityMode == FlowLayoutAdapter.GRAVITY_MODE_LEFT_AND_RIGHT) {
                        if (i == childListSize - 1) {
                            Log.d(TAG, "before leftOffset layout: " + left + "--" + top + "--" + (left + dividerViewWidth) + "--" + (top + height));
                            left += leftOffset;
                        }
                    }
                    realTop = top + ((height - dividerViewHeight) >> 1);
                    right = left + dividerViewWidth;
                    bottom = realTop + dividerViewHeight;
                } else {// textView
                    realTop = top;
                    int measuredWidth = child.getMeasuredWidth();
                    if (gravityMode == FlowLayoutAdapter.GRAVITY_MODE_LEFT_AND_RIGHT) {
                        if (i == 1) {
                            Log.d(TAG, "before rightOffset layout: " + left + "--" + top + "--" + (left + measuredWidth) + "--" + (top + height));
                            left += rightOffset;
                        }
                    }
                    if (left + measuredWidth + marginWidth - 1 > totalWidth - getPaddingRight()) {
                        Log.d(TAG, (left + measuredWidth + marginWidth - 1) + ">" + (totalWidth - getPaddingRight()));
                        Log.d(TAG, "for minRestWidth layout: " + left + "--" + top + "--" + (left + measuredWidth + marginWidth - 1) + "--" + (top + height));
                        right = totalWidth - getPaddingRight() - minRestWidth;
                        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth - minRestWidth - marginWidth - 1, MeasureSpec.EXACTLY);
                        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), MeasureSpec.EXACTLY);
                        child.measure(widthMeasureSpec, heightMeasureSpec);
                    } else {
                        right = left + measuredWidth;
                    }
                    bottom = top + height;
                }
                child.layout(left, realTop, right, bottom);
                Log.d(TAG, "layout: " + left + "--" + top + "--" + right + "--" + bottom);
                left = right;
                switch (gravityMode) {
                    case FlowLayoutAdapter.GRAVITY_MODE_LEFT:
                        left += minMargin;
                        break;
                    case FlowLayoutAdapter.GRAVITY_MODE_LEFT_AND_RIGHT:
                        left += marginWidth;
                        break;
                }
            }
        }

        private int getHeight() {
            return height;
        }
    }

    abstract public static class FlowLayoutAdapter {
        public static final int GRAVITY_MODE_LEFT = 1;
        public static final int GRAVITY_MODE_LEFT_AND_RIGHT = 2;

        private Context context;
        private List<View> children;

        public FlowLayoutAdapter(Context context) {
            this.context = context;
        }

        abstract public List<String> getData();

        abstract public View getDividerView();

        abstract public int getDividerViewWidth();

        abstract public int getDividerViewHeight();

        private List<View> getChildren() {
            if (children == null) {
                children = new ArrayList<>();
                List<String> data = getData();
                for (String str : data) {
                    TextView child = new TextView(context);
                    child.setSingleLine();
                    child.setHeight(ViewUtil.dp2px(context, 30));
                    child.setGravity(Gravity.CENTER_VERTICAL);
                    child.setText(str);
                    children.add(child);
                }
            }
            return children;
        }

        public int getGravityMode() {
            return GRAVITY_MODE_LEFT;
        }

        public int getDividerViewLeftMargin() {
            if (getGravityMode() == GRAVITY_MODE_LEFT) {
                return getDividerViewMinMargin();
            }
            return 0;
        }

        public int getDividerViewRightMargin() {
            if (getGravityMode() == GRAVITY_MODE_LEFT) {
                return getDividerViewMinMargin();
            }
            return 0;
        }

        public int getDividerViewMinMargin() {
            return 0;
        }

        public int getRowMargin() {
            return 0;
        }
    }
}
