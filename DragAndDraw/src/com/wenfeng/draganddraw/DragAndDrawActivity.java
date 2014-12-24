package com.wenfeng.draganddraw;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.Menu;

public class DragAndDrawActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new DragAndDrawFragment();
	}

	

}
