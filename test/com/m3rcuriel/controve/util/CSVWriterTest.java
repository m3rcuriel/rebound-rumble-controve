package com.m3rcuriel.controve.util;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class CSVWriterTest {

	@Test
	public void testWriter() throws IOException {
		File f = new File("test.txt");
		CSVWriter writer = new CSVWriter(f, "head1", "head2");
		BufferedReader fr = new BufferedReader(new FileReader(f));
		assertEquals(fr.readLine(), "head1,head2");
		writer.writeLine(0, 1);
		assertEquals(fr.readLine(), "0,1");
		f.delete();
	}

}
