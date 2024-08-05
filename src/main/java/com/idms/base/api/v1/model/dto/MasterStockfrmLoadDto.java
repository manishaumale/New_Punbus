package com.idms.base.api.v1.model.dto;

import java.util.List;


import lombok.Data;

/**
 * @author 1002902
 * */
@Data
public class MasterStockfrmLoadDto {
	
	
	public MasterStockfrmLoadDto() {
		super();
	}

	//private List<Integer> DenominationList;//done
	private List<DenominationDto> denominations;
	
	private  List<StateDto>  Statelist; //done

	//private List<String> transportunitlist;
	private List<TransportDto> transportunitlist;
	
	private List<TicketBookCountDto> ticketCountList;
	
	private List<BookPerBundleDto> bpbList;
	
	
	
	
}
