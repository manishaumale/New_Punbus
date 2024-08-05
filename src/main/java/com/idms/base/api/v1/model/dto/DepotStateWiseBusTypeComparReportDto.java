package com.idms.base.api.v1.model.dto;

public class DepotStateWiseBusTypeComparReportDto {

	private String date;
	private String diesel;
	private String kmGross;
	private String kmpl;
	private String mOil;
	private String mOil1000kmps;

	private String dateOfPreviousRoute;

	private String dateOfPreviousYear;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDiesel() {
		return diesel;
	}

	public void setDiesel(String diesel) {
		this.diesel = diesel;
	}

	public String getKmGross() {
		return kmGross;
	}

	public void setKmGross(String kmGross) {
		this.kmGross = kmGross;
	}

	public String getKmpl() {
		return kmpl;
	}

	public void setKmpl(String kmpl) {
		this.kmpl = kmpl;
	}

	public String getmOil() {
		return mOil;
	}

	public void setmOil(String mOil) {
		this.mOil = mOil;
	}

	public String getmOil1000kmps() {
		return mOil1000kmps;
	}

	public void setmOil1000kmps(String mOil1000kmps) {
		this.mOil1000kmps = mOil1000kmps;
	}

	public String getDateOfPreviousRoute() {
		return dateOfPreviousRoute;
	}

	public void setDateOfPreviousRoute(String dateOfPreviousRoute) {
		this.dateOfPreviousRoute = dateOfPreviousRoute;
	}

	public String getDateOfPreviousYear() {
		return dateOfPreviousYear;
	}

	public void setDateOfPreviousYear(String dateOfPreviousYear) {
		this.dateOfPreviousYear = dateOfPreviousYear;
	}

}
