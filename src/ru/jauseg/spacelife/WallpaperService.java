package ru.jauseg.spacelife;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;
import android.view.MotionEvent;

public class WallpaperService extends GLWallpaperService
{
	public WallpaperService()
	{
		super();
	}

	@Override
	public Engine onCreateEngine()
	{
		return new SnowWallpaperEngine();
	}

	class SnowWallpaperEngine extends GLEngine
	{
		WallpaperRenderer renderer;

		public SnowWallpaperEngine()
		{
			super();
			renderer = new WallpaperRenderer(WallpaperService.this);
			setRenderer(renderer);
			setRenderMode(RENDERMODE_CONTINUOUSLY);
		}

		@Override
		public void onDestroy()
		{
			super.onDestroy();
			if (renderer != null)
			{
				renderer.release();
			}
			renderer = null;
		}

		@Override
		public void onTouchEvent(MotionEvent event)
		{
			renderer.onTouch(event);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep,
				int xPixelOffset, int yPixelOffset)
		{
			// Log.v("Wallpaper1Service", String.format("%f %f %f %f %d %d",
			// xOffset, yOffset, xOffsetStep, yOffsetStep,
			// xPixelOffset, yPixelOffset));
			renderer.offset(xOffset, yOffset);
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
		}
	}
}
