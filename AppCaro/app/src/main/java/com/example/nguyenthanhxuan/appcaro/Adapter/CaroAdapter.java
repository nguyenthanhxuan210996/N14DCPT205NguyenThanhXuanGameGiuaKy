package com.example.nguyenthanhxuan.appcaro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.nguyenthanhxuan.appcaro.R;
import com.example.nguyenthanhxuan.appcaro.data.ItemCaro;

import java.util.List;

/**
 * Created by Nguyen Thanh Xuan on 4/11/2018.
 */

public class CaroAdapter extends BaseAdapter {
    private List<ItemCaro> list;
    private LayoutInflater inflate;
    public CaroAdapter(Context c,List<ItemCaro> ListPlayer)
    {
        this.list = ListPlayer;
        this.inflate =LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHoler holer;
        if(convertView == null)
        {
            holer = new ViewHoler();
            convertView = inflate.inflate(R.layout.gird_item,null);
            holer.img =(ImageView)convertView.findViewById(R.id.imageGridItem);
            if(list.get(position).getYou() =='o')
                holer.img.setImageResource(R.drawable.tick);
            if(list.get(position).getYou() =='x')
                holer.img.setImageResource(R.drawable.cross);
            convertView.setTag(holer);
        }
        else
            holer =(ViewHoler) convertView.getTag();
        holer.img.setTag(position);
        return convertView;
    }
    class ViewHoler
    {
        ImageView img;
    }
}
