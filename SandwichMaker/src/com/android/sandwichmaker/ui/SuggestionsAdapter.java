package com.android.sandwichmaker.ui;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.sandwichmaker.MainActivity;
import com.android.sandwichmaker.R;
import com.android.sandwichmaker.model.Action;
import com.android.sandwichmaker.provider.SandwichMakerDBHelper;
import com.android.sandwichmaker.provider.SandwichMakerDBHelper.Columns;

public class SuggestionsAdapter extends BaseAdapter {

	private Context mContext;
	ArrayList<Action> relevantActions; 
	ArrayList<Action> allActions;
	
	





	public SuggestionsAdapter(Context context) {
		super();
		this.mContext = context;
	    loadAllActions();
		
		
	}

	

	
	
	
	
	private void loadAllActions() {
		// TODO Auto-generated method stub
		ContentResolver cr = mContext.getContentResolver();
		relevantActions = new ArrayList<Action>();
		allActions = new ArrayList<Action>();
		
		Cursor cursor = cr.query(SandwichMakerDBHelper.PHI_DATA_ACTIONS_CONTENT_URI, null,
				null , null, null, null);
		
		while(cursor.moveToNext()){
			
			int idIndex = cursor.getColumnIndex(Columns.COLUMN_ACTION_ID);
			int desIndex = cursor.getColumnIndex(Columns.COLUMN_ACTION_DESC);
			
			int id = cursor.getInt(idIndex);
			String description = cursor.getString(desIndex);
			allActions.add(new Action(id,description));
		}
	}







	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return relevantActions.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflator = (LayoutInflater)MainActivity.getInstance().getSystemService(MainActivity.getInstance().LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.suggestion_item, null);
		TextView actionName = (TextView)v.findViewById(R.id.suggestiontext);
		actionName.setText(relevantActions.get(position).getDesc());
		return v;
	}

	public void setSearchableData(CharSequence text) {
		
		relevantActions.clear();
		for(Action action: allActions){
			if(action.getDesc().contains(text))
				relevantActions.add(action);
		}
		
		
	}







	public void dataChanged() {
		loadAllActions();
	}
	
	

}
