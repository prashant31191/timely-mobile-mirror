package dartmouth.timely;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.net.ParseException;
import android.os.AsyncTask;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar.Events.Insert;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

/**
 * Asynchronously inserts event into Google Calendar 
 * after AutoScheduler
 * 
 * @author Delos Chang
 */



		
class AsyncEventsInsert extends AsyncTask<MainActivity, String, Void>{

	final String CLASS_CALENDAR_FROM_API = "primary"; // Use the user's primary calendar
	
	private final MainActivity activity;
	private final String startDate;
	private final String startName;
	Context context;
	
	AsyncEventsInsert(MainActivity activity, String startDate, String startName){
		this.activity = activity;
		this.context = activity.getApplicationContext();
		this.startDate = startDate;
		this.startName = startName;
	}
	

	@Override
	protected Void doInBackground(MainActivity... params){
		try {
//			System.out.println("Grabbing estimate listing");
			
			// add class as a marker
			Event eventBody = new Event();
			
			eventBody.setSummary(startName);
			
			// startdate
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");  
			try {  
			    Date date = format.parse(startDate);  
				DateTime start = new DateTime(date, TimeZone.getTimeZone("UTC"));
				
				// offset for time
				int timeEst = 2400000 + (int)(Math.random());
				Date endDate = new Date(date.getTime() + timeEst);
				DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
				
				eventBody.setStart(new EventDateTime().setDateTime(start));
				eventBody.setEnd(new EventDateTime().setDateTime(end));
				
			} catch (ParseException e) {  
			    // TODO Auto-generated catch block  
			    e.printStackTrace();  
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			eventBody.setDescription("scheduled from Timely");
			Event events = activity.client.events()
				.insert(CLASS_CALENDAR_FROM_API, eventBody)
				.execute();
			
			System.out.println("Event Checkpoint: " + events);
			
		} catch (UserRecoverableAuthIOException e) {
	          activity.startActivityForResult(e.getIntent(), activity.REQUEST_AUTHORIZATION);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			return null;
		}
		return null;
	}
	
	protected final void onPostExecute() {
//		MainActivity.noteLatLong("Scheduled ", startName, context, "");
//		MainActivity.updateBar(Globals.LOAD_ESTIMATE, activity, wrapper.assignment_name + " ("+wrapper.subtext+")");
	}
}
