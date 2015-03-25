package com.burntout.burntout;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class StatePicker extends Spinner {
	
	@SuppressWarnings("unused")
	private String[] statesArray;
	private ArrayAdapter<CharSequence> adapter;
	public Spinner statePicker;
	public View statePickerView;
	public EditText statePickerHolder;
	public String currentItem;
	public OnItemSelectedListener listener;
	public LayoutInflater inflater;
	public Context c;
	
	public StatePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(!isInEditMode()) {
			
			c = context;
			// TODO Auto-generated constructor stub
			this.initialize();
		}
	}

	public StatePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(!isInEditMode()) {
			
			c = context;
			// TODO Auto-generated constructor stub
			this.initialize();
		}
	}
	
	@SuppressWarnings("unused")
	public StatePicker(Context context) {
		
		super(context);
		if(!isInEditMode()) {
			
			c = context;
			// TODO Auto-generated constructor stub
			this.initialize();
		}
		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public String getSelection() {
		
		return currentItem;
	}
	
	private void initialize() {
		
			//statesArray = getResources().getStringArray(R.array.states_array);
			//LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//inflater.inflate(R.layout.statepicker, this, true);
			//statePicker = (Spinner)findViewById(R.id.statepicker_spinner);
			//currentItem = statePicker.getSelectedItem().toString();
				
			
			
			adapter = ArrayAdapter.createFromResource(c, R.array.states_array, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			this.setAdapter(adapter);
			
			
			this.setOnItemSelectedListener(new StateSelectListener());
		
		
		
		
	}
	
	public class StateSelectListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			currentItem = parent.getItemAtPosition(position).toString();
			
			Log.d("selected", currentItem);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
		
	}

}
