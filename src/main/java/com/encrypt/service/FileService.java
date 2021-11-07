package com.encrypt.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import org.springframework.web.multipart.MultipartFile;

import com.encrypt.helper.EncryptHelper;
import com.encrypt.helper.ResponseHelper;

@Service
public class FileService {

	private byte key1 = 25;
	private byte key2 = 80;
	
	public byte[] uploadData(MultipartFile file, String password,String session) throws IOException, NotSerializableException {
		//encode password
		byte bytePassword = 0;
		for(int i=0; i<password.length(); i++) {
			bytePassword+=password.charAt(i);
		}
		//encrypt file
		byte[] inputStream = file.getBytes();
		for(int i=0; i<inputStream.length; i++) {
			if(i%3==0)
				inputStream[i] = (byte) (inputStream[i] ^ bytePassword);  
			else if(i%3==1)
				inputStream[i] = (byte) (inputStream[i] ^ key1);
			else if(i%3==2)
				inputStream[i] = (byte) (inputStream[i] ^ key2);
		}
		//get File extension
		String[] split = file.getOriginalFilename().split("\\.");
		String extension = split[split.length-1];
		//save file
		Files.copy(file.getInputStream(), Paths.get( 
				new ClassPathResource("static").getFile().getAbsolutePath()+
				File.separator+"img"+File.separator+"data"+
				File.separator+session+"."+extension),
				StandardCopyOption.REPLACE_EXISTING);
		//create object
		EncryptHelper encryptHelper = new EncryptHelper();
		encryptHelper.setPassword(bytePassword);
		encryptHelper.setInputStream(inputStream);
		encryptHelper.setExtension(extension);
		//convert object to byte[]
		byte[] serialize = SerializationUtils.serialize(encryptHelper);
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//	    try (ObjectOutputStream os = new ObjectOutputStream(bos)) {
//	      os.writeObject(encryptHelper);
//	    }
//	    System.out.println(bos.toByteArray());
//	    System.out.println(serialize);
//	    return bos.toByteArray();
		return serialize;
	}

	public ResponseHelper decryptData(MultipartFile file, String password) throws IOException, ClassNotFoundException {
		//get file to byte[]
		//convert byte[] to object
		String readLine = new BufferedReader(new InputStreamReader(file.getInputStream())).readLine();
//		System.out.println(readLine);
		byte[] decode = Base64.getDecoder().decode(readLine);
//		for (int i = 0; i < decode.length; i++) {
//			System.out.println(decode[i]);
//		}
		ByteArrayInputStream bis = new ByteArrayInputStream(decode);
	    ObjectInput in = new ObjectInputStream(bis);
		EncryptHelper deserialize = (EncryptHelper) in.readObject();
		//encode password
		byte bytePassword = 0;
		for(int i=0; i<password.length(); i++) {
			bytePassword+=password.charAt(i);
		}
		//check password is matched or not
		if(bytePassword==deserialize.getPassword()) {
			//decrypt file
			byte[] inputStream = deserialize.getInputStream();
			for(int i=0; i<inputStream.length; i++) {
				if(i%3==0)
					inputStream[i] = (byte) (inputStream[i] ^ bytePassword);  
				else if(i%3==1)
					inputStream[i] = (byte) (inputStream[i] ^ key1);
				else if(i%3==2)
					inputStream[i] = (byte) (inputStream[i] ^ key2);
			}
			return new ResponseHelper("done", inputStream, deserialize.getExtension());
		}
		else {
			return new ResponseHelper("passwordNot", null);
		}
	}
	
}
