package codeswithmultipledelimeters.domain;

import java.io.File;

/**
 * Created by elena on 03.05.16.
 */
public class OutputFile {
	private File file;

	public OutputFile(File file) {
		this.file = file;
	}

	public File getFile () {
		return file;
	}
}
