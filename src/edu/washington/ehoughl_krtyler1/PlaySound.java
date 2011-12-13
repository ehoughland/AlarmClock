package edu.washington.ehoughl_krtyler1;

import java.util.Arrays;
import java.util.Calendar;

import edu.washington.ehoughl_krtyler1.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class PlaySound extends Activity{
	private SoundPool soundPool;
	private int soundID; // the sound file
	private boolean loaded = false; // tells us if the sound file is loaded or not
	private int currentVolume;
	private boolean play = false;
	private int alarmVolume;
	private int[] intValDays;
	private int alarmHour;
	private int alarmMinute;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		
		Bundle bundle = getIntent().getExtras();
		String soundFile = bundle.getString("soundFile");
		alarmHour = bundle.getInt("hour");
		alarmMinute = bundle.getInt("minute");
		alarmVolume = bundle.getInt("volume");
		intValDays = bundle.getIntArray("days");
		//Toast.makeText(getBaseContext(), " days = " + Arrays.asList(intValDays.toString()), Toast.LENGTH_LONG).show();
		
		if (intValDays.length == 0){
			getSound(soundFile); // run the sound one time
		} else {
	        // see if the alarm should go off today
			// set a new intent for the next day
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, 1440); // 1440 that's 24 hours away
			Intent newIntent = new Intent(getBaseContext(), OneTimeAlarm.class);
	      	newIntent.putExtra("hour", alarmHour);
	      	newIntent.putExtra("minute", alarmMinute);
	      	newIntent.putExtra("volume", alarmVolume);
	        newIntent.putExtra("soundFile", soundFile);
	        newIntent.putExtra("days", intValDays);
	        PendingIntent newSender = PendingIntent.getBroadcast(getBaseContext(),33, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
	        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), newSender);
			
			Calendar c = Calendar.getInstance();
			int today = c.DAY_OF_WEEK;
			for (int i = 0; i < intValDays.length; i++){
				if(today == intValDays[i]){
					play = true;
				}
			}
			if (play == true){
				getSound(soundFile);
			} else {
				finished();
			}
		}
				
			}
	
	public void getSound(String soundFile){
		// Set the hardware buttons to control the music
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// Load the sound
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
		
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
				loaded = true;
				playSound(alarmVolume);
			}
		});
		soundID = soundPool.load(this, getID(soundFile), 1);

	}
	
	public void playSound(int volume){
		// Getting the user sound settings
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		// Is the sound loaded already?
		if (loaded) {
			soundPool.play(soundID, 1.0f, 1.0f, 1, 0, 1f);
			//Log.e("Test", "Actual volume = " + actualVolume + " max volume = " + maxVolume + " volume = " + volume2);
			while (audioManager.isMusicActive()== true){				
			}	
		}
	}
	
	private int getID(String soundFile){
		int id = 0 ;
		if( soundFile.equals("angry_cat"))
		{
			id = R.raw.angry_cat ;
		}
		if( soundFile.equals("siren"))
		{
			id = R.raw.siren;
		}
		return id;
	}
	
	public void onClickDismiss(View view){
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		finished();
	}
	
	public void finished(){
		PlaySound.this.finish();
	}
}
