package com.multilevelview;


import android.view.View;
import android.view.ViewGroup;

import com.multilevelview.models.HeaderItem;
import com.multilevelview.models.RecyclerViewItem;
import com.multilevelview.models.ViewType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public abstract class MultiLevelStickyHeaderAdapter extends
        MultiLevelAdapter {

    private LinkedHashMap<String, Integer> headerMap;

    public MultiLevelStickyHeaderAdapter(List recyclerViewItems) {
        super(recyclerViewItems);
        createSections(false);
    }

    List getRecyclerViewItemList() {
        return recyclerViewItemList;
    }

    void updateItemList(List itemList) {
        if (itemList != null && !itemList.isEmpty() && !(itemList.get(0) instanceof RecyclerViewItem)) {
            throw new IllegalStateException("List item must extend RecyclerViewItem");
        }
        super.recyclerViewItemList = itemList;
        createSections(true);
        notifyDataSetChanged();
    }

    public void updateList(List itemList) {
        if (itemList != null && !itemList.isEmpty() && !(itemList.get(0) instanceof RecyclerViewItem)) {
            throw new IllegalStateException("List item must extend RecyclerViewItem");
        }
        super.recyclerViewItemList = itemList;
        createSections(false);
        notifyDataSetChanged();
    }

    private void createSections(boolean fromRecyclerView) {
        if (super.recyclerViewItemList == null || super.recyclerViewItemList.isEmpty())
            return;
        int itemCount = 0;
        int headerIndex;
        String sectionName;
        if (fromRecyclerView) {
            headerMap = new LinkedHashMap<>();
            itemCount = 0;
            for (Object item : super.recyclerViewItemList) {
                RecyclerViewItem recyclerViewItem = (RecyclerViewItem) item;
                if (recyclerViewItem instanceof HeaderItem && !headerMap.containsKey(
                        recyclerViewItem.getSectionName())) {
                    headerMap.put(recyclerViewItem.getSectionName(), itemCount);
                }
                itemCount++;
            }
            return;
        }
//
//        final int childUnderSection = 3;
//
//        for (Object item : super.recyclerViewItemList) {
//            RecyclerViewItem recyclerViewItem = (RecyclerViewItem) item;
//            headerIndex = ((itemCount / childUnderSection) + 1);
//            sectionName = "Header " + headerIndex;
//            recyclerViewItem.setSectionName(sectionName);
//            itemCount++;
//            setSectionToChild(recyclerViewItem, sectionName);
//        }

        List<RecyclerViewItem> tempList = new ArrayList<>();
        headerMap = new LinkedHashMap<>();
        itemCount = 0;
        for (Object item : super.recyclerViewItemList) {
            RecyclerViewItem recyclerViewItem = (RecyclerViewItem) item;
            if (!headerMap.containsKey(recyclerViewItem.getSectionName())) {
                tempList.add(new HeaderItem(recyclerViewItem.getSectionName()));
                headerMap.put(recyclerViewItem.getSectionName(), itemCount++);
            }
            tempList.add(recyclerViewItem);
            itemCount++;
        }
        super.recyclerViewItemList = tempList;
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
        return super.recyclerViewItemList.get(position) instanceof HeaderItem ? ViewType.HEADER : ((RecyclerViewItem) recyclerViewItemList.get(position)).getLevel();
    }

    @Override
    public int getItemCount() {
        return super.recyclerViewItemList != null ? super.recyclerViewItemList.size() : 0;
    }

    public RecyclerViewItem getItemAt(int position) {
        return (RecyclerViewItem) super.recyclerViewItemList.get(position);
    }

    /**
     * This method gets called by {@link StickyHeaderItemDecorator} to fetch
     * the position of the header item in the adapter that is used for
     * (represents) item at specified position.
     *
     * @param itemPosition int. Adapter's position of the item for which to do
     *                     the search of the position of the header item.
     * @return int. Position of the header for an item in the adapter or
     * {@link RecyclerView.NO_POSITION} (-1) if an item has no header.
     */

    public int getHeaderPositionForItem(int itemPosition) {
        final Integer headerIndex = headerMap.get(((RecyclerViewItem) super.recyclerViewItemList.get(itemPosition)).getSectionName());
        return headerIndex != null ? headerIndex : 0;
    }

    /**
     * This method gets called by {@link StickyHeaderItemDecorator} to setup the header View.
     *
     * @param viewHolder     RecyclerView.ViewHolder. Holder to bind the data on.
     * @param headerPosition int. Position of the header item in the adapter.
     */

    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int headerPosition);

    /**
     * Called only twice when {@link StickyHeaderItemDecorator} needs
     * a new {@link RecyclerView.ViewHolder} to represent a sticky header item.
     * Those two instances will be cached and used to represent a current top sticky header
     * and the moving one.
     * <p>
     * You can either create a new android.view.View manually or inflate it from an XML layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindHeaderViewHolder(RecyclerView.ViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent The ViewGroup to resolve a layout params.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #onBindHeaderViewHolder(RecyclerView.ViewHolder, int)
     */
    public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);
}
