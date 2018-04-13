package caroclient;

import com.sun.security.ntlm.Client;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import caroclient.CaRoClient;

public class CaRoServer {
	public static JFrame f;
	JButton[][] bt;
	static boolean flat = false;// Kiem tra button click
	boolean winner;
	JPanel p;
	int xx, yy, x, y;
	int[][] matran;// danh dau vi tri doi thu danh
	int[][] matrandanh;// danh dau vi tri minh danh
	// Server Socket
	ServerSocket serversocket;
	Socket socket;
	OutputStream os;
	InputStream is;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	// menu bar
	MenuBar menubar;
	// ///
	public CaRoServer() throws IOException {
		f = new JFrame();
		f.setTitle("Game Caro");
		f.setSize(700, 500);
		x = 8;
		y = 8;
		f.getContentPane().setLayout(null);
		f.getContentPane().setBackground(Color.BLACK);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setResizable(false);
		//
		matran = new int[x][y];// ma tran ban co
		matrandanh = new int[x][y];// ma tran danh dau vi tri danh
		menubar = new MenuBar();
		p = new JPanel();
		p.setBounds(10, 30, 300, 300);
		p.setLayout(new GridLayout(x, y));
		f.add(p);
		f.setMenuBar(menubar);// tao menubar cho frame
		Menu game = new Menu("Game");
		menubar.add(game);
		MenuItem newItem = new MenuItem("New Game");
		game.add(newItem);
		MenuItem exit = new MenuItem("Exit");
		game.add(exit);
		game.addSeparator();
		newItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newgame();
				try {
					oos.writeObject("newgame,123");
				} catch (IOException ie) {
					//
				}
			}

		});
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		// /Game Caro
		bt = new JButton[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				final int a = i, b = j;
				bt[a][b] = new JButton();// o ban co
				bt[a][b].setBackground(Color.RED);
				bt[a][b].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						flat = true;// server da click
						matrandanh[a][b] = 1;
						bt[a][b].setEnabled(false);
						if (CaRoClient.flat)// new ben client click
							setEnableButton(true);
						else
							setEnableButton(false);
						bt[a][b].setIcon(new ImageIcon(
								"D:\\Học Viện Công NGhệ Bưu Chính Viễn Thông TP.HCM\\NĂM 4 HỌC KÌ 2\\LẬP TRÌNH ỨNG DỤNG TRÊN ĐẦU CUỐI DI ĐỘNG\\cross.png"));
						try {
							oos.writeObject("caro," + a + "," + b);
						} catch (IOException ie) {
							ie.printStackTrace();
						}
					}

				});
				p.add(bt[a][b]);
			}
		}
		// /main
		initMatran();
		try {
			serversocket = new ServerSocket(1234);
			System.out.println("Dang doi client...");
			socket = serversocket.accept();
			System.out.println("Client da ket noi!");
			os = socket.getOutputStream();
			is = socket.getInputStream();
			oos = new ObjectOutputStream(os);
			ois = new ObjectInputStream(is);
			while (true) {
				String stream = ois.readObject().toString();
				String[] data = stream.split(",");
				if (data[0].equals("caro")) {
					caro(data[1], data[2]);
					if (winner == false)
						setEnableButton(true);
				} else if (data[0].equals("newgame")) {
					newgame();
				} else if (data[0].equals("checkwin")) {
				}
			}
		} catch (Exception ie) {
		} finally {
			socket.close();
			serversocket.close();
		}
	}

	public void caro(String x, String y) {
		xx = Integer.parseInt(x);
		yy = Integer.parseInt(y);
		// danh dau vi tri danh
		matran[xx][yy] = 1;
		matrandanh[xx][yy] = 1;
		bt[xx][yy].setEnabled(false);
		bt[xx][yy].setIcon(new ImageIcon(
				"D:\\Học Viện Công NGhệ Bưu Chính Viễn Thông TP.HCM\\NĂM 4 HỌC KÌ 2\\LẬP TRÌNH ỨNG DỤNG TRÊN ĐẦU CUỐI DI ĐỘNG\\tick.png"));
		// Kiem tra thang hay chua
		System.out.println("CheckH:" + checkHang());
		System.out.println("CheckC:" + checkCot());
		System.out.println("CheckCp:" + checkCheoPhai());
		System.out.println("CheckCt:" + checkCheoTrai());
		winner = (checkHang() == 1 || checkCot() == 1 || checkCheoPhai() == 1 || checkCheoTrai() == 1);
		if (checkHang() == 1 || checkCot() == 1 || checkCheoPhai() == 1
				|| checkCheoTrai() == 1) {
			setEnableButton(false);
			try {
				oos.writeObject("checkwin,123");
			} catch (IOException ex) {
			}
			Object[] options = { "Dong y", "Huy bo" };
			int m = JOptionPane.showConfirmDialog(f,"Ban da thua.Ban co muon choi lai khong?", "Thong bao",JOptionPane.YES_NO_OPTION);
			if (m == JOptionPane.YES_OPTION) {
				setVisiblePanel(p);
				newgame();
				try {
					oos.writeObject("newgame,123");
				} catch (IOException ie) {
					//
				}
			} else if (m == JOptionPane.NO_OPTION) {
			}
		}
	}
	public void newgame() {
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++) {
				bt[i][j].setIcon(null);
				matran[i][j] = 0;
				matrandanh[i][j] = 0;
			}
		setEnableButton(true);
	}

	public void setEnableButton(boolean b) {
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++) {
				if (matrandanh[i][j] == 0)
					bt[i][j].setEnabled(b);
			}
	}
	public void setVisiblePanel(JPanel pHienthi) {
		f.add(pHienthi);
		pHienthi.setVisible(true);
		pHienthi.updateUI();// ......
	}
	public void initMatran() {
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++) {
				matran[i][j] = 0;
			}
	}
	// /thuat toan tinh thang thua
	public int checkHang() {
		int win = 0, hang = 0, n = 0, k = 0;
		boolean check = false;
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				if (check) {
					if (matran[i][j] == 1) {
						hang++;
						if (hang > 4) {
							win = 1;
							break;
						}
						continue;
					} else {
						check = false;
						hang = 0;
					}
				}
				if (matran[i][j] == 1) {
					check = true;
					hang++;
				} else {
					check = false;
				}
			}
			hang = 0;
		}
		return win;
	}
	public int checkCot() {
		int win = 0, cot = 0;
		boolean check = false;
		for (int j = 0; j < y; j++) {
			for (int i = 0; i < x; i++) {
				if (check) {
					if (matran[i][j] == 1) {
						cot++;
						if (cot > 4) {
							win = 1;
							break;
						}
						continue;
					} else {
						check = false;
						cot = 0;
					}
				}
				if (matran[i][j] == 1) {
					check = true;
					cot++;
				} else {
					check = false;
				}
			}
			cot = 0;
		}
		return win;
	}

	public int checkCheoPhai() {
		int win = 0, cheop = 0, n = 0, k = 0;
		boolean check = false;
		for (int i = x - 1; i >= 0; i--) {
			for (int j = 0; j < y; j++) {
				if (check) {
					if (matran[n - j][j] == 1) {
						cheop++;
						if (cheop > 4) {
							win = 1;
							break;
						}
						continue;
					} else {
						check = false;
						cheop = 0;
					}
				}
				if (matran[i][j] == 1) {
					n = i + j;
					check = true;
					cheop++;
				} else {
					check = false;
				}
			}
			cheop = 0;
			check = false;
		}
		return win;
	}

	public int checkCheoTrai() {
		int win = 0, cheot = 0, n = 0;
		boolean check = false;
		for (int i = 0; i < x; i++) {
			for (int j = y - 1; j >= 0; j--) {
				if (check) {
					if (matran[n - j - 2 * cheot][j] == 1) {
						cheot++;
						System.out.print("+" + j);
						if (cheot > 4) {
							win = 1;
							break;
						}
						continue;
					} else {
						check = false;
						cheot = 0;
					}
				}
				if (matran[i][j] == 1) {
					n = i + j;
					check = true;
					cheot++;
				} else {
					check = false;
				}
			}
			n = 0;
			cheot = 0;
			check = false;
		}
		return win;
	}

	public static void main(String[] args) throws IOException {
		new CaRoServer();
	}

}
