package codeswithmultipledelimeters.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by elena on 03.05.16.
 */
public class OutputFabric {

	public static void outFile (List<Integer> codeNumbers, String[] bijection, OutputFile outputFile) throws FileNotFoundException {
		writeFile(codeNumbers, bijection, outputFile.getFile());
	}

	private  static void writeFile (List<Integer> codeNumbers, String[] bijection, File outFile) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(new FileOutputStream(outFile));
		for (Integer number : codeNumbers) {
			writer.print(bijection[number] + " ");
		}

		writer.flush();
		writer.close();
	}
}
