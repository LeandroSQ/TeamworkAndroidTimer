package leandro.soares.quevedo.scheduller.model.teamwork.service;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkTimeEntry;

public class TeamworkTimeEntryListResponse extends TeamworkResponse {

	@SerializedName ("time-entries")
	private ArrayList<TeamworkTimeEntry> timeEntries;

	public ArrayList<TeamworkTimeEntry> getTimeEntries () {
		return timeEntries;
	}

	public void setTimeEntries (ArrayList<TeamworkTimeEntry> timeEntries) {
		this.timeEntries = timeEntries;
	}


}
