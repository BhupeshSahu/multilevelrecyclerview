package com.multilevelview.models;


import java.util.List;

public abstract class RecyclerViewItem {

    private List<RecyclerViewItem> children;

    private int level;

    private int position;

    private boolean expanded = false;

    private String sectionName;

    private boolean isSection = false;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    protected RecyclerViewItem(int level) {
        this.level = level;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public List<RecyclerViewItem> getChildren() {
        return children;
    }

    public void addChildren(List<RecyclerViewItem> children) {
        this.children = children;
    }

    public boolean hasChildren() {
        if (children != null && children.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public boolean getIsSection() {
        return isSection;
    }

    public void setIsSection(boolean isSection) {
        this.isSection = isSection;
    }
}
