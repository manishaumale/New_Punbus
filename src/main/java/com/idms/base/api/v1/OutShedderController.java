package com.idms.base.api.v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.OutShedderDto;
import com.idms.base.dao.entity.OutShedder;
import com.idms.base.service.OutShedderService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/outShedder")
@Log4j2
public class OutShedderController {

	@Autowired
	OutShedderService service;
	
	
	@ApiOperation("Returns List of IN and OUT data of bus using dpcode")
	@GetMapping(path = "/getOutShedder/{dpCode}")
	public List<OutShedderDto> getOutShedder(@PathVariable("dpCode") String dpCode) {		
		log.info("Enter into getOutShedder service");		
		List<OutShedderDto> getOutShedderList = this.service.getOutShedder(dpCode);
		return getOutShedderList;
	}
	

	@ApiOperation("Creates a new Out shedder returning status 200 when persisted successfully.")
	@PostMapping("/saveOutShedder")
	public ResponseEntity<ResponseStatus> saveOutShedder(@RequestBody OutShedderDto outShedder) {
		log.info("Enter into saveOutShedder service");
		return this.service.saveOutShedder(outShedder);
	}
	
	@ApiOperation("Returns List of IN and OUT data of bus using date and busId")
	@GetMapping(path = "/getOutShedderByBusId/{busId}")
	List<OutShedderDto> getOutShedderByBusId(@PathVariable("busId") String busNo) {		
		log.info("Enter into getOutShedderByBusId service");
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null; 
		
        String strDate = sm.format(new Date());
		try {
			date = sm.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<OutShedderDto> getOutShedderObj = this.service.getOutShedderByBusId(date, busNo);
		return getOutShedderObj;
	}
	
}
