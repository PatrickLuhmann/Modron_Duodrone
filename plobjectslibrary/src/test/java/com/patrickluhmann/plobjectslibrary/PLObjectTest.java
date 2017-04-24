package com.patrickluhmann.plobjectslibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PLObjectTest {
	@Test
	public void basic() throws Exception {
		assertEquals("PLObject", PLObject.getClassName());
	}
}