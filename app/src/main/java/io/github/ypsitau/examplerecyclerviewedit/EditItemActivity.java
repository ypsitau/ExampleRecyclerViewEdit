package io.github.ypsitau.examplerecyclerviewedit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
	public static String KEY_POS = "pos";
	public static String KEY_FIELD_FOCUS = "field_focus";
	public static String KEY_LABEL = "label";
	public static String KEY_PRICE = "price";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		final int pos = intent.getIntExtra(KEY_POS, -1);
		int fieldFocus = intent.getIntExtra(KEY_FIELD_FOCUS, Model.FIELD_LABEL);
		setContentView(R.layout.activity_edit_item);
		final EditText editText_label = findViewById(R.id.editText_label);
		final EditText editText_price = findViewById(R.id.editText_price);
		final Button button_enter = findViewById(R.id.button_enter);
		final Button button_cancel = findViewById(R.id.button_cancel);
		editText_label.setText(intent.getStringExtra(KEY_LABEL));
		editText_price.setText(Model.formatPrice(intent.getIntExtra(KEY_PRICE, 0)));
		View viewFocus = editText_label;
		switch (fieldFocus) {
			case Model.FIELD_LABEL: viewFocus = editText_label; break;
			case Model.FIELD_PRICE: viewFocus = editText_price; break;
			default: break;
		}
		viewFocus.setFocusable(true);
		viewFocus.setFocusableInTouchMode(true);
		viewFocus.requestFocus();
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(viewFocus, InputMethodManager.SHOW_IMPLICIT);
		button_enter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(KEY_POS, pos)
						.putExtra(KEY_LABEL, editText_label.getText().toString())
						.putExtra(KEY_PRICE, Integer.parseInt(editText_price.getText().toString()));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		button_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
