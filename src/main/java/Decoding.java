import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by elena on 30.04.16.
 */
public class Decoding { // TODO: Bijection with Array

	public void decode (String inFileName, String slowOutFileName,
	                    String fastOutFileName, String dictionaryFileName) throws IOException {
		byte[] input = readCode(inFileName);

		List<Integer> slowCodeNumbers = SlowDecoding.decode(input);

		Integer[][] table = ByteTable.create();
		List<Integer> fastCodeNumbers = FastDecoding.decode(table, input);

		List<String> words = readWords(dictionaryFileName);
		List<Integer> numbers = sortCodes(words.size());
		Map<Integer, String> bijection = createCodesBijection(numbers, words);

		writeFile(slowCodeNumbers, bijection, slowOutFileName);
		writeFile(fastCodeNumbers, bijection, fastOutFileName);
	}

	private List<String> readWords(String dictionaryFileName) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileReader(dictionaryFileName));
		List<String> words = new ArrayList<>();
		while (scanner.hasNext()) {
			words.add(scanner.next());
		}
		return words;
	}

	private byte[] readCode(String inFileName) throws IOException {
		return Files.readAllBytes(new File(inFileName).toPath());
	}

	private List<Integer> sortCodes (Integer amount) {
		String ending = "0110";
		List<Integer> codeNumbers = new ArrayList<>();
		codeNumbers.add(1);
		codeNumbers.add(2);
		int count = 2;
		for (int i = 1; count < amount; i++) {
			String codeWord = Integer.toBinaryString(i) + ending;
			codeNumbers.add(decodeWord(codeWord));
			count++;
			if (count < amount) {
				String code = Integer.toBinaryString(i);
				codeWord = "";
				for (int j = 0; j < code.length(); j++) {
					if (code.charAt(j) == '0')
						codeWord += "1";
					else
						codeWord += "0";
				}
				codeWord += ending;
				codeNumbers.add(decodeWord(codeWord));
				count++;
			}
		}
		return codeNumbers;
	}

	private Integer decodeWord (String code) {
		code = code.substring(0, code.length() - 3); //delete delimiter
		if (code.indexOf('1') != -1) //find words all from 0
			code = code.substring(0, code.length() - 1); //delete one more 0 if it is needed
		int count1 = 0; //delete one 1 from subsequent with more then two 1
		for (int i = 0; i < code.length(); i++) {
			if (code.charAt(i) == '1') count1++;
			else if (count1 < 2) count1 = 0; //skip subsequent with less then three 1
			else { //delete one 1
				code = code.substring(0, i - 1) + code.substring(i, code.length());
				count1 = 0;
				i--;
			}
		}
		Integer number = 1; //form a number
		for (int i = 0; i < code.length(); i++) {
			number <<= 1;
			if (code.charAt(i) == '1') number++;
		}
		return number;
	}

	private Map<Integer, String> createCodesBijection(List<Integer> numbers, List<String> words) {
		Map<Integer, String> bijection = new HashMap<>();
		for(int i = 0; i < words.size(); i++) {
			bijection.put(numbers.get(i), words.get(i));
		}
		return bijection;
	}

	private void writeFile (List<Integer> codeNumbers, Map<Integer, String> bijection, String outFileName) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(new FileOutputStream(new File(outFileName)));
		for (Integer number : codeNumbers) {
			writer.print(bijection.get(number) + " ");
		}
		writer.flush();
		writer.close();
	}

}
