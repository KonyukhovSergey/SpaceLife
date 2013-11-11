package ru.jauseg.spacelife;

import java.io.IOException;
import java.nio.Buffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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

		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_CULL_FACE);

		gl.glDisable(GL10.GL_DITHER);

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

		gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glClearColor(0, 0, 0, 0);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		Log.v(TAG, "onSurfaceChanged");

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glDepthRangef(-100, 100);
		//gl.glDisable(gl.gl_cu)

		gl.glViewport(0, 0, width, height);
		GLU.gluOrtho2D(gl, 0, width, height, 0);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		fps.frameBegin();

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity();
		//gl.glTranslatef(0, 0, -50);

		gl.glColor4f(1, 1, 1, 0);
		ship.setupBuffers(gl);
		ship.draw(gl);

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
