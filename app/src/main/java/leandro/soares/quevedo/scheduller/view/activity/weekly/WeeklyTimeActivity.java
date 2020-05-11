package leandro.soares.quevedo.scheduller.view.activity.weekly;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import leandro.soares.quevedo.scheduller.R;
import leandro.soares.quevedo.scheduller.annotations.ContentView;
import leandro.soares.quevedo.scheduller.generic.BaseActivity;
import leandro.soares.quevedo.scheduller.generic.BasePartial;
import leandro.soares.quevedo.scheduller.helper.MultilineAxisRenderer;
import leandro.soares.quevedo.scheduller.interfaces.SimpleCallback;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkPerson;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkTimeEntry;
import leandro.soares.quevedo.scheduller.service.TeamworkService;
import leandro.soares.quevedo.scheduller.util.SessionManager;

@ContentView(R.layout.activity_weekly_time)
public class WeeklyTimeActivity extends BasePartial {

	private TextView tvDateRange;
	private ImageButton btnLeft;
	private ProgressBar progressBar;
	private BarChart barChart;
	private ImageButton btnRight;
	private Calendar time = Calendar.getInstance ();
	private boolean chartReady;

	public WeeklyTimeActivity (Context context) {
		super (context);
	}

	public WeeklyTimeActivity (Context context, @Nullable AttributeSet attrs) {
		super (context, attrs);
	}

	public WeeklyTimeActivity (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super (context, attrs, defStyleAttr);
	}

	public WeeklyTimeActivity (Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super (context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onLoadComponents () {
		tvDateRange = findViewById (R.id.tvDateRange);
		btnLeft = findViewById (R.id.btnLeft);
		progressBar = findViewById (R.id.progressBar);
		barChart = findViewById (R.id.barChart);
		btnRight = findViewById (R.id.btnRight);

		btnLeft.setOnClickListener (this::onBtnLeftClick);
		btnRight.setOnClickListener (this::onBtnRightClick);
	}

	@Override
	protected void onInitValues () {
		setupCalendar ();
		loadTimeEntries ();
	}

	// ----------------------------------------------------------------------- Chart handling
	private void setupCalendar () {
		// Makes the calendar start at monday of the current week
		int day = time.get (Calendar.DAY_OF_WEEK);

		switch (day) {
			case Calendar.MONDAY:
				break;
			case Calendar.TUESDAY:
				time.add (Calendar.DAY_OF_MONTH, -1);
				break;
			case Calendar.WEDNESDAY:
				time.add (Calendar.DAY_OF_MONTH, -2);
				break;
			case Calendar.THURSDAY:
				time.add (Calendar.DAY_OF_MONTH, -3);
				break;
			case Calendar.FRIDAY:
				time.add (Calendar.DAY_OF_MONTH, -4);
				break;
			case Calendar.SATURDAY:
				time.add (Calendar.DAY_OF_MONTH, -5);
				break;
			case Calendar.SUNDAY:
				time.add (Calendar.DAY_OF_MONTH, -6);
				break;
		}

		time.add (Calendar.DAY_OF_MONTH, -7);
	}

	@SuppressLint("DefaultLocale")
	private void setupBarChart (List<TeamworkTimeEntry> entries, float dailyWorkingHours) throws ParseException {
		// Convert the time retrieved from API to barChartEntries
		List<BarEntry> chartEntries = new ArrayList<BarEntry> () {{
			add (new BarEntry (0f, 0f));// Monday
			add (new BarEntry (1f, 0f));// Tuesday
			add (new BarEntry (2f, 0f));// Wednesday
			add (new BarEntry (3f, 0f));// Thursday
			add (new BarEntry (4f, 0f));// Friday
			add (new BarEntry (5f, 0f));// Saturday
			add (new BarEntry (6f, 0f));// Sunday
		}};

		for (int i = 0; i < entries.size (); i++) {
			TeamworkTimeEntry entry = entries.get (i);
			int hours = Integer.parseInt (entry.getHours ());
			int minutes = Integer.parseInt (entry.getMinutes ());

			// Split the date to ignore Timezones and Timestamps
			String entryDate = entry.getDate ().substring (0, 10);

			// Sum the time of the entry's day of week
			Calendar entryCalendar = Calendar.getInstance ();
			entryCalendar.setTime (new SimpleDateFormat ("yyyy-MM-dd").parse (entryDate));
			int dayOfWeek = entryCalendar.get (Calendar.DAY_OF_WEEK);
			switch (dayOfWeek) {
				case Calendar.MONDAY:
					dayOfWeek = 0;
					break;
				case Calendar.TUESDAY:
					dayOfWeek = 1;
					break;
				case Calendar.WEDNESDAY:
					dayOfWeek = 2;
					break;
				case Calendar.THURSDAY:
					dayOfWeek = 3;
					break;
				case Calendar.FRIDAY:
					dayOfWeek = 4;
					break;
				case Calendar.SATURDAY:
					dayOfWeek = 5;
					break;
				case Calendar.SUNDAY:
					dayOfWeek = 6;
					break;
			}

			BarEntry barEntry = chartEntries.get (dayOfWeek);
			barEntry.setY (barEntry.getY () + hours + (minutes / 60f));
		}

		BarDataSet set = new BarDataSet (chartEntries, "Registro de horas");

		// Setup the bar style
		set.setColors (new int[] {R.color.yellow}, getContext ());
		set.setValueTextSize (13f);
		set.setValueTextColor (getResources ().getColor (R.color.gray));
		set.setValueFormatter ((value, entry, dataSetIndex, viewPortHandler) -> {
			float m = value * 60f;
			int hours = (int) (m / 60f);
			m -= hours * 60f;

			int minutes = (int) m;

			if (hours > 0 && minutes > 0) {
				return String.format ("%02d:%02d", hours, minutes);
			} else if (hours == 0 && minutes > 0) {
				return String.format ("%dm", minutes);
			} else if (hours > 0 && minutes == 0) {
				return String.format ("%dh", hours);
			} else {
				return "0h";
			}
		});

		BarData data = new BarData (set);

		// Put a reference line
		LimitLine referenceLine = new LimitLine (dailyWorkingHours);
		referenceLine.setLineColor (getResources ().getColor (R.color.gray));
		referenceLine.setLineWidth (2f);
		referenceLine.enableDashedLine (10f, 10f, 0f);
		barChart.getAxisRight ().addLimitLine (referenceLine);

		barChart.setData (data);

		// Setup the Y axis
		barChart.getAxis (YAxis.AxisDependency.RIGHT).setDrawAxisLine (false);
		barChart.getAxis (YAxis.AxisDependency.RIGHT).setDrawGridLines (false);
		barChart.getAxis (YAxis.AxisDependency.RIGHT).setDrawLabels (false);
		barChart.getAxis (YAxis.AxisDependency.LEFT).setDrawAxisLine (false);
		barChart.getAxis (YAxis.AxisDependency.LEFT).setDrawGridLines (false);
		barChart.getAxis (YAxis.AxisDependency.LEFT).setDrawLabels (false);

		// Setup the X axis
		barChart.getXAxis ().setDrawAxisLine (false);
		barChart.getXAxis ().setDrawGridLines (false);
		barChart.getXAxis ().setDrawLabels (true);
		barChart.getXAxis ().setPosition (XAxis.XAxisPosition.BOTTOM_INSIDE);
		barChart.getXAxis ().setValueFormatter ((value, axis) -> {
			Calendar targetTime = Calendar.getInstance ();
			targetTime.setTimeInMillis (time.getTimeInMillis ());
			targetTime.add (Calendar.DAY_OF_MONTH, (int) value);

			return String.format (
					"%s\n%02d/%02d",
					new String[] {"Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom"}[(int) value],
					targetTime.get (Calendar.DAY_OF_MONTH),
					targetTime.get (Calendar.MONTH) + 1
			);
		});
		barChart.setXAxisRenderer (new MultilineAxisRenderer (barChart.getViewPortHandler (), barChart.getXAxis (), barChart.getTransformer (YAxis.AxisDependency.LEFT)));
		barChart.getXAxis ().setGranularity (1f);

		// Setup the graph style
		barChart.getLegend ().setEnabled (false);
		barChart.getDescription ().setEnabled (false);
		barChart.setDragEnabled (false);
		barChart.setScaleEnabled (false);
		barChart.setDoubleTapToZoomEnabled (false);
		barChart.setPinchZoom (false);
		barChart.setFitBars (true);
		barChart.setNoDataText ("");
		barChart.setDrawGridBackground (false);
		barChart.setDrawBorders (false);
		barChart.setDrawValueAboveBar (true);
		barChart.setGridBackgroundColor (Color.TRANSPARENT);

		barChart.animateY (750, Easing.EaseInCubic);

		// Notify chart as ready
		barChart.postDelayed (() -> chartReady = true, 750);
	}

	// ----------------------------------------------------------------------- Events
	// EVENT On button left click
	private void onBtnLeftClick (View view) {
		if (!chartReady) { return; }

		time.add (Calendar.DAY_OF_MONTH, -7);
		loadTimeEntries ();
	}

	// EVENT On button right click
	private void onBtnRightClick (View view) {
		if (!chartReady) { return; }

		time.add (Calendar.DAY_OF_MONTH, 7);
		loadTimeEntries ();
	}
	// ----------------------------------------------------------------------- Time service handling
	@SuppressLint("DefaultLocale")
	private void loadTimeEntries () {
		chartReady = false;
		showLoader ();

		Calendar endOfWeek = Calendar.getInstance ();
		endOfWeek.setTimeInMillis (time.getTimeInMillis ());
		endOfWeek.add (Calendar.DAY_OF_MONTH, 6);

		String fromDate = String.format ("%04d%02d%02d", time.get (Calendar.YEAR), time.get (Calendar.MONTH) + 1, time.get (Calendar.DAY_OF_MONTH));
		String toDate = String.format ("%04d%02d%02d", endOfWeek.get (Calendar.YEAR), endOfWeek.get (Calendar.MONTH) + 1, endOfWeek.get (Calendar.DAY_OF_MONTH));

		tvDateRange.setText (String.format ("%02d/%02d/%04d - %02d/%02d/%04d",
				time.get (Calendar.DAY_OF_MONTH), time.get (Calendar.MONTH) + 1, time.get (Calendar.YEAR),
				endOfWeek.get (Calendar.DAY_OF_MONTH), endOfWeek.get (Calendar.MONTH) + 1, endOfWeek.get (Calendar.YEAR)
		));

		TeamworkService service = new TeamworkService (getContext ());
		service.getRegisteredTimeEntryList (fromDate, toDate, new SimpleCallback<List<TeamworkTimeEntry>> () {
			@Override
			public void onSuccess (List<TeamworkTimeEntry> response) {
				onTimeEntriesLoaded (response);

				hideLoader ();
			}

			@Override
			public void onError (String message, int statusCode) {
				hideLoader ();

				showErrorDialog (message);
			}
		});
	}

	@SuppressLint("DefaultLocale")
	private void onTimeEntriesLoaded (List<TeamworkTimeEntry> entries) {
		// Calculate the working time based on month day count
		TeamworkPerson account = SessionManager.getInstance ().loadAccountInfo ();
		float dailyWorkingHours = Float.parseFloat (account.getLengthOfDay ());

		try {
			setupBarChart (entries, dailyWorkingHours);
		} catch (ParseException e) {
			e.printStackTrace ();
		}
	}

	// ----------------------------------------------------------------------- Utils
	private void showLoader () {
		barChart.setVisibility (GONE);
		progressBar.setVisibility (VISIBLE);
	}

	private void hideLoader () {
		barChart.setVisibility (VISIBLE);
		progressBar.setVisibility (GONE);
	}

	private void showErrorDialog (String message) {

	}

}
