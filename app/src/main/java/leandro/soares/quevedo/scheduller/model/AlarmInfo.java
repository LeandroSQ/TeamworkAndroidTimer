package leandro.soares.quevedo.scheduller.model;

import com.google.gson.annotations.SerializedName;

public class AlarmInfo {
	@SerializedName("startHours")
	private String startHours;

	@SerializedName("endHours")
	private String endHours;

	@SerializedName("dayLength")
	private String dayLength;

	public AlarmInfo (String startours, String endHours, String dayLength) {
		this.startHours = startours;
		this.endHours = endHours;
		this.dayLength = dayLength;
	}

	public String getStartHours () {
		return startHours;
	}

	public void setStartHours (String startHours) {
		this.startHours = startHours;
	}

	public String getEndHours () {
		return endHours;
	}

	public void setEndHours (String endHours) {
		this.endHours = endHours;
	}

	public String getDayLength () {
		return dayLength;
	}

	public void setDayLength (String dayLength) {
		this.dayLength = dayLength;
	}
}
