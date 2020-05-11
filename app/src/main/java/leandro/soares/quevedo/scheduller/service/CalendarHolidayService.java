package leandro.soares.quevedo.scheduller.service;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import leandro.soares.quevedo.scheduller.interfaces.SimpleCallback;
import leandro.soares.quevedo.scheduller.model.calendar.CalendarHolidayDate;
import leandro.soares.quevedo.scheduller.model.calendar.service.CalendarGetHolidaysResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkAccount;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkPerson;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkProject;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkTask;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkTimeEntry;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkAccountResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkInsertTimeEntryResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkListProjectsResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkListTasksResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkPersonResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkTimeEntryListResponse;
import leandro.soares.quevedo.scheduller.util.SessionManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarHolidayService {
	private static final String BASE_URL = "https://api.calendario.com.br/";
	private static final String token = "bGVhbmRyby5zb2FyZXNAb3BlcmFjYW8ucmNhZGlnaXRhbC5jb20uYnImaGFzaD04MDQ5ODIwNQ";

	private ICalendarHolidayAPI api;
	private Context context;

	public CalendarHolidayService (Context context) {
		this.context = context;

		this.init ();
	}

	private void init () {
		Gson g = new GsonBuilder ().create ();

		HttpLoggingInterceptor bodyInterceptor = new HttpLoggingInterceptor ();
		bodyInterceptor.setLevel (HttpLoggingInterceptor.Level.BODY);

		OkHttpClient okHttpClient = new OkHttpClient.Builder ().connectTimeout (1, TimeUnit.MINUTES)
				.readTimeout (30, TimeUnit.SECONDS)
				.writeTimeout (15, TimeUnit.SECONDS)
				.addInterceptor (bodyInterceptor)
				.build ();

		Retrofit retrofit = new Retrofit.Builder ()
				.baseUrl (BASE_URL)
				.client (okHttpClient)
				.addConverterFactory (GsonConverterFactory.create (g))
				.build ();


		this.api = retrofit.create (ICalendarHolidayAPI.class);
	}

	private <ResponseType> Callback<ResponseType> createResponseMiddleware (SimpleCallback<ResponseType> callback) {
		return new Callback<ResponseType> () {
			@Override
			public void onResponse (Call<ResponseType> call, Response<ResponseType> response) {
				if (!response.isSuccessful ()) {
					if (response.body () == null) {
						callback.onError ("Ocorreu um erro inesperado, tente novamente mais tarde!", response.code ());
					} else {
						callback.onError (response.message (), response.code ());
					}
				} else {
					callback.onSuccess (response.body ());
				}
			}

			@Override
			public void onFailure (Call<ResponseType> call, Throwable t) {
				callback.onError (t.getMessage (), -1);
			}
		};
	}

	public void getHolidays (SimpleCallback<List<CalendarHolidayDate>> callback) {
		int currentYear = Calendar.getInstance ().get (Calendar.YEAR);
		String uf = "RS";
		String city = "porto alegre";

		api.getHolidays (true, token, currentYear, uf, city).enqueue (createResponseMiddleware (callback));
	}
}
