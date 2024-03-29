package edu.washington.ehoughl_krtyler1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
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
		int alarmMinute = (Integer) m.get((Object)"alarmMinute");
		@SuppressWarnings("unchecked")
		Set<String> days = (HashSet<String>) m.get((Object)"days");
		int soundFile = (Integer) m.get((Object)"soundFile");
		Integer alarmVolume = (Integer) m.get((Object)"alarmVolume");
		boolean alarmActive = (Boolean) m.get((Object)"alarmActive");
		
		if(alarmActive)
		{
			Button buttonON = (Button)findViewById(R.id.buttonON);
	    	Button buttonOFF = (Button)findViewById(R.id.buttonOFF);
	    	
			buttonON.setTextColor(Color.parseColor("#cccccc"));
			buttonON.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
			buttonOFF.setTextColor(Color.parseColor("black"));
			buttonOFF.getBackground().clearColorFilter();
		}
		else
		{
			Button buttonON = (Button)findViewById(R.id.buttonON);
	    	Button buttonOFF = (Button)findViewById(R.id.buttonOFF);
	    	
			buttonOFF.setTextColor(Color.parseColor("#cccccc"));
			buttonOFF.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
			buttonON.setTextColor(Color.parseColor("black"));
			buttonON.getBackground().clearColorFilter();
		}
		
		//make UI reflect current alarm settings
		TimePicker tp = (TimePicker)findViewById(R.id.timePickerAlarm);
		tp.setCurrentHour(alarmHour);
		tp.setCurrentMinute(alarmMinute);
		
		Spinner soundFileSpinner = (Spinner)findViewById(R.id.spinnerSoundFile);
		soundFileSpinner.setSelection(soundFile);
		
		SeekBar seekBarVolume = (SeekBar)findViewById(R.id.seekBarVolume);
		seekBarVolume.setProgress(alarmVolume);
		
		if(days.size() > 0)
		{
			SetSelectedDays(days);
		}
		
		UpdateSuggestedSleepTimes(alarmHour, alarmMinute);
    }
    
    private void UpdateSuggestedSleepTimes(int alarmHour, int alarmMinute)
    {
        Calendar c = Calendar.getInstance();
    
        c.set(Calendar.HOUR_OF_DAY, alarmHour);
        c.set(Calendar.MINUTE, alarmMinute);
        c.set(Calendar.SECOND, 0);
        
        c.add(Calendar.HOUR_OF_DAY, -9);
        Date date = c.getTime();
        String bedTime1 = (new SimpleDateFormat("hh:mm aa")).format(date);
        c.add(Calendar.HOUR_OF_DAY, 9);
        
        c.add(Calendar.HOUR_OF_DAY, -7);
        c.add(Calendar.MINUTE, -30);
        date = c.getTime();
        String bedTime2 = (new SimpleDateFormat("hh:mm aa")).format(date);
        c.add(Calendar.HOUR_OF_DAY, 7);
        c.add(Calendar.MINUTE, 30);
        
        c.add(Calendar.HOUR_OF_DAY, -6);
        date = c.getTime();
        String bedTime3 = (new SimpleDateFormat("hh:mm aa")).format(date);
        
    	TextView tvBedTime1 = (TextView)findViewById(R.id.textViewBedTime1);
    	TextView tvBedTime1AMPM = (TextView)findViewById(R.id.textViewBedTime1AMPM);
    	TextView tvBedTime2 = (TextView)findViewById(R.id.textViewBedTime2);
    	TextView tvBedTime2AMPM = (TextView)findViewById(R.id.textViewBedTime2AMPM);
    	TextView tvBedTime3 = (TextView)findViewById(R.id.textViewBedTime3);
    	TextView tvBedTime3AMPM = (TextView)findViewById(R.id.textViewBedTime3AMPM);
    	
    	tvBedTime1.setText(bedTime1.substring(0, 5));
    	tvBedTime1AMPM.setText(bedTime1.substring(6, 8));
    	tvBedTime2.setText(bedTime2.substring(0, 5));
    	tvBedTime2AMPM.setText(bedTime2.substring(6, 8));
    	tvBedTime3.setText(bedTime3.substring(0, 5)); 
    	tvBedTime3AMPM.setText(bedTime3.substring(6, 8));
    }
    
    private void ResetDayButtons()
    {
    	//set all to default
    	ArrayList<Button> buttonList = new ArrayList<Button>(); 
    	buttonList.add((Button)findViewById(R.id.buttonSunday));
    	buttonList.add((Button)findViewById(R.id.buttonMonday));
    	buttonList.add((Button)findViewById(R.id.buttonTuesday));
    	buttonList.add((Button)findViewById(R.id.buttonWednesday));
    	buttonList.add((Button)findViewById(R.id.buttonThursday));
    	buttonList.add((Button)findViewById(R.id.buttonFriday));
    	buttonList.add((Button)findViewById(R.id.buttonSaturday));
    	
    	for(Button b : buttonList)
    	{
    		b.setTextColor(Color.parseColor("black"));
    		b.getBackground().clearColorFilter();
    	}
    }
    
    private void MarkDayButtonsSelected(ArrayList<Button> selectedButtonList)
    {
    	for(Button b : selectedButtonList)
    	{
    		b.setTextColor(Color.parseColor("#cccccc"));
    		b.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
    	}
    }
    
    private void SetSelectedDays(Set<String> days)
    {
    	ResetDayButtons();
    	
    	ArrayList<Button> selectedButtonList = new ArrayList<Button>();
		
    	if(days.contains("Sunday"))
    	{
    		selectedButtonList.add((Button)findViewById(R.id.buttonSunday));
    	}
    	if(days.contains("Monday"))
    	{
    		selectedButtonList.add((Button)findViewById(R.id.buttonMonday));
    	}
    	if(days.contains("Tuesday"))
    	{
    		selectedButtonList.add((Button)findViewById(R.id.buttonTuesday));
    	}
    	if(days.contains("Wednesday"))
    	{
    		selectedButtonList.add((Button)findViewById(R.id.buttonWednesday));
    	}
    	if(days.contains("Thursday"))
    	{
    		selectedButtonList.add((Button)findViewById(R.id.buttonThursday));
    	}
    	if(days.contains("Friday"))
    	{
    		selectedButtonList.add((Button)findViewById(R.id.buttonFriday));
    	}
    	if(days.contains("Saturday"))
    	{
    		selectedButtonList.add((Button)findViewById(R.id.buttonSaturday));
    	}
    	
    	MarkDayButtonsSelected(selectedButtonList);
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
    
    private void SavePreferences(int alarmHour, int alarmMinute, int alarmVolume, int soundFile, Set<String> days, boolean active)
    {
    	//save selections to preferences
    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    	Editor e = prefs.edit();
		e.putBoolean("sleepwright", true);
		e.putInt("alarmHour", alarmHour);
		e.putInt("alarmMinute", alarmMinute);
		e.putStringSet("days", days);
		e.putInt("soundFile", soundFile);
		e.putInt("alarmVolume", alarmVolume);
		e.putBoolean("alarmActive", active);
		e.commit();
    }
    
    private int calculateNextAlarm(int alarmHour, int alarmMinute)
    {
    	int minutesToAlarm; // calculated by comparing user time to current time
    	
    	// get todays time and compare to what the alarm is set to
        Calendar today = Calendar.getInstance();
        Calendar alarmDay = Calendar.getInstance();
    
        alarmDay.set(Calendar.HOUR_OF_DAY, alarmHour);
        alarmDay.set(Calendar.MINUTE, alarmMinute);
        alarmDay.set(Calendar.SECOND, 0);
      
        // getting the current hour and minute in case its the next day
        int curHour = today.get(Calendar.HOUR_OF_DAY);
        int curMinute = today.get(Calendar.MINUTE);
      
        // add day if the time has already passed
        if(alarmHour<= curHour && alarmMinute <= curMinute)
        {
        	alarmDay.add(Calendar.DAY_OF_MONTH, 1);
        }
      
        // difference in times 
        int diff = (int) (alarmDay.getTimeInMillis() - today.getTimeInMillis());
        minutesToAlarm = diff/60000;
        
        if(minutesToAlarm < 0){
        	minutesToAlarm = 0;
        } else
        {
        	minutesToAlarm = minutesToAlarm + 1;
        }
        
        return minutesToAlarm;
    }
    
    private void SetAlarm(int alarmHour, int alarmMinute, int alarmVolume, String soundFileString, Set<String> days, boolean alarmActive)
    {
    	int[] intValDays = new int[days.size()];
    	int i = 0;
    	
    	for(String s : days)
    	{
    		if(s.equalsIgnoreCase("Sunday"))
    		{
    			intValDays[i] = 6;
    		}
    		else if(s.equalsIgnoreCase("Monday"))
    		{
    			intValDays[i] = 7;
    		}
    		else if(s.equalsIgnoreCase("Tuesday"))
    		{
    			intValDays[i] = 1;
    		}
    		else if(s.equalsIgnoreCase("Wednesday"))
    		{
    			intValDays[i] = 2;
    		}
    		else if(s.equalsIgnoreCase("Thursday"))
    		{
    			intValDays[i] = 3;
    		}
    		else if(s.equalsIgnoreCase("Friday"))
    		{
    			intValDays[i] = 4;
    		}
    		else if(s.equalsIgnoreCase("Saturday"))
    		{
    			intValDays[i] = 5;
    		}
    		
    		i++;
    	}
    	scheduleIntent(alarmHour, alarmMinute, alarmVolume, soundFileString, intValDays);
    }
    
    public void scheduleIntent(int alarmHour, int alarmMinute, int alarmVolume, String soundFileString, int[] intValDays){
    	
    	Calendar today = Calendar.getInstance();
    	String alarmSet; // for toast output to tell user when alarm will go off
    	//add selected values to bundle
      	Intent intent = new Intent(getBaseContext(), OneTimeAlarm.class);
      	intent.putExtra("hour", alarmHour);
      	intent.putExtra("minute", alarmMinute);
      	intent.putExtra("volume", alarmVolume);
        intent.putExtra("soundFile", soundFileString);
        intent.putExtra("days", intValDays);
        PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(),33, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        int minutesToAlarm = calculateNextAlarm(alarmHour, alarmMinute);
        int hoursToAlarm = minutesToAlarm / 60;
        
        if(hoursToAlarm == 0)
        {
        	alarmSet = "Alarm set for " + minutesToAlarm + " minutes(s) from now";
        } 
        else 
        {  
        	alarmSet = "Alarm set for " + hoursToAlarm + " hour(s) and " + minutesToAlarm % 60 + " minutes(s) from now";
        }
        
        today.setTimeInMillis(System.currentTimeMillis());
        today.add(Calendar.MINUTE, minutesToAlarm);
        // Schedule the alarm!
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, today.getTimeInMillis(), sender);
        Toast.makeText(this, alarmSet, Toast.LENGTH_LONG).show();
    }
    
    public void cancelIntent(int alarmHour, int alarmMinute, int alarmVolume, String soundFileString, int[] intValDays){
    	Intent intent = new Intent(getBaseContext(), OneTimeAlarm.class);
      	intent.putExtra("hour", alarmHour);
      	intent.putExtra("minute", alarmMinute);
      	intent.putExtra("volume", alarmVolume);
        intent.putExtra("soundFile", soundFileString);
        intent.putExtra("days", intValDays);
        PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(),33, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.cancel(sender); // cancel
    }
    
    public void onClickOFF(View view)
    {
    	Button b = (Button)view;
    	Button buttonON = (Button)findViewById(R.id.buttonON);
    	
		b.setTextColor(Color.parseColor("#cccccc"));
		b.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
		buttonON.setTextColor(Color.parseColor("black"));
		buttonON.getBackground().clearColorFilter();
		
		Toast.makeText(this, "Alarm disabled.", Toast.LENGTH_LONG).show();
		
		cancelAlarm();
    }
    
    public void cancelAlarm()
    {
    	Intent intent = new Intent(getBaseContext(), OneTimeAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(),33, intent, 
    	PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.cancel(sender);
        
        //save selections to preferences
    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    	Editor e = prefs.edit();
		e.putBoolean("alarmActive", false);
		e.commit();
    }
    
    public void onClickON(View view)
    {
    	Button b = (Button)view; 
    	Button buttonOFF = (Button)findViewById(R.id.buttonOFF);
    	
		b.setTextColor(Color.parseColor("#cccccc"));
		b.getBackground().setColorFilter(Color.parseColor("#666666"), android.graphics.PorterDuff.Mode.MULTIPLY);
		buttonOFF.setTextColor(Color.parseColor("black"));
		buttonOFF.getBackground().clearColorFilter();
		saveAlarm();
    }
    
    private void saveAlarm()
    {
    	//get selections from UI
    	TimePicker tp = (TimePicker)findViewById(R.id.timePickerAlarm);	
    	SeekBar sb = (SeekBar) findViewById(R.id.seekBarVolume);
    	int alarmHour = tp.getCurrentHour();
    	int alarmMinute = tp.getCurrentMinute();
    	int alarmVolume = sb.getProgress();
    	Set<String> days = GetSelectedDays();
    	Spinner s = (Spinner)findViewById(R.id.spinnerSoundFile);
    	int soundFile = s.getSelectedItemPosition();
    	String soundFileString = s.getSelectedItem().toString();
    	boolean alarmActive = true;
    	
    	UpdateSuggestedSleepTimes(alarmHour, alarmMinute);
    	SavePreferences(alarmHour, alarmMinute, alarmVolume, soundFile, days, alarmActive);
    	SetAlarm(alarmHour, alarmMinute, alarmVolume, soundFileString, days, alarmActive);
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