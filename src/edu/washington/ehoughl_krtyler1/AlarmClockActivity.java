package edu.washington.ehoughl_krtyler1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View; 
import android.widget.Button;
import android.widget.Spinner;
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
		Set<String> days = (HashSet<String>) m.get((Object)"days");
		String soundFile = (String) m.get((Object)"soundFile");
		//Integer alarmVolume = (Integer) m.get((Object)"alarmVolume");  
		
		//make UI reflect current alarm settings
		TimePicker tp = (TimePicker)findViewById(R.id.timePickerAlarm);
		tp.setCurrentHour(alarmHour);
		tp.setCurrentMinute(alarmMinute);
		
		if(days != null)
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
    	int alarmHour = tp.getCurrentHour();
    	int alarmMinute = tp.getCurrentMinute();
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
		e.commit();
		
		//let the user know it was saved.
		Toast toast = Toast.makeText(this, "Alarm Added!", Toast.LENGTH_SHORT);
		toast.show();
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
		
    	if (prefs.contains("sleepwright"))
    	{
    		return true;
    	}
    	
    	return false;
    }
}