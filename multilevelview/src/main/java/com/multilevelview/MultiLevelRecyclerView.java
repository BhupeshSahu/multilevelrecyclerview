package com.multilevelview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.multilevelview.animators.DefaultItemAnimator;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MultiLevelRecyclerView extends RecyclerView implements OnRecyclerItemClickListener {

    public static final String TAG = MultiLevelRecyclerView.class.getName();

    Context mContext;

    private boolean isExpanded = false, accordion = false;

    private int prevClickedPosition = -1, numberOfItemsAdded = 0;

    private MultiLevelAdapter mMultiLevelAdapter;

    private boolean isToggleItemOnClick = true;

    private RecyclerItemClickListener recyclerItemClickListener;

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public void setAccordion(boolean accordion) {
        this.accordion = accordion;
    }

    public void setOnItemClick(OnRecyclerItemClickListener onItemClick) {
        this.onRecyclerItemClickListener = onItemClick;
    }

    public void setToggleItemOnClick(boolean toggleItemOnClick) {
        isToggleItemOnClick = toggleItemOnClick;
    }

    public MultiLevelRecyclerView(Context context) {
        super(context);
        mContext = context;
        setUp(context);
    }

    public MultiLevelRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    public MultiLevelRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUp(context);
    }

    private void setUp(Context context) {
        recyclerItemClickListener = new RecyclerItemClickListener(context);

        recyclerItemClickListener.setOnItemClick(this);

        addOnItemTouchListener(recyclerItemClickListener);

        setItemAnimator(new DefaultItemAnimator());
    }

    public void removeItemClickListeners() {
        if (recyclerItemClickListener != null) {
            removeOnItemTouchListener(recyclerItemClickListener);
        }
    }

    @Override
    public void setItemAnimator(ItemAnimator animator) {
        super.setItemAnimator(animator);
    }

    public void setAdapter(MultiLevelAdapter adapter) {
        super.setAdapter(adapter);
        mMultiLevelAdapter = adapter;
    }

    public void removeAllChildren(List list) {
        for (Object item : list) {
            RecyclerViewItem i = (RecyclerViewItem) item;
            if (i.isExpanded()) {
                i.setExpanded(false);
                removeAllChildren(i.getChildren());
                removePrevItems(mMultiLevelAdapter.getRecyclerViewItemList(), i.getPosition(), i.getChildren().size());
            }
        }
    }

    private int getExpandedPosition(int level) {
        List adapterList = mMultiLevelAdapter.getRecyclerViewItemList();
        for (Object item : adapterList) {
            RecyclerViewItem i = (RecyclerViewItem) item;
            if (level == i.getLevel() && i.isExpanded()) {
                return adapterList.indexOf(i);
            }
        }

        return -1;
    }

    private int getItemsToBeRemoved(int level) {
        List adapterList = mMultiLevelAdapter.getRecyclerViewItemList();
        int itemsToRemove = 0;
        for (Object item : adapterList) {
            RecyclerViewItem i = (RecyclerViewItem) item;
            if (level < i.getLevel()) {
                itemsToRemove++;
            }
        }
        return itemsToRemove;
    }

    public void openTill(int... positions) {
        if (mMultiLevelAdapter == null) {
            return;
        }
        List adapterList = mMultiLevelAdapter.getRecyclerViewItemList();
        if (adapterList == null || positions.length <= 0) {
            return;
        }
        int posToAdd = 0;
        int insidePosStart = -1;
        int insidePosEnd = adapterList.size();
        for (int position : positions) {
            posToAdd += position;
            if (posToAdd > insidePosStart && posToAdd < insidePosEnd) {
                addItems((RecyclerViewItem) adapterList.get(posToAdd), adapterList, posToAdd);
                insidePosStart = posToAdd;
                if (((RecyclerViewItem) adapterList.get(posToAdd)).getChildren() == null) {
                    break;
                }
                insidePosEnd = ((RecyclerViewItem) adapterList.get(posToAdd)).getChildren().size();
                posToAdd += 1;
            }
        }
    }

    public void toggleItemsGroup(int position) {
        if (position == -1) return;

        List adapterList = mMultiLevelAdapter.getRecyclerViewItemList();

        RecyclerViewItem clickedItem = (RecyclerViewItem) adapterList.get(position);

        if (accordion) {
            if (clickedItem.isExpanded()) {
                clickedItem.setExpanded(false);
                removeAllChildren(clickedItem.getChildren());
                removePrevItems(adapterList, position, clickedItem.getChildren().size());
                prevClickedPosition = -1;
                numberOfItemsAdded = 0;
            } else {
                int i = getExpandedPosition(clickedItem.getLevel());
                int itemsToRemove = getItemsToBeRemoved(clickedItem.getLevel());

                if (i != -1) {
                    removePrevItems(adapterList, i, itemsToRemove);

                    ((RecyclerViewItem) adapterList.get(i)).setExpanded(false);

                    if (clickedItem.getPosition() > ((RecyclerViewItem) adapterList.get(i)).getPosition()) {
                        addItems(clickedItem, adapterList, position - itemsToRemove);
                    } else {
                        addItems(clickedItem, adapterList, position);
                    }
                } else {
                    addItems(clickedItem, adapterList, position);
                }
            }
        } else {
            if (clickedItem.isExpanded()) {
                clickedItem.setExpanded(false);
                removeAllChildren(clickedItem.getChildren());
                removePrevItems(adapterList, position, clickedItem.getChildren().size());
                prevClickedPosition = -1;
                numberOfItemsAdded = 0;
            } else {
                addItems(clickedItem, adapterList, position);
            }
        }
    }


    @Override
    public void onItemClick(View view, RecyclerViewItem clickedItem, int position) {
        if (isToggleItemOnClick) {
            toggleItemsGroup(position);
        }
        if (onRecyclerItemClickListener != null)
            onRecyclerItemClickListener.onItemClick(view, clickedItem, position);
    }

    private void removePrevItems(List tempList, int position, int numberOfItemsAdded) {
        for (int i = 0; i < numberOfItemsAdded; i++) {
            if (tempList.size() > position + 1)
                tempList.remove(position + 1);
        }
        isExpanded = false;
        mMultiLevelAdapter.updateItemList(tempList);
        mMultiLevelAdapter.notifyItemRangeRemoved(position + 1, numberOfItemsAdded);
        refreshPosition();
    }

    public void refreshPosition() {
        int position = 0;
        for (Object item : mMultiLevelAdapter.getRecyclerViewItemList()) {
            RecyclerViewItem i = (RecyclerViewItem) item;
            i.setPosition(position++);
        }
    }

    public RecyclerViewItem getParentOfItem(RecyclerViewItem item) {
        try {
            int i;
            List list = mMultiLevelAdapter.getRecyclerViewItemList();
            if (item.getLevel() == 1) {
                return (RecyclerViewItem) list.get(item.getPosition());
            } else {
                int l;
                for (i = item.getPosition(); ; i--) {
                    l = ((RecyclerViewItem) list.get(i)).getLevel();
                    if (l == item.getLevel() - 1) {
                        break;
                    }
                }
            }
            return (RecyclerViewItem) list.get(i);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private void addItems(RecyclerViewItem clickedItem, List tempList, int position) {

        if (clickedItem.hasChildren()) {
            isExpanded = true;

            prevClickedPosition = position;

            tempList.addAll(position + 1, clickedItem.getChildren());

            clickedItem.setExpanded(true);

            numberOfItemsAdded = clickedItem.getChildren().size();

            mMultiLevelAdapter.updateItemList(tempList);

            mMultiLevelAdapter.notifyItemRangeInserted(position + 1, clickedItem.getChildren().size());

            smoothScrollToPosition(position);
            refreshPosition();

        }
    }

    private final class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector mGestureDetector;

        private OnRecyclerItemClickListener onItemClick;

        void setOnItemClick(OnRecyclerItemClickListener onItemClick) {
            this.onItemClick = onItemClick;
        }

        RecyclerItemClickListener(Context context) {

            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mGestureDetector.onTouchEvent(e)) {
                childView.performClick();
                int position = view.getChildAdapterPosition(childView);
                if (Constants.IS_LOG_ENABLED) {
                    Log.e(TAG, position + " Clicked On RecyclerView");
                }
                if (onItemClick != null && position != NO_POSITION) {
                    onItemClick.onItemClick(childView,
                            (RecyclerViewItem) mMultiLevelAdapter.getRecyclerViewItemList().get(position), position);
                }
                return isToggleItemOnClick;
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean arg0) {

        }

    }

    /**
     * Convenient method to get hierarchy stack of input item
     * @param item for which
     * @return a flat list of item's hierarchy starting from item itself(inclusive)
     */
    public List getNodeHierarchy(RecyclerViewItem item) {
        List nodeList = new ArrayList();
        List tempList = mMultiLevelAdapter.getRecyclerViewItemList();
        if (tempList.contains(item)) {
            nodeList.add(item);
            int lastLevel = item.getLevel();
            for (int i = tempList.indexOf(item); i >= 0; i--) {
                RecyclerViewItem iterItem = ((RecyclerViewItem) tempList.get(i));
                if (iterItem.getLevel() < lastLevel) {
                    nodeList.add(iterItem);
                    lastLevel = iterItem.getLevel();
                }
                if (lastLevel < 1)
                    break;
            }
        }
        return nodeList;
    }

}
