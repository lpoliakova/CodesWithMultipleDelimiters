import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by elena on 20.02.16.
 */
public class Encoding {

	public void encode (String inFileName, String outFileName, String dictionaryFileName) throws IOException {
		List<String> input = readWords(inFileName);
		List<String> words = sortWords(input);
		List<Integer> numbers = sortCodes(words.size());
		Map<String, Integer> bijection = createCodesBijection(numbers, words);
		byte[] output = encodeWords(input, bijection);
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

	private Map<String, Integer> createCodesBijection(List<Integer> numbers, List<String> words) {
		Map<String, Integer> bijection = new HashMap<>();
		for(int i = 0; i < words.size(); i++) {
			bijection.put(words.get(i), numbers.get(i));
		}
		return bijection;
	}

	private String encodeWord (Integer number) {
		String code = Integer.toBinaryString(number);
		code = code.substring(1, code.length());
		if (code.indexOf('1') == -1) return code + "110";
		int count1 = 0;
		for (int i = 0; i < code.length(); i++) {
			if (code.charAt(i) == '1') count1++;
			else if (count1 == 1) count1 = 0; //skip subsequent with less then three 1
			else { //add one 1
				code = code.substring(0, i) + "1" + code.substring(i, code.length());
				count1 = 0;
				i++;
			}
		}
		return code + "0110";
	}

	private byte[] encodeWords (List<String> input, Map<String, Integer> bijection) {
		List<Byte> out = new ArrayList<>();
		String curByte = "";
		for (String word : input) {
			Integer number = bijection.get(word);
			String codeWord = encodeWord(number);
			for (int i = 0; i < codeWord.length(); i++) {
				curByte += codeWord.charAt(i);
				if (curByte.length() == 8) {
					Byte code = formByte(curByte);
					out.add(code);
					curByte = "";
				}
			}
		}
		if (curByte.length() != 0) {
			while (curByte.length() < 8) {
				curByte += 0;
				if (curByte.length() == 8) {
					Byte code = formByte(curByte);
					out.add(code);
				}
			}
		}

		byte[] output = new byte[out.size()];
		for (int i = 0; i < out.size(); i++) {
			output[i] = out.get(i);
		}
		return output;
	}

	private Byte formByte (String codeWord) {
		byte code = 0;
		if (codeWord.charAt(0) == '1') code = 1;
		for (int i = 1; i < codeWord.length(); i++) {
			code <<= 1;
			if (codeWord.charAt(i) == '1') code++;
		}
		return code;
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
