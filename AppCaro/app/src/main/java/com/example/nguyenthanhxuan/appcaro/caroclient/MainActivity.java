package com.example.nguyenthanhxuan.appcaro.caroclient;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenthanhxuan.appcaro.Adapter.CaroAdapter;
import com.example.nguyenthanhxuan.appcaro.Adapter.PlayerAdapter;
import com.example.nguyenthanhxuan.appcaro.R;
import com.example.nguyenthanhxuan.appcaro.data.DataKetNoi;
import com.example.nguyenthanhxuan.appcaro.data.ItemCaro;
import com.example.nguyenthanhxuan.appcaro.data.ParseString;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Boolean YourTurn;//luot cua ban
    private Boolean Playing = false;//dang choi
    private Boolean Connect = false;//ket noi
    private Character Your;
    private Handler handler;
    private ThreadBackgound Thread;//nen chu de
    private ThreadReceive ThreadReceive;
    private List<String> ListPlayer;
    private List<ItemCaro> ListCaro;
    private PlayerAdapter Player;
    private CaroAdapter Caro;
    private Dialog xinDoi;
    private EditText txtName;
    private ListView ListViewPlayer;
    private GridView GridViewCaro;
    private LinearLayout layoutRePlayer;
    private LinearLayout layoutLBottom;
    private LinearLayout layoutLCaro;
    private ImageButton btnConnect;
    private ImageButton btnDisConnect;
    private ImageButton btnBack;
    private ImageButton btnNewGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HopThoaiCho();
        handler = new Handler();
        ListPlayer = new ArrayList<String>();
        txtName = (EditText) findViewById(R.id.txt_name);
        btnConnect = (ImageButton) findViewById(R.id.btn_connect);
        btnDisConnect = (ImageButton) findViewById(R.id.btn_cancel);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnNewGame = (ImageButton) findViewById(R.id.btn_newGame);
        layoutRePlayer = (LinearLayout) findViewById(R.id.layoutLePlayer);
        layoutLBottom = (LinearLayout) findViewById(R.id.layoutLBottom);
        layoutLCaro = (LinearLayout) findViewById(R.id.layoutLCaro);
        ListViewPlayer = (ListView) findViewById(R.id.listViewPlayer);
        GridViewCaro = (GridView) findViewById(R.id.gridViewCaro);
        GridViewCaro.setVerticalScrollBarEnabled(false);
        ListCaro = new ArrayList<ItemCaro>();
        Caro = new CaroAdapter(this, ListCaro);
        GridViewCaro.setAdapter(Caro);
        layoutRePlayer.setOnClickListener(ClickAction);
        btnConnect.setOnClickListener(ClickAction);
        btnDisConnect.setOnClickListener(ClickAction);
        btnBack.setOnClickListener(ClickAction);
        btnNewGame.setOnClickListener(ClickAction);
        GridViewCaro.setOnItemClickListener(ClickItemAction);
        ListViewPlayer.setOnItemClickListener(ClickItemAction2);
    }
    private AdapterView.OnItemClickListener ClickItemAction2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            Thread.setData(ParseString.Invite(DataKetNoi.strCurrPlayer, ListPlayer.get(arg2)));
            xinDoi.show();
        }
    };
    private AdapterView.OnItemClickListener ClickItemAction = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
            if (YourTurn && ListCaro.get(pos).getYou() == 'n') {
                ListCaro.get(pos).setYou(Your);
                YourTurn = false;
                CaroThongBao();
                Thread.setData(ParseString.Tic(DataKetNoi.strPlayer2, pos + "", Your));
                Win(pos, Your, ListCaro);
            }
        }
    };
    private View.OnClickListener ClickAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_connect:
                    Thread = new ThreadBackgound();
                    ThreadReceive = new ThreadReceive();
                    Thread.start();
                    String strPlayer = txtName.getText().toString();
                    if (strPlayer.equals(""))
                        Toast.makeText(getBaseContext(), "Hãy chọn một tên khác", Toast.LENGTH_LONG).show();
                    else {
                        DataKetNoi.strCurrPlayer = strPlayer;
                        Thread.setData(ParseString.Connect(strPlayer));
                    }
                    break;
                case R.id.btn_cancel:
                    Exit();
                    break;
                case R.id.layoutLePlayer:
                    NgatKetNoi();
                    break;
                case R.id.btn_back:
                    Back(Connect, Playing);
                    break;
                case R.id.btn_newGame:
                    if (Playing)
                        NewCaro();
                    break;
            }
        }
    };
    private class ThreadBackgound extends Thread {
        private Boolean StopAfterExe = false;
        private String strData = null;
        private Boolean RunBg = true;
        private Socket soc;

        public void setData(String str) {
            this.strData = str;
        }
        public void run() {
            try {
                soc = new Socket(DataKetNoi.strHost, DataKetNoi.intPort);
                PrintWriter pwOut = new PrintWriter(soc.getOutputStream(), true);
                InputStreamReader inStream = new InputStreamReader(soc.getInputStream());
                BufferedReader buff = new BufferedReader(inStream);
                ThreadReceive.create(buff);
                ThreadReceive.start();
                while (RunBg) {
                    sleep(200);
                    if (strData != null) {
                        pwOut.println(strData);
                        strData = null;
                        if (StopAfterExe)
                            this.RunBg = false;
                    }
                }
            } catch (Exception ex) {
                Log.d("Luồng bị lỗi", ex.toString());
            }
        }
    }
    private class ThreadReceive extends Thread {
        private BufferedReader buff;
        private Boolean RunBg = true;

        public void mStop() {
            this.RunBg = false;
        }
        public void create(BufferedReader buff) {
            this.buff = buff;
        }

        public void run() {
            try {
                String valReceive = null;
                while (RunBg) {
                    valReceive = buff.readLine();
                    if (valReceive != null) {
                        BaoCao(valReceive);
                    }
                }
            } catch (Exception ex) {
                Log.d("Luồng nhận bị lỗi", ex.toString());
            }
        }
    }
    public void Win(final int pos, final Character c,final List<ItemCaro> mList) {
        new Runnable() {
            @Override
            public void run() {
                if (CheckWin.Win(pos, c, mList)) {
                    ThongBaoWin();
                    Thread.setData(ParseString.Win(DataKetNoi.strPlayer2));
                }
            }
        }.run();
    }
    public void CaroThongBao() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                GridViewCaro.setAdapter(Caro);
            }
        });
    }
    public void BaoCao(String result) {
        final String[] arr = result.split("-");
        final int action = Integer.parseInt(arr[0]);
        handler.post(new Runnable() {
            @Override
            public void run() {
                switch (action) {
                    case 0:// set player
                        Connect = true;
                        SetListPlayer(arr[1]);
                        break;
                    case 1:// update player
                        UpdateListPlayer(false, arr[1]);
                        break;
                    case 2: // update player (remove)
                        UpdateListPlayer(true, arr[1]);
                        break;
                    case 3: // name exit
                        KetNoiLoi();
                        break;
                    case 4:
                        HienThiLoiMoi(arr[1]);
                        break;
                    case 5:
                        KQMoiBanChoi(arr[1]);
                        break;
                    case 6:
                        Tic(arr[1]);
                        break;
                    case 7:
                        ResetBanCaro(true);
                        break;
                    case 8:
                        ExitCaro(false,R.string.toastEndGame);
                        break;
                    case 9:
                        HienThiWin();
                        break;
                    case 10:
                        ResetBanCaro(false);
                        xinDoi.dismiss();
                        break;
                    case 11:
                        NoContinue(false);
                        break;
                }
            }
        });
    }
    private void DataCaro(Boolean Turn, String player2, Character Your) {
        YourTurn = Turn;
        DataKetNoi.strPlayer2 = player2;
        this.Your = Your;
    }
    private void StartCaro(Boolean Create) {
        Playing = true;
        ListCaro.clear();
        for (int i = 0; i < 64; i++)
            ListCaro.add(new ItemCaro('n'));
        CaroThongBao();
        if (Create) {
            CaroThongBao();
            ViewInput(false);
            HienThiDsPlayer(false);
            HienThiCaro(true);
        }
    }
    private void HopThoaiCho() {
        LayoutInflater inflate = LayoutInflater.from(MainActivity.this);
        View view = inflate.inflate(R.layout.layout_dialog_wait, null);
        xinDoi = new Dialog(MainActivity.this, R.style.AppTheme);
        xinDoi.setContentView(view);
    }
    private void ResetBanCaro(Boolean ShowMes) {
        if (ShowMes) {
            Toast.makeText(getBaseContext(), "Người chơi đã đầu hàng", Toast.LENGTH_SHORT).show();
        } else
        YourTurn = false;
        StartCaro(false);
    }
    private void NextCaro() {
        YourTurn = true;
        Thread.setData(ParseString.NextCaro(DataKetNoi.strPlayer2));
        StartCaro(false);
    }
    private void NewCaro() {
        YourTurn = true;
        Thread.setData(ParseString.NewCaro(DataKetNoi.strPlayer2));
        StartCaro(false);
    }
    private void NoContinue(Boolean Request) {
        Playing = false;
        HienThiCaro(false);
        HienThiDsPlayer(true);
        if (Request)
            Thread.setData(ParseString.NoContinue(DataKetNoi.strPlayer2));
        else
        {
            xinDoi.dismiss();
            Toast.makeText(getBaseContext(), "Người chơi không tiếp tục",Toast.LENGTH_SHORT).show();
        }
        DataKetNoi.strPlayer2 = null;
    }
    private void ExitCaro(Boolean Request,int Mess) {
        Playing = false;
        HienThiCaro(false);
        HienThiDsPlayer(true);
        if (Request)
            Thread.setData(ParseString.EndCaro(DataKetNoi.strPlayer2));
        else
            Toast.makeText(getBaseContext(), getText(Mess), Toast.LENGTH_SHORT).show();
        DataKetNoi.strPlayer2 = null;
    }
    private void Back(Boolean Connect, Boolean Playing) {
        if (Connect && Playing) {
            ExitCaro(true,R.string.toastEndGame);
            return;
        }
        if (Connect) {
            NgatKetNoi();
            return;
        }
    }
    private void Exit() {
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void HienThiWin() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.AppTheme);
        LayoutInflater inflate = LayoutInflater.from(MainActivity.this);
        View view = inflate.inflate(R.layout.layout_dialog_win, null);
        dialog.setContentView(view);
        ImageButton btnOK = (ImageButton) view.findViewById(R.id.ibtnOkDialog);
        ImageButton btnCannel = (ImageButton) view.findViewById(R.id.ibtnCannelDialog);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextCaro();
                dialog.dismiss();
            }
        });
        btnCannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                NoContinue(true);
            }
        });
        dialog.show();

    }
    private void HienThiLoiMoi(final String Invitefrom) {
        final Dialog dilog = new Dialog(MainActivity.this, R.style.AppTheme);
        LayoutInflater inflate = LayoutInflater.from(MainActivity.this);
        View view = inflate.inflate(R.layout.layout_dialog2, null);
        dilog.setContentView(view);
        TextView text = (TextView) view.findViewById(R.id.lablePlayerDialog);
        ImageButton btnOK = (ImageButton) view.findViewById(R.id.ibtnOkDialog);
        ImageButton btnCannel = (ImageButton) view.findViewById(R.id.ibtnCannelDialog);
        text.setText(Invitefrom);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread.setData(ParseString.ReplyInvite("2", Invitefrom, DataKetNoi.strCurrPlayer));
                dilog.dismiss();
                DataCaro(false, Invitefrom, 'o');
                StartCaro(true);
            }
        });
        btnCannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread.setData(ParseString.ReplyInvite("1", Invitefrom, " "));
                dilog.dismiss();
            }
        });
        dilog.show();
    }
    private void ThongBaoWin() {
        xinDoi = new Dialog(MainActivity.this, R.style.AppTheme);
        LayoutInflater inflate = LayoutInflater.from(MainActivity.this);
        View view = inflate.inflate(R.layout.layout_dialog_wait, null);
        xinDoi.setContentView(view);
        TextView lable = (TextView) view.findViewById(R.id.lableWaitDialog);
        lable.setText("Bạn đã chiến thắng");
        xinDoi.show();
    }
    private void KQMoiBanChoi(String result) {
        xinDoi.dismiss();
        String[] arr = result.split(":");
        if (arr[0].equals("0")) {
            Toast.makeText(getBaseContext(), "Người chơi đang bận", Toast.LENGTH_LONG).show();
            return;
        }
        if (arr[0].equals("1")) {
            Toast.makeText(getBaseContext(), "Người chơi từ chối", Toast.LENGTH_LONG).show();
            return;
        }
        DataCaro(true, arr[1], 'x');
        StartCaro(true);
    }
    private void Tic(String result) {
        YourTurn = true;
        String[] arr = result.split(":");
        int pos = Integer.parseInt(arr[0]);
        if (pos < 64) {
            ListCaro.get(pos).setYou(arr[1].charAt(0));
            CaroThongBao();
        }
    }
    private void NgatKetNoi() {
        Connect = false;
        Thread.setData(ParseString.DisConnect(DataKetNoi.strCurrPlayer));
        DataKetNoi.strCurrPlayer = null;
        ViewInput(true);
        HienThiDsPlayer(false);
        try {
            Thread.StopAfterExe = true;
            ThreadReceive.mStop();
        } catch (Exception e) {
        }
    }
    private void KetNoiLoi() {
        DataKetNoi.strCurrPlayer = null;
        Toast.makeText(getBaseContext(), "Hãy chọn một tên khác", Toast.LENGTH_LONG).show();
    }
    private void SetListPlayer(String result) {
        HienThiDsPlayer(true);
        ViewInput(false);
        ListPlayer = ParseString.getListPlayer(result);
        Player = new PlayerAdapter(MainActivity.this, ListPlayer);
        ListViewPlayer.setAdapter(Player);
    }
    private void UpdateListPlayer(Boolean isRemove, String result) {
        if (isRemove) ListPlayer.remove(result);
        else ListPlayer.add(result);
        Player.notifyDataSetChanged();
    }
    private void HienThiCaro(Boolean Show) {
        Animation anim;
        if (Show) {
            if (layoutLCaro.getVisibility() == View.GONE) {
                anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.game_in);
                layoutLCaro.startAnimation(anim);
                layoutLCaro.setVisibility(View.VISIBLE);
            }
        } else if (layoutLCaro.getVisibility() == View.VISIBLE) {
            anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.game_out);
            layoutLCaro.startAnimation(anim);
            layoutLCaro.setVisibility(View.GONE);
        }
    }
    private void HienThiDsPlayer(Boolean Show) {
        if (Show) {
            if (layoutRePlayer.getVisibility() == View.GONE) {
                layoutRePlayer.setVisibility(View.VISIBLE);
            }
        } else {
            if (layoutRePlayer.getVisibility() == View.VISIBLE) {
                layoutRePlayer.setVisibility(View.GONE);
            }
        }
    }
    private void ViewInput(Boolean Show) {
        Animation anim;
        if (Show) {
            if (layoutLBottom.getVisibility() == View.GONE) {
                anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_in);
                layoutLBottom.setAnimation(anim);
                layoutLBottom.setVisibility(View.VISIBLE);
            }
        } else {
            if (layoutLBottom.getVisibility() == View.VISIBLE) {
                anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_out);
                layoutLBottom.startAnimation(anim);
                layoutLBottom.setVisibility(View.GONE);
            }
        }
    }
    @Override
    protected void onDestroy() {
        NgatKetNoi();
        super.onDestroy();
    }
}
