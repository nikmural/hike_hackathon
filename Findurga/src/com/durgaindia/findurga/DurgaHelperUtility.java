package com.durgaindia.findurga;

import android.content.Context;
import android.content.SharedPreferences;

public class DurgaHelperUtility {
	private final static String DURGA_USER_CREDENTIAL="finddurga_user_credential";
    private final static String USER_ID = "user_id";
    private final static String PASSWORD = "password";
    private final static String USER_TYPE ="user_type";
    private final static String CONTACT_NUMBER = "contact_number";
    public final static int TYPE_USER = 0;
    public final static int TYEP_DURGA = 1;
	
	/**
	 * Stores User Id Password phone number.
	 * 
	 * @param context
	 * @param user id, password, phone no.
	 */
	public static void saveUserInfoToSharedPref(Context context,
			String userid,String password,int type,long contactNumber) {
		SharedPreferences prefsAcitivity = context.getSharedPreferences(
				DURGA_USER_CREDENTIAL, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefsAcitivity.edit();
		editor.putString(USER_ID, userid);
		editor.putString(PASSWORD, password);
		editor.putInt(USER_TYPE, type);
		editor.putLong(CONTACT_NUMBER, contactNumber);		
		editor.commit();
	}
	
	/**
	 * Returns the user object with all details as
	 * user id, password, type, phone number
	 * 
	 * @param context
	 * @return
	 */
	public static User getUserDetailFromSharedPref(Context context) {
		SharedPreferences prefsActivity = context.getSharedPreferences(
				DURGA_USER_CREDENTIAL, Context.MODE_PRIVATE);
		String userId =  prefsActivity.getString(
				USER_ID, "");
		String password =  prefsActivity.getString(
				PASSWORD, "");
		int type =  prefsActivity.getInt(
				USER_TYPE, -1);
		long contactNumber =  prefsActivity.getLong(
				CONTACT_NUMBER, 0);
		return new User(userId,password,type,contactNumber);
	}
}
