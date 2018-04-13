/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpchat;

public class GameBoard {
  private int[][] board;
  private int colQty;
   private int rowQty;
    public GameBoard(int rowQty, int colQty) {
        this.colQty=colQty;
        this.rowQty=rowQty;
        board=new int[colQty][colQty];
        for (int i=0; i<rowQty;i++){
            for (int j=0; j<colQty;j++)
            this.board[i][j] = -1;
        }
    }
   
    public String check(String string,int  player){
        int index=Integer.parseInt(string);
        int rowIndex=(int) index/10;
        int colIndex= (int) index%10;
       if (board[rowIndex][colIndex]!=-1) return "false";
        board[rowIndex][colIndex] = player;
        if(checkHang()==1||checkCot()==1||checkCheoPhai()==1||checkCheoTrai()==1){
             setText("người thắng là"+player);
        }
        return "true";
    }
    public int checkHang(){
		int win=0,hang=0,n=0,k=0;
		boolean check=false;
		for (int i=0;i<rowQty;i++){
	            for (int j=0;j<colQty;j++){
			if (check){
	                    if (board[i][j]==1){
				hang++;
				if (hang>4){
	                            win=1;
	                            break;
				}
				continue;
	                    }else {
				check=false;
				hang=0;
	                    }
			}
			if (board[i][j]==1){
	                    check=true;
	                    hang++;
			}else{
	                    check = false ;
	                }
	            }
	            hang=0; 
		}
		return win;
	    }
    public int checkCot(){
	        int win=0,cot=0;
		boolean check=false;
		for (int j=0;j<colQty;j++){
	            for (int i=0;i<rowQty;i++){
			if (check){
	                    if (board[i][j]==1){
				cot++;
				if (cot>4){
	                            win=1;
	                            break;
				}
				continue;
	                    }else {
	                    check=false;
	                    cot=0;
	                    }
			}
			if (board[i][j]==1){
	                    check=true;
	                    cot++;
			}else{
	                    check = false ;
	                }
	            }
	            cot=0;
		}
		return win;
	    }
    public int checkCheoPhai(){
		int win=0,cheop=0,n=0,k=0;
		boolean check=false;
		for (int i=rowQty-1;i>=0;i--){
	            for (int j=0;j<colQty;j++){                            
			if (check){
	                    if (board[n-j][j]==1){
	                        cheop++;
	                        if(cheop>4){
	                            win=1;
	                            break;
				}
				continue;
	                    }else {
	                    check=false;
	                    cheop=0;
	                    }
			}
			if (board[i][j]==1){
	                    n=i+j;
	                    check=true;
	                    cheop++;
			}else{
	                    check = false ;
	                }
	            }
	            cheop =0;
	            check = false ;
	        }
	        return win;
	    }
    public int checkCheoTrai(){
		int win=0,cheot=0,n=0;
		boolean check=false;
		for (int i=0;i<rowQty;i++){
	            for (int j=colQty-1;j>=0;j--){
			if (check){
	                    if (board[n-j-2*cheot][j]==1){
				cheot++;
	                        System.out.print("+"+j);
	                            if (cheot>4){
	                            win=1;
	                            break;
				}
				continue;
	                    }else {
				check=false;
				cheot=0;
	                    }
			}
			if (board[i][j]==1){
	                    n=i+j;
	                    check=true;
	                    cheot++;
			}else{
	                    check = false ;
	                }
	            }
	            n=0;
	            cheot = 0 ;
	            check = false ;
	        }
		return win;
	    }

    private void setText(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
