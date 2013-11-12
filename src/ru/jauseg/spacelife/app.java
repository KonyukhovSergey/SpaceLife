package ru.jauseg.spacelife;

import java.util.Random;

import android.app.Application;
import android.os.SystemClock;

public class app extends Application
{
	private final static Random rnd = new Random(SystemClock.elapsedRealtime());

	public final static float rnd()
	{
		return rnd.nextFloat();
	}

	public final static float rnd(float from, float size)
	{
		return size * rnd.nextFloat() + from;
	}
}
