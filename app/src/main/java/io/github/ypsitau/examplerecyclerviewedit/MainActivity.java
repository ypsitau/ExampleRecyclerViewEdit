package io.github.ypsitau.examplerecyclerviewedit;

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

	List<String> labels = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		for (int i = 0; i < 5; i++) {
			labels.add(String.format("item #%d", i));
		}
		setContentView(R.layout.activity_main);
		final RecyclerView recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		recyclerView.setAdapter(new ItemAdapter(labels));
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
						labels.add(toPos, labels.remove(fromPos));
						recyclerView.getAdapter().notifyItemMoved(fromPos, toPos);
						return true; // true if moved, false otherwise
					}

					public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
						final int fromPos = viewHolder.getAdapterPosition();
						labels.remove(fromPos);
						recyclerView.getAdapter().notifyItemRemoved(fromPos);
					}

				});
		itemTouchHelper.attachToRecyclerView(recyclerView);
		App.setLogEditText((EditText)findViewById(R.id.editText_log), true);
	}
}
