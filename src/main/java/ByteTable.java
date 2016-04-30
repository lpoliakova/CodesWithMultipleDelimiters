import java.util.ArrayList;
import java.util.List;

/**
 * Created by elena on 30.04.16.
 */
public class ByteTable {

	public static Integer[][] create () {
		return createByteTable();
	}

	private static Integer[][] createByteTable() {
		Integer[][] byteTable = new Integer[7][256];
		for (int column = 0; column < 256; column++) {
			String word = Integer.toBinaryString(column);
			if (word.length() > 8) word = word.substring(word.length() - 8, word.length());
			else while (word.length() != 8) word = "0" + word;
			for (int line = 0; line < 7; line++) {
				byteTable[line][column] = codeAnalise(getRestString(line), word);
			}
		}
		return byteTable;
	}

	private static Integer codeAnalise (String rest, String code) {
		List<String> numbers = new ArrayList<>();
		code = rest + code;
		String currentNumber = "";
		for (int i = 0; i < code.length(); i++) {
			if (currentNumber.equals("") && code.startsWith("110", i)) {
				numbers.add("");
				i += 2;
			} else if (code.startsWith("0110", i)) {
				if (!currentNumber.contains("1")) {
					if ((numbers.isEmpty() && rest.equals("")) || !(numbers.isEmpty()))
						currentNumber += "0";
				}
				else {
					Integer runningOnes = 0;
					for (int j = 0; j < currentNumber.length(); j++) {
						if (currentNumber.charAt(j) == '1') runningOnes++;
						else if (currentNumber.charAt(j) == '0' && runningOnes < 3) runningOnes = 0;
						else {
							currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
							runningOnes = 0;
						}
					}
					if (runningOnes >= 3) currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
				}
				numbers.add(currentNumber);
				currentNumber = "";
				i += 3;
			} else {
				currentNumber += code.charAt(i);
			}
		}
		if (currentNumber.length() == 0) {
			numbers.add("");
			numbers.add("");
		}
		else {
			Integer runningOnes = 0;
			for (int j = 0; j < currentNumber.length(); j++) {
				if (currentNumber.charAt(j) == '1') runningOnes++;
				else if (currentNumber.charAt(j) == '0' && runningOnes < 3) runningOnes = 0;
				else {
					currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
					runningOnes = 0;
				}
			}
			if (runningOnes >= 3) {
				currentNumber = currentNumber.substring(0, currentNumber.length() - 3);
				numbers.add(currentNumber);
				numbers.add("111");
			} else if (runningOnes == 2 && currentNumber.length() == 2) {
				numbers.add("");
				numbers.add("11");
			} else if (runningOnes == 2 && currentNumber.length() > 2) {
				currentNumber = currentNumber.substring(0, currentNumber.length() - 2);
				if (!currentNumber.contains("1")) {
					numbers.add(currentNumber);
					numbers.add("11");
				} else {
					currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
					numbers.add(currentNumber);
					numbers.add("011");
				}
			} else if (runningOnes == 1 && currentNumber.length() == 1) {
				numbers.add("");
				numbers.add("1");
			} else if (runningOnes == 1 && currentNumber.length() > 1) {
				currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
				if (!currentNumber.contains("1")) {
					numbers.add(currentNumber);
					numbers.add("1");
				} else {
					currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
					numbers.add(currentNumber);
					numbers.add("01");
				}
			} else {
				if (!currentNumber.contains("1")) {
					numbers.add(currentNumber);
					numbers.add("");
				} else {
					numbers.add(currentNumber.substring(0, currentNumber.length() - 1));
					numbers.add("0");
				}
			}

		}
		Integer result = formInteger(numbers);
		if (currentNumber.length() == 0 && numbers.size() == 5) result |= 0x20000000;
		return result;
	}

	private static Integer formInteger (List<String> numbers) {
		Integer result = 0;
		if (numbers.size() == 2) {
			result |= getRestInteger(numbers.get(1));
			result = addWord(result, 10, 4, numbers.get(0));
		} else if (numbers.size() == 3) {
			result |= getRestInteger(numbers.get(2));
			result = addWord(result, 7, 3, numbers.get(1));
			result = addWord(result, 6, 3, numbers.get(0));
			result |= 0x80000000;
		} else {
			result |= getRestInteger(numbers.get(3));
			result = addWord(result, 4, 3, numbers.get(2));
			result = addWord(result, 4, 3, numbers.get(1));
			result = addWord(result, 6, 3, numbers.get(0));
			result |= 0xC0000000;
		}
		return result;
	}

	private static Integer addWord (Integer result, Integer placeForWord, Integer placeForWordLength, String word) {
		result <<= placeForWord - word.length();
		for (int i = 0; i < word.length(); i++) {
			result <<= 1;
			if (word.charAt(i) == '1') result |= 1;
		}
		result <<= placeForWordLength;
		result |= word.length();
		return result;
	}

	private static String getRestString(int number) {
		if (number == 0) return "";
		if (number == 1) return "0";
		if (number == 2) return "1";
		if (number == 3) return "01";
		if (number == 4) return "11";
		if (number == 5) return "011";
		return "111";
	}

	private static Integer getRestInteger (String number) {
		if (number.equals("")) return 0;
		if (number.equals("0")) return 1;
		if (number.equals("1")) return 2;
		if (number.equals("01")) return 3;
		if (number.equals("11")) return 4;
		if (number.equals("011")) return 5;
		return 6;
	}
}
