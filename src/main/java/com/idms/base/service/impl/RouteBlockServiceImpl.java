package com.idms.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.RouteBlockDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.RouteBlock;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.RouteBlockRepository;
import com.idms.base.dao.repository.RouteMasterRepository;
import com.idms.base.service.RouteBlockService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RouteBlockServiceImpl implements RouteBlockService {

	@Autowired
	DepotMasterRepository depotMasterRepo;

	@Autowired
	RouteBlockRepository routeBlockRepo;

	@Autowired
	RouteMasterRepository routeMasterRepo;

	@Override
	public List<RouteBlockDto> getRouteBlock(String depotCode) {

		List<RouteBlockDto> list = new ArrayList<>();
		RouteBlockDto dto = null;
		RouteMasterDto routeDto = null;
		// DepotMasterDto depotDto = null;

		try {
			Integer depoMasterId = depotMasterRepo.getIdByDepoName(depotCode);
			List<RouteBlock> routeBlockList = routeBlockRepo.findAllOutByDepot(depoMasterId);
			for (RouteBlock routeBlockObj : routeBlockList) {
				dto = new RouteBlockDto();

				if (routeBlockObj.getRoute() != null) {
					routeDto = new RouteMasterDto();
					routeDto.setId(routeBlockObj.getRoute().getId());
					routeDto.setRouteName(routeBlockObj.getRoute().getRouteName());
					routeDto.setRouteCode(routeBlockObj.getRoute().getRouteCode());
					dto.setRoute(routeDto);

				}
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseEntity<ResponseStatus> saveRouteblock(RouteBlockDto routeBlock) {

		log.info("Entering into saveRouteblock service");

		RouteBlock tempRouteBlock = new RouteBlock();
		//RouteMaster route = new RouteMaster();
		//DepotMaster depot = new DepotMaster();
		Date fromDate, toDate = null;
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (routeBlock.getId() != null) {
				tempRouteBlock.setId(routeBlock.getId());
			}

			RouteMaster routeObj = routeMasterRepo.findById(routeBlock.getRoute().getId()).get();
			tempRouteBlock.setRoute(routeObj);

			DepotMaster DepotObj= depotMasterRepo.findById(routeBlock.getDepot().getId()).get();
			tempRouteBlock.setDepot(DepotObj);			
			
			if (routeBlock.getFromDate() != null && routeBlock.getToDate() != null) {
				fromDate = sm.parse(routeBlock.getFromDate());
				tempRouteBlock.setFromDate(fromDate);
				toDate = sm.parse(routeBlock.getToDate());
				tempRouteBlock.setToDate(toDate);
			}
			tempRouteBlock.setDetailedReason(routeBlock.getDetailedReason());

			routeBlockRepo.save(tempRouteBlock);

			return new ResponseEntity<>(new ResponseStatus("Record persisted successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}

	}

	@Override
	public List<RouteBlockDto> getRouteBlockOnLoad(String depotCode) {

		List<RouteBlockDto> list = new ArrayList<>();
		RouteBlockDto dto = null;
		RouteMasterDto routeDto = null;
		// DepotMasterDto depotDto = null;

		try {
			Integer depoMasterId = depotMasterRepo.getIdByDepoName(depotCode);
			List<RouteBlock> routeBlockList = routeBlockRepo.findAllOutByDepot(depoMasterId);
			for (RouteBlock routeBlockObj : routeBlockList) {
				dto = new RouteBlockDto();

				if (routeBlockObj.getRoute() != null) {
					routeDto = new RouteMasterDto();
					routeDto.setId(routeBlockObj.getRoute().getId());
					routeDto.setRouteName(routeBlockObj.getRoute().getRouteName());
					routeDto.setRouteCode(routeBlockObj.getRoute().getRouteCode());
					dto.setRoute(routeDto);

				}
				
				dto.setFromDate(routeBlockObj.getFromDate().toString());
				dto.setToDate(routeBlockObj.getToDate().toString());
				dto.setDetailedReason(routeBlockObj.getDetailedReason());
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
