package com.encrypt.helper;

import java.util.Arrays;

public class ResponseHelper {

	private String message;
	
	private byte[] content;

	private String extention;
	
	public ResponseHelper(String message, byte[] content, String extention) {
		super();
		this.message = message;
		this.content = content;
		this.extention = extention;
	}

	public ResponseHelper(String message, byte[] content) {
		super();
		this.message = message;
		this.content = content;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getExtention() {
		return extention;
	}

	public void setExtention(String extention) {
		this.extention = extention;
	}
	
	@Override
	public String toString() {
		return "ResponseHelper [message=" + message + ", content=" + Arrays.toString(content) + ", extention="
				+ extention + "]";
	}
	
}
