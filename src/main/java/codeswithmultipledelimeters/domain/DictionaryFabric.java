package codeswithmultipledelimeters.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by elena on 30.04.16.
 */
public class DictionaryFabric {

	public static void dictionary(Dictionary dictionary) throws FileNotFoundException {
		final List<String> words = readWords(dictionary.getFile());
		dictionary.writeWords(words);
	}

	private static List<String> readWords(final File dictionaryFile) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileReader(dictionaryFile));
		List<String> words = new ArrayList<>();
		while (scanner.hasNext()) {
			words.add(scanner.next());
		}
		return words;
	}

}
