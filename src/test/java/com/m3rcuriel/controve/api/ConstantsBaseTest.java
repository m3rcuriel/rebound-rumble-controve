package com.m3rcuriel.controve.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.m3rcuriel.controve.api.ConstantsBase.Constant;

public class ConstantsBaseTest {

	public static class TestClass extends ConstantsBase {
		public static int thingA = 5;
		public static double thingB = 5.115;

		public static double kEndEditableArea = 0;

		public static double thingC = new Double(12.4);

		@Override
		public String getFileLocation() {
			return "~/constants_test.txt";
		}
	}

	@Test
	public void testKeyExtraction() {
		Collection<Constant> constants = (new TestClass()).getConstants();
		Constant[] expected = new Constant[] { new Constant("thingA", int.class, 5),
				new Constant("thingB", double.class, 5.115) };
		assertTrue(Arrays.equals(expected, constants.toArray()));
	}

	@Test
	public void testCanSet() {
		int old = TestClass.thingA;
		boolean set = new TestClass().setConstant("thingA", 8);
		assertEquals(TestClass.thingA, 8);
		assertTrue(set);
		set = new TestClass().setConstant("thingA", old);
		assertEquals(TestClass.thingA, old);
		assertTrue(set);
	}

	@Test
	public void testFailOnType() {
		int old = TestClass.thingA;
		boolean set = new TestClass().setConstant("thingA", 1.15);
		assertFalse(set);
		assertEquals(TestClass.thingA, 5);
		set = new TestClass().setConstant("thingA", old);
		assertEquals(TestClass.thingA, old);
		assertTrue(set);
	}

	@Test
	public void testReadFile() {
		int old = TestClass.thingA;
		JSONObject j = new JSONObject();
		j.put("thingA", 123);
		File f = new TestClass().getFile();
		try {
			FileWriter w = new FileWriter(f);
			w.write(j.toJSONString());
			w.close();

		} catch (IOException e) {
			assertTrue("couldn't write file", false);
		}
		new TestClass().loadFromFile();

		assertEquals(TestClass.thingA, 123);

		new TestClass().setConstant("thingA", old);
		assertEquals(TestClass.thingA, old);
	}

	@Test
	public void testWriteFile() {
		TestClass t = new TestClass();
		int oldA = TestClass.thingA;
		double oldB = TestClass.thingB;
		t.setConstant("thingB", 987.76);
		t.setConstant("thingA", 1234);

		t.saveToFile();

		try {
			JSONObject j = t.getJsonObjectFromFile();
			assertEquals(j.get("thingB"), 987.76);
			assertEquals(j.get("thingA"), 1234L);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			assertTrue("couldn't get file jsonobject", false);
		}

		t.setConstant("thingA", oldA);
		t.setConstant("thingB", oldB);
	}

}
