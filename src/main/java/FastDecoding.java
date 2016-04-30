import java.util.ArrayList;
import java.util.List;

/**
 * Created by elena on 30.04.16.
 */
public class FastDecoding {

	public static List<Integer> decode (Integer[][] byteTable, byte[] input) {
		return decodeBytes(input, byteTable);
	}

	private static List<Integer> decodeBytes(byte[] input, Integer[][] byteTable) {
		List<Integer> codeNumbers = new ArrayList<>();
		byte rest = 0;
		Integer word = 1;
		Integer wordLength;
		for (int i = 0; i < input.length; i++) { // f1 = 0
			int in = (input[i] + 256) % 256;
			Integer byteFromTable = byteTable[rest][in];
			if ((byteFromTable & 0x80000000) == 0) {
				wordLength = byteFromTable & 0xF;
				byteFromTable >>= 4;
				word <<= wordLength;
				word |= byteFromTable & 0x3FF;
				rest = (byte)((byteFromTable >> 10) & 0x7);
			} else { // f1 = 1
				wordLength = byteFromTable & 0x7;
				byteFromTable >>= 3;
				word <<= wordLength;
				word |= byteFromTable & 0x3F;
				codeNumbers.add(word);
				word = 1;
				byteFromTable >>= 6;
				if ((byteFromTable & 0x200000) == 0) { // f2 = 0
					wordLength = byteFromTable & 0x7;
					byteFromTable >>= 3;
					word <<= wordLength;
					word |= byteFromTable & 0x7F;
					rest = (byte)((byteFromTable >> 7) & 0x7);
				} else { //f2 = 1
					wordLength = byteFromTable & 0x7;
					byteFromTable >>= 3;
					word <<= wordLength;
					word |= byteFromTable & 0xF;
					codeNumbers.add(word);
					word = 1;
					byteFromTable >>= 4;
					wordLength = byteFromTable & 0x7;
					byteFromTable >>= 3;
					word <<= wordLength;
					word |= byteFromTable & 0xF;
					rest = (byte)((byteFromTable >> 4) & 0x7);
					if ((byteFromTable & 0x400) != 0) { // f3 = 1
						codeNumbers.add(word);
						word = 1;
					}
				}
			}
		}
		return codeNumbers;
	}
}
