package ru.jauseg.spacelife;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class ActivitySetup extends Activity
{
	private static final String TAG = "ActivitySetup";

	private GLSurfaceView engine;
	private WallpaperRenderer renderer;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		engine = new GLSurfaceView(this);
		renderer = new WallpaperRenderer(this);
		engine.setRenderer(renderer);
		engine.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		setContentView(R.layout.setup);
		((ViewGroup) findViewById(R.id.layout_container)).addView(engine);
	}

	@Override
	protected void onResume()
	{
		Log.v(TAG, "onResume");
		engine.onResume();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		Log.v(TAG, "onPause");
		engine.onPause();
		super.onPause();
		finish();
	}

	@Override
	protected void onDestroy()
	{
		Log.v(TAG, "onDestroy");
		renderer.release();
		super.onDestroy();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		renderer.onTouch(ev);
		return super.dispatchTouchEvent(ev);
	}
}
