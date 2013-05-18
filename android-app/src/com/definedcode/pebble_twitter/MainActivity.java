package com.definedcode.pebble_twitter;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.text.Html;
import android.text.Spannable.Factory;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import org.json.*;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

public class MainActivity extends Activity {
	
	static String TWITTER_CONSUMER_KEY = "VOhi6gDmn58wOrjvxseHw";
	static String TWITTER_CONSUMER_SECRET = "Q7mjIS5gqoqlBufwkttPYsqo7M8M3YJTD4PQhgTrA";
	
	static String PREVIOUS_TWEET = "";
	static final UUID APP_UUID = UUID.fromString("a9e89a76-3427-4ad0-861a-a629d2152991");
	
	static String PREFERENCE_NAME = "pebble-twitter";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLoggedIn";
	
	static final String TWITTER_CALLBACK_URL = "oauth://pebble-twitter";
	
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	
	Button btnLoginTwitter;
	
	ProgressDialog pDialog;
	
	private static Twitter twitter;
	private static RequestToken requestToken;
	
	private static SharedPreferences mSharedPreferences;
	
	AlertManager alert = new AlertManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        
        //load the interface shit
        btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);
        
        mSharedPreferences = getApplicationContext().getSharedPreferences("pebble-twitter-pref", 0);
        
        //check if someone wants to log this shit in
        
        btnLoginTwitter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// should probably call the login function now?
				loginTwitter();
			}
		});
        
        if (!loggedInAlready()) {
        	Uri uri = getIntent().getData();
        	if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
        		String verifier = uri
        				.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
        		try {
        			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
        			
        			Editor editor = mSharedPreferences.edit();
        			
        			editor.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
        			editor.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
        			editor.putBoolean(PREF_KEY_TWITTER_LOGIN, false);
        			//commit that shit!
        			editor.commit(); 
        			
        			ConfigurationBuilder builder = new ConfigurationBuilder();
            		builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            		builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            		builder.setOAuthAccessToken(accessToken.getToken());
            		builder.setOAuthAccessTokenSecret(accessToken.getTokenSecret());
            		Configuration configuration = builder.build();
            		
        			TwitterStream twitterStream = new TwitterStreamFactory(configuration).getInstance();
        			UserStreamListener listener = new UserStreamListener() {
        				public void onStatus(Status status) {
        					PebbleDictionary data = new PebbleDictionary();
        					data.addString(0, "@" + status.getUser().getScreenName() + " - " + status.getText());
        					PebbleKit.sendDataToPebble(getApplicationContext(), APP_UUID, data);
        		        }
        		        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
        		        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
        		        public void onException(Exception ex) {
        		            //fuck
        		        }
						@Override
						public void onScrubGeo(long arg0, long arg1) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onStallWarning(StallWarning arg0) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onBlock(User arg0, User arg1) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onDeletionNotice(long arg0, long arg1) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onDirectMessage(DirectMessage arg0) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onFavorite(User arg0, User arg1, Status arg2) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onFollow(User arg0, User arg1) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onFriendList(long[] arg0) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onUnblock(User arg0, User arg1) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onUnfavorite(User arg0, User arg1,
								Status arg2) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onUserListCreation(User arg0, UserList arg1) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onUserListDeletion(User arg0, UserList arg1) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onUserListMemberAddition(User arg0,
								User arg1, UserList arg2) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onUserListMemberDeletion(User arg0,
								User arg1, UserList arg2) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onUserListSubscription(User arg0,
								User arg1, UserList arg2) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onUserListUnsubscription(User arg0,
								User arg1, UserList arg2) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onUserListUpdate(User arg0, UserList arg1) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onUserProfileUpdate(User arg0) {
							// TODO Auto-generated method stub
							
						}
        			};
        	        twitterStream.addListener(listener);
        	        twitterStream.user();
        			//sendAuthenticationNotificationToPebble();
        			//ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
                	//scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                		//public void run() {
                			//try {
                   		     //List<Status> statuses = twitter.getHomeTimeline();
                   		     //PebbleDictionary data = new PebbleDictionary();
                   		     //data.addString(0, statuses.get(0).getText());
                   		     //PebbleKit.sendDataToPebble(getApplicationContext(), APP_UUID, data);
                   			//} catch (TwitterException e) {
                       			//shit hits the fan.
                       			//Log.e("Error:", e.getMessage());
                       		//}
                		//}
                	//}, 0, 1, TimeUnit.MINUTES);
        			
        			btnLoginTwitter.setVisibility(View.GONE);
        		} catch (Exception e) {
        			//oh fuck.
        			Log.e("Error:", e.getMessage());
        		}
        	}
        }
    }
    
    private void loginTwitter() {
    		ConfigurationBuilder builder = new ConfigurationBuilder();
    		builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
    		builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
    		Configuration configuration = builder.build();
    		
    		TwitterFactory tfactory = new TwitterFactory(configuration);
    		twitter = tfactory.getInstance();
    		
    		try {
    			requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
    			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
    		} catch (TwitterException e) {
    			//shit hits the fan.
    			Log.e("Error:", e.getMessage());
    		}
    }
    
    public void sendAuthenticationNotificationToPebble() {
        final Intent i = new Intent("com.getpebble.action.SEND_NOTIFICATION");

        final Map data = new HashMap();
        data.put("title", "Twitter Logged in");
        data.put("body", "You've authenticated with twitter and the twitter app on your pebble will receive tweets now :-)");
        final JSONObject jsonData = new JSONObject(data);
        final String notificationData = new JSONArray().put(jsonData).toString();

        i.putExtra("messageType", "PEBBLE_ALERT");
        i.putExtra("sender", "Pebble-Twitter");
        i.putExtra("notificationData", notificationData);
        sendBroadcast(i);
    }
    
    private boolean loggedInAlready() {
    	return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }
    
    protected void onResume() {
    	super.onResume();
    }
}
