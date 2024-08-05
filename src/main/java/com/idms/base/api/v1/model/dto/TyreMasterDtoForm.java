package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class TyreMasterDtoForm {
	 private Integer id;
	  private String tyreNumber;
	  public TyreMasterDtoForm(Integer id, String tyreNumber) {
			this.id = id;
			this.tyreNumber = tyreNumber;
		}

}
