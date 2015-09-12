package com.durgaindia.findurga;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class DurgaRegistrationActivity extends Activity {

	EditText userIdEdit, pwdEdit, contactEdit;
	RadioButton userRadio, durgaRadio;
	String userId, password, contact;
	int userType = -1;
	long contactNumber = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_durga_registration);
		userIdEdit = (EditText) findViewById(R.id.user_id_text);
		pwdEdit = (EditText) findViewById(R.id.password_text);
		contactEdit = (EditText) findViewById(R.id.contact_text);
		userRadio = (RadioButton) findViewById(R.id.option_user);
		durgaRadio = (RadioButton) findViewById(R.id.option_durga);
		Button registerBtn = (Button) findViewById(R.id.register_btn);

		registerBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				userId = userIdEdit.getText().toString();
				password = pwdEdit.getText().toString();
				contact = contactEdit.getText().toString();
				contactNumber = Long.parseLong(contact);
				if (userRadio.isChecked()) {
					userType = 0;
				} else if (durgaRadio.isChecked()) {
					userType = 1;
				}

				if (userId == null || password == null || contactNumber == -1
						|| userType == -1) {
					Toast.makeText(DurgaRegistrationActivity.this,
							getString(R.string.input_error), Toast.LENGTH_SHORT)
							.show();
					return;
				}

				DurgaHelperUtility.saveUserInfoToSharedPref(
						getApplicationContext(), userId, password, userType,
						contactNumber);
				JSONObject reqObj = prepareRequestParams();
				if (reqObj != null && isNetworkAvailable())
					new RegisterUserTask().execute(reqObj);

			}
		});

	}

	JSONObject prepareRequestParams() {
		JSONObject reqObj = new JSONObject();
		try {
			reqObj.put("username", userId);
			reqObj.put("password", password);
			reqObj.put("user_type", userType);
			reqObj.put("phone_number", contactNumber);

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return reqObj;
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	class RegisterUserTask extends AsyncTask<JSONObject, Void, HttpResponse> {

		protected HttpResponse doInBackground(JSONObject... reqObj) {

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response;
			Log.i("Sonika", reqObj[0].toString());

			// url with the post data
			HttpPost httpost = new HttpPost(
					"http://192.168.0.169:6543/register/");

			// passes the results to a string builder/entity
			StringEntity se = null;
			try {
				se = new StringEntity(reqObj[0].toString());

				// sets the post request as the resulting string
				httpost.setEntity(se);
				// sets a request header so the page receving the request
				// will know what to do with it
				// httpost.setHeader("Accept", "application/json");
				httpost.setHeader("Content-type", "application/json");

				response = httpClient.execute(httpost);

				Log.i("Sonika", response.getEntity().getContent() + "");

			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return response;

		}

		protected void onPostExecute(HttpResponse response) {
			// TODO: check this.exception
			// TODO: do something with the feed

			if (response.getStatusLine().getStatusCode() == 200) {
				if (userType == DurgaHelperUtility.TYEP_DURGA) {
					Intent intent = new Intent(getApplicationContext(),
							DurgaLocationService.class);
					startService(intent);
				}
				finish();
			}
		}
	}

}
