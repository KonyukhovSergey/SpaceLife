package ru.jauseg.spacelife;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

import js.gles11.tools.Light;

public class WorldController
{
	private static final int STATUS_NOT_ACTIVE = 0;
	private static final int STATUS_PLAY = 1;
	private static final int STATUS_PAUSE = 2;

	private static final float LASER_SPEED = 400.0f;

	private static final float AST_DISP = 2500;
	private static final float SHIP_DISP = 1000;

	private List<Ship> ships = new ArrayList<Ship>();
	private Light light = new Light();

	public void init(GL10 gl, int count, ObjectMCX mcxShip)
	{

		while (count-- > 0)
		{
			Ship ship = new Ship();
			ship.init(app.rnd(-SHIP_DISP / 2, SHIP_DISP), app.rnd(-SHIP_DISP / 2, SHIP_DISP),
					app.rnd(-SHIP_DISP / 2, SHIP_DISP), mcxShip);
			ship.rotate(app.rnd(0, 8), app.rnd(0, 8), app.rnd(0, 8));
			ships.add(ship);
		}

		light.init(gl, 0).on();
	}

	public void draw(GL10 gl)
	{
		Ship ship0 = ships.get(0);
		//ship0.setCamera(gl);
		//light.setPosition(ship0.px, ship0.py, ship0.pz);
//GLU.gluLookAt(gl, 0, 100, 0, 0, 0, 0, 0, 0, 1);
		ship0.mcx.setupBuffers(gl);

		for (int i = 0; i < ships.size(); i++)
		{
			
			ships.get(i).draw(gl);
		}
	}

	public void timeTick(float dt)
	{
		solveCollisions(dt);

		for (Ship ship : ships)
		{
			ship.timeTick(dt);
		}

	}

	public void solveCollisions(float dt)
	{

	}

	public void controlWorld(float dt)
	{
		for (int i = 0; i < ships.size(); i++)
		{
			Ship ship = ships.get((i + 3) % ships.size());
			ships.get(i).solveFlyTo(ship.px, ship.py, ship.pz, ship.min_dist, ship.max_dist, dt);
		}
	}

}
