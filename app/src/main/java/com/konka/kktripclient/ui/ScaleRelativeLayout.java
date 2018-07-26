package com.konka.kktripclient.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

public class ScaleRelativeLayout extends RelativeLayout implements SpringListener {

	private Spring mSpring;
	private float SPRING_MIN_VALUE = 1;
	private float SPRING_MAX_VALUE = 1.1f;

	public ScaleRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setSPRING_MAX_VALUE(float SPRING_MAX_VALUE) {
		this.SPRING_MAX_VALUE = SPRING_MAX_VALUE;
	}

	public ScaleRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScaleRelativeLayout(Context context) {
		super(context);
	}

	private void render() {
		float val = (float) mSpring.getCurrentValue();
		this.setScaleX(val);
		this.setScaleY(val);
	}

	public void scaleOut() {

		if (null == mSpring) {
			mSpring = SpringSystem.create().createSpring().addListener(this).setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100, 6));
			mSpring.setCurrentValue(SPRING_MIN_VALUE);
		}

		mSpring.setEndValue(SPRING_MAX_VALUE);

	}

	public void scaleIn() {
		if (null == mSpring) {
			mSpring = SpringSystem.create().createSpring().addListener(this).setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100, 6));
		}
		mSpring.setEndValue(SPRING_MIN_VALUE);
	}

	@Override
	public void onSpringActivate(Spring arg0) {
		this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
	}

	@Override
	public void onSpringAtRest(Spring arg0) {
		this.setLayerType(View.LAYER_TYPE_NONE, null);
	}

	@Override
	public void onSpringEndStateChange(Spring arg0) {
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		if(gainFocus){
			scaleOut();
		}
		else{
			scaleIn();
		}
	}

	@Override
	public void onSpringUpdate(Spring arg0) {
		render();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

}
