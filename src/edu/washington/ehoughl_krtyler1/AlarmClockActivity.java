package edu.washington.ehoughl_krtyler1;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import edu.washington.ehoughl_krtyler1.AlarmClockActivity;
import edu.washington.ehoughl_krtyler1.OneTimeAlarm;
import edu.washington.ehoughl_krtyler1.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View; 
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmClockActivity extends Activity {
	private int hoursToAlarm; // calculated by comparing user time to current time
	private int minutesToAlarm; // calculated by comparing user time to current time
	private String alarmSet; // for toast output to tell user when alarm will go off
	//private String soundFile = "angry_cat"; // coming from user -- hook into sound selection

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
		@SuppressWarnings("unchecked")
		Set<String> days = (HashSet<String>) m.get((Object)"days");
		String soundFile = (String) m.get((Object)"soundFile");
		Integer alarmVolume = (Integer) m.get((Object)"alarmVolume");  
		
		//make UI reflect current alarm settings
		TimePicker tp = (TimePicker)findViewById(R.id.timePickerAlarm);
		tp.setCurrentHour(alarmHour);
		tp.setCurrentMinute(alarmMinute);
		
		if(days.size() > 0)
		{
			SetSelectedDays(days);
		}
    }
    
    private void SetSelectedDays(Set<String> days)
    {
    	if(days.contains("Sunday"))
    	{
    		Button b = (Button)findViewById(R.id.buttonSunday);
    		b.setTextColor(Color.parseColor("#cccccc"));
    		b.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
    	}
    	if(days.contains("Monday"))
    	{
    		Button b = (Button)findViewById(R.id.buttonMonday);
    		b.setTextColor(Color.parseColor("#cccccc"));
    		b.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
    	}
    	if(days.contains("Tuesday"))
    	{
    		Button b = (Button)findViewById(R.id.buttonTuesday);
    		b.setTextColor(Color.parseColor("#cccccc"));
    		b.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
    	}
    	if(days.contains("Wednesday"))
    	{
    		Button b = (Button)findViewById(R.id.buttonWednesday);
    		b.setTextColor(Color.parseColor("#cccccc"));
    		b.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
    	}
    	if(days.contains("Thursday"))
    	{
    		Button b = (Button)findViewById(R.id.buttonThursday);
    		b.setTextColor(Color.parseColor("#cccccc"));
    		b.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
    	}
    	if(days.contains("Friday"))
    	{
    		Button b = (Button)findViewById(R.id.buttonFriday);
    		b.setTextColor(Color.parseColor("#cccccc"));
    		b.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
    	}
    	if(days.contains("Saturday"))
    	{
    		Button b = (Button)findViewById(R.id.buttonSaturday);
    		b.setTextColor(Color.parseColor("#cccccc"));
    		b.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
    	}
    }
    
    public void onClickDay(View view)
	{
    	Button b = (Button)view;
    	
    	if(b.getCurrentTextColor() != Color.parseColor("#cccccc"))
    	{
    		b.setTextColor(Color.parseColor("#cccccc"));
    		b.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
    	}
    	else
    	{
    		b.setTextColor(Color.parseColor("black"));
    		b.getBackground().clearColorFilter();
    	}
	}
    
    public void onClickSave(View view)
	{
    	SaveAlarmToPreferencesAndCreateAlarm();
	} 
    
    private void SaveAlarmToPreferencesAndCreateAlarm()
    {
    	//get selections from UI
    	TimePicker tp = (TimePicker)findViewById(R.id.timePickerAlarm);	
    	SeekBar sb = (SeekBar) findViewById(R.id.seekBarVolume);
    	int alarmHour = tp.getCurrentHour();
    	int alarmMinute = tp.getCurrentMinute();
    	int alarmVolume = sb.getProgress();
    	Set<String> days = GetSelectedDays();
    	Spinner s = (Spinner)findViewById(R.id.spinnerSoundFile);
    	String soundFile = s.getSelectedItem().toString();
    	
    	//save selections to preferences
    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    	Editor e = prefs.edit();
		e.putBoolean("sleepwright", true);
		e.putInt("alarmHour", alarmHour);
		e.putInt("alarmMinute", alarmMinute);
		e.putStringSet("days", days); 
		e.putString("soundFile", soundFile);
		e.putInt("alarmVolume", alarmVolume);
		e.commit();
		
		//let the user know it was saved.
		Toast toast = Toast.makeText(this, "Alarm Added!", Toast.LENGTH_SHORT);
		toast.show();
	
      	Intent intent = new Intent(AlarmClockActivity.this, OneTimeAlarm.class);
      	intent.putExtra("hour", alarmHour);
      	intent.putExtra("minute", alarmMinute);
      	intent.putExtra("volume", alarmVolume);
        intent.putExtra("soundFile", soundFile); // passing this to the intent
        PendingIntent sender = PendingIntent.getBroadcast(AlarmClockActivity.this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // get todays time and compare to what the alarm is set to
        Calendar today = Calendar.getInstance();
        Calendar alarmDay = Calendar.getInstance();
    
        alarmDay.set(Calendar.HOUR, alarmHour);
        alarmDay.set(Calendar.MINUTE, alarmMinute);
        alarmDay.set(Calendar.SECOND, 0);
      
        // getting the current hour and minute in case its the next day
        int curHour = today.get(Calendar.HOUR);
        int curMinute = today.get(Calendar.MINUTE);
      
        // add day if the time has already passed
        if(alarmHour<= curHour && alarmMinute <= curMinute){
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
    
     private Set<String> GetSelectedDays()
    {
    	int selectedTextColor = Color.parseColor("#cccccc");
    	Set<String> s = new HashSet<String>();
    	
    	Button b = (Button)findViewById(R.id.buttonSunday);
    	if(b.getCurrentTextColor() == selectedTextColor)
    	{
    		s.add("Sunday");
    	}
    	b = (Button)findViewById(R.id.buttonMonday);
    	if(b.getCurrentTextColor() == selectedTextColor)
    	{
    		s.add("Monday");
    	}
    	b = (Button)findViewById(R.id.buttonTuesday);
    	if(b.getCurrentTextColor() == selectedTextColor)
    	{
    		s.add("Tuesday");
    	}
    	b = (Button)findViewById(R.id.buttonWednesday);
    	if(b.getCurrentTextColor() == selectedTextColor)
    	{
    		s.add("Wednesday");
    	}
    	b = (Button)findViewById(R.id.buttonThursday);
    	if(b.getCurrentTextColor() == selectedTextColor)
    	{
    		s.add("Thursday");
    	}
    	b = (Button)findViewById(R.id.buttonFriday);
    	if(b.getCurrentTextColor() == selectedTextColor)
    	{
    		s.add("Friday");
    	}
    	b = (Button)findViewById(R.id.buttonSaturday);
    	if(b.getCurrentTextColor() == selectedTextColor)
    	{
    		s.add("Saturday");
    	}
    	
    	return s;

    } 
    private boolean PreferencesExist()
    {
    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		
    	//Editor e = prefs.edit();
		//e.clear();
    	//e.commit();
    	
    	if (prefs.contains("sleepwright"))
    	{
    		return true;
    	}
    	
    	return false;
    } 
}