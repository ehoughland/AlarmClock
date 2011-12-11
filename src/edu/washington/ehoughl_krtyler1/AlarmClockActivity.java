package edu.washington.ehoughl_krtyler1;

import java.util.Map;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmClockActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        if(PreferencesExist())
        {
        	LoadAlarmFromPreferences(); 
        }
    }
    
    private void LoadAlarmFromPreferences()
    {
    	//get parameters from preferences
    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		Map<String, ?> m = prefs.getAll();
    	
		int alarmHour = (Integer) m.get((Object)"alarmHour");
		int alarmMinute = (Integer) m.get((Object)"alarmHour");
		String[] days = (String[]) m.get((Object)"days");
		String soundFile = (String) m.get((Object)"soundFile");
		Integer alarmVolume = (Integer) m.get((Object)"alarmVolume");
		
		//make UI reflect current alarm settings
		TimePicker tp = (TimePicker)findViewById(R.id.timePickerAlarm);
		tp.setCurrentHour(alarmHour);
		tp.setCurrentMinute(alarmMinute);
			
    }
    
    public void onClickSunday(View view)
	{
    	Button b = (Button)view;
    	b.getBackground().setColorFilter(0x66666600, android.graphics.PorterDuff.Mode.MULTIPLY);
	}
    
    public void onClickSave(View view)
	{
    	SaveAlarmToPreferences();
	} 
    
    private void SaveAlarmToPreferences()
    {
    	TimePicker tp = (TimePicker)findViewById(R.id.timePickerAlarm);	
    	int alarmHour = tp.getCurrentHour();
    	int alarmMinute = tp.getCurrentMinute();
    	
    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    	Editor e = prefs.edit();
		e.putBoolean("sleepwright", true);
		e.putInt("alarmHour", alarmHour);
		e.putInt("alarmMinute", alarmMinute);
		e.commit();
		
		Toast toast = Toast.makeText(this, "Alarm Added!", Toast.LENGTH_SHORT);
		toast.show();
    }
    
    private boolean PreferencesExist()
    {
    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		
    	if (prefs.contains("sleepwright"))
    	{
    		return true;
    	}
    	
    	return false;
    }
}