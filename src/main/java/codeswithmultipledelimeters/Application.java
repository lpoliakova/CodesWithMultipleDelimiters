package codeswithmultipledelimeters;

import codeswithmultipledelimeters.domain.*;
import codeswithmultipledelimeters.logic.Encoding;
import codeswithmultipledelimeters.logic.Decoding;

import java.io.File;
import java.io.IOException;

/**
 * Created by elena on 20.02.16.
 */
public class Application {

	public static void main(String[] args) throws IOException {
		Dictionary dictionary = new Dictionary(new File("exmpDictionary"));
		Bijection bijection = new Bijection();

		Encoding encoding = new Encoding();
		encoding.encode("exmpIn", "exmpCode", "exmpDictionary", bijection);

		DictionaryFabric.dictionary(dictionary);
		Decoding decoding = new Decoding();
		decoding.decode("exmpCode", new OutputFile(new File("exmpOutSlow")), new OutputFile(new File("exmpOutFast")), dictionary, bijection);

		System.out.println(CompareTexts.compare(new File("exmpOutSlow"), new File("exmpIn")));
		System.out.println(CompareTexts.compare(new File("exmpOutFast"), new File("exmpIn")));
	}


}
