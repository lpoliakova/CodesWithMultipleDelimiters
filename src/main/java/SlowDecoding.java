import java.util.ArrayList;
import java.util.List;

/**
 * Created by elena on 30.04.16.
 */
public class SlowDecoding {

	public static List<Integer> decode (byte[] in) { // TODO: Decode without Strings
		List<String> input = divideInCodeWords(in);
		return decodeWords(input);
	}

	private static List<String> divideInCodeWords(byte[] in) {
		List<String> input = new ArrayList<>();
		String curCode = "";
		for (byte byteCode : in) { //read one byte
			String code = Integer.toBinaryString(byteCode);
			if (code.length() > 8) code = code.substring(code.length() - 8, code.length()); //change into eight digits if number is negative or starts with 0
			while (code.length() < 8)
				code = "0" + code;
			curCode += code; //add new code to current not analyzed
			while (curCode.startsWith("110")) { //find if there is 110 at tne beginning meaning it is a separate word
				input.add("110");
				curCode = curCode.substring(3, curCode.length());
			}
			int ending = curCode.indexOf("0110"); //finding delimiters
			while (ending != -1) {
				input.add(curCode.substring(0, ending + 4));
				curCode = curCode.substring(ending + 4, curCode.length());
				while (curCode.startsWith("110")) {
					input.add("110");
					curCode = curCode.substring(3, curCode.length());
				}
				ending = curCode.indexOf("0110");
			}
		}
		return input;
	}

	private static List<Integer> decodeWords(List<String> codes) {
		List<Integer> codeNumbers = new ArrayList<>();
		for (String code : codes) {
			codeNumbers.add(decodeWord(code));
		}
		return codeNumbers;
	}

	private static Integer decodeWord(String code) {
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
}
