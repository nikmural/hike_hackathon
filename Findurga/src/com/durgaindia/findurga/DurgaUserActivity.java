package com.durgaindia.findurga;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DurgaUserActivity extends Activity {

	private static final String TAG = DurgaUserActivity.class.getSimpleName();
	private RelativeLayout initiaLayout;
	private RelativeLayout secondLayout;
	private Button login;
	private Button register;
	private TextView welcome;
	private Button findDurga;
	private User newUser;
	private ProgressDialog mProgressDialog;
	private FindDurgaTask mFindDurgaTask;
	private ListView mUserList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_durga_user);
		initiaLayout = (RelativeLayout) findViewById(R.id.initia_page);
		secondLayout = (RelativeLayout) findViewById(R.id.later_page);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
		welcome = (TextView) findViewById(R.id.welcome);
		findDurga = (Button) findViewById(R.id.findDurga);

		newUser = DurgaHelperUtility.getUserDetailFromSharedPref(this);

		mUserList = (ListView) findViewById(R.id.durgaList);
		mUserList.setVisibility(View.GONE);
		if (newUser.getUserId().isEmpty()) {
			secondLayout.setVisibility(View.GONE);
			initiaLayout.setVisibility(View.VISIBLE);
		} else {
			secondLayout.setVisibility(View.VISIBLE);
			initiaLayout.setVisibility(View.GONE);
			welcome.append(newUser.getType() == DurgaHelperUtility.TYPE_USER ? " User"
					+ newUser.getUserId()
					: " Durga" + newUser.getUserId());
			if (newUser.getType() == DurgaHelperUtility.TYPE_USER) {
				findDurga.setVisibility(View.VISIBLE);
			} else {
				findDurga.setVisibility(View.GONE);
			}
		}
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadLoginScreen();
			}
		});

		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadRegisterScreen();
			}
		});

		findDurga.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				findNearestDurga(newUser);
			}
		});
	}

	private void loadLoginScreen() {
		Log.i(TAG, "Launch login screen");

	}

	private void loadRegisterScreen() {
		Log.i(TAG, "LAunch login screen");
		Intent loadRegisterScreen = new Intent(getApplicationContext(),
				DurgaRegistrationActivity.class);
		loadRegisterScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(loadRegisterScreen);
	}

	public void findNearestDurga(User newUser1) {
		Log.i(TAG, "LAunch login screen");
		/*mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("Searching");
		mProgressDialog.setMessage("Please wait");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();*/
		if (mFindDurgaTask == null
				|| mFindDurgaTask.getStatus() != Status.RUNNING) {
			mFindDurgaTask = new FindDurgaTask();
		}
		Log.i(TAG, "Find Durga task started");
		mFindDurgaTask.execute(newUser1);

	}

	@Override
	protected void onResume() {
		super.onResume();
		/*
		 * User newUser = DurgaHelperUtility.getUserDetailFromSharedPref(this);
		 * if (newUser.getUserId().isEmpty()) {
		 * secondLayout.setVisibility(View.GONE);
		 * initiaLayout.setVisibility(View.VISIBLE); } else {
		 * secondLayout.setVisibility(View.VISIBLE);
		 * initiaLayout.setVisibility(View.GONE);
		 * welcome.append(newUser.getType() == DurgaHelperUtility.TYPE_USER ?
		 * " User" : " Durga" + newUser.getUserId()); if (newUser.getType() ==
		 * DurgaHelperUtility.TYPE_USER) {
		 * findDurga.setVisibility(View.VISIBLE); } else {
		 * findDurga.setVisibility(View.GONE); } }
		 */
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.durga_user, menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * action bar item clicks here. The action bar will // automatically handle
	 * clicks on the Home/Up button, so long // as you specify a parent activity
	 * in AndroidManifest.xml. int id = item.getItemId(); if (id ==
	 * R.id.action_settings) { return true; } return
	 * super.onOptionsItemSelected(item); }
	 */

	@SuppressWarnings("deprecation")
	private List<User> sendSearchRequest(User newUser1) {
		JSONObject reqObj = new JSONObject();
		try {
			reqObj.put("username", newUser1.getUserId());

		} catch (JSONException e) {
			e.printStackTrace();
			reqObj = null;
		}
		@SuppressWarnings("deprecation")
		HttpClient httpClient = new DefaultHttpClient();
		Log.i(TAG, reqObj.toString());

		// url with the post data
		HttpPost httpost = new HttpPost(
				"http://192.168.0.169:6543/durga_find");

		// passes the results to a string builder/entity
		StringEntity se = null;
		try {
			se = new StringEntity(reqObj.toString());

			// sets the post request as the resulting string
			httpost.setEntity(se);
			// sets a request header so the page receving the request
			// will know what to do with it.
			httpost.setHeader("Content-type", "application/json");
			HttpResponse response = httpClient.execute(httpost);

			HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            Log.i("Sonika",responseString);
            JSONObject obj = null;
            try {
				obj=new JSONObject(responseString);
				Log.i("Sonika","obj"+ obj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            ArrayList<User> durgalist=new ArrayList<User>();
            Iterator<String> keys = obj.keys();
            Log.i("Sonika","Keys"+ keys);
            while(keys.hasNext()){
                String key = keys.next();
                
                String val = null;
                try{
                    //JSONObject value = obj.getJSONObject(key);
                    val = obj.getString(key);
                    Log.i("Sonika", "key"+key+"val"+val);
                }catch(Exception e){

                }
               User user = new User();
                user.setUserId(key);
                user.setContactNumber(Long.parseLong(val));
                durgalist.add(user);
            }
			
			return durgalist;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public class FindDurgaTask extends AsyncTask<User, Void, List<User>> {

		@Override
		protected List<User> doInBackground(User... params) {
			User user1 = new User("prag", "prag", 1, 9972176291L);
			User user2 = new User("nik", "nik", 1, 9945318605L);

			List<User> userList = new ArrayList<User>();
			//userList.add(user1);
			//userList.add(user2);// sendSearchRequest();
			userList = sendSearchRequest(params[0]);			
			return userList;
		}

		@Override
		protected void onPostExecute(List<User> result) {
			//mProgressDialog.dismiss();
			sendMessageToUser(result);
			showList(result);
		}
	}

	private void sendMessageToUser(List<User> usrList) {
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
				DurgaUserActivity.class), 0);
		SmsManager sms = SmsManager.getDefault();
		Log.i(TAG, "Sending message to " + usrList.get(0).getUserId());
		sms.sendTextMessage(String.valueOf(usrList.get(0).getContactNumber()),
				null, "help + " + newUser.getContactNumber(), pi, null);
	}

	private void showList(final List<User> usrList) {
		BaseAdapter mListShowAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final int pos = position;
				if (convertView == null) {
					convertView = inflator.inflate(R.layout.simple_list_item_1,
							null);
				}
				TextView text1 = (TextView) convertView
						.findViewById(R.id.text1);
				TextView text2 = (TextView) convertView
						.findViewById(R.id.text2);
				text1.setText(usrList.get(position).getUserId());
				text2.setText(String.valueOf(usrList.get(position)
						.getContactNumber()));
				text2.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						/*
						 * Intent callIntent = new Intent(Intent.ACTION_CALL);
						 * callIntent
						 * .setData(Uri.parse(String.valueOf(usrList.get(pos)
						 * .getContactNumber()))); startActivity(callIntent);
						 */
						Intent i = new Intent(Intent.ACTION_DIAL);
						String p = "tel:"
								+ String.valueOf(usrList.get(pos)
										.getContactNumber());
						i.setData(Uri.parse(p));
						startActivity(i);
					}
				});
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 3;
			}
		};
		mUserList.setVisibility(View.VISIBLE);
		mUserList.setAdapter(mListShowAdapter);
	}
}
