package io.github.ypsitau.examplerecyclerviewedit;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
	private List<String> labels;
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public View viewRoot;
		boolean isMovable;
		public TextView textView_label;
		public TextView textView_overlap;
		public ViewHolder(View viewRoot) {
			super(viewRoot);
			this.viewRoot = viewRoot;
			this.isMovable = false;
			this.textView_label = viewRoot.findViewById(R.id.textView_label);
			this.textView_overlap = viewRoot.findViewById(R.id.textView_overlap);
			textView_overlap.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					App.Printf("New Item\n");
				}
			});
		}
	}

	public ItemAdapter(List<String> labels) {
		this.labels = labels;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext())
				.inflate(R.layout.adapter_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		if (position < labels.size()) {
			holder.textView_label.setText(labels.get(position));
			holder.isMovable = true;
		} else {
			holder.textView_overlap.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public int getItemCount() {
		return labels.size() + 1;
	}
}
