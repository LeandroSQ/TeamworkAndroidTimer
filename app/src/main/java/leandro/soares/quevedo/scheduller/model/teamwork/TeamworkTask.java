package leandro.soares.quevedo.scheduller.model.teamwork;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TeamworkTask implements Serializable {
	@SerializedName("id")
	private String id;

	@SerializedName("canComplete")
	private boolean canComplete;

	@SerializedName("comments-count")
	private int commentsCount;

	@SerializedName("description")
	private String description;

	@SerializedName("has-reminders")
	private boolean hasReminders;

	@SerializedName("has-unread-comments")
	private boolean hasUnreadComments;

	@SerializedName("private")
	private int _private;

	@SerializedName("content")
	private String content;

	@SerializedName("order")
	private int order;

	@SerializedName("project-id")
	private int projectId;

	@SerializedName("project-name")
	private String projectName;

	@SerializedName("todo-list-id")
	private int todoListId;

	@SerializedName("todo-list-name")
	private String todoListName;

	@SerializedName("tasklist-private")
	private boolean tasklistPrivate;

	@SerializedName("tasklist-isTemplate")
	private boolean tasklistIsTemplate;

	@SerializedName("status")
	private String status;

	@SerializedName("company-name")
	private String companyName;

	@SerializedName("company-id")
	private int companyId;

	@SerializedName("creator-id")
	private int creatorId;

	@SerializedName("creator-firstname")
	private String creatorFirstname;

	@SerializedName("creator-lastname")
	private String creatorLastname;

	@SerializedName("updater-id")
	private int updaterId;

	@SerializedName("updater-firstname")
	private String updaterFirstname;

	@SerializedName("updater-lastname")
	private String updaterLastname;

	@SerializedName("completed")
	private boolean completed;

	@SerializedName("start-date")
	private String startDate;

	@SerializedName("due-date-base")
	private String dueDateBase;

	@SerializedName("due-date")
	private String dueDate;

	@SerializedName("created-on")
	private String createdOn;

	@SerializedName("last-changed-on")
	private String lastChangedOn;

	@SerializedName("position")
	private int position;

	@SerializedName("estimated-minutes")
	private int estimatedMinutes;

	@SerializedName("priority")
	private String priority;

	@SerializedName("progress")
	private int progress;

	@SerializedName("harvest-enabled")
	private boolean harvestEnabled;

	@SerializedName("parentTaskId")
	private String parentTaskId;

	@SerializedName("lockdownId")
	private String lockdownId;

	@SerializedName("tasklist-lockdownId")
	private String tasklistLockdownId;

	@SerializedName("has-dependencies")
	private int hasDependencies;

	@SerializedName("has-predecessors")
	private int hasPredecessors;

	@SerializedName("hasTickets")
	private boolean hasTickets;

	@SerializedName("timeIsLogged")
	private String timeIsLogged;

	@SerializedName("attachments-count")
	private int attachmentsCount;

	@SerializedName("predecessors")
	private List<Object> predecessors = null;

	@SerializedName("canEdit")
	private boolean canEdit;

	@SerializedName("viewEstimatedTime")
	private boolean viewEstimatedTime;

	@SerializedName("creator-avatar-url")
	private String creatorAvatarUrl;

	@SerializedName("canLogTime")
	private boolean canLogTime;

	@SerializedName("userFollowingComments")
	private boolean userFollowingComments;

	@SerializedName("userFollowingChanges")
	private boolean userFollowingChanges;

	@SerializedName("DLM")
	private int DLM;

	public String getId () {
		return id;
	}

	public void setId (String id) {
		this.id = id;
	}

	public boolean isCanComplete () {
		return canComplete;
	}

	public void setCanComplete (boolean canComplete) {
		this.canComplete = canComplete;
	}

	public int getCommentsCount () {
		return commentsCount;
	}

	public void setCommentsCount (int commentsCount) {
		this.commentsCount = commentsCount;
	}

	public String getDescription () {
		return description;
	}

	public void setDescription (String description) {
		this.description = description;
	}

	public boolean isHasReminders () {
		return hasReminders;
	}

	public void setHasReminders (boolean hasReminders) {
		this.hasReminders = hasReminders;
	}

	public boolean isHasUnreadComments () {
		return hasUnreadComments;
	}

	public void setHasUnreadComments (boolean hasUnreadComments) {
		this.hasUnreadComments = hasUnreadComments;
	}

	public int get_private () {
		return _private;
	}

	public void set_private (int _private) {
		this._private = _private;
	}

	public String getContent () {
		return content;
	}

	public void setContent (String content) {
		this.content = content;
	}

	public int getOrder () {
		return order;
	}

	public void setOrder (int order) {
		this.order = order;
	}

	public int getProjectId () {
		return projectId;
	}

	public void setProjectId (int projectId) {
		this.projectId = projectId;
	}

	public String getProjectName () {
		return projectName;
	}

	public void setProjectName (String projectName) {
		this.projectName = projectName;
	}

	public int getTodoListId () {
		return todoListId;
	}

	public void setTodoListId (int todoListId) {
		this.todoListId = todoListId;
	}

	public String getTodoListName () {
		return todoListName;
	}

	public void setTodoListName (String todoListName) {
		this.todoListName = todoListName;
	}

	public boolean isTasklistPrivate () {
		return tasklistPrivate;
	}

	public void setTasklistPrivate (boolean tasklistPrivate) {
		this.tasklistPrivate = tasklistPrivate;
	}

	public boolean isTasklistIsTemplate () {
		return tasklistIsTemplate;
	}

	public void setTasklistIsTemplate (boolean tasklistIsTemplate) {
		this.tasklistIsTemplate = tasklistIsTemplate;
	}

	public String getStatus () {
		return status;
	}

	public void setStatus (String status) {
		this.status = status;
	}

	public String getCompanyName () {
		return companyName;
	}

	public void setCompanyName (String companyName) {
		this.companyName = companyName;
	}

	public int getCompanyId () {
		return companyId;
	}

	public void setCompanyId (int companyId) {
		this.companyId = companyId;
	}

	public int getCreatorId () {
		return creatorId;
	}

	public void setCreatorId (int creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorFirstname () {
		return creatorFirstname;
	}

	public void setCreatorFirstname (String creatorFirstname) {
		this.creatorFirstname = creatorFirstname;
	}

	public String getCreatorLastname () {
		return creatorLastname;
	}

	public void setCreatorLastname (String creatorLastname) {
		this.creatorLastname = creatorLastname;
	}

	public int getUpdaterId () {
		return updaterId;
	}

	public void setUpdaterId (int updaterId) {
		this.updaterId = updaterId;
	}

	public String getUpdaterFirstname () {
		return updaterFirstname;
	}

	public void setUpdaterFirstname (String updaterFirstname) {
		this.updaterFirstname = updaterFirstname;
	}

	public String getUpdaterLastname () {
		return updaterLastname;
	}

	public void setUpdaterLastname (String updaterLastname) {
		this.updaterLastname = updaterLastname;
	}

	public boolean isCompleted () {
		return completed;
	}

	public void setCompleted (boolean completed) {
		this.completed = completed;
	}

	public String getStartDate () {
		return startDate;
	}

	public void setStartDate (String startDate) {
		this.startDate = startDate;
	}

	public String getDueDateBase () {
		return dueDateBase;
	}

	public void setDueDateBase (String dueDateBase) {
		this.dueDateBase = dueDateBase;
	}

	public String getDueDate () {
		return dueDate;
	}

	public void setDueDate (String dueDate) {
		this.dueDate = dueDate;
	}

	public String getCreatedOn () {
		return createdOn;
	}

	public void setCreatedOn (String createdOn) {
		this.createdOn = createdOn;
	}

	public String getLastChangedOn () {
		return lastChangedOn;
	}

	public void setLastChangedOn (String lastChangedOn) {
		this.lastChangedOn = lastChangedOn;
	}

	public int getPosition () {
		return position;
	}

	public void setPosition (int position) {
		this.position = position;
	}

	public int getEstimatedMinutes () {
		return estimatedMinutes;
	}

	public void setEstimatedMinutes (int estimatedMinutes) {
		this.estimatedMinutes = estimatedMinutes;
	}

	public String getPriority () {
		return priority;
	}

	public void setPriority (String priority) {
		this.priority = priority;
	}

	public int getProgress () {
		return progress;
	}

	public void setProgress (int progress) {
		this.progress = progress;
	}

	public boolean isHarvestEnabled () {
		return harvestEnabled;
	}

	public void setHarvestEnabled (boolean harvestEnabled) {
		this.harvestEnabled = harvestEnabled;
	}

	public String getParentTaskId () {
		return parentTaskId;
	}

	public void setParentTaskId (String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public String getLockdownId () {
		return lockdownId;
	}

	public void setLockdownId (String lockdownId) {
		this.lockdownId = lockdownId;
	}

	public String getTasklistLockdownId () {
		return tasklistLockdownId;
	}

	public void setTasklistLockdownId (String tasklistLockdownId) {
		this.tasklistLockdownId = tasklistLockdownId;
	}

	public int getHasDependencies () {
		return hasDependencies;
	}

	public void setHasDependencies (int hasDependencies) {
		this.hasDependencies = hasDependencies;
	}

	public int getHasPredecessors () {
		return hasPredecessors;
	}

	public void setHasPredecessors (int hasPredecessors) {
		this.hasPredecessors = hasPredecessors;
	}

	public boolean isHasTickets () {
		return hasTickets;
	}

	public void setHasTickets (boolean hasTickets) {
		this.hasTickets = hasTickets;
	}

	public String getTimeIsLogged () {
		return timeIsLogged;
	}

	public void setTimeIsLogged (String timeIsLogged) {
		this.timeIsLogged = timeIsLogged;
	}

	public int getAttachmentsCount () {
		return attachmentsCount;
	}

	public void setAttachmentsCount (int attachmentsCount) {
		this.attachmentsCount = attachmentsCount;
	}

	public List<Object> getPredecessors () {
		return predecessors;
	}

	public void setPredecessors (List<Object> predecessors) {
		this.predecessors = predecessors;
	}

	public boolean isCanEdit () {
		return canEdit;
	}

	public void setCanEdit (boolean canEdit) {
		this.canEdit = canEdit;
	}

	public boolean isViewEstimatedTime () {
		return viewEstimatedTime;
	}

	public void setViewEstimatedTime (boolean viewEstimatedTime) {
		this.viewEstimatedTime = viewEstimatedTime;
	}

	public String getCreatorAvatarUrl () {
		return creatorAvatarUrl;
	}

	public void setCreatorAvatarUrl (String creatorAvatarUrl) {
		this.creatorAvatarUrl = creatorAvatarUrl;
	}

	public boolean isCanLogTime () {
		return canLogTime;
	}

	public void setCanLogTime (boolean canLogTime) {
		this.canLogTime = canLogTime;
	}

	public boolean isUserFollowingComments () {
		return userFollowingComments;
	}

	public void setUserFollowingComments (boolean userFollowingComments) {
		this.userFollowingComments = userFollowingComments;
	}

	public boolean isUserFollowingChanges () {
		return userFollowingChanges;
	}

	public void setUserFollowingChanges (boolean userFollowingChanges) {
		this.userFollowingChanges = userFollowingChanges;
	}

	public int getDLM () {
		return DLM;
	}

	public void setDLM (int DLM) {
		this.DLM = DLM;
	}
}
