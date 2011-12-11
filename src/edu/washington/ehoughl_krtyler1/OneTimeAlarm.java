package edu.washington.ehoughl_krtyler1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class OneTimeAlarm extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent){
		Bundle bundle = intent.getExtras();
		String soundFile = bundle.getString("soundFile");
		Toast.makeText(context, "Alarm worked and soundFile is " + soundFile, Toast.LENGTH_LONG).show();
		Intent i = new Intent(context, PlaySound.class);
		i.putExtra("soundFile2", soundFile);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
}
