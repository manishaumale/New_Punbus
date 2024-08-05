package com.idms.base.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.IndentDto;
import com.idms.base.api.v1.model.dto.ItemFormDto;
import com.idms.base.dao.entity.Indent;
import com.idms.base.dao.entity.IndentChildEntity;
import com.idms.base.dao.entity.ItemTypeMaster;
import com.idms.base.service.IndentService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/indent")
@Log4j2
public class IndentController {
	
	
@Autowired
IndentService service;

@ApiOperation("Returns List of ItemtypeList Details")
@GetMapping(value="/getallItemList")
public ResponseEntity<List<ItemTypeMaster>> getAllItemList()
{
	log.info("Fetch the all the itemlist ");
	List<ItemTypeMaster> allItemMasterList = service.getAllItemMasterList();
	return new ResponseEntity<List<ItemTypeMaster>>(allItemMasterList,HttpStatus.OK);
	
}



@ApiOperation("Returns List of ItemTypeDetails")
@GetMapping(value="/getAllItemDetails/{id}")
public ResponseEntity<List<ItemFormDto>> getAllItemDetails(@PathVariable("id")Integer id)
{
	log.info("Fetch the all ItemFormLoaddetails ");
	List<ItemFormDto> allItemFormLoad = service.getAllItemFormLoad(id);
	return new ResponseEntity<List<ItemFormDto>>( allItemFormLoad,HttpStatus.OK);
	
}

@ApiOperation("Creates a new Indent returning status 200 when persisted successfully")
@PostMapping(value = "/addIndentDetails", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<ResponseStatus> addIndentDetails(@RequestBody Indent indent) 
{
  log.info(" Enter into Indent service");
	return this.service.saveIndentDetails(indent);

}



@ApiOperation("Returns List of Indent Details")
@GetMapping(value="/getIndentDetails/{depoCode}")
public ResponseEntity<List<IndentDto>> getIndentDetails(@PathVariable("depoCode") String depoCode)
{
	log.info("Fetch the Indent details ");
	List<IndentDto> indentDetails = service.getIndentDetails(depoCode);
	return new ResponseEntity<List<IndentDto>>(indentDetails,HttpStatus.OK);
	
}

@ApiOperation("Returns ItemDetails by id")
@GetMapping(value="/getAllIndentItemdetails/{id}")
public ResponseEntity<List<IndentDto>>getAllIndentItemdetails(@PathVariable("id")Integer id)
{
	log.info("Fetch the all Items details based on indentid ");
	  List<IndentDto> allItemDetails = service.getAllItemDetailsById(id);
	return new ResponseEntity<List<IndentDto>>(allItemDetails,HttpStatus.OK);
	
}



@ApiOperation("Delete the status of specific Indent by its identifier. ")
@PutMapping(path = "/deleteIndentStatusFlag/{id}")
public ResponseEntity<ResponseStatus> deleteIndentStatusFlag(@PathVariable("id") Integer id) {
	log.info("delete Indent based on indentId ");
	return this.service.deleteIndentStatusFlag(id);
	
}


@ApiOperation("Update the status of specific Indent by its identifier. ")
@PutMapping(path = "/updateIndentStatus/{id}")
public ResponseEntity<ResponseStatus> updateIndentStatus(@PathVariable("id") Integer id)
{
	log.info("update Indent Status by indentId ");
	return this.service.updateIndentStatus(id);
}



@ApiOperation("Returns PrevousDemandQuantityAndPreviousSupplyDate by id")
@GetMapping(value="/getPrevousDemandQuantityAndPreviousSupplyDate/{itemid}/{manufactureid}/{measurementId}/{specificationId}")
public ResponseEntity<IndentDto>getAllIndentItemdetails(@PathVariable("itemid") Integer itemid,@PathVariable("manufactureid")Integer manufactureid,@PathVariable("measurementId")Integer measurementId,
@PathVariable("specificationId")Integer specificationId)
{
	log.info("Fetch the all Items details based on indentid ");
	  IndentDto indentdtls = service.getPrevousDemandQuantityAndPreviousSupplyDate(itemid, manufactureid, measurementId, specificationId);
	return new ResponseEntity<IndentDto>(indentdtls ,HttpStatus.OK);
	
}


@ApiOperation("Creates a new IndentChild returning status 200 when persisted successfully")
@PostMapping(value = "/addIndentChildDetails/{indentId}", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<ResponseStatus> addIndentDetails(@RequestBody List<IndentChildEntity> indentChildEntity,@PathVariable("indentId") Integer indentId) 
{
  log.info(" Enter into Indent service");
	return this.service.saveIndentChildDetails(indentChildEntity,indentId);

}

@ApiOperation("Delete the status of specific Indentchildlist by its identifier. ")
@PutMapping(path = "/deleteIndentChildStatusFlag/{id}")
public ResponseEntity<ResponseStatus> deleteIndentChildStatusFlag(@PathVariable("id") Integer id) {
	log.info("delete IndentChild by Id ");
	return this.service.deleteIndentChildStatusFlag(id);
	
}

@ApiOperation("Update the status of specific Indent by its identifier. ")
@PutMapping(path = "/updateIndentChildQuantity/{itemQuantity}/{id}")
public ResponseEntity<ResponseStatus> IndentChildQuantity(@PathVariable("itemQuantity")Float itemQuantity,@PathVariable("id")Integer id)
{
	log.info("update quantity based on indentChildId ");
	return this.service.updateIndentChildQuantity(itemQuantity,id);
}





}
