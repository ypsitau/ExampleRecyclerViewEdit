package io.github.ypsitau.examplerecyclerviewedit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	RecyclerView recyclerView;

	final int REQCODE_EDITITEM = 1;
	final int REQCODE_NEWITEM = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		final Activity activity = this;
		recyclerView.setAdapter(new ItemAdapter(new ItemAdapter.Listener() {
			@Override
			public void onEditItem(int pos, int field) {
				Intent intent = new Intent(activity, EditItemActivity.class);
				intent.putExtra(EditItemActivity.KEY_POS, pos)
						.putExtra(EditItemActivity.KEY_FIELD_FOCUS, field)
						.putExtra(EditItemActivity.KEY_LABEL, Model.getInstance().getItem(pos).getLabel())
						.putExtra(EditItemActivity.KEY_PRICE, Model.getInstance().getItem(pos).getPrice());
				startActivityForResult(intent, REQCODE_EDITITEM);
			}
			@Override
			public void onNewItem() {
				Intent intent = new Intent(activity, EditItemActivity.class);
				int pos = Model.getInstance().getItemCount();
				intent.putExtra(EditItemActivity.KEY_POS, pos)
						.putExtra(EditItemActivity.KEY_LABEL, String.format("Item #%d", pos))
						.putExtra(EditItemActivity.KEY_PRICE, 0);
				startActivityForResult(intent, REQCODE_NEWITEM);
			}
		}));
		final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
				new ItemTouchHelper.SimpleCallback(
						ItemTouchHelper.UP | ItemTouchHelper.DOWN,
						ItemTouchHelper.RIGHT) {
					@Override
					public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
						if (!((ItemAdapter.ViewHolder)viewHolder).isMovable) return 0;
						return super.getMovementFlags(recyclerView, viewHolder);
					}

					@Override
					public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
						super.onSelectedChanged(viewHolder, actionState);
						if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
							((ItemAdapter.ViewHolder)viewHolder).viewRoot.
									setBackgroundColor(Color.rgb(255, 255, 192));
						}
					}

					@Override
					public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
						super.clearView(recyclerView, viewHolder);
						((ItemAdapter.ViewHolder)viewHolder).viewRoot.setBackgroundColor(Color.WHITE);
					}

					@Override
					public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolderCurrent, RecyclerView.ViewHolder viewHolderTgt) {
						if (!((ItemAdapter.ViewHolder)viewHolderTgt).isMovable) return false;
						return super.canDropOver(recyclerView, viewHolderCurrent, viewHolderTgt);
					}

					public boolean onMove(RecyclerView recyclerView,
										  RecyclerView.ViewHolder viewHolder,
										  RecyclerView.ViewHolder viewHolderTgt) {
						final int fromPos = viewHolder.getAdapterPosition();
						final int toPos = viewHolderTgt.getAdapterPosition();
						App.Printf("onMove %d->%d\n", fromPos, toPos);
						((ItemAdapter)recyclerView.getAdapter()).doMoveItem(toPos, fromPos);
						return true; // true if moved, false otherwise
					}

					public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
						final int fromPos = viewHolder.getAdapterPosition();
						((ItemAdapter)recyclerView.getAdapter()).doRemoveItem(fromPos);
					}

				});
		itemTouchHelper.attachToRecyclerView(recyclerView);
		App.setLogEditText((EditText)findViewById(R.id.editText_log), true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (intent == null) return;
		if (resultCode == RESULT_OK) {
			int pos = intent.getIntExtra(EditItemActivity.KEY_POS, 0);
			String label = intent.getStringExtra(EditItemActivity.KEY_LABEL);
			int price = intent.getIntExtra(EditItemActivity.KEY_PRICE, 0);
			if (requestCode == REQCODE_NEWITEM) {
				((ItemAdapter)recyclerView.getAdapter()).doAddItem(label, price);
				recyclerView.scrollToPosition(Model.getInstance().getItemCount());
			} else if (requestCode == REQCODE_EDITITEM) {
				((ItemAdapter) recyclerView.getAdapter()).doChangeItem(pos, label, price);
			}
		} else if (resultCode == RESULT_CANCELED) {
			App.Printf("canceled\n");
			// nothing to do
		}
	}
}
