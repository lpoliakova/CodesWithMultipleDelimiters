package codeswithmultipledelimeters.domain;

/**
 * Created by elena on 03.05.16.
 */
public class CodeInIntegerRepresentation {
	private Integer code;
	private Integer codeLength;

	public CodeInIntegerRepresentation(Integer code, Integer codeLength) {
		this.code = code;
		this.codeLength = codeLength;
	}

	public void set(Integer code, Integer codeLength) {
		this.code = code;
		this.codeLength = codeLength;
	}

	public Integer getCode() {
		return code;
	}

	public Integer getCodeLength() {
		return codeLength;
	}
}
