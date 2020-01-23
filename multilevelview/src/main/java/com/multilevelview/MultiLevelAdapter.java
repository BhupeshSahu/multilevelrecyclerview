package com.multilevelview;


import androidx.recyclerview.widget.RecyclerView;

import com.multilevelview.models.RecyclerViewItem;

import java.util.List;


public abstract class MultiLevelAdapter<T extends RecyclerViewItem> extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> recyclerViewItemList;

    public MultiLevelAdapter(List<T> recyclerViewItems) {
        this.recyclerViewItemList = recyclerViewItems;
    }

    List<T> getRecyclerViewItemList() {
        return recyclerViewItemList;
    }

    public void updateItemList(List<T> itemList) {
        this.recyclerViewItemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        return recyclerViewItemList.get(position).getLevel();
    }


}
