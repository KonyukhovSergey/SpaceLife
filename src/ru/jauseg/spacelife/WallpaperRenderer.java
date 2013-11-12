package ru.jauseg.spacelife;

import java.io.IOException;
import java.nio.Buffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import js.engine.FrameRateCalculator;
import js.engine.FrameRateCalculator.FrameRateUpdateInterface;
import js.gles11.tools.Light;
import js.gles11.tools.Lighting;
import js.utils.TimeCounter;
import net.rbgrn.android.glwallpaperservice.GLWallpaperService;
import android.content.Context;
import android.graphics.LightingColorFilter;
import android.opengl.GLU;
import android.util.Log;
import android.view.MotionEvent;

public class WallpaperRenderer implements GLWallpaperService.Renderer, FrameRateUpdateInterface
{
	private static final String TAG = "WallpaperRenderer";

	private FrameRateCalculator fps;

	private ObjectMCX ship = null;
	private Light light = new Light();
	private TimeCounter timeCounter = new TimeCounter();

	public WallpaperRenderer(Context context)
	{
		fps = new FrameRateCalculator(this);

		try
		{
			ship = new ObjectMCX(context.getAssets().open("ship.mcx"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		Log.v(TAG, "onSurfaceCreated");

		float[] cl0 = new float[] { 0.75f, 0.75f, 0.75f, 1.0f };
		float[] fog = new float[] { 0.0f, 0.5f, 0.75f, 1.0f };

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepthf(1.0f);
		gl.glDepthFunc(GL10.GL_LESS);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glShadeModel(GL10.GL_SMOOTH);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);

		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		// gl.glColorMaterial(GL10.GL_FRONT, GL10.GL_SPECULAR);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		Log.v(TAG, "onSurfaceChanged");

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 2.0f, 2000.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		Lighting.on(gl);
		Lighting.setModelAmbient(gl, 0.1f, 0.1f, 0.1f, 1);
		light.init(gl, 0).setPosition(100, 100, 100).on();
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		fps.frameBegin();

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		gl.glTranslatef(0, 0, -100);
		gl.glRotatef(timeCounter.elapsed() * 50.0f, 1, 0, 0);
		gl.glColor4f(1, 1, 1, 0);
		ship.setupBuffers(gl);

		// for (int i = 0; i < 16; i++)
		{
			// gl.glRotatef(20, 0, 0, 1);
			ship.draw(gl);
		}
		gl.glPopMatrix();

		fps.frameDone();
	}

	public void offset(float offset, float yOffset)
	{

	}

	public synchronized void release()
	{
		Log.v(TAG, "release");
	}

	public boolean onTouch(MotionEvent event)
	{
		return false;
	}

	@Override
	public void onFrameRateUpdate(FrameRateCalculator frameRateCalculator)
	{
		Log.v(TAG, frameRateCalculator.frameString());
	}

	Buffer pixels = null;

}
