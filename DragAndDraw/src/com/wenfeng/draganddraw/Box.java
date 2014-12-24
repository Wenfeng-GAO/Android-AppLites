package com.wenfeng.draganddraw;

import android.graphics.PointF;

public class Box {
	private PointF origin, current;

	public Box(PointF origin) {
		this.origin = current = origin;
	}
	
	public PointF getOrigin() {
		return origin;
	}

	public PointF getCurrent() {
		return current;
	}

	public void setCurrent(PointF current) {
		this.current = current;
	}
	
}
