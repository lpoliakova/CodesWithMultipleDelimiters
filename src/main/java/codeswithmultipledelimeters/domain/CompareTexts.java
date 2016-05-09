package codeswithmultipledelimeters.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by elena on 07.05.16.
 */
public class CompareTexts {

	public static Integer compare(File firstFile, File secondFile) throws FileNotFoundException {
		List<String> firstText = readFile(firstFile);
		List<String> secondText = readFile(secondFile);
		for (int i = 0; i < firstText.size(); i++) {
			if (!firstText.get(i).equals(secondText.get(i))) return i;
		}
		return 0;
		//return firstText.equals(secondText);
	}

	private static List<String> readFile(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileReader(file));
		List<String> text = new ArrayList<>();
		while (scanner.hasNext()) {
			text.add(scanner.next());
		}
		return text;
	}
}
