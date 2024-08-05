package com.idms.base.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_book_reading_closing")
@EqualsAndHashCode(callSuper = true)
public class BookReadingClosingMaster extends BaseEntity {
	
	
	private static final long serialVersionUID = -1063874510660616183L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookReadingClosingId")
    private Integer id;
	
	private String name;
	
	private String shortCode;

}
