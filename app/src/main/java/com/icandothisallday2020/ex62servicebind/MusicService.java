package com.icandothisallday2020.ex62servicebind;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    MediaPlayer mp;
    @Override//startService() 메소드로 실행했을때만 동작하는 콜백메소드
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {super.onDestroy();}
    @Nullable @Override//bindService() 로 실행했을때 자동호출되는 메소드
    public IBinder onBind(Intent intent) {
        MyBinder binder=new MyBinder();
        return binder;//MainActivity 로 파견갈 직원객체(Binder)를 리턴
    }
    //이 MusicService 객체의 메모리 주소(객체 참조값) 리턴 기능을 가진
    //Binder class 설계
    class MyBinder extends Binder{
        //이 서비스객체의 주소를 리턴해주는 메소드
        public MusicService getService(){
            return MusicService.this;
        }
    }//MyBinder
    void playMusic(){//음악 재생 기능
        if(mp==null){
            mp=MediaPlayer.create(this,R.raw.gang);
            mp.setLooping(true); mp.setVolume(1.0f,1.0f);
        }
        mp.start();//start & resume(이어하기)
    }
    //음악 일시정지 기능
    void pauseMusic() {if(mp!=null && mp.isPlaying()) mp .pause();    }
    //음악 정지 기능
    void stopMusic() {
        if(mp!=null){
            mp.stop(); mp.release(); mp=null;//가비지콜렉터에의해 삭제될 수 있도록
        }
    }
}
