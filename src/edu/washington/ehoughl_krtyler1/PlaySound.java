package edu.washington.ehoughl_krtyler1;

import java.util.Arrays;

import edu.washington.ehoughl_krtyler1.R;
import android.app.Activity;
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		
		Bundle bundle = getIntent().getExtras();
		String soundFile = bundle.getString("soundFile");
		int alarmHour = bundle.getInt("hour");
		int alarmMinute = bundle.getInt("minute");
		String[] days = bundle.getStringArray("days");
		//Toast.makeText(getBaseContext(), "hour = " + alarmHour + " minute = " + alarmMinute + " volume = " + alarmVolume + " days = " + Arrays.asList(days), Toast.LENGTH_LONG).show();
		
		// Set the hardware buttons to control the music
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// Load the sound
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
		
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
				loaded = true;
				Bundle b = getIntent().getExtras();
				int alarmVolume = b.getInt("volume");
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
		//float volume2 = maxVolume/maxVolume;
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
		PlaySound.this.finish();
	}
}
