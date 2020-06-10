package com.multilevelview;


import android.view.ViewGroup;

import com.multilevelview.models.HeaderItem;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public abstract class MultiLevelAdapter extends
        StickyAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder> {

    private List recyclerViewItemList;
    public final static int HEADER = 1000;
    private HashMap<String, Integer> headerMap;

    public MultiLevelAdapter(List recyclerViewItems) {
        this.recyclerViewItemList = recyclerViewItems;
        if (recyclerViewItems != null && !recyclerViewItems.isEmpty() && !(recyclerViewItems.get(0) instanceof RecyclerViewItem)) {
            throw new IllegalStateException("List item must extend RecyclerViewItem");
        }
        createSections(false);
    }

    List getRecyclerViewItemList() {
        return recyclerViewItemList;
    }

    void updateItemList(List itemList) {
        if (itemList != null && !itemList.isEmpty() && !(itemList.get(0) instanceof RecyclerViewItem)) {
            throw new IllegalStateException("List item must extend RecyclerViewItem");
        }
        this.recyclerViewItemList = itemList;
        createSections(true);
        notifyDataSetChanged();
    }

    public void updateList(List itemList) {
        if (itemList != null && !itemList.isEmpty() && !(itemList.get(0) instanceof RecyclerViewItem)) {
            throw new IllegalStateException("List item must extend RecyclerViewItem");
        }
        this.recyclerViewItemList = itemList;
        createSections(false);
        notifyDataSetChanged();
    }

    private void createSections(boolean fromRecyclerView) {
        int itemCount = 0;
        int headerIndex;
        String sectionName;
        if (fromRecyclerView) {
            headerMap = new HashMap<>();
            itemCount = 0;
            for (Object item : recyclerViewItemList) {
                RecyclerViewItem recyclerViewItem = (RecyclerViewItem) item;
                if (recyclerViewItem.getIsSection() && !headerMap.containsKey(
                        recyclerViewItem.getSectionName())) {
                    headerMap.put(recyclerViewItem.getSectionName(), itemCount);
                }
                itemCount++;
            }
            return;
        }
        if (recyclerViewItemList == null || recyclerViewItemList.isEmpty())
            return;
        final int childUnderSection = 3;

        for (Object item : recyclerViewItemList) {
            RecyclerViewItem recyclerViewItem = (RecyclerViewItem) item;
            headerIndex = ((itemCount / childUnderSection) + 1);
            sectionName = "Header " + headerIndex;
            recyclerViewItem.setSectionName(sectionName);
            itemCount++;
            setSectionToChild(recyclerViewItem, sectionName);
        }

        List<RecyclerViewItem> tempList = new ArrayList<>();
        headerMap = new HashMap<>();
        itemCount = 0;
        for (Object item : recyclerViewItemList) {
            RecyclerViewItem recyclerViewItem = (RecyclerViewItem) item;
            if (!headerMap.containsKey(recyclerViewItem.getSectionName())) {
                tempList.add(new HeaderItem(recyclerViewItem.getLevel(), recyclerViewItem.getSectionName()));
                headerMap.put(recyclerViewItem.getSectionName(), itemCount++);
            }
            tempList.add(recyclerViewItem);
            itemCount++;
        }
        recyclerViewItemList = tempList;
    }

    private void setSectionToChild(RecyclerViewItem recyclerViewItem, String sectionName) {
        if (recyclerViewItem.hasChildren()) {
            for (RecyclerViewItem child : recyclerViewItem.getChildren()) {
                child.setSectionName(sectionName);
                setSectionToChild(child, sectionName);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return recyclerViewItemList.get(position) instanceof HeaderItem ? HEADER : ((RecyclerViewItem) recyclerViewItemList.get(position)).getLevel();
    }

    @Override
    public int getItemCount() {
        return recyclerViewItemList != null ? recyclerViewItemList.size() : 0;
    }

    public RecyclerViewItem getItemAt(int position) {
        return (RecyclerViewItem) recyclerViewItemList.get(position);
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        final Integer headerIndex = headerMap.get(((RecyclerViewItem) recyclerViewItemList.get(itemPosition)).getSectionName());
        return headerIndex != null ? headerIndex : 0;
    }

    @Override
    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i);

    @Override
    public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);
}
