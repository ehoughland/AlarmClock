package edu.washington.ehoughl_krtyler1;

import java.util.Calendar;

import edu.washington.ehoughl_krtyler1.AlarmClockActivity;
import edu.washington.ehoughl_krtyler1.OneTimeAlarm;
import edu.washington.ehoughl_krtyler1.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class AlarmClockActivity extends Activity {
	private int hour = 8; // coming from the user -- hook into clock
	private int minute = 35; // coming from the user -- hook into clock
	private int hoursToAlarm; // calculated by comparing user time to current time
	private int minutesToAlarm; // calculated by comparing user time to current time
	private String alarmSet; // for toast output to tell user when alarm will go off
	private String soundFile = "angry_cat"; // coming from user -- hook into sound selection
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
}