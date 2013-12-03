package ch.unibe.ese.shopnote.core;





import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.activities.HomeActivity;
import ch.unibe.ese.shopnote.activities.ViewListActivity;
import android.annotation.SuppressLint;
import android.app.Notification;
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
	
	private NotificationManager mManager;
      
 
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
   public void onStart(Intent intent, int startId)
   {
	   super.onStart(intent, startId);
	     
       mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
       
       // ViewActivity geht nicht...
	   Intent intent1 = new Intent(this.getApplicationContext(),ViewListActivity.class);

	
	   Notification notification = new Notification(R.drawable.ic_launcher,"shopping time!", System.currentTimeMillis());
	
	   intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);

	   PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),startId, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
       notification.flags |= Notification.FLAG_AUTO_CANCEL;
       notification.setLatestEventInfo(this.getApplicationContext(), "Shopnote", "shopping time!", pendingNotificationIntent);

       mManager.notify(startId, notification);
	   
	   
	   
		
   }
 
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
 
}