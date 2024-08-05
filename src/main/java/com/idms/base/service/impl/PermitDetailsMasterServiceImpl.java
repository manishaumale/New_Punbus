package com.idms.base.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.idms.base.api.v1.model.dto.BusTypeDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.PermitDetailsFormDto;
import com.idms.base.api.v1.model.dto.StateDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.api.v1.model.dto.UserDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.Document;
import com.idms.base.dao.entity.PermitDetailsMaster;
import com.idms.base.dao.entity.RoutePermitMaster;
import com.idms.base.dao.entity.StateWiseKmMaster;
import com.idms.base.dao.entity.ViaInformation;
import com.idms.base.dao.repository.BusSubTypeMasterRepository;
import com.idms.base.dao.repository.BusTyperMasterRepository;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DocumentRepository;
import com.idms.base.dao.repository.PermitDetailsMasterRepository;
import com.idms.base.dao.repository.RouteMasterRepository;
import com.idms.base.dao.repository.StateMasterRepository;
import com.idms.base.dao.repository.TaxMasterRepository;
import com.idms.base.dao.repository.TaxTypeMasterRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.PermitDetailsMasterService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PermitDetailsMasterServiceImpl implements PermitDetailsMasterService{
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	PermitDetailsMasterRepository permitDetailsMasterRepository;
	
	@Autowired
	StateMasterRepository stateMasterRepository;
	
	@Autowired
	CityMasterRepository cityMasterRepository;
	
	@Autowired
	TransportUnitRepository transportUnitRepository;
	
	@Autowired
	DepotMasterRepository depotMasterRepository;
	
	@Autowired
	TaxTypeMasterRepository taxTypeMasterRepository;
	
	@Autowired
	BusTyperMasterRepository busTyperMasterRepository;
	
	@Autowired
	BusSubTypeMasterRepository busSubTypeMasterRepository;
	
	@Autowired
	TaxMasterRepository taxMasterRepository;
	
	@Autowired
	DocumentRepository documentRepository;
	
	@Autowired
	RouteMasterRepository routeMasterRepository;
	
	@Value("${file.path}")
	private String filePath;
	
	@Override
	public List<PermitDetailsMaster> getallPermitDetailsMaster(String dpCode) {
		log.info("Entering into getallPermitDetailsMaster service");
		List<PermitDetailsMaster> permitDetailsMaster = null;
		try {
			//permitDetailsMaster = permitDetailsMasterRepository.findAllByStatus(true);
			//permitDetailsMaster = permitDetailsMasterRepository.findAll();
			permitDetailsMaster = permitDetailsMasterRepository.findAllByDepotCode(dpCode);
			for(PermitDetailsMaster permitObj : permitDetailsMaster){
				if(permitObj.getDocument() == null){
					Document doc = new Document();
					doc.setId(null);
					doc.setDocumentName("");
					permitObj.setDocument(doc);
					permitDetailsMaster.set(permitDetailsMaster.indexOf(permitObj), permitObj);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return permitDetailsMaster;
	}

	@Override
	public PermitDetailsFormDto permitDetailsMasterLoad(String dpCode) {
		log.info("Entering into permitDetailsMasterLoad service");
		PermitDetailsFormDto permitDto = new PermitDetailsFormDto();
		try {
			List<StateDto> stateMasterList = stateMasterRepository.findAllByStatus(true).stream()
					.map(state -> new StateDto(state.getId(), state.getStateName())).collect(Collectors.toList());
			DepotMaster depotMaster = depotMasterRepository.findByDepotCode(dpCode);
			List<BusTypeDto> busTyperMasterList = busTyperMasterRepository.findAllByStatus(true).stream()
					.map(busType -> new BusTypeDto(busType.getId(), busType.getBusTypeName()))
					.collect(Collectors.toList());
			List<TransportDto> TransportDtoList = transportUnitRepository.allTransportMasterByDepot(depotMaster.getId()).stream()
					.map(transport -> new TransportDto(transport.getTransportUnitMaster().getId(), transport.getTransportUnitMaster().getTransportName()))
					.collect(Collectors.toList());

			if (stateMasterList != null && stateMasterList.size() > 0)
				permitDto.setFromStateList(stateMasterList);
			if (busTyperMasterList != null && busTyperMasterList.size() > 0)
				permitDto.setBusTyperMasterList(busTyperMasterList);
			if (depotMaster != null)
				permitDto.setDepotMasterList(this.mapper.map(depotMaster, DepotMasterDto.class));
			if (TransportDtoList != null && TransportDtoList.size() > 0)
				permitDto.setTransportUnitMasterList(TransportDtoList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return permitDto;
	}

	@Override
	//@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseEntity<ResponseStatus> savePermitMaster(PermitDetailsMaster permitMaster, MultipartFile uploadFile) {
		log.info("Entering into savePermitMaster service");
		List<ViaInformation> viaInfoList = new ArrayList<>();
		List<StateWiseKmMaster> stateWiseList = new ArrayList<>();
		String pattern = "ddMMyyyy";
		String currentDate =new SimpleDateFormat(pattern).format(new Date());
		Document uploadDocument = null;
		try {
			if (permitMaster.getId() == null) {
				if(permitMaster.getPermitNumber() == null || permitMaster.getPermitNumber().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Permit Number is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getPermitIssuingAuthority() == null || permitMaster.getPermitIssuingAuthority().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Permit issuing authority is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getIssueDate() == null || permitMaster.getIssueDate().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Permit issue date is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getValidUpTo() == null || permitMaster.getValidUpTo().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Permit valid upto is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getFromState() == null || permitMaster.getFromState().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("From State is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getToState() == null || permitMaster.getToState().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("To State is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getFromCity() == null || permitMaster.getFromCity().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("From City is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getToCity() == null || permitMaster.getToCity().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("To City is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getBusTyperMaster() == null || permitMaster.getBusTyperMaster().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Bus Type is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getDepotMaster() == null || permitMaster.getDepotMaster().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Depot is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getTotalTrips() == null || permitMaster.getTotalTrips().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Total Trips is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getTotalKms() == null || permitMaster.getTotalKms().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Total Kms is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getPlainKms() == null || permitMaster.getPlainKms().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Plain Kms is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getHillKms() == null || permitMaster.getHillKms().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Hill Kms is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getViaInfoList() == null ||permitMaster.getViaInfoList().size() == 0) {
					return new ResponseEntity<>(new ResponseStatus("Via Info is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(permitMaster.getStateWiseKmList() == null ||permitMaster.getStateWiseKmList().size() == 0) {
					return new ResponseEntity<>(new ResponseStatus("State Wise Kms is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}
				
				if(uploadFile != null && !uploadFile.isEmpty()) {
					File dir = new File(filePath+File.separator+"PermitNo"+File.separator+permitMaster.getPermitNumber()+File.separator+currentDate);
		    		log.info(dir.toPath());
		            if (!dir.exists()) {
		                dir.mkdirs();
		            }
	            	uploadDocument = new Document();
	            	Files.copy(uploadFile.getInputStream(), dir.toPath().resolve(uploadFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
	            	uploadDocument.setContentType(uploadFile.getContentType());
	            	uploadDocument.setDocumentName(uploadFile.getOriginalFilename());
	            	uploadDocument.setDocumentPath(dir+File.separator+uploadDocument.getDocumentName());
	            	uploadDocument = documentRepository.save(uploadDocument);
				}
				
				if(uploadDocument != null && uploadDocument.getId() != null) {
					permitMaster.setDocument(uploadDocument);
				}
				
				for (ViaInformation viaObj : permitMaster.getViaInfoList()) {
					viaObj.setPermitDetailsMaster(permitMaster);
					viaInfoList.add(viaObj);
				}
				for (StateWiseKmMaster stateWiseObj : permitMaster.getStateWiseKmList()) {
					stateWiseObj.setPermitDetails(permitMaster);
					stateWiseList.add(stateWiseObj);
				}
				permitMaster.setViaInfoList(viaInfoList);
				permitMaster.setStateWiseKmList(stateWiseList);
				permitMaster.setIsDeleted(false);
				permitDetailsMasterRepository.save(permitMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Permit detail master has been persisted successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				for (ViaInformation viaObj : permitMaster.getViaInfoList()) {
					viaObj.setPermitDetailsMaster(permitMaster);
					viaInfoList.add(viaObj);
				}
				for (StateWiseKmMaster stateWiseObj : permitMaster.getStateWiseKmList()) {
					stateWiseObj.setPermitDetails(permitMaster);
					stateWiseList.add(stateWiseObj);
				}
				permitMaster.setViaInfoList(viaInfoList);
				
				  permitMaster.setStateWiseKmList(stateWiseList);
				  permitMaster.setFromState(stateMasterRepository.findById(permitMaster.
				  getFromState().getId()).get());
				  permitMaster.setToState(stateMasterRepository.findById(permitMaster.
				  getFromState().getId()).get());
				  permitMaster.setFromCity(cityMasterRepository.findById(permitMaster.
				  getFromCity().getId()).get());
				  permitMaster.setToCity(cityMasterRepository.findById(permitMaster.getToCity()
				  .getId()).get());
				permitDetailsMasterRepository.save(permitMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Permit detail master has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ResponseEntity<ResponseStatus> updatePermitMaster(Integer id, MultipartFile uploadFile,Date issueDate, Date validUpTo) {
	    String pattern = "ddMMyyyy";
	    int i= 0;
		String currentDate = new SimpleDateFormat(pattern).format(new Date());
		PermitDetailsMaster permitMaster = permitDetailsMasterRepository.findById(id).get();
		Document uploadDocument = null;
		if(issueDate == null || issueDate.equals("")) {
			return new ResponseEntity<>(new ResponseStatus("Permit issue date is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			
		}else if(validUpTo == null || validUpTo.equals("")) {
			return new ResponseEntity<>(new ResponseStatus("Permit valid upto date is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			
		}else if(uploadFile == null || uploadFile.equals("")) {
			return new ResponseEntity<>(new ResponseStatus("Document upload is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			
		}
		if(permitMaster != null ) {
			if(uploadFile != null && !uploadFile.isEmpty()) {
    			File dir = new File(filePath+File.separator+"permit"+File.separator+id+File.separator+currentDate);
	    		log.info(dir.toPath());
	            if (!dir.exists())
	                dir.mkdirs(); 
	            try {
	            	uploadDocument = new Document();
	            	Files.copy(uploadFile.getInputStream(), dir.toPath().resolve(uploadFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
	            	uploadDocument.setContentType(uploadFile.getContentType());
	            	uploadDocument.setDocumentName(uploadFile.getOriginalFilename());
	            	uploadDocument.setDocumentPath(filePath+File.separator+"permit"+File.separator+id+File.separator+currentDate+File.separator+uploadFile.getOriginalFilename());
	            	uploadDocument = documentRepository.save(uploadDocument);
	            	if(uploadDocument != null && uploadDocument.getId() != null)
	            	 i = permitDetailsMasterRepository.updatePermitDetailsForRenewal(id,uploadDocument.getId(),issueDate,validUpTo);
	            	if(i == 1)
	            	return new ResponseEntity<>(new ResponseStatus("Permit master has been updated successfully.", HttpStatus.OK), HttpStatus.OK);
	            	else
	            		return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.BAD_REQUEST), HttpStatus.OK);
	            } catch(IOException fe) {
	            	log.info("Something went wrong please try again later.");
					fe.printStackTrace();
					return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.BAD_REQUEST), HttpStatus.OK);		
	            }
			} else {
	    		log.info("File not found.");
				return new ResponseEntity<>(new ResponseStatus("File not found.", HttpStatus.BAD_REQUEST), HttpStatus.OK);			
			}
		}
		return null;
	}
	
	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updatePermitMasterStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updatePermitMasterStatusFlag service");
		try {
			int i = permitDetailsMasterRepository.updatePermitMasterStatusFlag(flag,id);
			if(i == 1)
			return new ResponseEntity<>(new ResponseStatus("Status has been updated successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);	
		} catch (Exception e) {
			e.printStackTrace();
 			return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}
	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updatePermitMasterIsDeletedFlag(Integer id, Boolean flag) {
		log.info("Entering into updatePermitMasterIsDeletedFlag service");
		try {
			List<RoutePermitMaster> routePermitList = routeMasterRepository.findAllRoutesByPermitId(id);
			if(routePermitList != null && routePermitList.size() > 0) {
				return new ResponseEntity<>(new ResponseStatus("Kindly delete associated route first, to delete the permit.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			else {
			int i = permitDetailsMasterRepository.updatePermitMasterIsDeletedFlag(flag,id);
			if(i == 1)
			return new ResponseEntity<>(new ResponseStatus("Permit has been deleted successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
 			return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}

}
