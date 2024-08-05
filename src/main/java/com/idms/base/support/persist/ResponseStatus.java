package com.idms.base.support.persist;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseStatus {
	
	String message;
	
	Integer status;

	public ResponseStatus(String message, HttpStatus status) {
		super();
		this.message = message;
		this.status = status.value();
	}
}
