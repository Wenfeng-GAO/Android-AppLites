package com.wenfeng.runtracker;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.Menu;

public class RunActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new RunFragment();
	}

	
}
