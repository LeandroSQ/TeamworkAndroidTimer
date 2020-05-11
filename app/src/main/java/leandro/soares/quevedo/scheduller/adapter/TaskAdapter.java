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
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkTask;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

	private Context context;
	private ArrayList<TeamworkTask> taskList;
	private LayoutInflater inflater;
	private OnTaskSelectedListener listener;

	public TaskAdapter (Context context, List<TeamworkTask> taskList) {
		this.context = context;
		this.listener = (OnTaskSelectedListener) context;
		this.taskList = new ArrayList<> (taskList);
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
		TeamworkTask task = taskList.get (position);
		viewHolder.textView.setText (String.format ("%s - %s",task.getTodoListName (), task.getContent ()));

		viewHolder.textView.setOnClickListener ((view) -> {
			if (this.listener != null) {
				this.listener.onTaskSelected (task, position);
			}
		});
	}

	@Override
	public int getItemCount () {
		return taskList.size ();
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

	public interface OnTaskSelectedListener {
		void onTaskSelected (TeamworkTask task, int index);
	}
}
