package com.idms.base.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.idms.base.dao.entity.IntegrationLog;
import com.idms.base.dao.repository.IntegrationLogRepository;
import com.idms.base.support.rest.RestConstants;

@Component
public class VTS_Util {

	private final RestTemplate restTemplate;

	@Autowired
	private IntegrationLogRepository integrationLog;

	public VTS_Util(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public String VTS_diesel_api(String reg_no, Date date_time, String diesel) throws JSONException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String url = "https://punbus.itracking.in/api_sms/api_diesel.php?reg_no=" + reg_no + "&date_time="
				+ df.format(date_time) + "&diesel=" + String.valueOf(diesel);
		String response = this.restTemplate.getForObject(url, String.class);

		integrationLog.save(Logger(url, response,reg_no,"VTS-DIESEL"));
		return response;

	}

	public String VTS_diesel_distance_api(String reg_no, String date_time) throws JSONException {
		String url = "https://punbus.itracking.in/api_sms/api_diesel_distance.php?reg_no=" + reg_no + "&date_time="
				+ date_time;
		String response = this.restTemplate.getForObject(url, String.class);
		integrationLog.save(Logger(url, response,reg_no,"VTS-DIST"));
		return response;

	}
	
	/*public String VTS_insert_diesel_api(String reg_no, Date date_time, String diesel) throws JSONException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:i:s");
		String url = "https://punbus.itracking.in/api_sms/api_diesel.php?reg_no=" + reg_no + "&date_time="+ df.format(date_time) + "&diesel=" + String.valueOf(diesel);
		String response = this.restTemplate.getForObject(url, String.class);
		integrationLog.save(Logger(url, response,reg_no));
		return response;

	}*/

	public String VTS_tyre_wise_kms_api(String reg_no, String date_time) throws JSONException {
		String url =null;
		boolean isResChange = false;
		if(!reg_no.isEmpty() && !date_time.isEmpty()){
		    url=RestConstants.VTS_TYRE_KMS_API+"?reg_no=" + reg_no + "&date_time="+ date_time;
		}
		else if(!reg_no.isEmpty()){
		    url=RestConstants.VTS_TYRE_KMS_API+"?reg_no=" + reg_no ;
		}
	    else if(!date_time.isEmpty()){
	    	isResChange=true;
		    url=RestConstants.VTS_TYRE_KMS_API+"?date_time="+ date_time;
	    }
		else{
			isResChange=true;
		    url=RestConstants.VTS_TYRE_KMS_API;
		}
				
		String response = this.restTemplate.getForObject(url, String.class);
		if(isResChange)
			response="vts_tyre_kms_details_saveresponse";
		integrationLog.save(Logger(url, response,reg_no,"VTS"));
		return response;

	}

	private IntegrationLog Logger(String api, String response,String busId,String type) {
		IntegrationLog log = new IntegrationLog();
		log.setType(type);
		log.setWay("OUT-GOING");
		log.setApiText(api);
		log.setResponse(response);
		log.setLogDateTime(new Date());
		log.setCreatedBy("Local");
		log.setRegId(busId);
		return log;
	}

}
