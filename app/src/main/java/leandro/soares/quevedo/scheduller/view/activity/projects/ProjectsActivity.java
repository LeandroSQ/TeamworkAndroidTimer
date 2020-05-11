package leandro.soares.quevedo.scheduller.view.activity.projects;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;

import java.util.List;

import leandro.soares.quevedo.scheduller.R;
import leandro.soares.quevedo.scheduller.adapter.ProjectAdapter;
import leandro.soares.quevedo.scheduller.annotations.ContentView;
import leandro.soares.quevedo.scheduller.generic.BaseActivity;
import leandro.soares.quevedo.scheduller.interfaces.SimpleCallback;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkProject;
import leandro.soares.quevedo.scheduller.service.TeamworkService;
import leandro.soares.quevedo.scheduller.view.activity.tasks.TasksActivity;

@ContentView(R.layout.activity_projects)
public class ProjectsActivity extends BaseActivity implements ProjectAdapter.OnProjectSelectedListener {

	private RecyclerView recyclerView;

	private List<TeamworkProject> projectList;
	private ProjectAdapter adapter;

	@Override
	protected void onPreload () {

	}

	@Override
	protected void onLoadComponents () {
		recyclerView = findViewById (R.id.recyclerView);
	}

	@Override
	protected void onInitValues () {
		setupRecyclerview ();

		loadProjects ();
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
	// ----------------------------------------------------------------------- TaskActivity handling

	@Override
	protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
		// If the activity had successfully picked a task, finishes
		if (resultCode == Activity.RESULT_OK) {
			setResult (Activity.RESULT_OK, data);
			finish ();
		}
	}

	// ----------------------------------------------------------------------- Projects service handling
	private void loadProjects () {
		showLoader ();

		TeamworkService service = new TeamworkService (this);
		service.getProjects (new SimpleCallback<List<TeamworkProject>> () {
			@Override
			public void onSuccess (List<TeamworkProject> response) {
				onProjectsLoaded (response);

				hideLoader ();
			}

			@Override
			public void onError (String message, int statusCode) {
				hideLoader ();

				showErrorDialog (message);
			}
		});
	}

	private void onProjectsLoaded (List<TeamworkProject> projectList) {
		this.projectList = projectList;
		adapter = new ProjectAdapter (this, projectList);
		recyclerView.setAdapter (adapter);
		recyclerView.startLayoutAnimation ();
	}

	@Override
	public void onProjectSelected (TeamworkProject project, int index) {
		// Start activity and pass the selected project's id
		Intent intent = new Intent (this, TasksActivity.class);
		intent.putExtra ("projectId", project.getId ());
		startActivityForResult (intent, 100);
	}
}
