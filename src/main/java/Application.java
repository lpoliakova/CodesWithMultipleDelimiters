import helen.Dictionary;

import java.io.IOException;

/**
 * Created by elena on 20.02.16.
 */
public class Application {
	public static void main(String[] args) throws IOException {
		Encoding encoding = new Encoding();
		encoding.encode("exmpIn", "exmpCode", "exmpDictionary");
		Decoding decoding = new Decoding();

		new Dictionary();

		decoding.decode("exmpCode", "exmpOutSlow", "exmpOutFast", "exmpDictionary");
	}
}
