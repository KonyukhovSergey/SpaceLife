package ru.jauseg.spacelife;

import java.io.IOException;
import java.nio.Buffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import js.engine.FrameRateCalculator;
import js.engine.ObjectMCX;
import js.engine.TextureManager;
import js.engine.FrameRateCalculator.FrameRateUpdateInterface;
import js.gles11.tools.Light;
import js.gles11.tools.Lighting;
import js.math.Basis;
import js.utils.TimeCounter;
import net.rbgrn.android.glwallpaperservice.GLWallpaperService;
import android.content.Context;
import android.opengl.GLU;
import android.util.Log;
import android.view.MotionEvent;

public class WallpaperRenderer implements GLWallpaperService.Renderer, FrameRateUpdateInterface
{
	private static final String TAG = "WallpaperRenderer";

	private FrameRateCalculator fps;

	private Light light = new Light();
	private TimeCounter timeCounter = new TimeCounter();

	private WorldController wc;

	private int textureSky = -1;

	public WallpaperRenderer(Context context)
	{
		Log.v(TAG, "construct");

		fps = new FrameRateCalculator(this);

		if (app.bitmapSphere == null)
		{
			app.bitmapSphere = TextureManager.load(context, "sky.jpg");
			try
			{
				app.mcxShip = new ObjectMCX(context.getAssets().open("ship.mcx"), 0.4f);
				app.mcxSphere = new ObjectMCX(context.getAssets().open("sky3.mcx"), 2f);
				app.wc.init(32);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		wc = app.wc;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		Log.v(TAG, "onSurfaceCreated");

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepthf(1.0f);
		gl.glDepthFunc(GL10.GL_LESS);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glShadeModel(GL10.GL_SMOOTH);

		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);

		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);

		if (textureSky < 0)
		{
			textureSky = app.tm.createTexture(gl, app.bitmapSphere, false);
		}
	}

	private Basis basis = new Basis();

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		Log.v(TAG, "onSurfaceChanged");

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

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
		gl.glLoadIdentity();

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// gl.glTranslatef(0, 0, -1500);

		wc.draw(gl);
		float dt = timeCounter.delta();

		wc.timeTick(dt);
		wc.controlWorld(dt);

		basis.px = wc.ships.get(0).px;
		basis.py = wc.ships.get(0).py;
		basis.pz = wc.ships.get(0).pz;
		basis.transform(gl);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureSky);
		app.mcxSphere.setupBuffers(gl);
		app.mcxSphere.draw(gl);
		gl.glDisable(GL10.GL_TEXTURE_2D);

		fps.frameDone();
	}

	public void offset(float offset, float yOffset)
	{

	}

	public void release()
	{
		Log.v(TAG, "release");
		// tm.deleteTexure(glForRelease, textureSky);
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
