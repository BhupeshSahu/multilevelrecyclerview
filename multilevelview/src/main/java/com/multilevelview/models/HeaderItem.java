package com.multilevelview.models;

public class HeaderItem extends RecyclerViewItem {

    public HeaderItem(int level,String section) {
        super(level);
        setSectionName(section);
        setIsSection(true);
    }
}
