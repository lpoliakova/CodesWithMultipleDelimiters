package codeswithmultipledelimeters.domain;

import java.io.File;
import java.util.List;

/**
 * Created by elena on 30.04.16.
 */
public final class Dictionary {

	private List<String> words;
	private final File file;

	public Dictionary(final File file) {
		this.file = file;
	}

	public void writeWords (List<String> words) {
		this.words = words;
	}

	public List<String> getWords() {
		return words;
	}

	public File getFile() {
		return file;
	}

}
