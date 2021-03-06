package com.example.nguyenthanhxuan.appcaro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nguyenthanhxuan.appcaro.R;

import java.util.List;

/**
 * Created by Nguyen Thanh Xuan on 4/11/2018.
 */

public class PlayerAdapter extends BaseAdapter {
    private List<String> list;
    private LayoutInflater inflate;
    public PlayerAdapter(Context c, List<String> ListPlayer)
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
            convertView = inflate.inflate(R.layout.list_item,null);
            holer.lableName =(TextView)convertView.findViewById(R.id.labelPlayerItem);
            convertView.setTag(holer);
        }
        else
            holer =(ViewHoler) convertView.getTag();
        holer.lableName.setTag(position);
        holer.lableName.setText(list.get(position));
        return convertView;
    }
    class ViewHoler
    {
        TextView lableName;
    }
}
