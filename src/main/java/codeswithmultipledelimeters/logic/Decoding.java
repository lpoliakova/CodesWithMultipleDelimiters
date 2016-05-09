package codeswithmultipledelimeters.logic;

import codeswithmultipledelimeters.domain.*;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
/**
 * Created by elena on 30.04.16.
 */
public final class Decoding {

	public void decode(String inFileName, OutputFile slowOutputFile,
                   OutputFile fastOutputFile, Dictionary dictionary, Bijection bijection) throws IOException {
		byte[] input = readCode(inFileName);

		List<Integer> slowCodeNumbers = SlowDecoding.decode(input);

		Integer[][] table = ByteTable.create();
		List<Integer> fastCodeNumbers = FastDecoding.decode(table, input);

		List<String> words = dictionary.getWords();
		String[] biject = bijection.createCodesBijection(words);

		OutputFabric.outFile(slowCodeNumbers, biject, slowOutputFile);
		OutputFabric.outFile(fastCodeNumbers, biject, fastOutputFile);
	}

	private byte[] readCode(String inFileName) throws IOException {
		return Files.readAllBytes(new File(inFileName).toPath());
	}
}
