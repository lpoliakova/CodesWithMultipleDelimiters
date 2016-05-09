package codeswithmultipledelimeters.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by elena on 03.05.16.
 */
public class Bijection {

	private static final Integer SORTED_CODES = 1000000;
	private List<Integer> numbers;

	public Bijection() {
		numbers = sortCodes(SORTED_CODES);
	}

	public String[] createCodesBijection(List<String> words) {
		String[] bijection = new String[Collections.max(numbers) + 1];
		for (int i = 0; i < words.size(); i++) {
			bijection[numbers.get(i)] = words.get(i);
		}
		return bijection;
	}

	private List<Integer> sortCodes(Integer amountOfCodesToCreate) {
		List<Integer> codeNumbers = new ArrayList<>();
		codeNumbers.add(1);
		codeNumbers.add(2);

		for (int i = 1; codeNumbers.size() < amountOfCodesToCreate; i++) {
			Integer lengthI = (int)(Math.log(i) / Math.log(2)) + 1;
			if (!containsTwoOnes(i, lengthI)) codeNumbers.add(decodeWord(addDelimiter(i), lengthI + 4));
			if (codeNumbers.size() < amountOfCodesToCreate) {
				if (!containsTwoOnes(invertBinary(i, lengthI), lengthI)) codeNumbers.add((decodeWord(addDelimiter(invertBinary(i, lengthI)), lengthI + 4)));
			}
		}

		return codeNumbers;
	}

	private Boolean containsTwoOnes(int number, final int lengthNumber) {
		Integer runningOnes = 0;
		for (int i = 0; i <= lengthNumber; i++) {
			boolean twoOnes = (runningOnes == 2);
			if ((number & 1) == 1) runningOnes++;
			else runningOnes = 0;
			if (runningOnes == 0 && twoOnes) return true;
			number >>= 1;
		}
		return false;
	}

	private Integer addDelimiter(final int i) {
		return (i << 4) | 6;
	}

	private static Integer invertBinary(final int i, final int lengthI) {
		return ((~i) << (Integer.SIZE - lengthI)) >> (Integer.SIZE - lengthI);
	}

	private Integer decodeWord(Integer code, Integer codeLengthInBinary) {
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

	private Integer addOneToTheBeginning(Integer number, Integer numberLength) {
		return number | (1 << numberLength);
	}

	private Integer deleteLastAddedOne(Integer number, Integer numberLength) {
		return number & (~(1 << (numberLength - 1)));
	}
}
