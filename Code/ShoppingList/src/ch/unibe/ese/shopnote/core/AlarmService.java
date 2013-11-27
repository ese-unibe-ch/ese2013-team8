package ch.unibe.ese.shopnote.core;

import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.activities.ViewListActivity;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


                            
 
@SuppressLint("NewApi")
public class AlarmService extends Service
{
      
 
    @Override
    public IBinder onBind(Intent arg0)
    {
       // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public void onCreate()
    {
       // TODO Auto-generated method stub 
       super.onCreate();
    }
 
 
   @Override
   public int onStartCommand(Intent intent, int flags, int startId)
   {
	   
	   handleStart(intent, startId);

		return START_NOT_STICKY;
		
   }
   
   
   private void handleStart(Intent intent, int startId) {
	   

	 		Intent resultIntent = new Intent(this, ViewListActivity.class);

	 		
	 		 NotificationCompat.Builder mBuilder =
	 		        new NotificationCompat.Builder(this)
	 		        .setSmallIcon(R.drawable.ic_notification)
	 		        .setContentTitle("Shopnote")
	 		        .setContentText("Shopping Time!")
	 		        // funktioniert nicht, ich weiss nicht wieso..
	 		        .setAutoCancel(true);

	 		// The stack builder object will contain an artificial back stack for the
	 		// started Activity.
	 		// This ensures that navigating backward from the Activity leads out of
	 		// your application to the Home screen.
	 		 TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
	 		// Adds the back stack for the Intent (but not the Intent itself)
	 		stackBuilder.addParentStack(ViewListActivity.class);
	 		// Adds the Intent that starts the Activity to the top of the stack
	 		stackBuilder.addNextIntent(resultIntent);
	 		
	 		PendingIntent resultPendingIntent =
	 		        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
	 		
	 		mBuilder.setContentIntent(resultPendingIntent);
	 		
	 		
	 		NotificationManager mNotificationManager =
	 		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	 	
	 		mNotificationManager.notify(0, mBuilder.build());
	   
	  
		
   }
 
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
 
}