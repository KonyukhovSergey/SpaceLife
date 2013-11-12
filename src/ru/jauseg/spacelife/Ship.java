package ru.jauseg.spacelife;

import javax.microedition.khronos.opengles.GL10;

import js.math.Basis;
import js.math.Vector3D;

public class Ship extends Basis
{
	private static final float ACSELERATION_VALUE = 200.0f;
	private static final float KEYBOARD_ROTATE_VALUE = 0.02f;

	public int fraction;

	public ObjectMCX mcx = null;

	public Vector3D velosity = new Vector3D();
	public float spr_lr, spr_ud, spr_cw;

	public float min_dist, max_dist;

	public float shield = 1.0f;

	public void timeTick(float dt)
	{
		px += velosity.x * dt;
		py += velosity.y * dt;
		pz += velosity.z * dt;

		rotate(spr_lr * dt, spr_ud * dt, spr_cw * dt);

		float szam = (float) Math.pow(0.25f, dt);

		velosity.scale(szam);

		spr_lr *= szam;
		spr_ud *= szam;
		spr_cw *= szam;
	}

	public void controlAction(float acсeleration, float strafe, float acs_lr, float acs_ud, float acs_cw, float dt)
	{
		velosity.plus(fx, fy, fz, acсeleration * dt);
		velosity.plus(rx, ry, rz, strafe * dt);

		spr_lr += acs_lr;
		spr_ud += acs_ud;
		spr_cw += acs_cw;
	}

	public void init(float x, float y, float z, ObjectMCX mcx)
	{
		moveTo(x, y, z);

		velosity.set(0, 0, 0);

		this.mcx = mcx;

		min_dist = app.rnd(10, 50);
		max_dist = app.rnd(min_dist, 20.0f);

		rotate(0.0f, 0.0f, 3.1415962f);
	}

	public void draw(GL10 gl)
	{
		gl.glPushMatrix();
		transform(gl);
		gl.glColor4f(1, 1, 1, 1);
		mcx.draw(gl);
		gl.glPopMatrix();
	}

	public void solveFlyTo(float x,float y,float z, float minDist, float maxDist, float dt)
	{
		float pf, pr, pu, plen, dist, lx, ly, lz, spd_acs, t;

		lx = px - x;
		ly = py - y;
		lz = pz - z;

		pf = lx * fx + ly * fy + lz * fz;
		pr = lx * rx + ly * ry + lz * rz;
		pu = lx * ux + ly * uy + lz * uz;

		plen = (float) (1.0f / Math.sqrt(pr * pr + pu * pu + pf * pf));

		pf *= plen;
		pr *= plen;
		pu *= plen;

		dist = (float) Math.sqrt(lx * lx + ly * ly + lz * lz);// norm(Position-vector(px,py,pz));

		if (dist < minDist || shield < 0.5f)
		{
			controlAction(pf * ACSELERATION_VALUE, 0, -pr * KEYBOARD_ROTATE_VALUE, -pu * KEYBOARD_ROTATE_VALUE, 0, dt);
			return;
		}

		if (dist > maxDist)
		{
			controlAction(-pf * ACSELERATION_VALUE, 0, pr * KEYBOARD_ROTATE_VALUE, pu * KEYBOARD_ROTATE_VALUE, 0, dt);
			return;
		}

		controlAction(0, 0, pr * KEYBOARD_ROTATE_VALUE, pu * KEYBOARD_ROTATE_VALUE, 0, dt);
	}

}
