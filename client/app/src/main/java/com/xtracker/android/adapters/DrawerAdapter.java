package com.xtracker.android.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.xtracker.android.R;

import java.util.ArrayList;

public class DrawerAdapter extends BaseAdapter {

    private final LayoutInflater lInflater;
    private final ArrayList<Object> items;

    public DrawerAdapter(Context context) {
        String[] drawerItems = context.getResources().getStringArray(R.array.drawer_items);
        TypedArray drawerIcons = context.getResources().obtainTypedArray(R.array.drawer_icons);

        items = new ArrayList<>();
        for (int i = 0; i < drawerItems.length; i++) {
            Item item = new Item();
            item.text = drawerItems[i];
            item.iconId = drawerIcons.getResourceId(i, 0);
            items.add(item);
        }
        drawerIcons.recycle();

        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.drawer_list_item, parent, false);
        }

        Item item = (Item) getItem(position);

        ((TextView) view.findViewById(R.id.drawer_item_text)).setText(item.text);
        ((ImageView) view.findViewById(R.id.drawer_item_icon)).setImageResource(item.iconId);
        return view;
    }

    class Item {
        String text;
        int iconId;
    }
}
