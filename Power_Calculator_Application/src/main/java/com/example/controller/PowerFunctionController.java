package com.example.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DataDTO;
import com.example.service.PowerFunctionImpl;

@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/powercalculator")
@RestController
public class PowerFunctionController {

	PowerFunctionImpl service=new PowerFunctionImpl();

	@PostMapping("/calculate")
	public int calculation(@RequestBody DataDTO dataDTO) {
		System.out.println("in controller");

		int response = service.calculatePower(dataDTO);
		return response;
	}

}
