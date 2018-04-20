/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocaro;

/**
 *
 * @author Nguyen Thanh Xuan
 */
public class ItemPlaying {
    private String player1;
	private String player2;
	public ItemPlaying(String strPlayer1,String strPlayer2)
	{
		this.player1 = strPlayer1;
		this.player2 = strPlayer2;
	}
	public void setPlayer1(String strPlayer1)
	{
		this.player1 = strPlayer1;
	}
	public String getPlayer1()
	{
		return this.player1;
	}
	public void setPlayer2(String strPlayer2)
	{
		this.player2 = strPlayer2;
	}
	public String getPlayer2()
	{
		return this.player2;
	}
}
