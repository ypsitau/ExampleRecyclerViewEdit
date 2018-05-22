package io.github.ypsitau.examplerecyclerviewedit;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
	public abstract static class Listener {
		abstract public void onEditItem(int pos, int field);
		abstract public void onNewItem();
	};

	public class ViewHolder extends RecyclerView.ViewHolder {
		public View viewRoot;
		boolean isMovable;
		public TextView textView_label;
		public TextView textView_price;
		public TextView textView_last;
		public ViewHolder(View viewRoot) {
			super(viewRoot);
			this.viewRoot = viewRoot;
			this.isMovable = false;
			textView_label = viewRoot.findViewById(R.id.textView_label);
			textView_label.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					listener.onEditItem(getAdapterPosition(), Model.FIELD_LABEL);
				}
			});
			textView_price = viewRoot.findViewById(R.id.textView_price);
			textView_price.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					listener.onEditItem(getAdapterPosition(), Model.FIELD_PRICE);
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
			holder.textView_label.setText(Model.getInstance().getItem(position).getLabel());
			holder.textView_price.setText(Model.formatPrice(Model.getInstance().getItem(position).getPrice()));
			holder.textView_label.setVisibility(View.VISIBLE);
			holder.textView_price.setVisibility(View.VISIBLE);
			holder.textView_last.setVisibility(View.GONE);
			holder.isMovable = true;
		} else {
			holder.textView_label.setVisibility(View.GONE);
			holder.textView_price.setVisibility(View.GONE);
			holder.textView_last.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public int getItemCount() {
		return Model.getInstance().getItemCount() + 1;
	}

	public void doAddItem(String label, int price) {
		Model.getInstance().addItem(label, price);
		notifyItemRangeChanged(Model.getInstance().getItemCount() - 1, 2);
	}

	public void doRemoveItem(int fromPos) {
		Model.getInstance().removeItem(fromPos);
		notifyItemRemoved(fromPos);
	}

	public void doMoveItem(int toPos, int fromPos) {
		Model.getInstance().moveItem(toPos, fromPos);
		notifyItemMoved(fromPos, toPos);
	}

	public void doChangeItem(int pos, String label, int price) {
		Model.getInstance().getItem(pos).setValues(label, price);
		notifyItemChanged(pos);
	}
}
