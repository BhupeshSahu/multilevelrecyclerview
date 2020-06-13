package com.multilevelview;


import com.multilevelview.models.RecyclerViewItem;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public abstract class MultiLevelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List recyclerViewItemList;

    public MultiLevelAdapter(List recyclerViewItems) {
        this.recyclerViewItemList = recyclerViewItems;
        if (recyclerViewItems != null && !recyclerViewItems.isEmpty() && !(recyclerViewItems.get(0) instanceof RecyclerViewItem)) {
            throw new IllegalStateException("List item must extend RecyclerViewItem");
        }
    }

    List getRecyclerViewItemList() {
        return recyclerViewItemList;
    }

    public void updateList(List itemList) {
        updateItemList(itemList);
    }

    void updateItemList(List itemList) {
        if (itemList != null && !itemList.isEmpty() && !(itemList.get(0) instanceof RecyclerViewItem)) {
            throw new IllegalStateException("List item must extend RecyclerViewItem");
        }
        this.recyclerViewItemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return ((RecyclerViewItem) recyclerViewItemList.get(position)).getLevel();
    }


}
