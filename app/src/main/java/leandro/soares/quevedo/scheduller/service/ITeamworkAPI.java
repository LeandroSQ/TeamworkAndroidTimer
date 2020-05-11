package leandro.soares.quevedo.scheduller.service;

import com.google.gson.JsonObject;

import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkAccountResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkInsertTimeEntryResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkListProjectsResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkListTasksResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkPersonResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkResponse;
import leandro.soares.quevedo.scheduller.model.teamwork.service.TeamworkTimeEntryListResponse;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITeamworkAPI {

	@GET("projects.json")
	Call<TeamworkListProjectsResponse> getProjects ();

	@GET("tasks.json")
	Call<TeamworkListTasksResponse> getTasks (@Query ("projectId") String projectId);

	@GET("account.json")
	Call<TeamworkAccountResponse> getCompanyAccountInfo ();

	@GET("me.json")
	Call<TeamworkPersonResponse> getAccountInfo ();

	@GET("time_entries.json")
	Call<TeamworkTimeEntryListResponse> getTimeEntries (
			@Query("userId") String userId,
			@Query("fromdate") String fromDate,
			@Query("todate") String toDate,
			@Query("billableType") String billableType,
			@Query("sortorder") String sortOrder
	);

	@POST("tasks/{id}/time_entries.json")
	Call<TeamworkInsertTimeEntryResponse> postInsertTimeEntry (@Path ("id") String taskId, @Body JsonObject body);

}
