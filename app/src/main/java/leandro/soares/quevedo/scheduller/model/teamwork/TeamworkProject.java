package leandro.soares.quevedo.scheduller.model.teamwork;

import com.google.gson.annotations.SerializedName;

public class TeamworkProject {

	@SerializedName("starred")
	private boolean starred;
	@SerializedName("name")
	private String name;
	@SerializedName("description")
	private String description;
	@SerializedName("status")
	private String status;
	@SerializedName("isProjectAdmin")
	private boolean isProjectAdmin;
	@SerializedName("created-on")
	private String createdOn;
	@SerializedName("category")
	private Category category;
	@SerializedName("start-page")
	private String startPage;
	@SerializedName("startDate")
	private String startDate;
	@SerializedName("logo")
	private String logo;
	@SerializedName("id")
	private String id;
	@SerializedName("last-changed-on")
	private String lastChangedOn;
	@SerializedName("endDate")
	private String endDate;

	public boolean isStarred () {
		return starred;
	}

	public void setStarred (boolean starred) {
		this.starred = starred;
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getDescription () {
		return description;
	}

	public void setDescription (String description) {
		this.description = description;
	}

	public String getStatus () {
		return status;
	}

	public void setStatus (String status) {
		this.status = status;
	}

	public boolean isProjectAdmin () {
		return isProjectAdmin;
	}

	public void setProjectAdmin (boolean projectAdmin) {
		isProjectAdmin = projectAdmin;
	}

	public String getCreatedOn () {
		return createdOn;
	}

	public void setCreatedOn (String createdOn) {
		this.createdOn = createdOn;
	}

	public Category getCategory () {
		return category;
	}

	public void setCategory (Category category) {
		this.category = category;
	}

	public String getStartPage () {
		return startPage;
	}

	public void setStartPage (String startPage) {
		this.startPage = startPage;
	}

	public String getStartDate () {
		return startDate;
	}

	public void setStartDate (String startDate) {
		this.startDate = startDate;
	}

	public String getLogo () {
		return logo;
	}

	public void setLogo (String logo) {
		this.logo = logo;
	}

	public String getId () {
		return id;
	}

	public void setId (String id) {
		this.id = id;
	}

	public String getLastChangedOn () {
		return lastChangedOn;
	}

	public void setLastChangedOn (String lastChangedOn) {
		this.lastChangedOn = lastChangedOn;
	}

	public String getEndDate () {
		return endDate;
	}

	public void setEndDate (String endDate) {
		this.endDate = endDate;
	}

	public class Category {

		@SerializedName("name")
		public String name;
		@SerializedName("id")
		public String id;

		public String getName () {
			return name;
		}

		public void setName (String name) {
			this.name = name;
		}

		public String getId () {
			return id;
		}

		public void setId (String id) {
			this.id = id;
		}
	}

}
