package edu.washington.ehoughl_krtyler1;

import java.util.Calendar;
import java.util.Map;

import edu.washington.ehoughl_krtyler1.AlarmClockActivity;
import edu.washington.ehoughl_krtyler1.OneTimeAlarm;
import edu.washington.ehoughl_krtyler1.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toast;

public class AlarmClockActivity extends Activity {
	private Integer hour; // coming from the user -- hook into clock
	private Integer minute; // coming from the user -- hook into clock
	private int hoursToAlarm; // calculated by comparing user time to current time
	private int minutesToAlarm; // calculated by comparing user time to current time
	private String alarmSet; // for toast output to tell user when alarm will go off
	private String soundFile = "angry_cat"; // coming from user -- hook into sound selection
	private TimePicker timePicker1;
	private Button buttonSave;

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
                
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        Toast.makeText(getBaseContext(),"Time selected:" + timePicker1.getCurrentHour() + ":" + timePicker1.getCurrentMinute(),Toast.LENGTH_SHORT).show();
        
        minute = timePicker1.getCurrentMinute();
    	hour = timePicker1.getCurrentHour();
    	
    	Intent intent = new Intent(AlarmClockActivity.this, OneTimeAlarm.class); 
        intent.putExtra("soundFile", soundFile); // passing this to the intent
        PendingIntent sender = PendingIntent.getBroadcast(AlarmClockActivity.this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    	// get todays time and compare to what the alarm is set to
        Calendar today = Calendar.getInstance();
        Calendar alarmDay = Calendar.getInstance();
      
        alarmDay.set(Calendar.HOUR, hour);
        alarmDay.set(Calendar.MINUTE, minute);
        alarmDay.set(Calendar.SECOND, 0);
        
        // getting the current hour and minute in case its the next day
        int curHour = today.get(Calendar.HOUR);
        int curMinute = today.get(Calendar.MINUTE);
        
        // add day if the time has already passed
        if(hour<= curHour && minute <= curMinute){
        	alarmDay.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        // difference in times
        int diff = (int) (alarmDay.getTimeInMillis() - today.getTimeInMillis());
        
        today.setTimeInMillis(System.currentTimeMillis());
        today.add(Calendar.MILLISECOND, 5000); // add diff here instead of 5000 -- just running in 5 seconds for testing
        minutesToAlarm = diff/60000;
        if(minutesToAlarm < 0){
        	minutesToAlarm = 0;
        } else
        {
        	minutesToAlarm = minutesToAlarm + 1;
        }
        hoursToAlarm = minutesToAlarm / 60;
        
        // determining whether or not to include hours
        String hours = "hours"; // when 1 = minute else minutes
        String minutes = "minutes"; // when 1 = hour else hours
        if (hoursToAlarm == 1) {
        	hours = "hour";
        }
        if (minutesToAlarm == 1){
        	minutes = "minute";
        }
        if(hoursToAlarm == 0){
        	alarmSet = "Alarm set for " + minutesToAlarm + " " + minutes + " from now";
        } else {  
        	alarmSet = "Alarm set for " + hoursToAlarm + " " + hours + " and " + minutesToAlarm % 60 + " " + minutes + " from now";
        }
        
        // Schedule the alarm!
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, today.getTimeInMillis(), sender);
        Toast.makeText(this, alarmSet, Toast.LENGTH_LONG).show();
 
        
    } 
    
    public void onClickSave(View view){
    	
    	   }
}