package com.idms.base.master.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.master.service.MasterService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(path = "/api/v1/state")
@Validated
@Log4j2
public class StateMasterController {
	
	@Autowired
	MasterService masterService;
	
	@GetMapping("/states")
	public List<String> getStates() {
		List<String> states = new ArrayList<String>();
		states.add("Punjab");
		states.add("Haryana");
		states.add("Himachar Pradesh");
		return states;
	}

}
