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
