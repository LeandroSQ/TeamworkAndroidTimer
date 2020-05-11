package leandro.soares.quevedo.scheduller.view.activity.tasks;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;

import java.util.List;

import leandro.soares.quevedo.scheduller.R;
import leandro.soares.quevedo.scheduller.adapter.TaskAdapter;
import leandro.soares.quevedo.scheduller.annotations.ContentView;
import leandro.soares.quevedo.scheduller.generic.BaseActivity;
import leandro.soares.quevedo.scheduller.interfaces.SimpleCallback;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkTask;
import leandro.soares.quevedo.scheduller.service.TeamworkService;

@ContentView(R.layout.activity_tasks)
public class TasksActivity extends BaseActivity implements TaskAdapter.OnTaskSelectedListener {

	private RecyclerView recyclerView;

	private List<TeamworkTask> taskList;
	private TaskAdapter adapter;

	private String selectedProjectId;

	@Override
	protected void onPreload () {
		this.selectedProjectId = getIntent ().getStringExtra ("projectId");
	}

	@Override
	protected void onLoadComponents () {
		recyclerView = findViewById (R.id.recyclerView);
	}

	@Override
	protected void onInitValues () {
		setupRecyclerview ();

		loadTasks ();
	}

	private void setupRecyclerview () {
		// Setup animation controller
		AlphaAnimation alphaAnimation = new AlphaAnimation (0f, 1f);
		alphaAnimation.setDuration (250);
		alphaAnimation.setInterpolator (new AccelerateInterpolator ());
		LayoutAnimationController animationController = new LayoutAnimationController (alphaAnimation, 0.15f);
		recyclerView.setLayoutAnimation (animationController);

		// Divider
		recyclerView.addItemDecoration (new DividerItemDecoration (this, DividerItemDecoration.VERTICAL));

		// Optimizations
		recyclerView.setHasFixedSize (true);
		recyclerView.setItemViewCacheSize (30);
		recyclerView.setDrawingCacheQuality (View.DRAWING_CACHE_QUALITY_HIGH);
	}

	// ----------------------------------------------------------------------- Projects service handling
	private void loadTasks () {
		showLoader ();

		TeamworkService service = new TeamworkService (this);
		service.getTasks (selectedProjectId, new SimpleCallback<List<TeamworkTask>> () {
			@Override
			public void onSuccess (List<TeamworkTask> response) {
				onTasksLoaded (response);

				hideLoader ();
			}

			@Override
			public void onError (String message, int statusCode) {
				hideLoader ();

				showErrorDialog (message);
			}
		});
	}

	private void onTasksLoaded (List<TeamworkTask> taskList) {
		this.taskList = taskList;
		adapter = new TaskAdapter (this, taskList);
		recyclerView.setAdapter (adapter);
		recyclerView.startLayoutAnimation ();
	}

	@Override
	public void onTaskSelected (TeamworkTask task, int index) {
		Intent data = new Intent ();
		data.putExtra ("task", task);
		data.putExtra ("taskId", task.getId ());
		data.putExtra ("projectId", task.getProjectId ());

		setResult (Activity.RESULT_OK, data);
		finish ();
	}
}
