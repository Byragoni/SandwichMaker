package com.android.sandwichmaker;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sandwichmaker.data.InfoFromData;
import com.android.sandwichmaker.model.Action;
import com.android.sandwichmaker.model.Moment;
import com.android.sandwichmaker.provider.SandwichMakerDBHelper;
import com.android.sandwichmaker.provider.SandwichMakerDBHelper.Columns;
import com.android.sandwichmaker.ui.NotifyService;
import com.android.sandwichmaker.ui.SuggestionListView;
import com.android.sandwichmaker.ui.SuggestionsAdapter;

public class MainActivity extends Activity implements OnClickListener, TextWatcher {

	protected static final String DEBUG_TAG = "PHI";
	private static Context mContext;
	private EditText mTask;
	private SuggestionListView lw;
	private SuggestionsAdapter adapter;
	RelativeLayout ratingButtons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.gravity = Gravity.BOTTOM | Gravity.CENTER;
		lp.dimAmount = 0;
		lp.flags = LayoutParams.FLAG_LAYOUT_NO_LIMITS
				| LayoutParams.FLAG_NOT_TOUCH_MODAL;

		RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.activity_main, null);
		ratingButtons = (RelativeLayout)rl.findViewById(R.id.ratingbuttons);
		setContentView(rl, lp);

		Point size = new Point();
		WindowManager w = getWindowManager();
		w.getDefaultDisplay().getSize(size);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = size.x + 20;

		this.getWindow().setAttributes(params);

		mContext = this;
		mTask = (EditText) findViewById(R.id.task);
       
	    mTask.addTextChangedListener(this);
		
		lw = (SuggestionListView) findViewById(R.id.suggestionlist);
		lw.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView actionName = (TextView)view.findViewById(R.id.suggestiontext);
                lw.setVisibility(View.GONE);
                mTask.setText(actionName.getText().toString());
               showSuggestions(false);
                Log.v(DEBUG_TAG, actionName.getText().toString());
            }
        });
		adapter = new SuggestionsAdapter(this);
		
		adapter.notifyDataSetChanged();

		lw.setAdapter(adapter);
		findViewById(R.id.love).setOnClickListener(this);
		findViewById(R.id.like).setOnClickListener(this);
		findViewById(R.id.dislike).setOnClickListener(this);
		findViewById(R.id.addbutton).setOnClickListener(this);

		

		Utils.startAlarm(mContext);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
        default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onClick(View v) {

		if (isEmptyTask()) {
			return;
		}

		int rating = 0;
		String enteredTask = mTask.getText().toString().trim();

		switch (v.getId()) {
		case R.id.addbutton:
			insertAction(new Action(enteredTask));
            Toast toast = Toast.makeText(getApplicationContext(), "New action added to list",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			adapter.dataChanged();
			return;
		case R.id.love:
			rating = 10;
			insertMoment(new Moment(enteredTask, rating));
			this.finish();
			break;
		case R.id.like:
			rating = 5;
			insertMoment(new Moment(enteredTask, rating));
			this.finish();
			break;
		case R.id.dislike:
			rating = 0;
			insertMoment(new Moment(enteredTask, rating));
			this.finish();
			break;

		}

	}

	private void insertMoment(Moment moment) {


		ContentResolver cr = getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Columns.COLUMN_MOMENT_DESC, moment.getDescription());
		values.put(Columns.COLUMN_MOMENT_RATING, moment.getRating());
		values.put(Columns.COLUMN_MOMENT_TIME, moment.getDate());
		values.put(Columns.COLUMN_MOMENT_LOCATION, moment.getLocation());
		cr.insert(SandwichMakerDBHelper.PHI_DATA_MOMENTS_CONTENT_URI, values);

       /* Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                InfoFromData ifd = new InfoFromData(mContext);
                ifd.updateLocationInfo();
            }
        },2000);*/

	}

	private void insertAction(Action action) {
		ContentResolver cr = getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Columns.COLUMN_ACTION_DESC, action.getDesc());
		cr.insert(SandwichMakerDBHelper.PHI_DATA_ACTIONS_CONTENT_URI, values);
	}

	private boolean isEmptyTask() {
		String enteredTask = mTask.getText().toString().trim();
		if (enteredTask.isEmpty()) {
			String message = this.getResources().getString(
					R.string.empty_message);
			Toast toast = Toast.makeText(getApplicationContext(), message,
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return true;
		}
		return false;
	}

	public static Context getInstance() {
		return mContext;

	}

	

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence text, int start, int before, int count) {
		// TODO Auto-generated method stub
		if(!text.toString().isEmpty())
			showSuggestions(true);
		else 
			showSuggestions(false);
		adapter.setSearchableData(text);
		adapter.notifyDataSetChanged();
		Log.v("Action field", text.toString());
	}

	private void showSuggestions(boolean show) {
		if(show){
			lw.setVisibility(View.VISIBLE);
			ratingButtons.setVisibility(View.GONE);
		} else{
			lw.setVisibility(View.GONE);
			ratingButtons.setVisibility(View.VISIBLE);
		}
	}
}
