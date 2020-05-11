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
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkAccount;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkAccountResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkInsertTimeEntryResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkListProjectsResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkListTasksResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkPerson;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkPersonResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkProject;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkTask;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkTimeEntry;
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

public class TeamworkService {

	private static final String BASE_URL = "https://rcadigital.teamwork.com/";

	private ITeamworkAPI api;
	private Context context;

	private TeamworkPerson currentUser;
	private String credentials;

	public TeamworkService (Context context, String credentials) {
		this.context = context;
		this.credentials = credentials;
		this.init ();
	}

	public TeamworkService (Context context) {
		this.context = context;

		SessionManager localStorage = SessionManager.getInstance ();
		this.credentials = localStorage.loadCredentials ();
		this.currentUser = localStorage.loadAccountInfo ();
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
				.addInterceptor (requestInterceptor ())
				.build ();

		Retrofit retrofit = new Retrofit.Builder ()
				.baseUrl (BASE_URL)
				.client (okHttpClient)
				.addConverterFactory (GsonConverterFactory.create (g))
				.build ();


		this.api = retrofit.create (ITeamworkAPI.class);
	}

	private Interceptor requestInterceptor () {
		return chain -> {
			Request originalResponse = chain.request ();

			Request.Builder builder = originalResponse.newBuilder ()
					.header ("Authorization", credentials);

			Request request = builder.build ();

			return chain.proceed (request);
		};
	}

	private <ResponseType extends TeamworkResponse> Callback<ResponseType> createResponseMiddleware (SimpleCallback<ResponseType> callback) {
		return new Callback<ResponseType> () {
			@Override
			public void onResponse (Call<ResponseType> call, Response<ResponseType> response) {
				if (!response.isSuccessful ()) {
					if (response.body () == null) {
						callback.onError ("Ocorreu um erro inesperado, tente novamente mais tarde!", response.code ());
					} else if (!Objects.equals (response.body ().getStatus (), "OK") && response.body ().getMessage () != null) {
						callback.onError (response.body ().getMessage (), response.code ());
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

	public void getProjects (SimpleCallback<List<TeamworkProject>> callback) {
		api.getProjects ().enqueue (createResponseMiddleware (new SimpleCallback<TeamworkListProjectsResponse> () {
			@Override
			public void onSuccess (TeamworkListProjectsResponse response) {
				callback.onSuccess (response.getProjectList ());
			}

			@Override
			public void onError (String message, int statusCode) {
				callback.onError (message, statusCode);
			}
		}));
	}

	public void getTasks (String projectId, SimpleCallback<List<TeamworkTask>> callback) {
		api.getTasks (projectId).enqueue (createResponseMiddleware (new SimpleCallback<TeamworkListTasksResponse> () {
			@Override
			public void onSuccess (TeamworkListTasksResponse response) {
				callback.onSuccess (response.getTaskList ());
			}

			@Override
			public void onError (String message, int statusCode) {
				callback.onError (message, statusCode);
			}
		}));
	}

	public void getCompanyAccountInfo (SimpleCallback<TeamworkAccount> callback) {
		api.getCompanyAccountInfo ().enqueue (createResponseMiddleware (new SimpleCallback<TeamworkAccountResponse> () {
			@Override
			public void onSuccess (TeamworkAccountResponse response) {
				callback.onSuccess (response.getAccount ());
			}

			@Override
			public void onError (String message, int statusCode) {
				callback.onError (message, statusCode);
			}
		}));
	}

	public void getAccountInfo (SimpleCallback<TeamworkPerson> callback) {
		api.getAccountInfo ().enqueue (createResponseMiddleware (new SimpleCallback<TeamworkPersonResponse> () {
			@Override
			public void onSuccess (TeamworkPersonResponse response) {
				callback.onSuccess (response.getPerson ());
			}

			@Override
			public void onError (String message, int statusCode) {
				callback.onError (message, statusCode);
			}
		}));
	}

	@SuppressLint("DefaultLocale")
	public void getMonthRegisteredTimeEntryList (SimpleCallback<List<TeamworkTimeEntry>> callback) {
		Calendar now = Calendar.getInstance ();

		String fromDate = String.format ("%04d%02d%02d", now.get (Calendar.YEAR), now.get (Calendar.MONTH) + 1, 01);
		String toDate = String.format ("%04d%02d%02d", now.get (Calendar.YEAR), now.get (Calendar.MONTH) + 1, now.getActualMaximum (Calendar.DAY_OF_MONTH));

		api.getTimeEntries (currentUser.getId (), fromDate, toDate, "billable", "asc").enqueue (createResponseMiddleware (new SimpleCallback<TeamworkTimeEntryListResponse> () {
			@Override
			public void onSuccess (TeamworkTimeEntryListResponse response) {
				callback.onSuccess (response.getTimeEntries ());
			}

			@Override
			public void onError (String message, int statusCode) {
				callback.onError (message, statusCode);
			}
		}));
	}

	@SuppressLint("DefaultLocale")
	public void getRegisteredTimeEntryList (String fromDate, String toDate, SimpleCallback<List<TeamworkTimeEntry>> callback) {
		api.getTimeEntries (currentUser.getId (), fromDate, toDate, "billable", "asc").enqueue (createResponseMiddleware (new SimpleCallback<TeamworkTimeEntryListResponse> () {
			@Override
			public void onSuccess (TeamworkTimeEntryListResponse response) {
				callback.onSuccess (response.getTimeEntries ());
			}

			@Override
			public void onError (String message, int statusCode) {
				callback.onError (message, statusCode);
			}
		}));
	}

	public void postInsertTimeEntry (String taskId, String description, String startedTime, int hours, int minutes, SimpleCallback<String> callback) {
		JsonObject body = new JsonObject ();
		JsonObject timeEntry = new JsonObject ();

		// Appends description
		timeEntry.addProperty ("description", description);
		// Appends and format the current date
		timeEntry.addProperty ("date", new SimpleDateFormat ("yyyyMMdd").format (new Date ()));
		// Notify as billable time entry, 1 means true
		timeEntry.addProperty ("isbillable", "1");
		// Empty tags
		timeEntry.addProperty ("tags", "automated");
		// Appends the logged user id
		timeEntry.addProperty ("person-id", currentUser.getId ());
		// Appends the time related properties
		timeEntry.addProperty ("hours", String.valueOf (hours));
		timeEntry.addProperty ("minutes", String.valueOf (minutes));
		timeEntry.addProperty ("time", startedTime);


		body.add ("time-entry", timeEntry);

		api.postInsertTimeEntry (taskId, body).enqueue (createResponseMiddleware (new SimpleCallback<TeamworkInsertTimeEntryResponse> () {
			@Override
			public void onSuccess (TeamworkInsertTimeEntryResponse response) {
				callback.onSuccess (response.getStatus ());
			}

			@Override
			public void onError (String message, int statusCode) {
				callback.onError (message, statusCode);
			}
		}));
	}

}
