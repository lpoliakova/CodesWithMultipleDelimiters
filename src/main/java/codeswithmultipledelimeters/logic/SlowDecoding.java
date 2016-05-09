package codeswithmultipledelimeters.logic;

import codeswithmultipledelimeters.domain.CodeInIntegerRepresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elena on 30.04.16.
 */
public class SlowDecoding {

	public static List<Integer> decode (byte[] in) {
		List<CodeInIntegerRepresentation> input = divideInCodewords(in);
		return decodeWords(input);
	}

	private static List<CodeInIntegerRepresentation> divideInCodewords(byte[] in) {
		List<CodeInIntegerRepresentation> input = new ArrayList<>();
		Integer code = 0;
		Integer length = 0;
		for (byte byteCode : in) {
			for (int i = Byte.SIZE ; i > 0; i--) {
				if (length == 3 && checkForShortDelimiter(code)) {
					input.add(new CodeInIntegerRepresentation(6, 3));
					code = 0;
					length = 0;
				} else if (length >= 4 && checkForDelimiter(code)) {
					input.add(new CodeInIntegerRepresentation(code, length));
					code = 0;
					length = 0;
				}
				code <<= 1;
				code |= (byteCode & (1 << i - 1)) >> (i - 1);
				length++;
			}
		}
		return input;
	}

	private static Boolean checkForShortDelimiter(Integer code) {
		return code == 6;
	}

	private static Boolean checkForDelimiter(Integer code) {
		return (code & 15) == 6;
	}

	private static List<Integer> decodeWords(List<CodeInIntegerRepresentation> codes) {
		List<Integer> codeNumbers = new ArrayList<>();
		for (CodeInIntegerRepresentation code : codes) {
			codeNumbers.add(decodeWord(code.getCode(), code.getCodeLength()));
		}
		return codeNumbers;
	}

	private static Integer decodeWord(Integer code, Integer codeLengthInBinary) {
		if (code == 6) return 1 << (codeLengthInBinary - 3);
		code >>= 4;
		codeLengthInBinary -= 4;
		int runningOnes = 0;
		int number = 0;
		int numberLength = 0;
		for (int i = 0; i <= codeLengthInBinary; i++) {
			if ((code & 1) == 1) {
				runningOnes++;
				number = addOneToTheBeginning(number, numberLength);
			}
			else {
				if (runningOnes >= 3)  {
					number = deleteLastAddedOne(number, numberLength);
					numberLength--;
				}
				runningOnes = 0;
			}
			numberLength++;
			code >>= 1;
		}
		numberLength--;
		return addOneToTheBeginning(number, numberLength);
	}

	private static Integer addOneToTheBeginning(Integer number, Integer numberLength) {
		return number | (1 << numberLength);
	}

	private static Integer deleteLastAddedOne(Integer number, Integer numberLength) {
		return number & (~(1 << (numberLength - 1)));
	}
}
