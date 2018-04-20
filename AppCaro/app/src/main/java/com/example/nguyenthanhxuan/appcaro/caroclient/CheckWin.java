package com.example.nguyenthanhxuan.appcaro.caroclient;

import com.example.nguyenthanhxuan.appcaro.data.ItemCaro;

import java.util.List;

/**
 * Created by Nguyen Thanh Xuan on 4/11/2018.
 */

public class CheckWin {
    public static Boolean Win(int pos,Character c,List<ItemCaro> list)
    {
        if(checkHangNgang(pos, c, list))
            return true;
        if(checkHangDoc(pos, c, list))
            return true;
        if(checkCheoChinh(pos, c, list))
            return true;
        if(checkCheoPhu(pos, c, list))
            return true;
        return false;
    }
    private static Boolean checkHangNgang(int pos,Character c,List<ItemCaro> list)
    {
        int count=0;
        pos = pos % 8;// chia lay phan du
        for(int i=0;i<8;i++)
        {
            if(list.get(i*8+pos).getYou() == c)
            {
                count++;
                if(count ==5)
                    return true;
            }
            else
                count=0;
        }
        return false;
    }
    private static Boolean checkHangDoc(int pos,Character c,List<ItemCaro> list)
    {
        int count=0;
        pos = pos / 8;//chia lay phan nguyen
        for(int i=0;i<8;i++)
        {
            if(list.get(pos*8+i).getYou() == c)
            {
                count++;
                if(count ==5)
                    return true;
            }
            else
                count=0;
        }
        return false;
    }
    private static Boolean checkCheoPhu(int pos,Character c,List<ItemCaro> list)
    {
        int count = 0;
        while( pos > 8 && pos % 8 < 8 )
            pos -=7;
        while(pos % 8 !=0 && pos / 8 < 8)
        {
            if(list.get(pos).getYou() == c)
            {
                count++;
                if(count == 5)
                    return true;
            }
            else
                count = 0;
            pos +=7;
        }
        return false;
    }
    private static Boolean checkCheoChinh(int pos,Character c,List<ItemCaro> list)
    {
        int count = 0;
        while( pos > 8 && pos % 8 !=0 )
            pos -=9;
        while(pos / 8 < 8 && pos % 8 < 8)
        {
            if(list.get(pos).getYou() == c)
            {
                count++;
                if(count == 5)
                    return true;
            }
            else
                count = 0;
            pos +=9;
        }
        return false;
    }
}
