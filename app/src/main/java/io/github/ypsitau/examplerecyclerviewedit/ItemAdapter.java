package io.github.ypsitau.examplerecyclerviewedit;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
	public abstract static class Listener {
		abstract public void onEditItem(int pos);
		abstract public void onNewItem();
	};

	public class ViewHolder extends RecyclerView.ViewHolder {
		public View viewRoot;
		boolean isMovable;
		public TextView textView_label;
		public TextView textView_last;
		public ViewHolder(View viewRoot) {
			super(viewRoot);
			this.viewRoot = viewRoot;
			this.isMovable = false;
			textView_label = viewRoot.findViewById(R.id.textView_label);
			textView_label.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					listener.onEditItem(getAdapterPosition());
				}
			});
			textView_last = viewRoot.findViewById(R.id.textView_last);
			textView_last.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					listener.onNewItem();
				}
			});
		}
	}

	//private List<String> labels;
	private Listener listener;

	public ItemAdapter(Listener listener) {
		this.listener = listener;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext())
				.inflate(R.layout.adapter_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		App.Printf("onBindViewHolder(%d)\n", position);
		if (position < Model.getInstance().getItemCount()) {
			holder.textView_label.setText(Model.getInstance().getItem(position).label);
			holder.textView_label.setVisibility(View.VISIBLE);
			holder.textView_last.setVisibility(View.GONE);
			holder.isMovable = true;
		} else {
			holder.textView_last.setVisibility(View.VISIBLE);
			holder.textView_label.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		return Model.getInstance().getItemCount() + 1;
	}

	public void doAddItem(String label) {
		Model.getInstance().addItem(label);
		notifyItemRangeChanged(Model.getInstance().getItemCount() - 1, 2);
	}

	public void doRemoveItem(int fromPos) {
		//labels.remove(fromPos);
		Model.getInstance().removeItem(fromPos);
		notifyItemRemoved(fromPos);
	}

	public void doMoveItem(int toPos, int fromPos) {
		//labels.add(toPos, labels.remove(fromPos));
		Model.getInstance().moveItem(toPos, fromPos);
		notifyItemMoved(fromPos, toPos);
	}

	public void doChangeItem(int pos, String label) {
		//labels.set(pos, label);
		Model.getInstance().getItem(pos).label = label;
		notifyItemChanged(pos);
	}
}
