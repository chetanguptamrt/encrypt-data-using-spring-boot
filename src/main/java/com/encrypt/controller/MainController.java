package com.encrypt.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.encrypt.helper.ResponseHelper;
import com.encrypt.service.FileService;

@Controller
public class MainController {
	
	@Autowired
	private FileService fileService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		model.addAttribute("title", "Home | EncrypDt");
		return "index";
	}
	
	@RequestMapping(value = "/encrypt", method = RequestMethod.GET)
	public String encrypt(Model model) {
		model.addAttribute("title", "Encrypt File | EncrypDt");
		return "encrypt";
	}
	
	@RequestMapping(value = "/decrypt", method = RequestMethod.GET)
	public String decrypt(Model model) {
		model.addAttribute("title", "Decrypt File | EncrypDt");
		return "decrypt";
	}
	
	@ResponseBody
	@RequestMapping(value = "/file-upload", method = RequestMethod.POST)
	public ResponseEntity<ResponseHelper> upload(@RequestParam("image") MultipartFile file, 
							@RequestParam("password") String password,
							HttpSession httpSession) {
		if(password.trim().equals("") || file.isEmpty()) {
			return ResponseEntity.ok(new ResponseHelper("no", null)); 
		}
		try {
			String fileName = httpSession.getId()+"_"+new Date().getTime();
			ResponseHelper responseHelper = new ResponseHelper("done", this.fileService.uploadData(file, password,fileName));
//			System.out.println(responseHelper);
			return ResponseEntity.ok(responseHelper);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.ok(new ResponseHelper("fileError", null));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/file-decyrpt", method = RequestMethod.POST)
	public ResponseEntity<ResponseHelper> decrypt(@RequestParam("image") MultipartFile file, 
							@RequestParam("password") String password) {
		//get File extension
		String[] split = file.getOriginalFilename().split("\\.");
		String extension = split[split.length-1];
		if( !extension.equals("cgs")) {
			return ResponseEntity.ok(new ResponseHelper("extentionError", null));	
		}
		if(password.trim().equals("") || file.isEmpty()) {
			return ResponseEntity.ok(new ResponseHelper("no", null)); 
		}
		try {
			return ResponseEntity.ok(
					this.fileService.decryptData(file, password));
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.ok(new ResponseHelper("fileError", null));
		}
	}

	@RequestMapping(value = "/img/data/{path}", method = RequestMethod.GET)
	public String privateFolder() {
		return "error/404";
	}
	
}
