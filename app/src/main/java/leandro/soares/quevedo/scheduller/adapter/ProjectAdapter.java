package leandro.soares.quevedo.scheduller.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import leandro.soares.quevedo.scheduller.R;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkProject;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

	private Context context;
	private ArrayList<TeamworkProject> projectList;
	private LayoutInflater inflater;
	private OnProjectSelectedListener listener;

	public ProjectAdapter (Context context, List<TeamworkProject> projectList) {
		this.context = context;
		this.listener = (OnProjectSelectedListener) context;
		this.projectList = new ArrayList<> (projectList);
		this.inflater = LayoutInflater.from (context);
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int viewType) {
		ViewHolder viewHolder = new ViewHolder (inflater.inflate (R.layout.item_simple_textview, viewGroup, false));

		if (viewType == 0) {
			viewHolder.textView.setBackgroundColor (Color.TRANSPARENT);
		} else {
			viewHolder.textView.setBackgroundColor (Color.argb (10, 0, 0, 0));
		}

		return viewHolder;
	}

	@Override
	public void onBindViewHolder (@NonNull ViewHolder viewHolder, int position) {
		TeamworkProject project = projectList.get (position);
		viewHolder.textView.setText (String.format ("%s", project.getName ()));

		viewHolder.textView.setOnClickListener ((view) -> {
			if (this.listener != null) {
				this.listener.onProjectSelected (project, position);
			}
		});
	}

	@Override
	public int getItemCount () {
		return projectList.size ();
	}

	@Override
	public int getItemViewType (int position) {
		return position % 2;
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		private TextView textView;

		public ViewHolder (@NonNull View itemView) {
			super (itemView);
			this.textView = (TextView) itemView;
		}
	}

	public interface OnProjectSelectedListener {
		void onProjectSelected (TeamworkProject project, int index);
	}
}
