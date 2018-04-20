/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocaro;
import java.io.PrintWriter;
/**
 *
 * @author Nguyen Thanh Xuan
 */
public class ItemClient {
    private String name;
	private PrintWriter pwriter;
	public ItemClient()
	{}
	public ItemClient(String name,PrintWriter pw)
	{
		this.name = name;
		this.pwriter = pw;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setPrintWriter(PrintWriter pw)
	{
		this.pwriter = pw;
	}
	public String getName()
	{
		return this.name;
	}
	public PrintWriter getPrintWriter()
	{
		return this.pwriter;
	}
}
