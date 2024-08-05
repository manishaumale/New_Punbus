package com.idms.base.api.v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.DailyRoasterAutoDto;
import com.idms.base.service.DailyRoasterServiceAuto;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/dailyRoasterAuto")
@Log4j2
public class DailyRoasterAutoController {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private DailyRoasterServiceAuto service;
	
	
	@ApiOperation("")
	@GetMapping(path = "/generateRoasterAuto/{dpCode}/{tpId}")
	public ResponseEntity<ResponseStatus> generateRoasterForm(@PathVariable("dpCode") String dpCode, @PathVariable("tpId") Integer tpId) {
		log.info("Enter into DailyRoasterController function generateRoasterForm");
		return this.service.generateAutoRoaster(dpCode, tpId, this.getDate(1));
		
	}
	
	public Date getDate(Integer i) {
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, i);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dt = sdf.parse(sdf.format(c.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dt;
	}
	
	@ApiOperation("")
	@GetMapping(path = "/getGeneratedRoasterList/{dpCode}/{tuId}") 
	public List<DailyRoasterAutoDto> getGeneratedRoasterList(@PathVariable("dpCode") String dpCode,@PathVariable("tuId") String tuId) {
		return this.service.getGeneratedRoasterList(dpCode,tuId);
	}

}