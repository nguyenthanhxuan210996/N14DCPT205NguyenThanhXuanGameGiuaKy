/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocaro;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 *
 * @author Nguyen Thanh Xuan
 */
public class CoCaro {
    private static int intPort =8888;
    private List<ItemClient> listClient;
    private List<ItemPlaying> listPlaying;
    
    public static void main(String[] args) {
        new CoCaro().start();
    }
    public CoCaro()
	{
		this.listClient = new LinkedList<ItemClient>();
		this.listPlaying = new LinkedList<ItemPlaying>();
	}
	public void start()
	{
		System.out.println("Server đã mở Port: "+intPort);
		try
		{
			ServerSocket socServer = new ServerSocket(intPort);
			while(true)
			{
				Socket socComing = socServer.accept();
				new ClientThread(socComing).start();
			}
		}
		catch (Exception ex)
		{}
	}
	private void RemoveClient(String client)
	{
		for(int i=0;i<listClient.size();i++)
		{
			if(listClient.get(i).getName().equals(client))
			{
				System.out.println(client+": Đã thoát");
				listClient.remove(i);
				return;
			}
		}
	}
	private Boolean CheckBusy(String toPlayer)
	{
		for(ItemPlaying item: listPlaying)
		{
			if(item.getPlayer1().equals(toPlayer))
				return true;
			if(item.getPlayer2().equals(toPlayer))
				return true;
		}
		return false;
	}
	private Boolean ExitName(String player)
	{
		for(int i=0;i<listClient.size();i++)
		{
			if(listClient.get(i).getName().equals(player))
			{
				return true;
			}
		}
		return false;
	}
	private void Connect(String player,PrintWriter pw)
	{	
		ItemClient client = new ItemClient(player, pw);
		listClient.add(client);
	}
	private void ActionNewGame(String data)
	{
		replayClient(data,ParaData.NewGame());
	}
	private void UpdatePlaying(Boolean isAdd,String player1,String player2)
	{
		if(isAdd)
			listPlaying.add(new ItemPlaying(player1, player1));
		else
			for(int i=0;i<listPlaying.size();i++)
			{
				ItemPlaying item = listPlaying.get(i);
				if(item.getPlayer1().equals(player2) || item.getPlayer2().equals(player2))
				{
					listPlaying.remove(i);
					return;
				}
			}
	}
	private void replayClient(String target,String strData)
	{
		for(ItemClient item: listClient)
		{
			if(item.getName().equals(target))
			{
				item.getPrintWriter().println(strData);
				return;
			}
		}
	}
	private void replayUpdateClient(Boolean isRemoveClient,String strPlayer)
	{
		String strData;
		if(isRemoveClient)
			strData =ParaData.UpdatePlayer(true, strPlayer);
		else
		    strData =ParaData.UpdatePlayer(false, strPlayer);
		for(ItemClient item: listClient)
			item.getPrintWriter().println(strData);
	}
	private void NoContinue(String data)
	{
		replayClient(data, ParaData.NoContinue());
	}
	private void ActionEndCaro(String data)
	{
		UpdatePlaying(false,"",data);
		replayClient(data, ParaData.EndGame());
	}
	private void ActionDisconnect(String data)
	{
		RemoveClient(data);
		replayUpdateClient(true,data); 
	}
	private void ActionTic(String data)
	{
		String[] arr = data.split(":"); 
		replayClient(arr[0],ParaData.Tic(arr[1],arr[2]));
	}
	private void ActionInvite(String data)
	{
		String[] arr = data.split(":"); // người thách đấu:người bị thách
		
		if(CheckBusy(arr[1]))
			replayClient(arr[0], ParaData.ResultInvite("0",""));
		else
			replayClient(arr[1],ParaData.Invite(arr[0]));
	}
	private void ActionNextCaro(String data)
	{
		replayClient(data,ParaData.NextCaro());
	}
	private void ActionWin(String data)
	{
		replayClient(data,ParaData.Win());
	}
	private void ActionResultInvite(String data)
	{
		String[] arr = data.split(":");  // kết quả:người thách đấu:người bị thách
		if(data.equals("1"))
		{
			replayClient(arr[1], ParaData.ResultInvite(arr[0],""));
		}
		else
		{	
			replayClient(arr[1], ParaData.ResultInvite(arr[0],arr[2]));
			UpdatePlaying(true,arr[1],arr[2]);
		}
	}
	private class ClientThread extends Thread
	{
		private Boolean isRun = true;
		private Socket socComing;
		public ClientThread(Socket soc)
		{
			this.socComing = soc;
		}
		public void run()
		{
			try
			{
				PrintWriter pwOut = new PrintWriter(socComing.getOutputStream(),true);
				InputStreamReader inStream = new InputStreamReader(socComing.getInputStream());
				BufferedReader buffReader = new BufferedReader(inStream);
				while(isRun)
				{
					String strData = buffReader.readLine();
					if(strData == null)
					{
						inStream.close();
						this.socComing.close();
						break;
					}
					String arrData[] = strData.split("-");
					switch (arrData[0])
					{
					case "0": // 0: hành động kết nối 	
						if(ExitName(arrData[1])==false)
						{
							System.out.println(arrData[1]+": Đã kết nối");
							pwOut.println(ParaData.getPlayer(listClient)); // trả về danh sách player
							replayUpdateClient(false,arrData[1]); // trả về tên người chơi mới cho tất cả client
							Connect(arrData[1],pwOut);
						}
						else
						{
							pwOut.println("3");
						}
						break;
					case "1":
						ActionDisconnect(arrData[1]);
						break;
					case "2": // gửi yêu cầu thách 
						ActionInvite(arrData[1]);
						break;
					case "3": // nhận kết quả yêu cầu thách đấu
						ActionResultInvite(arrData[1]);
						break;
					case "4":
						ActionTic(arrData[1]);
						break;
					case "5":
						ActionNewGame(arrData[1]);
						break;
					case "6":
						ActionEndCaro(arrData[1]);
						break;
					case "7":
						ActionWin(arrData[1]);
						break;
					case "8":
						ActionNextCaro(arrData[1]);
						break;
//					
					case "9":
						NoContinue(arrData[1]);
						break;
					}
				}
			}
			catch(Exception ex){}
		}
		
	}
    
}
