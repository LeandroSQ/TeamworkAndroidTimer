package leandro.soares.quevedo.scheduller.util;

import leandro.soares.quevedo.scheduller.model.ClockInfo;

public class ClockUtil {

	private static boolean clockRunning;

	public static void pauseClock (ClockInfo clockInfo) {
		clockRunning = false;

		ClockInfo.TimeCount lastTimePart = clockInfo.getLastTimeCount ();

		lastTimePart.setEndedMillis (System.currentTimeMillis ());
		lastTimePart.setHasEnded (true);

		// Last time count has less than one minute, remove it and append to last one
		if (clockInfo.getTimeParts ().size () > 1 && lastTimePart.getTime () < 1000 * 60) {
			clockInfo.getTimeParts ().remove (lastTimePart);
			ClockInfo.TimeCount previousTimePart = clockInfo.getLastTimeCount ();
			previousTimePart.setEndedMillis (previousTimePart.getEndedMillis () + lastTimePart.getTime ());
		}

		// Save to local storage
		SessionManager.getInstance ().saveClockInfo (clockInfo);
	}

	public static void startDelayedClock (ClockInfo clockInfo, long millis) {
		clockRunning = true;

		// Add time count
		ClockInfo.TimeCount timeCount = new ClockInfo.TimeCount (millis);
		clockInfo.getTimeParts ().add (timeCount);

		// Save to local storage
		SessionManager.getInstance ().saveClockInfo (clockInfo);
	}


	public static void resumeClock (ClockInfo clockInfo) {
		clockRunning = true;

		// Add time count
		ClockInfo.TimeCount timeCount = new ClockInfo.TimeCount (System.currentTimeMillis ());
		clockInfo.getTimeParts ().add (timeCount);

		// Save to local storage
		SessionManager.getInstance ().saveClockInfo (clockInfo);
	}

	public static void cancelClock () {
		clockRunning = false;

		// Save an invalid clock info to local storage
		SessionManager.getInstance ().saveClockInfo (null);
	}

	public static void playPauseClock (ClockInfo clockInfo) {
		if (clockRunning) {
			pauseClock (clockInfo);
		} else {
			resumeClock (clockInfo);
		}
	}

	public static void playPauseClock () {
		ClockInfo clockInfo = SessionManager.getInstance ().loadClockInfo ();
		playPauseClock (clockInfo);
	}

	public static boolean isClockRunning () {
		return clockRunning;
	}

	public static void setClockRunning (boolean clockRunning) {
		ClockUtil.clockRunning = clockRunning;
	}

}
