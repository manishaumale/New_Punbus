package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.RouteBlockDto;
import com.idms.base.support.persist.ResponseStatus;

public interface RouteBlockService {

	List<RouteBlockDto> getRouteBlock(String dpCode);

	ResponseEntity<ResponseStatus> saveRouteblock(RouteBlockDto routeBlockDto);

	List<RouteBlockDto> getRouteBlockOnLoad(String dpCode);

}
