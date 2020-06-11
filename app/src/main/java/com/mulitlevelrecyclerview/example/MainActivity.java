package com.mulitlevelrecyclerview.example;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.mulitlevelrecyclerview.R;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.StickyHeaderItemDecorator;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {

    private MultiLevelRecyclerView multiLevelRecyclerView;
    private StickyHeaderItemDecorator decorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        multiLevelRecyclerView = findViewById(R.id.rv_list);
        multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSimpleAdapter();

    }

    private void setSimpleAdapter() {

        MyAdapter myAdapter = new MyAdapter(this, new ArrayList(), multiLevelRecyclerView);

        // Just to clean-up previous sticky header residues
        // Not required in most of the scenarios
        if (decorator != null) {
            multiLevelRecyclerView.removeItemDecoration(decorator);
            decorator = null;
        }
        multiLevelRecyclerView.setAdapter(myAdapter);

        //If you are handling the click on your own then you can
        multiLevelRecyclerView.setToggleItemOnClick(true);

        multiLevelRecyclerView.setAccordion(false);

        // use this to update date at later stage
        myAdapter.updateList(recursivePopulateFakeData(0, 12));


    }

    private void setAdapterWithStickyHeader() {

        MyAdapterWithStickyHeader myAdapter = new MyAdapterWithStickyHeader(this, new ArrayList(), multiLevelRecyclerView);

        multiLevelRecyclerView.setAdapter(myAdapter);

        //If you are handling the click on your own then you can
        // multiLevelRecyclerView.removeItemClickListeners();
        multiLevelRecyclerView.setToggleItemOnClick(true);

        multiLevelRecyclerView.setAccordion(false);

        // use this to update date at later stage
        myAdapter.updateList(recursivePopulateFakeData(0, 12));

        decorator = new StickyHeaderItemDecorator(myAdapter);
        decorator.attachToRecyclerView(multiLevelRecyclerView);
    }


    private List<RecyclerViewItem> recursivePopulateFakeData(int levelNumber, int depth) {
        List<RecyclerViewItem> itemList = new ArrayList<>();

        String title;
        switch (levelNumber) {
            case 1:
                title = "PQRST %d";
                break;
            case 2:
                title = "XYZ %d";
                break;
            default:
                title = "ABCDE %d";
                break;
        }

        for (int i = 0; i < depth; i++) {
            Item item = new Item(levelNumber);
            item.setText(String.format(Locale.ENGLISH, title, i));
            item.setSecondText(String.format(Locale.ENGLISH, title.toLowerCase(), i));
            if (depth % 2 == 0) {
                item.addChildren(recursivePopulateFakeData(levelNumber + 1, depth / 2));
            }
            itemList.add(item);
        }

        return itemList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        /** Get the action view of the menu item whose id is search */
        View v = menu.findItem(R.id.action_list_type).getActionView();

        /** Get the edit text from the action view */
        Switch switchListType = v.findViewById(R.id.switch_list_type);
        switchListType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    setAdapterWithStickyHeader();
                else
                    setSimpleAdapter();
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_list_type) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
