package io.github.ypsitau.examplerecyclerviewedit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	List<String> labels = new ArrayList<String>();

	RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		for (int i = 0; i < 5; i++) {
			labels.add(String.format("item #%d", i));
		}
		setContentView(R.layout.activity_main);
		recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		final Context context = this;
		recyclerView.setAdapter(new ItemAdapter(labels, new ItemAdapter.Listener() {
			@Override
			public void onNewItem(View viewLast) {
				Intent intent = new Intent(context, NewItemActivity.class);
				intent.putExtra(NewItemActivity.KEY_LABEL, String.format("item #%d", labels.size()));
				startActivityForResult(intent, 123);
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
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (resultCode == RESULT_OK) {
				String label = bundle.getString(NewItemActivity.KEY_LABEL);
				((ItemAdapter)recyclerView.getAdapter()).doAddItem(label);
				recyclerView.scrollToPosition(labels.size());
			} else if (resultCode == RESULT_CANCELED) {
				// nothing to do
			}
		}
	}
}
