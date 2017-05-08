package com.patrickluhmann.plobjectslibrary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PLObjectInstrTest {
	@Test
	public void willCollide_NoBecauseNotMoving() {
		PLObject objMoving = PLObject.builder(10, 10)
			.posX(100).posY(100)
			.velX(0).velY(0)
			.build();
		PLObject objTarget = PLObject.builder(1000, 10)
			.posX(0).posY(90)
			.build();
		float deltaT = 1;

		assertEquals(-1.0f, objMoving.willCollide(objTarget, deltaT), 0.0001f);
	}

	@Test
	public void willCollide_YesOneTickOnlyNegVertical() {
		PLObject objMoving = PLObject.builder(10, 10)
			.posX(100).posY(100)
			.velX(0).velY(-1)
			.build();
		PLObject objTarget = PLObject.builder(1000, 10)
			.posX(0).posY(90)
			.build();
		float deltaT = 1;

		// obj will only move 1 px, but that is all it needs to hit target.
		assertEquals(0.0f, objMoving.willCollide(objTarget, deltaT), 0.00001f);
	}

	@Test
	public void willCollide_YesAlreadyOverlap() {
		PLObject objMoving = PLObject.builder(10, 10)
			.posX(0).posY(0)
			.velX(54321).velY(12345)
			.build();
		PLObject objTarget = PLObject.builder(10, 10)
			.posX(9).posY(9)
			.velX(-987).velY(486)
			.build();
		float deltaT = 1;

		assertEquals(-2.0f, objMoving.willCollide(objTarget, deltaT), 0.0001f);
	}

	@Test
	public void willCollide_YesOneTickOnlyVertical() {
		PLObject objMoving = PLObject.builder(10, 10)
			.posX(0).posY(0)
			.velX(0).velY(1)
			.build();
		PLObject objTarget = PLObject.builder(1000, 10)
			.posX(0).posY(10)
			.build();
		float deltaT = 1;

		// obj will only move 1 px, but that is all it needs to hit target.

		assertEquals(0.0f, objMoving.willCollide(objTarget, deltaT), 0.00001f);
	}

	@Test
	public void willCollide_YesBothMoving() {
		PLObject obj1 = PLObject.builder(10, 10)
			.posX(0).posY(1000)
			.velX(0).velY(-500)
			.build();
		PLObject obj2 = PLObject.builder(10, 10)
			.posX(0).posY(0)
			.velX(0).velY(500)
			.build();
		float deltaT = 1;

		assertEquals(0.99f, obj1.willCollide(obj2, deltaT), 0.00001f);
	}

	@Test
	public void willCollide_YesSimpleVertical() {
		PLObject objMoving = PLObject.builder(10, 10)
			.posX(0).posY(0)
			.velX(0).velY(1000)
			.build();
		PLObject objTarget = PLObject.builder(1000, 10)
			.posX(0).posY(200)
			.build();
		float deltaT = 1; // thus obj moves 1000 down

		// After moving 191 px, the bottom of objMoving will collide with
		// the top of objTarget, so the instant before is at 190 px.
		assertEquals(0.190f, objMoving.willCollide(objTarget, deltaT), 0.0001f);
	}

	@Test
	public void willCollide_YesNearHitVertical() {
		PLObject objMoving = PLObject.builder(10, 10)
			.posX(0).posY(0)
			.velX(0).velY(1000)
			.build();
		PLObject objTarget = PLObject.builder(1000, 10)
			.posX(0).posY(1009)
			.build();
		float deltaT = 1; // thus obj moves 1000 down

		assertEquals(0.999f, objMoving.willCollide(objTarget, deltaT), 0.00001f);
	}

	@Test
	public void willCollide_YesSimpleHorizontal() {
		PLObject objMoving = PLObject.builder(10, 10)
			.posX(0).posY(0)
			.velX(1000).velY(0)
			.build();
		PLObject objTarget = PLObject.builder(10, 1000)
			.posX(200).posY(0)
			.build();
		float deltaT = 1; // thus obj moves 1000 right

		assertEquals(0.190, objMoving.willCollide(objTarget, deltaT), 0.0001f);
	}

	@Test
	public void willCollide_YesNearHitHorizontal() {
		PLObject objMoving = PLObject.builder(10, 10)
			.posX(0).posY(0)
			.velX(1000).velY(0)
			.build();
		PLObject objTarget = PLObject.builder(10, 1000)
			.posX(1009).posY(0)
			.build();
		float deltaT = 1; // thus obj moves 1000 right

		assertEquals(0.999f, objMoving.willCollide(objTarget, deltaT), 0.00001f);
	}

	@Test
	public void willCollide_NoBothMoving() {
		PLObject obj1 = PLObject.builder(10, 10)
			.posX(0).posY(1000)
			.velX(0).velY(-500)
			.build();
		PLObject obj2 = PLObject.builder(10, 10)
			.posX(0).posY(0)
			.velX(0).velY(490)
			.build();
		float deltaT = 1;

		assertEquals(-1, obj1.willCollide(obj2, deltaT), 0.0f);
	}

	@Test
	public void willCollide_NoSimpleVertical() {
		PLObject objMoving = PLObject.builder(10, 10)
			.posX(0).posY(0)
			.velX(0).velY(1000)
			.build();
		PLObject objTarget = PLObject.builder(1000, 10)
			.posX(0).posY(200)
			.build();
		float deltaT = 0.1f; // thus obj moves 100 down

		assertEquals(-1, objMoving.willCollide(objTarget, deltaT), 0.0f);
	}

	@Test
	public void willCollide_NoNearMissVertical() {
		PLObject objMoving = PLObject.builder(10, 10)
			.posX(0).posY(0)
			.velX(0).velY(1000)
			.build();
		PLObject objTarget = PLObject.builder(1000, 10)
			.posX(0).posY(110)
			.build();
		float deltaT = 0.1f; // thus obj moves 100 down

		assertEquals(-1, objMoving.willCollide(objTarget, deltaT), 0.0f);
	}

	@Test
	public void willCollide_NoSimpleHorizontal() {
		PLObject objMoving = PLObject.builder(10, 10)
			.posX(0).posY(0)
			.velX(1000).velY(0)
			.build();
		PLObject objTarget = PLObject.builder(10, 1000)
			.posX(200).posY(0)
			.build();
		float deltaT = 0.1f; // thus obj moves 100 right

		assertEquals(-1, objMoving.willCollide(objTarget, deltaT), 0.0f);
	}

	@Test
	public void willCollide_NoNearMissHorizontal() {
		PLObject objMoving = PLObject.builder(10, 10)
			.posX(0).posY(0)
			.velX(1000).velY(0)
			.build();
		PLObject objTarget = PLObject.builder(10, 1000)
			.posX(110).posY(0)
			.build();
		float deltaT = 0.1f; // thus obj moves 100 right

		assertEquals(-1, objMoving.willCollide(objTarget, deltaT), 0.0f);
	}

	// This test makes sure that the object at the new position does
	// not overlap the object at the old location.
	@Test
	public void updatePosition_BigMove() {
		PLObject obj = PLObject.builder(10, 10)
			.posX(100).posY(200)
			.velX(10).velY(100)
			.build();
		float deltaT = 5.6f;
		float[] actFloats = new float[2];

		obj.updatePosition(deltaT);
		obj.getPosition(actFloats);
		float newX = 100 + deltaT * 10;
		float newY = 200 + deltaT * 100;
		assertEquals(newX, actFloats[0], 0.0d);
		assertEquals(newY, actFloats[1], 0.0d);
		assertEquals(true, obj.contains(newX, newY));
		assertEquals(false, obj.contains(100, 200));
	}

	@Test
	public void updatePosition_NoVelocity() {
		PLObject obj = PLObject.builder(10, 10).posX(100).posY(200).velX(0).velY(0).build();
		float deltaT = 0.01f;
		float[] actFloats = new float[2];

		obj.updatePosition(deltaT);
		obj.getPosition(actFloats);
		assertEquals(100, actFloats[0], 0.0d);
		assertEquals(200, actFloats[1], 0.0d);
	}

	@Test
	public void updatePosition_PositiveXVelocity() {
		PLObject obj = PLObject.builder(10, 10).posX(100).posY(200).velX(20).velY(0).build();
		float deltaT = 0.01f;
		float[] actFloats = new float[2];

		obj.updatePosition(deltaT);
		obj.getPosition(actFloats);
		assertEquals(100 + deltaT * 20, actFloats[0], 0.0d);
		assertEquals(200, actFloats[1], 0.0d);
	}

	@Test
	public void updatePosition_NegativeXVelocity() {
		PLObject obj = PLObject.builder(10, 10).posX(100).posY(200).velX(-30).velY(0).build();
		float deltaT = 0.01f;
		float[] actFloats = new float[2];

		obj.updatePosition(deltaT);
		obj.getPosition(actFloats);
		assertEquals(100 + deltaT * -30, actFloats[0], 0.0d);
		assertEquals(200, actFloats[1], 0.0d);
	}

	@Test
	public void updatePosition_PositiveYVelocity() {
		PLObject obj = PLObject.builder(10, 10).posX(100).posY(200).velX(0).velY(40).build();
		float deltaT = 0.01f;
		float[] actFloats = new float[2];

		obj.updatePosition(deltaT);
		obj.getPosition(actFloats);
		assertEquals(100, actFloats[0], 0.0d);
		assertEquals(200 + deltaT * 40, actFloats[1], 0.0d);
	}

	@Test
	public void updatePosition_NegativeYVelocity() {
		PLObject obj = PLObject.builder(10, 10).posX(100).posY(200).velX(0).velY(-50).build();
		float deltaT = 0.01f;
		float[] actFloats = new float[2];

		obj.updatePosition(deltaT);
		obj.getPosition(actFloats);
		assertEquals(100, actFloats[0], 0.0d);
		assertEquals(200 + deltaT * -50, actFloats[1], 0.0d);
	}

	@Test
	public void updatePosition_PositiveBothVelocity() {
		PLObject obj = PLObject.builder(10, 10).posX(100).posY(200).velX(60).velY(40).build();
		float deltaT = 0.01f;
		float[] actFloats = new float[2];

		obj.updatePosition(deltaT);
		obj.getPosition(actFloats);
		assertEquals(100 + deltaT * 60, actFloats[0], 0.0d);
		assertEquals(200 + deltaT * 40, actFloats[1], 0.0d);
	}

	@Test
	public void updatePosition_NegativeBothVelocity() {
		PLObject obj = PLObject.builder(10, 10).posX(100).posY(200).velX(-70).velY(-50).build();
		float deltaT = 0.01f;
		float[] actFloats = new float[2];

		obj.updatePosition(deltaT);
		obj.getPosition(actFloats);
		assertEquals(100 + deltaT * -70, actFloats[0], 0.0d);
		assertEquals(200 + deltaT * -50, actFloats[1], 0.0d);
	}

	@Test
	public void updatePosition_MixedVelocity() {
		PLObject obj = PLObject.builder(10, 10).posX(100).posY(200).velX(-70).velY(50).build();
		float deltaT = 0.01f;
		float[] actFloats = new float[2];

		obj.updatePosition(deltaT);
		obj.getPosition(actFloats);
		assertEquals(100 + deltaT * -70, actFloats[0], 0.0d);
		assertEquals(200 + deltaT * 50, actFloats[1], 0.0d);
	}

	@Test
	public void contains_Inside() {
		PLObject obj = PLObject.builder(100, 100).posX(0).posY(0).build();

		assertEquals(true, obj.contains(0, 0));
		assertEquals(true, obj.contains(0, 99));
		assertEquals(true, obj.contains(99, 0));
		assertEquals(true, obj.contains(99, 99));
		assertEquals(true, obj.contains(50, 50));
	}

	@Test
	public void contains_Outside() {
		PLObject obj = PLObject.builder(100, 100).posX(0).posY(0).build();

		assertEquals(false, obj.contains(-1, -1));
		assertEquals(false, obj.contains(0, 100));
		assertEquals(false, obj.contains(100, 0));
		assertEquals(false, obj.contains(100, 100));
	}

	@Test
	public void overlaps_Does() {
		PLObject obj1 = PLObject.builder(100, 100).posX(0).posY(0).build();
		PLObject obj2 = PLObject.builder(100, 100).posX(99).posY(99).build();

		assertEquals(true, obj1.overlaps(obj1));
		assertEquals(true, obj1.overlaps(obj2));
	}

	@Test
	public void overlaps_DoesNot() {
		PLObject obj1 = PLObject.builder(100, 100).posX(0).posY(0).build();
		PLObject obj2 = PLObject.builder(100, 100).posX(100).posY(100).build();

		assertEquals(false, obj1.overlaps(obj2));

		obj2.setPosition(0, 100);
		assertEquals(false, obj1.overlaps(obj2));

		obj2.setPosition(100, 0);
		assertEquals(false, obj1.overlaps(obj2));

		obj2.setPosition(-100, 0);
		assertEquals(false, obj1.overlaps(obj2));

		obj2.setPosition(0, -100);
		assertEquals(false, obj1.overlaps(obj2));

		obj2.setPosition(-100, -100);
		assertEquals(false, obj1.overlaps(obj2));
	}

	@Test
	public void useAppContext() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		assertEquals("com.patrickluhmann.plobjectslibrary.test", appContext.getPackageName());
	}
}
