package org.tang.exam.adapter;

import java.util.ArrayList;
import org.tang.exam.R;
import org.tang.exam.base.MyBaseAdatper;
import org.tang.exam.entity.TaskCurrentDay;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TaskCurrentDayListAdapter extends MyBaseAdatper {
	private LayoutInflater mInflater;
	private ArrayList<TaskCurrentDay> mTaskCurrentDayList = new ArrayList<TaskCurrentDay>();

	public TaskCurrentDayListAdapter(Context context, ArrayList<TaskCurrentDay> taskWeeklyList) {
		super();
		mTaskCurrentDayList = taskWeeklyList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mTaskCurrentDayList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mTaskCurrentDayList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_task_current_day, null);

			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_task_current_day_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		TaskCurrentDay taskWeekly = mTaskCurrentDayList.get(pos);
		if (taskWeekly != null) {
			holder.tvTitle.setText(taskWeekly.getTitle());
		}

		return convertView;
	}

	private final class ViewHolder {
		public TextView tvTitle;
	}
}
