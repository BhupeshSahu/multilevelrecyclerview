[![](https://jitpack.io/v/BhupeshSahu/multilevelrecyclerview.svg)](https://jitpack.io/#BhupeshSahu/multilevelrecyclerview)
# MultiLevel Expandable RecyclerView
This library is an extension of the `RecyclerView` class and behaves like the `ExpandableListView` widget but with more than just 2-levels.


## Prerequisites


```
Add the following dependency in your `build.gradle` file in your app folder:

dependencies {
    implementation 'com.muditsen.multilevelrecyclerview:multilevelview:1.0.0'
}
 ```
 
 
## Usage
Put the following snippet in your layout file:
```xml
<com.multilevelview.MultiLevelRecyclerView
        android:id="@+id/rv_list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

And declare your `MultiLevelRecyclerView` in your Activity's `onCreate()` (or your Fragment's `onCreateView()`) method like this:
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    MultiLevelRecyclerView multiLevelRecyclerView = (MultiLevelRecyclerView) findViewById(R.id.rv_list);
    multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Add items here...
    List<Item> itemList = new ArrayList<>();

    MyAdapter myAdapter = new MyAdapter(this, itemList, multiLevelRecyclerView);

    multiLevelRecyclerView.setAdapter(myAdapter);


    //If you want to already opened Multi-RecyclerView just call openTill where is parameter is
    // position to corresponding each level.
    multiLevelRecyclerView.openTill(0,1,2,3);
}
```

**Your Adapter file must extend from the** `MultiLevelAdapter.java` **class in order to work properly**:
```java
public class MyAdapter extends MultiLevelAdapter { ... }
```

The Accordion feature can be enabled by adding the following line of code:
```java
multiLevelRecyclerView.setAccordion(true);
```

The `recursivePopulateFakeData()` method adds items to an `ArrayList<>()` which are passed to the adapter and then get populated in the `MultiLevelRecyclerView`. This is how the items look like:

![MultiLevelRecyclerView][image1]

**Important: By default the** `MultiLevelRecyclerView` **sets a click listener on the whole item!**

If you want different click events on one item e.g.: one click event on the item itself and one click event on the expand button then add the following line of code after declaring your `MultiLevelRecyclerView`:
```java
multiLevelRecyclerView.removeItemClickListeners();
```

```
Now you do not have to remove the removeItemClickListeners() instead call setToggleItemOnClick to TRUE or FALSE
accordingly if you want to expand or collapse on click of that item.
```
## Adding Sticky Header

From `v2.1` onwards you can add Sticky Header on listview by extending your adapter with `MultiLevelStickyHeaderAdapter` instead of `MultiLevelAdapter`.
```java
public class MyAdapterWithStickyHeader extends MultiLevelStickyHeaderAdapter {
```
Attach this decorator to your `MultiLevelRecyclerView`

```java
StickyHeaderItemDecorator decorator = new StickyHeaderItemDecorator(myAdapter);
decorator.attachToRecyclerView(multiLevelRecyclerView);
```

Please clone or download the repository in order to see the sample code to better understand the usage!

# License
Copyright (C) 2015 Mudti Sen <muditsen1234@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[//]: # (References)
[image1]: ./images/multilevelrecyclerview-screenshot1.png?raw=true "MultiLevelRecyclerView"
