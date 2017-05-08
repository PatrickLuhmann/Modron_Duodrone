package com.patrickluhmann.plobjectslibrary;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class PLObjectTest {
	@Test
	public void adjustVelocity() {
		PLObject obj = PLObject.builder(10, 10).posX(100).posY(200).velX(0).velY(0).build();
		float[] vel = new float[2];

		obj.adjustVelocity(1.5f, 2.5f);
		obj.getVelocity(vel);
		assertEquals(1.5f, vel[0], 0.0d);
		assertEquals(2.5f, vel[1], 0.0d);

		obj.adjustVelocity(-11.5f, -12.5f);
		obj.getVelocity(vel);
		assertEquals(-10.0f, vel[0], 0.0d);
		assertEquals(-10.0f, vel[1], 0.0d);	}

	@Test
	public void construction() throws Exception {
		assertEquals("PLObject", PLObject.getClassName());

		int[] actInts = new int[2];
		float[] actFloats = new float[2];

		// Don't specify any optional values
		PLObject obj0 = PLObject.builder(1, 2).build();

		obj0.getPosition(actFloats);
		assertEquals(0, actFloats[0], 0.0d);
		assertEquals(0, actFloats[1], 0.0d);

		obj0.getVelocity(actFloats);
		assertEquals(0, actFloats[0], 0.0d);
		assertEquals(0, actFloats[1], 0.0d);

		obj0.getSize(actInts);
		assertEquals(1, actInts[0]);
		assertEquals(2, actInts[1]);

		// Specify all values
		Bitmap testBitmap = mock(Bitmap.class);
		PLObject obj1 = PLObject.builder(123, 456)
			.posX(1).posY(2)
			.skin(testBitmap)
			.velX(3).velY(4)
			.build();

		obj1.getPosition(actFloats);
		assertEquals(1, actFloats[0], 0.0d);
		assertEquals(2, actFloats[1], 0.0d);

		obj1.getVelocity(actFloats);
		assertEquals(3, actFloats[0], 0.0d);
		assertEquals(4, actFloats[1], 0.0d);

		obj1.getSize(actInts);
		assertEquals(123, actInts[0]);
		assertEquals(456, actInts[1]);
	}
}