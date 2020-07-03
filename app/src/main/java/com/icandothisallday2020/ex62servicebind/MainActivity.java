package com.icandothisallday2020.ex62servicebind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MusicService musicService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override//액티비티가 화면에 보일때
    protected void onResume() {
        super.onResume();
        //서비스객체 실행 및 연결(bind)
        if(musicService==null){//연결된 뮤직서비스가 없을 때
            Intent intent=new Intent(this,MusicService.class);
            startService(intent);//일단 서비스 객체 생성
            //[서비스 객체가 없으면 생성-onStartCommand()발동,있으면 생성X-onStartCommand()발동]
            //만들어진 Service 객체와 연결
            //bindService()호출시 Service class 안, onBind() method 발동
            //onBind()가 Service 객체 참조값을 가진 객체를 리턴
            bindService(intent,connection,0);//flags:0 == AutoCreate-X
            //bindService(intent,connection,BIND_AUTO_CREATE);
            //└객체가없다면 자동 으로 startService()
            //└AUTO:나랑 연결된 녀석이(MainActivity) kill 되면 같이 죽음
        }
    }
    //bindService() parameter:Service 객체와 연결된 통로 객체
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //IBinder service: 서비스객체의 참조주소를 주는 메소드를 가진 객체
            MusicService.MyBinder binder=(MusicService.MyBinder) service;
            musicService=binder.getService(); //리턴된 서비스객체 주소 참조
            Toast.makeText(musicService, "Connected Music Service", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) { }
    };

    public void play(View view) {
        if(musicService!=null)    musicService.playMusic();
    }
    public void pause(View view) {if(musicService!=null)musicService.pauseMusic();}
    public void stop(View view) {
        if(musicService!=null) {
            musicService.stopMusic();
            unbindService(connection);
            musicService=null;
            //서비스 객체 종료
            Intent intent=new Intent(this,MusicService.class);
            stopService(intent);
            finish();//액티비티 종료
        }
    }
}
