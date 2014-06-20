package org.tang.exam.adapter;

import java.util.ArrayList;

import org.tang.exam.R;
import org.tang.exam.base.MyBaseAdatper;
import org.tang.exam.entity.TaskWeekly;
import org.tang.exam.utils.DateTimeUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TaskWeeklyListAdapter extends MyBaseAdatper {
	private LayoutInflater mInflater;
	private ArrayList<TaskWeekly> mTaskWeeklyList = new ArrayList<TaskWeekly>();

	public TaskWeeklyListAdapter(Context context, ArrayList<TaskWeekly> taskWeeklyList) {
		super();
		mTaskWeeklyList = taskWeeklyList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mTaskWeeklyList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mTaskWeeklyList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_taskweekly, null);

			holder = new ViewHolder();
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_taskweekly_create_time);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_taskweekly_title);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_taskweekly_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		TaskWeekly taskWeekly = mTaskWeeklyList.get(pos);
		if (taskWeekly != null) {
			holder.tvTime.setText(DateTimeUtil.toStandardTime(taskWeekly.getCreateTime()));
			holder.tvTitle.setText(taskWeekly.getTitle());
			holder.tvContent.setText(taskWeekly.getContent());
		}

		return convertView;
	}

	private final class ViewHolder {
		public TextView tvTime;
		public TextView tvTitle;
		public TextView tvContent;
	}
}
