package com.result.management.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.result.management.entities.Result;
import com.result.management.services.ResultService;
import com.result.management.utility.SubClassWrapper;

@RestController
@RequestMapping("/result/*")
@CrossOrigin("*")
public class ResultController {

	@Autowired
	ResultService rService;

	// Handles GET Request to get result details by classId and SubjectId
	@GetMapping("/getForClassSubject/{cId}/{subId}")
	public List<Result> getForClassSubject(@PathVariable("cId") int cId, @PathVariable("subId") int subId) {
		return rService.getForClassSubject(cId, subId);
	}

	// Handles POST Request to save result
	@PostMapping("/saveResult")
	public int saveResult(@RequestBody SubClassWrapper wrapper) {
		return rService.saveResult(wrapper);
	}

	// Handles GET Request to get result details for Class by classId
	@GetMapping("/getResultForClass/{classId}")
	public SubClassWrapper getResultForClass(@PathVariable("classId") int classId) {
		return rService.getResultForClass(classId);
	}

	// Handles GET Request to get result details for student by student Id
	@GetMapping("/getForStudent/{stId}")
	public SubClassWrapper getForStudent(@PathVariable("stId") int stId) {
		return rService.getForStudent(stId);
	}

}
