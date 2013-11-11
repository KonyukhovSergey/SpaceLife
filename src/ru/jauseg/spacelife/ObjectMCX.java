package ru.jauseg.spacelife;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import js.engine.BufferAllocator;

public class ObjectMCX {
	private FloatBuffer points = null;
	private FloatBuffer normals = null;
	private FloatBuffer textures = null;
	private ByteBuffer indexes = null;

	public ObjectMCX(InputStream stream) {
		try {
			byte[] data = new byte[stream.available()];
			stream.read(data);
			ByteBuffer bb = ByteBuffer.wrap(data);
			bb.order(ByteOrder.LITTLE_ENDIAN);
			initFrom(bb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initFrom(ByteBuffer buffer) {
		int header_id = buffer.getInt();

		int vnum = buffer.getInt();
		int fnum = buffer.getInt();

		int texture = buffer.getInt();

		points = BufferAllocator.createFloatBuffer(vnum * 3);
		normals = BufferAllocator.createFloatBuffer(vnum * 3);

		if (texture != 0) {
			textures = BufferAllocator.createFloatBuffer(vnum * 2);
		}

		for (int i = 0; i < vnum; i++) {
			points.put(buffer.getFloat());
			points.put(buffer.getFloat());
			points.put(buffer.getFloat());
			normals.put(buffer.getFloat());
			normals.put(buffer.getFloat());
			normals.put(buffer.getFloat());

			if (texture != 0) {
				textures.put(buffer.getFloat());
				textures.put(buffer.getFloat());
			}
		}

		indexes = BufferAllocator.createBuffer(fnum * 2 * 3);

		for (int i = 0; i < fnum; i++) {
			indexes.putShort(buffer.getShort());
			indexes.putShort(buffer.getShort());
			indexes.putShort(buffer.getShort());
		}

		points.position(0);
		normals.position(0);
		if (textures != null) {
			textures.position(0);
		}
		indexes.position(0);
	}

	public void setupBuffers(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, points);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normals);
		if (textures != null) {
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glTexCoordPointer(2, GL10.GL_TEXTURE_COORD_ARRAY, 0, textures);
		}
	}

	public void draw(GL10 gl) {
		gl.glDrawElements(GL10.GL_TRIANGLES, indexes.capacity() >> 1,
				GL10.GL_UNSIGNED_SHORT, indexes);
	}
}
