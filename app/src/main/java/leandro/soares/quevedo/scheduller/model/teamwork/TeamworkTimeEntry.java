package leandro.soares.quevedo.scheduller.model.teamwork;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamworkTimeEntry {

	@SerializedName("project-id")
	private String projectId;

	@SerializedName("isbillable")
	private String isbillable;

	@SerializedName("tasklistId")
	private String tasklistId;

	@SerializedName("todo-list-name")
	private String todoListName;

	@SerializedName("todo-item-name")
	private String todoItemName;

	@SerializedName("isbilled")
	private String isbilled;

	@SerializedName("updated-date")
	private String updatedDate;

	@SerializedName("todo-list-id")
	private String todoListId;

	@SerializedName("tags")
	private List<Object> tags = null;

	@SerializedName("canEdit")
	private boolean canEdit;

	@SerializedName("taskEstimatedTime")
	private String taskEstimatedTime;

	@SerializedName("company-name")
	private String companyName;

	@SerializedName("id")
	private String id;

	@SerializedName("invoiceNo")
	private String invoiceNo;

	@SerializedName("person-last-name")
	private String personLastName;

	@SerializedName("parentTaskName")
	private String parentTaskName;

	@SerializedName("dateUserPerspective")
	private String dateUserPerspective;

	@SerializedName("minutes")
	private String minutes;

	@SerializedName("person-first-name")
	private String personFirstName;

	@SerializedName("description")
	private String description;

	@SerializedName("ticket-id")
	private String ticketId;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("taskIsPrivate")
	private String taskIsPrivate;

	@SerializedName("parentTaskId")
	private String parentTaskId;

	@SerializedName("company-id")
	private String companyId;

	@SerializedName("project-status")
	private String projectStatus;

	@SerializedName("person-id")
	private String personId;

	@SerializedName("project-name")
	private String projectName;

	@SerializedName("task-tags")
	private List<Object> taskTags = null;

	@SerializedName("taskIsSubTask")
	private String taskIsSubTask;

	@SerializedName("todo-item-id")
	private String todoItemId;

	@SerializedName("date")
	private String date;

	@SerializedName("has-start-time")
	private String hasStartTime;

	@SerializedName("hours")
	private String hours;

	public String getProjectId () {
		return projectId;
	}

	public void setProjectId (String projectId) {
		this.projectId = projectId;
	}

	public String getIsbillable () {
		return isbillable;
	}

	public void setIsbillable (String isbillable) {
		this.isbillable = isbillable;
	}

	public String getTasklistId () {
		return tasklistId;
	}

	public void setTasklistId (String tasklistId) {
		this.tasklistId = tasklistId;
	}

	public String getTodoListName () {
		return todoListName;
	}

	public void setTodoListName (String todoListName) {
		this.todoListName = todoListName;
	}

	public String getTodoItemName () {
		return todoItemName;
	}

	public void setTodoItemName (String todoItemName) {
		this.todoItemName = todoItemName;
	}

	public String getIsbilled () {
		return isbilled;
	}

	public void setIsbilled (String isbilled) {
		this.isbilled = isbilled;
	}

	public String getUpdatedDate () {
		return updatedDate;
	}

	public void setUpdatedDate (String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getTodoListId () {
		return todoListId;
	}

	public void setTodoListId (String todoListId) {
		this.todoListId = todoListId;
	}

	public List<Object> getTags () {
		return tags;
	}

	public void setTags (List<Object> tags) {
		this.tags = tags;
	}

	public boolean isCanEdit () {
		return canEdit;
	}

	public void setCanEdit (boolean canEdit) {
		this.canEdit = canEdit;
	}

	public String getTaskEstimatedTime () {
		return taskEstimatedTime;
	}

	public void setTaskEstimatedTime (String taskEstimatedTime) {
		this.taskEstimatedTime = taskEstimatedTime;
	}

	public String getCompanyName () {
		return companyName;
	}

	public void setCompanyName (String companyName) {
		this.companyName = companyName;
	}

	public String getId () {
		return id;
	}

	public void setId (String id) {
		this.id = id;
	}

	public String getInvoiceNo () {
		return invoiceNo;
	}

	public void setInvoiceNo (String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getPersonLastName () {
		return personLastName;
	}

	public void setPersonLastName (String personLastName) {
		this.personLastName = personLastName;
	}

	public String getParentTaskName () {
		return parentTaskName;
	}

	public void setParentTaskName (String parentTaskName) {
		this.parentTaskName = parentTaskName;
	}

	public String getDateUserPerspective () {
		return dateUserPerspective;
	}

	public void setDateUserPerspective (String dateUserPerspective) {
		this.dateUserPerspective = dateUserPerspective;
	}

	public String getMinutes () {
		return minutes;
	}

	public void setMinutes (String minutes) {
		this.minutes = minutes;
	}

	public String getPersonFirstName () {
		return personFirstName;
	}

	public void setPersonFirstName (String personFirstName) {
		this.personFirstName = personFirstName;
	}

	public String getDescription () {
		return description;
	}

	public void setDescription (String description) {
		this.description = description;
	}

	public String getTicketId () {
		return ticketId;
	}

	public void setTicketId (String ticketId) {
		this.ticketId = ticketId;
	}

	public String getCreatedAt () {
		return createdAt;
	}

	public void setCreatedAt (String createdAt) {
		this.createdAt = createdAt;
	}

	public String getTaskIsPrivate () {
		return taskIsPrivate;
	}

	public void setTaskIsPrivate (String taskIsPrivate) {
		this.taskIsPrivate = taskIsPrivate;
	}

	public String getParentTaskId () {
		return parentTaskId;
	}

	public void setParentTaskId (String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public String getCompanyId () {
		return companyId;
	}

	public void setCompanyId (String companyId) {
		this.companyId = companyId;
	}

	public String getProjectStatus () {
		return projectStatus;
	}

	public void setProjectStatus (String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getPersonId () {
		return personId;
	}

	public void setPersonId (String personId) {
		this.personId = personId;
	}

	public String getProjectName () {
		return projectName;
	}

	public void setProjectName (String projectName) {
		this.projectName = projectName;
	}

	public List<Object> getTaskTags () {
		return taskTags;
	}

	public void setTaskTags (List<Object> taskTags) {
		this.taskTags = taskTags;
	}

	public String getTaskIsSubTask () {
		return taskIsSubTask;
	}

	public void setTaskIsSubTask (String taskIsSubTask) {
		this.taskIsSubTask = taskIsSubTask;
	}

	public String getTodoItemId () {
		return todoItemId;
	}

	public void setTodoItemId (String todoItemId) {
		this.todoItemId = todoItemId;
	}

	public String getDate () {
		return date;
	}

	public void setDate (String date) {
		this.date = date;
	}

	public String getHasStartTime () {
		return hasStartTime;
	}

	public void setHasStartTime (String hasStartTime) {
		this.hasStartTime = hasStartTime;
	}

	public String getHours () {
		return hours;
	}

	public void setHours (String hours) {
		this.hours = hours;
	}

}
