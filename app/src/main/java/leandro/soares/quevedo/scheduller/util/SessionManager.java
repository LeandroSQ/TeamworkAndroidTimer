package leandro.soares.quevedo.scheduller.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import leandro.soares.quevedo.scheduller.model.AlarmInfo;
import leandro.soares.quevedo.scheduller.model.ClockInfo;
import leandro.soares.quevedo.scheduller.model.calendar.CalendarHolidayDate;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkPerson;
import okhttp3.Credentials;

public class SessionManager {
	// Shared Preferences name
	private static final String preferencesName = SessionManager.class.getPackage ().getName ();
	private static SessionManager sessionManagerInstance;

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	// Shared Preferences mode
	private int privateMode = 0;

	@SuppressLint("CommitPrefEdits")
	private SessionManager (Context context) {
		preferences = context.getSharedPreferences (preferencesName, privateMode);
		editor = preferences.edit ();
	}

	public static void initialize (Context context) {
		sessionManagerInstance = new SessionManager (context);
	}

	public static synchronized SessionManager getInstance () {
		return sessionManagerInstance;
	}

	public void clear () {
		editor.clear ();
		editor.apply ();
	}

	public void saveCredentials (String login, String password) {
		String credentials = Credentials.basic (login, password);
		editor.putString ("credentials", credentials);
		editor.apply ();
	}

	public String loadCredentials () {
		return preferences.getString ("credentials", null);
	}

	public void saveAccountInfo (TeamworkPerson response) {
		editor.putString ("accountInfo", new Gson ().toJson (response));
		editor.apply ();
	}

	public TeamworkPerson loadAccountInfo () {
		String json = preferences.getString ("accountInfo", null);
		if (json == null || json.isEmpty ()) return null;
		else return new Gson ().fromJson (json, TeamworkPerson.class);
	}

	public void saveClockInfo (ClockInfo clockInfo) {
		Gson gson = new Gson ();
		String json = gson.toJson (clockInfo);

		editor.putString ("clockInfo", json);
		editor.apply ();
	}

	public ClockInfo loadClockInfo () {
		String json = preferences.getString ("clockInfo", null);
		if (json == null) return null;

		Gson gson = new Gson ();
		return gson.fromJson (json, ClockInfo.class);
	}

	public void saveAlarmInfo (AlarmInfo alarmInfo) {
		Gson gson = new Gson ();
		String json = gson.toJson (alarmInfo);

		editor.putString ("alarmInfo", json);
		editor.apply ();
	}

	public AlarmInfo loadAlarmInfo () {
		String json = preferences.getString ("alarmInfo", null);
		if (json == null) return null;

		Gson gson = new Gson ();
		return gson.fromJson (json, AlarmInfo.class);
	}

	public void saveHolidayDateList (List<CalendarHolidayDate> holidayDateList) {
		Gson gson = new Gson ();
		String json = gson.toJson (holidayDateList.toArray ());

		editor.putString ("holidays", json);
		editor.apply ();
	}

	public List<CalendarHolidayDate> loadHolidayDateList () {
		String json = preferences.getString ("holidays", null);
		if (json == null) return null;

		Gson gson = new Gson ();
		return Arrays.asList (gson.fromJson (json, CalendarHolidayDate[].class));
	}

}
