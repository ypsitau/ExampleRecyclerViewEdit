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
	public static String KEY_POS = "POS";
	public static String KEY_FIELD_FOCUS = "FIELD_FOCUS";
	public static String KEY_LABEL = "LABEL";
	public static String KEY_PRICE = "PRICE";

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
		switch (fieldFocus) {
			case Model.FIELD_LABEL: editText_label.requestFocus(); break;
			case Model.FIELD_PRICE: editText_price.requestFocus(); break;
			default: break;
		}
		final Activity thisActivity = this;
		button_enter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt(KEY_POS, pos);
				bundle.putString(KEY_LABEL, editText_label.getText().toString());
				bundle.putInt(KEY_PRICE, Integer.parseInt(editText_price.getText().toString()));
				intent.putExtras(bundle);
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
