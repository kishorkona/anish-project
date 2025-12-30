package com.anish.controllers;

import com.anish.data.Emp;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("testrest")
public class KishorRestControllers {
	
	Gson gson = new Gson();
	
	@GetMapping(value = "/getEmployee")
	public String getEmployee() {
		Emp e = new Emp();
		e.setEmpName("kishor");
		e.setEmpId(300);
		e.setAge(35);
		return gson.toJson(e);
	}
	
	//(Using @PathVariable)
	//http://localhost:5050/anishtestsnew/testrest/checkPathVariable/kishor/100
	@GetMapping(value = "/checkPathVariable/{empName}/{empId}")
	public String checkPathVariable(
			@PathVariable("empName") String empName, 
			@PathVariable("empId") Integer empId) {
		System.out.println("values empName="+empName+",empId="+empId);
		return "success";
	}
	
	//(Using @RequestParam)
	//http://localhost:5050/anishtestsnew/testrest/checkPathVariable/kishor/100
	@GetMapping(value = "/checkRequestParam")
	public String checkRequestParam(
			@RequestParam("empName") String empName, 
			@RequestParam("empId") Integer empId) {
		System.out.println("values empName="+empName+",empId="+empId);
		return "success";
	}
	
	//(Using Both @PathVariable, @RequestParam)
	//http://localhost:5050/anishtestsnew/testrest/bothRequestAndPathParam/kishor?empId=100&age=35
	@GetMapping(value = "/bothRequestAndPathParam/{empName}")
	public String bothRequestAndPathParam(
			@PathVariable("empName") String empName, 
			@RequestParam("empId") Integer empId,
			@RequestParam("age") Integer age) {
		System.out.println("values empName="+empName+",empId="+empId);
		return "success";
	}
	
	//(Using Both @RequestHeader)
	//http://localhost:5050/anishtestsnew/testrest/usingHeader/kishor
	@GetMapping(value = "/usingHeader/{empName}")
	public String usingHeader(
			@PathVariable("empName") String empName, 
			@RequestHeader("empId") Integer empId,
			@RequestHeader("age") Integer age) {
		System.out.println("values empName="+empName+",empId="+empId+",age="+age);
		return "success";
	}
	
	//(Using Both @RequestBody)
	//http://localhost:5050/anishtestsnew/testrest/usingRequestBody
	@PostMapping(value = "/usingRequestBody")
	public String usingRequestBody(@RequestBody Emp emp) {
		System.out.println("values="+emp.toString());
		return "success";
	}
	
	//(Using Both @RequestBody)
	//http://localhost:5050/anishtestsnew/testrest/usingModelAttribute
	@PostMapping(value = "/usingModelAttribute")
	public String usingModelAttribute(@ModelAttribute Emp emp) {
		System.out.println("values="+emp.toString());
		return "success";
	}		
	
}
