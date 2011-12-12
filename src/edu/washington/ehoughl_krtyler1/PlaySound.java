package edu.washington.ehoughl_krtyler1;

import edu.washington.ehoughl_krtyler1.R;
import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.Log;

public class PlaySound extends Activity{
	private SoundPool soundPool;
	private int soundID; // the sound file
	private boolean loaded = false; // tells us if the sound file is loaded or not
	private int counter = 15; // used to loop through sound until it reaches maximum volume
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		        
		Bundle bundle = getIntent().getExtras();
		String soundFile = bundle.getString("soundFile");
		
		//Toast.makeText(getBaseContext(), "Sound worked and soundFile is " + soundFile2, Toast.LENGTH_SHORT).show();
		
		// Set the hardware buttons to control the music
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// Load the sound
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
		
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
				loaded = true;
				playSound();
			}
		});
		soundID = soundPool.load(this, getID(soundFile), 1);
	}
	
	public void playSound(){
		// Getting the user sound settings
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume;
		// Is the sound loaded already?
		if (loaded) {
				while (counter <= maxVolume)
				{
					volume = counter/maxVolume;
					soundPool.play(soundID, volume, volume, 1, 0, 1f);
					Log.e("Test", "Played sound at " + volume + " volume");
					while (audioManager.isMusicActive()== true){
						
					}
					counter++;
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

}
