package codeswithmultipledelimeters.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elena on 07.05.16.
 */
public class ByteTableByteAnalise {
	private final static Integer REST_ = 0;
	private final static Integer REST_0 = 1;
	private final static Integer REST_1 = 2;
	private final static Integer REST_01 = 3;
	private final static Integer REST_11 = 4;
	private final static Integer REST_011 = 5;
	private final static Integer REST_111 = 6;

	public static Integer[][] create () {
		return createByteTable();
	}

	private static Integer[][] createByteTable() {
		return new Integer[0][0]; // TODO: rewrite table creation
	}

	private static Integer codeAnalise (Integer rest, Integer code) { // TODO: code consists of 32-8 zeros and then number
		List<CodeInIntegerRepresentation> numbers = new ArrayList<>();
		CodeInIntegerRepresentation restCode = restCode(rest);
		CodeInIntegerRepresentation number = new CodeInIntegerRepresentation(0, 0);
		code = (restCode.getCode() << 8) | code;
		for (int i = Byte.SIZE + restCode.getCodeLength(); i > 0; i--) {
			if (number.getCodeLength() == 3 && checkForShortDelimiter(number.getCode())) {
				number = new CodeInIntegerRepresentation(0, 0);
				numbers.add(number);
			} else if (number.getCodeLength() >= 4 && checkForDelimiter(number.getCode())) {
				number = new CodeInIntegerRepresentation(number.getCode() >> 4, number.getCodeLength() - 4);
				if (number.getCode() == 0) {
					if (numbers.isEmpty() && rest.equals(REST_) || !numbers.isEmpty()) {
						number = new CodeInIntegerRepresentation(number.getCode() << 1, number.getCodeLength() + 1);
					}
				} else {
					number = getRealNumber(number);
				}
				numbers.add(number);
				number = new CodeInIntegerRepresentation(0, 0);
			} else {
				number = addBit(number, getBitOnPosition(code, i));
			}
		}
		// TODO: analise new rest
		return 0;
	}

	private static Boolean checkForShortDelimiter(Integer code) {
		return code == 6;
	}

	private static Boolean checkForDelimiter(Integer code) {
		return (code & 15) == 6;
	}

	private static Integer getBitOnPosition(Integer code, Integer position) {
		return ((code >> (position - 1)) & 1);
	}

	private static CodeInIntegerRepresentation getRealNumber(CodeInIntegerRepresentation code) {
		Integer number = code.getCode();
		Integer numberLength = code.getCodeLength();
		Integer runningOnes = 0;
		for (int j = 0; j <= numberLength; j++) {
			if (getBitOnPosition(number, j) == 1) {
				runningOnes++;
			}
			else {
				if (runningOnes >= 3)  {
					number = deleteBitOnPosition(number, j - 1);
					numberLength--;
				}
				runningOnes = 0;
			}
		}
		return new CodeInIntegerRepresentation(number, numberLength);
	}

	private static Integer deleteBitOnPosition(Integer code, Integer position) {
		Integer codeBeforePosition = code & (-1 << (Integer.SIZE - position + 1));
		return ((code >> position) << (position - 1)) | codeBeforePosition;
	}

	private static CodeInIntegerRepresentation addBit(CodeInIntegerRepresentation code, Integer bit) {
		return new CodeInIntegerRepresentation((code.getCode() << 1) | bit, code.getCodeLength() + 1);
	}

	private static CodeInIntegerRepresentation restCode(Integer rest) {
		if (rest.equals(REST_)) return new CodeInIntegerRepresentation(0, 0);
		if (rest.equals(REST_0)) return new CodeInIntegerRepresentation(0, 1);
		if (rest.equals(REST_1)) return new CodeInIntegerRepresentation(1, 1);
		if (rest.equals(REST_01)) return new CodeInIntegerRepresentation(1, 2);
		if (rest.equals(REST_11)) return new CodeInIntegerRepresentation(3, 2);
		if (rest.equals(REST_011)) return new CodeInIntegerRepresentation(3, 3);
		if (rest.equals(REST_111)) return new CodeInIntegerRepresentation(7, 3);
		return new CodeInIntegerRepresentation(0, 0);
	}

	private static Integer addWord (Integer result, Integer placeForWord, Integer placeForWordLength, Integer word, Integer wordLength) {
		result <<= placeForWord;
		result |= word;
		result <<= placeForWordLength;
		result |= wordLength;
		return result;
	}
}
