/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocaro;
import java.util.List;
/**
 *
 * @author Nguyen Thanh Xuan
 */
public class ParaData {
    public static String NoContinue()
	{
		return "11";
	}
	public static String NextCaro()
	{
		return "10";
	}
	public static String Win()
	{
		return "9";
	}
	public static String EndGame()
	{
		return "8";
	}
	public static String NewGame()
	{
		return "7";
	}
	public static String Tic(String xy,String c)
	{
		return "6-"+xy+":"+c;
	}
	public static String ResultInvite(String result,String player2)
	{
		return "5-"+result+":"+player2;
	}
	public static String Invite(String from)
	{
		return "4-"+from;
	}
	public static String NameExit()
	{
		return "3";
	}
	public static String UpdatePlayer(Boolean Remove,String Player)
	{
		if(Remove)
			return "2-"+Player;
		else
			return "1-"+Player;
	}
	public static String getPlayer(List<ItemClient> listClient)
	{
		if(listClient.isEmpty())
			return "0-0";
		String val ="0-"+ listClient.get(0).getName();
		for(int i=1;i<listClient.size();i++)
			val+=":"+listClient.get(i).getName();
		return val;
	}
}
