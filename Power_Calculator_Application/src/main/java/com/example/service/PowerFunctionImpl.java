package com.example.service;

import org.springframework.stereotype.Service;

import com.example.dto.DataDTO;

@Service
public class PowerFunctionImpl implements IPowerFunction {
	@Override
	public int calculatePower(DataDTO dataDTO) {
		System.out.println("CalculatorService.calculate()");
		int baseValue = dataDTO.getBase();
		int exponentValue = dataDTO.getExponent();

		System.out.println(baseValue);
		System.out.println(exponentValue);

		int resultValue = 1;

		for (int i = 1; i <= exponentValue; i++) {
			resultValue = resultValue * baseValue;
		}
		// TODO Auto-generated method stub
		System.out.println(resultValue);
		return resultValue;
	}

}
