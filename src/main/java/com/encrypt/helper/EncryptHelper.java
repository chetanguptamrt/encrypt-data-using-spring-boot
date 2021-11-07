package com.encrypt.helper;

import java.io.Serializable;

public class EncryptHelper implements Serializable {

	private static final long serialVersionUID = 2929209520937627206L;

	private byte password;

	private byte[] inputStream;
	
	private String extension;
	
	public byte getPassword() {
		return password;
	}

	public void setPassword(byte password) {
		this.password = password;
	}

	public byte[] getInputStream() {
		return inputStream;
	}

	public void setInputStream(byte[] inputStream) {
		this.inputStream = inputStream;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

}
