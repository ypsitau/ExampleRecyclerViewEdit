package io.github.ypsitau.examplerecyclerviewedit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewItemActivity extends AppCompatActivity {
	public static String KEY_LABEL = "LABEL";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_new_item);
		final EditText editText = findViewById(R.id.editText);
		final Button button_Enter = findViewById(R.id.button_Enter);
		editText.setText(intent.getStringExtra(KEY_LABEL));
		final Activity thisActivity = this;
		button_Enter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString(KEY_LABEL, editText.getText().toString());
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}
