package codeswithmultipledelimeters.logic;

import codeswithmultipledelimeters.domain.Bijection;
import codeswithmultipledelimeters.domain.CodeInIntegerRepresentation;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by elena on 20.02.16.
 */
public class Encoding {

	public void encode (String inFileName, String outFileName, String dictionaryFileName, Bijection bijection) throws IOException {
		List<String> input = readWords(inFileName);
		List<String> words = sortWords(input);
		String[] biject = bijection.createCodesBijection(words);
		List<Integer> integerInput = integerInput(input, biject);
		byte[] output = encodeWords(integerInput);
		writeFile(output, outFileName, words, dictionaryFileName);
	}

	private List<String> readWords(String fileName) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileReader(fileName));
		List<String> input = new ArrayList<>();
		while (scanner.hasNext()) {
			input.add(scanner.next());
		}
		return input;
	}

	private List<String> sortWords(List<String> input) throws FileNotFoundException {
		Map<String, Integer> wordCounter = new HashMap<>();
		for (String word : input) {
			Integer counter = wordCounter.getOrDefault(word, 0);
			wordCounter.put(word, ++counter);
		}
		return wordCounter.entrySet().stream()
				.sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
	}

	private List<Integer> integerInput(List<String> input, String[] bijection) {
		List<Integer> integerInput = new ArrayList<>();
		for (String word : input) {
			integerInput.add(indexOfWord(word, bijection));
		}
		return integerInput;
	}

	private Integer indexOfWord(String word, String[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null && array[i].equals(word)) return i;
		}
		return 0;
	}

	private CodeInIntegerRepresentation encodeWord(Integer number) {
		Integer numberLength = (int)(Math.log(number) / Math.log(2)) + 1;
		number = deleteFirstOne(number, numberLength);
		numberLength--;
		if (number == 0) return new CodeInIntegerRepresentation(6, numberLength + 3);
		Integer runningOnes = 0;
		Integer code = 0;
		Integer codeLength = 0;
		for (int i = 0; i <= numberLength; i++) {
			if ((number & 1) == 1) {
				runningOnes++;
				code = addOneToTheBeginning(code, codeLength);
			} else {
				if (runningOnes >= 2) {
					code = addOneToTheBeginning(code, codeLength);
					codeLength++;
				}
				runningOnes = 0;
			}
			codeLength++;
			number >>= 1;
		}
		codeLength--;
		return new CodeInIntegerRepresentation(addDelimiter(code), codeLength + 4);
	}

	private static Integer deleteFirstOne(Integer number, Integer numberLength) {
		return number & (~(1 << (numberLength - 1)));
	}

	private static Integer addOneToTheBeginning(Integer number, Integer numberLength) {
		return number | (1 << numberLength);
	}

	private static Integer addDelimiter(final int code) {
		return (code << 4) | 6;
	}

	private byte[] encodeWords(List<Integer> input) {
		List<Byte> out = new ArrayList<>();
		byte currentByte = 0;
		Integer freePlace = 8;
		for (Integer number : input) {
			CodeInIntegerRepresentation code = encodeWord(number);
			Integer codeNumber = code.getCode();
			Integer codeLength = code.getCodeLength();
			while (freePlace <= codeLength) {
				currentByte |= codeNumber >> (codeLength - freePlace);
				out.add(currentByte);
				codeLength -= freePlace;
				currentByte = 0;
				freePlace = 8;
			}
			currentByte |= codeNumber << (freePlace - codeLength);
			freePlace -= codeLength;
		}
		out.add(currentByte);

		byte[] output = new byte[out.size()];
		for (int i = 0; i < out.size(); i++) {
			output[i] = out.get(i);
		}
		return output;
	}

	private void writeFile (byte[] output, String outFileName, List<String> words, String dictionaryFileName) throws IOException {
		FileOutputStream out = new FileOutputStream(outFileName);
		out.write(output);
		PrintWriter writer = new PrintWriter(new FileOutputStream(new File(dictionaryFileName)));
		for (String word : words) {
			writer.write(word + " ");
		}
		writer.flush();
		writer.close();
	}
}
