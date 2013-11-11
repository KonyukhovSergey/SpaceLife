package ru.jauseg.spacelife;

import java.io.IOException;
import java.nio.Buffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import js.engine.FrameRateCalculator;
import js.engine.FrameRateCalculator.FrameRateUpdateInterface;
import net.rbgrn.android.glwallpaperservice.GLWallpaperService;
import android.content.Context;
import android.opengl.GLU;
import android.util.Log;
import android.view.MotionEvent;

public class WallpaperRenderer implements GLWallpaperService.Renderer, FrameRateUpdateInterface
{
	private static final String TAG = "WallpaperRenderer";

	private FrameRateCalculator fps;

	private ObjectMCX ship = null;

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

		/*
		 * glEnable(GL_FOG); glFogi(GL_FOG_MODE,GL_EXP);
		 * glFogfv(GL_FOG_COLOR,fog); glFogf(GL_FOG_DENSITY,0.02);
		 */

		// somelight.InitLight();
		// somelight.SetDiffuseColor(1.0f, 1.0f, 0.0f);
		// somelight.SetSpecularColor(1.0f, 1.0f, 0.0f);
		// somelight.SetProfile(1.0f, 0.05f, 0.0f);

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
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		fps.frameBegin();

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		gl.glTranslatef(0, 0, -100);
		gl.glRotatef(45, 1, 0, 0);
		gl.glColor4f(1, 1, 1, 0);
		ship.setupBuffers(gl);
		
		for (int i = 0; i < 16; i++)
		{
			gl.glRotatef(20, 0, 0, 1);
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
