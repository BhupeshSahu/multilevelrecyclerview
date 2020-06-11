package com.mulitlevelrecyclerview.example;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mulitlevelrecyclerview.R;
import com.multilevelview.MultiLevelAdapter;
import com.multilevelview.MultiLevelRecyclerView;

import java.util.List;
import java.util.Locale;


public class MyAdapter extends MultiLevelAdapter {

    private Holder mViewHolder;
    private Context mContext;
    private List mListItems;
    private Item mItem;
    private MultiLevelRecyclerView mMultiLevelRecyclerView;

    MyAdapter(Context mContext, List mListItems, MultiLevelRecyclerView mMultiLevelRecyclerView) {
        super(mListItems);
        this.mListItems = mListItems;
        this.mContext = mContext;
        this.mMultiLevelRecyclerView = mMultiLevelRecyclerView;
    }

    private void setExpandButton(ImageView expandButton, boolean isExpanded) {
        // set the icon based on the current state
        expandButton.setImageResource(isExpanded ? R.drawable.ic_keyboard_arrow_down_black_24dp : R.drawable.ic_keyboard_arrow_up_black_24dp);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void updateList(List list) {
        super.updateList(list);
        this.mListItems = list;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        mViewHolder = (Holder) holder;
        mItem = (Item) mListItems.get(position);

        switch (getItemViewType(position)) {
            case 1:
                holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
                break;
            case 2:
                holder.itemView.setBackgroundColor(Color.parseColor("#dedede"));
                break;
            default:
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
        }
        mViewHolder.mTitle.setText(mItem.getText());
        mViewHolder.mSubtitle.setText(mItem.getSecondText());

        if (mItem.hasChildren() && mItem.getChildren().size() > 0) {
            setExpandButton(mViewHolder.mExpandIcon, mItem.isExpanded());
            mViewHolder.mExpandButton.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.mExpandButton.setVisibility(View.GONE);
        }

        // indent child items
        // Note: the parent item should start at zero to have no indentation
        // e.g. in populateFakeData(); the very first Item shold be instantiate like this: Item item = new Item(0);
        float density = mContext.getResources().getDisplayMetrics().density;
        ((ViewGroup.MarginLayoutParams) mViewHolder.mTextBox.getLayoutParams()).leftMargin = (int) ((getItemViewType(position) * 20) * density + 0.5f);
        // The following code snippets are only necessary if you set multiLevelRecyclerView.removeItemClickListeners(); in MainActivity.java
        // this enables more than one click event on an item (e.g. Click Event on the item itself and click event on the expand button)
        mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set click event on item here
                Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d was clicked!",
                        mViewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });

        //set click listener on LinearLayout because the click area is bigger than the ImageView
        mViewHolder.mExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set click event on expand button here
                mMultiLevelRecyclerView.toggleItemsGroup(position);
                // rotate the icon based on the current state
                // but only here because otherwise we'd see the animation on expanded items too while scrolling
                mViewHolder.mExpandIcon.animate().rotation(mItem.isExpanded() ? -180 : 0).start();

                Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d is expanded: %s",
                        position, mItem.isExpanded()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListItems != null ? mListItems.size() : 0;
    }

    private class Holder extends RecyclerView.ViewHolder {

        TextView mTitle, mSubtitle;
        ImageView mExpandIcon;
        LinearLayout mTextBox, mExpandButton;

        Holder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mSubtitle = itemView.findViewById(R.id.subtitle);
            mExpandIcon = itemView.findViewById(R.id.image_view);
            mTextBox = itemView.findViewById(R.id.text_box);
            mExpandButton = itemView.findViewById(R.id.expand_field);
        }
    }

}
