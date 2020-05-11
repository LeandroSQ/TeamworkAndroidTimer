package leandro.soares.quevedo.scheduller.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkTask;

public final class ClockInfo {

	@SerializedName ("targetTask")
	private TeamworkTask targetTask;

	@SerializedName("timeParts")
	private ArrayList<TimeCount> timeParts;

	public TeamworkTask getTargetTask () {
		return targetTask;
	}

	public void setTargetTask (TeamworkTask targetTask) {
		this.targetTask = targetTask;
	}

	public ArrayList<TimeCount> getTimeParts () {
		return timeParts;
	}

	public void setTimeParts (ArrayList<TimeCount> time) {
		this.timeParts = time;
	}

	public TimeCount getLastTimeCount () {
		return this.timeParts.get (this.timeParts.size ()-1);
	}

	public TimeCount getFirstTimeCount () {
		return this.timeParts.get (0);
	}

	public float getPassedSeconds () {
		float total = 0f;

		for (TimeCount timePart : timeParts) {
			// Get the passed time
			long passedTime = timePart.getTime ();
			// Convert in hours
			total += passedTime / 1000f;
		}

		return total;
	}

	public final static class TimeCount {

		@SerializedName("startedMillis")
		private long startedMillis;
		@SerializedName("endedMillis")
		private long endedMillis;
		@SerializedName("hasEnded")
		private boolean hasEnded;

		public TimeCount (long startedMillis) {
			this.startedMillis = startedMillis;
			this.endedMillis = -1L;
			this.hasEnded = false;
		}

		public long getStartedMillis () {
			return startedMillis;
		}

		public void setStartedMillis (long startedMillis) {
			this.startedMillis = startedMillis;
		}

		public long getEndedMillis () {
			return endedMillis;
		}

		public void setEndedMillis (long endedMillis) {
			this.endedMillis = endedMillis;
		}

		public boolean hasEnded () {
			return hasEnded;
		}

		public void setHasEnded (boolean hasEnded) {
			this.hasEnded = hasEnded;
		}

		public long getTime () {
			if (hasEnded) {
				// If has ended, calculate the passed time
				return endedMillis - startedMillis;
			} else {
				// Otherwise calculate the passed time from the startedMillis
				return System.currentTimeMillis () - startedMillis;
			}
		}
	}

}
