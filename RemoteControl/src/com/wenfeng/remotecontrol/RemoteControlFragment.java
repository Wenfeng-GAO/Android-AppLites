package com.wenfeng.remotecontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RemoteControlFragment extends Fragment {
	private TextView mSelectedTextView;
	private TextView mWorkingTextView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_remote_control, container, false);
		
		mSelectedTextView = (TextView) view.findViewById(R.id.textView_selected_fragment_remote_control);
		mWorkingTextView = (TextView) view.findViewById(R.id.textView_working_fragment_remote_control);
		
		View.OnClickListener numberButtonListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				String working = mWorkingTextView.getText().toString();
				String text = textView.getText().toString();
				if (working.equals("0")) {
					mWorkingTextView.setText(text);
				} else {
					mWorkingTextView.setText(working + text);
				}
			}
		};
		
		TableLayout tableLayout = (TableLayout) view.findViewById(R.id.fragment_remote_control_tableLayout);
		int number = 1;
		for (int i = 2; i < tableLayout.getChildCount() -1; i++) {
			TableRow row = (TableRow) tableLayout.getChildAt(i);
			for (int j = 0; j < row.getChildCount(); j++) {
				Button button = (Button) row.getChildAt(j);
				button.setText("" + number++);
				button.setOnClickListener(numberButtonListener);
			}
		}
		
		TableRow bottomRow = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount() - 1);
		Button buttonDelete = (Button) bottomRow.getChildAt(0);
		buttonDelete.setText("Delete");
		buttonDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mWorkingTextView.setText("0");
			}
		});
		
		Button buttonZero = (Button) bottomRow.getChildAt(1);
		buttonZero.setText("0");
		buttonZero.setOnClickListener(numberButtonListener);
		
		Button buttonEnter = (Button) bottomRow.getChildAt(2);
		buttonEnter.setText("Enter");
		buttonEnter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CharSequence working = mWorkingTextView.getText();
				if (working.length() > 0) {
					mSelectedTextView.setText(working);
					mWorkingTextView.setText("0");
				}
			}
		});
		return view;
	}
	
}
