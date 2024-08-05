package com.idms.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.IndentDto;
import com.idms.base.api.v1.model.dto.ItemFormDto;
import com.idms.base.api.v1.model.dto.ItemNameDto;
import com.idms.base.api.v1.model.dto.ManufactureDto;
import com.idms.base.api.v1.model.dto.MeasurementDto;
import com.idms.base.api.v1.model.dto.SpecificationDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.Indent;
import com.idms.base.dao.entity.IndentChildEntity;
import com.idms.base.dao.entity.ItemTypeMaster;
import com.idms.base.dao.entity.MasterStatus;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.IndentChildRepository;
import com.idms.base.dao.repository.IndentRepository;
import com.idms.base.dao.repository.ItemNameMasterRepository;
import com.idms.base.dao.repository.ItemTypeRepository;
import com.idms.base.dao.repository.ManufactureRepository;
import com.idms.base.dao.repository.MasterStatusRepository;
import com.idms.base.dao.repository.MeasurementRepository;
import com.idms.base.dao.repository.SpecificationRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.IndentService;
import com.idms.base.support.persist.ResponseStatus;

@Service
public class IndentServiceImpl implements IndentService {

	@Autowired
	ItemTypeRepository itemRepo;
	@Autowired
	IndentRepository indentRepo;
	@Autowired
	TransportUnitRepository transUnitRepo;
	@Autowired
	DepotMasterRepository depoRepo;
	@Autowired
	MasterStatusRepository masterStatusRepo;
	@Autowired
	SpecificationRepository specificationRepo;
	@Autowired
	ManufactureRepository manufactureRepo;
	@Autowired
	MeasurementRepository measurementRepo;
	@Autowired
	ItemNameMasterRepository itemNameRepo;
	@Autowired
	IndentChildRepository indentChildRepo;

	@Override
	public List<ItemTypeMaster> getAllItemMasterList() {

		List<ItemTypeMaster> itemdtls = new ArrayList<>();
		try {
			List<ItemTypeMaster> itemList = itemRepo.findAll();
			for (ItemTypeMaster ob : itemList) {
				ItemTypeMaster item = new ItemTypeMaster();
				if(ob.getId()!=null)
				item.setId(ob.getId());
				if(ob.getName()!=null)
				item.setName(ob.getName());
				itemdtls.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemdtls;
	}

	@Override
	public List<ItemFormDto> getAllItemFormLoad(Integer id) {
		List<ItemFormDto> itemformlist = new ArrayList();
		ItemFormDto itemForm = new ItemFormDto();
		try {

			List<ItemNameDto> itemname = new ArrayList();
			List<Object[]> itemList = itemNameRepo.findByItemTypeId(id);
			for (Object[] ob : itemList) {
				ItemNameDto item = new ItemNameDto();
				if (ob[0] != null)
					item.setId(Integer.parseInt(ob[0].toString()));
				if (ob[1] != null)
					item.setName(ob[1].toString());
				itemname.add(item);

			}
			itemForm.setItemNames(itemname);
			List<Object[]> itemmeasure = measurementRepo.findByItemTypeId(id);

			List<MeasurementDto> measurelist = new ArrayList();
			for (Object[] ob : itemmeasure) {
				MeasurementDto item = new MeasurementDto();
				if (ob[0] != null)
					item.setId(Integer.parseInt(ob[0].toString()));
				if (ob[1] != null)
					item.setName(ob[1].toString());
				measurelist.add(item);
			}
			itemForm.setMeasurement(measurelist);

			List<ManufactureDto> manufacturelist = new ArrayList();
			List<Object[]> manufacture = manufactureRepo.findByItemTypeId(id);
			for (Object[] ob : manufacture) {
				ManufactureDto item = new ManufactureDto();
				if (ob[0] != null)
					item.setId(Integer.parseInt(ob[0].toString()));
				if (ob[1] != null)
					item.setName(ob[1].toString());
				manufacturelist.add(item);

			}
			itemForm.setManufacture(manufacturelist);
			List<SpecificationDto> specificationlist = new ArrayList();
			List<Object[]> specification = specificationRepo.findByItemTypeId(id);

			for (Object[] ob : specification) {
				SpecificationDto item = new SpecificationDto();
				if (ob[0] != null)
					item.setId(Integer.parseInt(ob[0].toString()));
				if (ob[1] != null)
					item.setName(ob[1].toString());
				specificationlist.add(item);

			}
			itemForm.setSpecification(specificationlist);
			itemformlist.add(itemForm);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemformlist;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveIndentDetails(Indent indent) {

		try {
			
			 Integer id = indent.getDepot().getId();
			
			Optional<DepotMaster> findById = depoRepo.findById(id);
			
			indent.setIsDeleted(false);
			List<IndentChildEntity> indentchild = indent.getIndentchild();
			Integer itemtype = null;
			Optional<ItemTypeMaster> itemlist=null;
			for (IndentChildEntity o : indentchild) {
				itemtype = o.getItemtype().getId();
				 itemlist = itemRepo.findById(itemtype);
				o.setIsDeleted(false); 
                o.setIndentObj(indent);
			}	

			
			String item = itemlist.get().getName();

			String depotName = findById.get().getDepotName();

			String depotName1 = depotName.replaceAll("-", "");

			String substring = depotName1.substring(depotName1.length() - 4);

			String stringdp = substring.replaceAll("\\d", "");

			Date date = new Date();
			SimpleDateFormat DateFor = new SimpleDateFormat("yyyy/MM/dd");
			String format = DateFor.format(date);

			MasterStatus findAllByStatusName = masterStatusRepo.findAllByStatusName("Draft");
			Optional<MasterStatus> masterStatus = masterStatusRepo.findById(findAllByStatusName.getId());

			indent.setMasterstatus(masterStatus.get());

			List<Object[]> findbyIndentcount = indentRepo.findbyIndentcount();

			int count = 0;

			for (Object[] ob : findbyIndentcount) {
				Indent indent1 = new Indent();
				String createdDate = ob[0].toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String indentcreatedate = sdf.format(date);
				if (indentcreatedate.equals(createdDate)) {
					if(ob[1] != null)
					count = Integer.parseInt(ob[1].toString());
					count++;
					indent.setCount(count);
					indent.setIdentNumber("Indent" + "/" + item + "/" + stringdp + "/" + format + "/" + "000" + count);

				} else {
					count++;
					indent.setCount(count);

					indent.setIdentNumber("Indent" + "/" + item + "/" + stringdp + "/" + format + "/" + "000" + count);
				}
			}
			Indent indentdtls = indentRepo.save(indent);
			if (indentdtls.getId() != null) {
				return new ResponseEntity<>(
						new ResponseStatus("Indentform has been persisted successfully.", HttpStatus.OK), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}

		return null;

	}

	@Override
	public List<IndentDto> getIndentDetails(String depoCode)

	{
		List<IndentDto> indentdtls = new ArrayList<IndentDto>();
		try {

			DepotMaster findByDepotCode = depoRepo.findByDepotCode(depoCode);

			List<Object[]> indentlist = indentRepo.findbydepoCode(findByDepotCode.getId());
			for (Object[] ob : indentlist) {
				IndentDto indent = new IndentDto();
				if (ob[0] != null)
					indent.setId(Integer.parseInt(ob[0].toString()));
				if (ob[1] != null)
					indent.setIdentNumber(ob[1].toString());
				TransportUnitMaster trans = new TransportUnitMaster();
				if (ob[2] != null)
					trans.setTransportName(ob[2].toString());
				indent.setTransportUnit(trans);
				DepotMaster depo = new DepotMaster();
				if (ob[3] != null)
					depo.setDepotCode(ob[3].toString());
				indent.setDepot(depo);
				MasterStatus status = new MasterStatus();
				if (ob[4] != null)
					status.setName(ob[4].toString());
				indent.setIndentStatus(status);
				if (ob[5] != null)
					indent.setIndentRaisedOn(ob[5].toString());

				indentdtls.add(indent);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return indentdtls;

	}

	@Override
	public List<IndentDto> getAllItemDetailsById(Integer id) {
		List<IndentDto> itemList = new ArrayList<>();
		try {
			List<Object[]> indentChildListItems = indentRepo.findByIndentChildListItems(id);
			for (Object[] ob : indentChildListItems) {
				IndentDto indent = new IndentDto();
				if (ob[0] != null)
					indent.setId(Integer.parseInt(ob[0].toString()));
				if (ob[1] != null)
					indent.setManufacture(ob[1].toString());
				if (ob[2] != null)
					indent.setMeasurementUnitName(ob[2].toString());
				if (ob[3] != null)
					indent.setItemSpecification(ob[3].toString());
				if (ob[4] != null)
					indent.setItemType(ob[4].toString());
				if (ob[5] != null)
					indent.setItemName(ob[5].toString());
				if (ob[6] != null)
					indent.setItemQuantity(Float.parseFloat(ob[6].toString()));
				if (ob[7] != null)
					indent.setJustification(ob[7].toString());
				if (ob[8] != null)
					indent.setTargetDateToReceive(ob[8].toString());
				if (ob[9] != null)
					indent.setManufactureId(Integer.parseInt(ob[9].toString()));
				if (ob[10] != null)
					indent.setMeasurementId(Integer.parseInt(ob[10].toString()));
				if (ob[11] != null)
					indent.setSpecificationId(Integer.parseInt(ob[11].toString()));
				if (ob[12] != null)
					indent.setItemId(Integer.parseInt(ob[12].toString()));
				if (ob[13] != null)
					indent.setItemNameId(Integer.parseInt(ob[13].toString()));
				
				
				itemList.add(indent);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemList;
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> deleteIndentStatusFlag(Integer id) {

		try {
			int i = indentRepo.deleteIndentMasterStatusFlag(true, id);
			if (i == 1)
				return new ResponseEntity<>(new ResponseStatus("Indent has been deleted successfully.", HttpStatus.OK),
						HttpStatus.OK);
			else
				return new ResponseEntity<>(
						new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.FORBIDDEN);
		}

	}

	@Transactional
	@Override
	public ResponseEntity<ResponseStatus> updateIndentStatus(Integer id) {

		try {
			MasterStatus findAllByStatusName = masterStatusRepo.findAllByStatusName("Pending");
			Optional<MasterStatus> masterStatus = masterStatusRepo.findById(findAllByStatusName.getId());
			Integer statusid = masterStatus.get().getId();
			int i = indentRepo.updateIndentMasterStatus(statusid, id);

			if (i == 1)
				return new ResponseEntity<>(
						new ResponseStatus("IndentStatus has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			else
				return new ResponseEntity<>(
						new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.FORBIDDEN);
		}

	}

	@Override
	public IndentDto getPrevousDemandQuantityAndPreviousSupplyDate(Integer itemid, Integer manufactureid,
			Integer measurementId, Integer specificationId) {

		IndentDto indent = new IndentDto(); 
		try {
			List<Object[]> indentDtls = indentRepo.findbyPrevousDemandQuantityAndPreviousSupplyDate(itemid,
					manufactureid, measurementId, specificationId);
			for (Object[] ob : indentDtls) {

				
				
				if (ob[0] != null)
					indent.setIndentChildId(Integer.parseInt(ob[0].toString()));
				if (ob[1] != null)
					indent.setPrevioueDemandedQuantity(Float.parseFloat(ob[1].toString()));
				if (ob[2] != null)
					indent.setPreviousSuppliedDate(ob[2].toString());
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return indent;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveIndentChildDetails(List<IndentChildEntity> indentChildEntity,Integer indentId)
	{
		try {
			
			Indent indentObject=null;
			for(IndentChildEntity obj : indentChildEntity)
			{
				IndentChildEntity indent= new IndentChildEntity();
				
				indent.setItemQuantity(obj.getItemQuantity());
				indent.setMeasurementUnit(obj.getMeasurementUnit());
				indent.setItemManufacture(obj.getItemManufacture());
				indent.setItemSpecification(obj.getItemSpecification());
				indent.setTargetDateToReceive(obj.getTargetDateToReceive());
				indent.setJustification(obj.getJustification());
				indent.setItemNameMaster(obj.getItemNameMaster());
				indent.setItemtype(obj.getItemtype());
				indent.setItemSpecifications(obj.getItemSpecifications());
				indent.setMeasurementUnitName(obj.getMeasurementUnitName());
				indent.setItemName(obj.getItemName());
				indent.setItemSupplier(obj.getItemSupplier());
				indentObject = indentRepo.findById(indentId).get();
		        indent.setIndentObj(indentObject);
		        indent.setIsDeleted(false);
			    indentChildRepo.save(indent);
			}
				return new ResponseEntity<>(
						new ResponseStatus("IndentChildEntity has been persisted successfully.",HttpStatus.OK),
						HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.",HttpStatus.FORBIDDEN),
					HttpStatus.FORBIDDEN);
		}
		
	}
    @Transactional
	@Override
	public ResponseEntity<ResponseStatus> deleteIndentChildStatusFlag(Integer id) {
		try {
			
			int i = indentChildRepo.deleteIndentChildEntityStatusFlag(true, id);
			if (i == 1)
				return new ResponseEntity<>(new ResponseStatus("Indent has been deleted successfully.", HttpStatus.OK),
						HttpStatus.OK);
			else
				return new ResponseEntity<>(
						new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.FORBIDDEN);
		}

	}

    @Transactional
	@Override
	public ResponseEntity<ResponseStatus> updateIndentChildQuantity(Float itemQuantity,Integer id)
	{
		
      try {
			
			int i = indentChildRepo.updateChildIndentQuantity(itemQuantity,id);
			if (i == 1)
				return new ResponseEntity<>(new ResponseStatus("IndentChild has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			else
				return new ResponseEntity<>(
						new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.FORBIDDEN);
		}
		
	}

}
