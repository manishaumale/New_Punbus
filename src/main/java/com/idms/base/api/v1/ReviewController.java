package com.idms.base.api.v1;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.ReviewCommentsDto;
import com.idms.base.api.v1.model.dto.ReviewMasterDto;
import com.idms.base.service.impl.ReviewServiceImpl;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/review")
public class ReviewController {

	@Autowired
	ReviewServiceImpl service;
	
	@GetMapping("/reviewData")
	public ReviewMasterDto getDetailsOnId(@RequestParam Integer id ,@RequestParam String type,@RequestParam String previousReviewDate) throws ParseException {
		return service.getDetailsOnId(id, type,previousReviewDate);
	}
	
	
	@PostMapping("/saveReviewComments")
	public ResponseEntity<ResponseStatus> saveReviewComments(@RequestParam Integer id , @RequestParam String comments ,@RequestParam String type,@RequestParam String depotCode,@RequestParam Integer categoryCode) {
		return service.saveReviewDetails(comments, id, type, depotCode,categoryCode);
	}
	
	@GetMapping("/getReviewList/{depotCode}")
	public List<ReviewCommentsDto> getReviewList(@PathVariable String depotCode) {
		return service.getReviewCommentsList(depotCode);
	}
	
	@GetMapping("/getReviewEntity/{depotCode}/{type}")
	public List<ReviewCommentsDto> getReviewEntityList(@PathVariable String depotCode,@PathVariable String type) {
		return service.reviewIndividualEntities(depotCode,type);
	}
	
	@GetMapping("/getIndividualHistory")
	public List<ReviewCommentsDto> getIndividualEntityHistory(@RequestParam Integer id ,@RequestParam String type) {
		return service.getIndividualHistoryData(id, type);
	}
}
