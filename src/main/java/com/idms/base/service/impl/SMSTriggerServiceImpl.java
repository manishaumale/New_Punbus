package com.idms.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.SMSController;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.MessageLogs;
import com.idms.base.dao.entity.MessageTemplates;
import com.idms.base.dao.entity.SystemSettings;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.MessageLogsRepository;
import com.idms.base.dao.repository.MesssageTemplatesRepository;
import com.idms.base.dao.repository.SystemSettingsRepository;

@Service
public class SMSTriggerServiceImpl {

	@Autowired
	SMSController smsController;
	
	@Autowired
	MesssageTemplatesRepository messageTemplatesRepo;
	
	@Autowired
	DriverMasterRepository driverMasterRepo;
	
	@Autowired
	ConductorMasterRepository conductorMasterRepo;
	
	@Autowired
	MessageLogsRepository messageLogsRepo;
	
	@Autowired
	SystemSettingsRepository systemSettingsRepo;
	
	// pass status as Approved 
	public String sendAbsentSMS(Integer driverId, String status,
			String driverConductorFlg,Date date) throws Exception {
		String output = "";
		MessageLogs log= null;
		MessageTemplates templates = messageTemplatesRepo.findByCode("Absent");																					
		Object[] settings  =systemSettingsRepo.findCredentials();
		String userName=settings[0].toString();
		String pass=settings[1].toString();
		String secureId=settings[2].toString();
		String senderId=settings[3].toString();
		if (templates!=null && driverConductorFlg.equals("D")) {
			Optional<DriverMaster> driver = driverMasterRepo.findById(driverId);
			String message = smsText(templates.getMessage_content(), driver.get().getDriverName(),date,driver.get().getDriverCode());
			output = smsController.sendSingleSMS(userName, pass, message, senderId, driver.get().getMobileNumber(), secureId, templates.getMessage_id());
			if(output.equals("sent")) {
				log = new MessageLogs();
				log.setMessage(message);
				log.setLog_date(new Date());
				log.setResponse(output);
				log.setMessageId(templates);
				log.setPhoneNumber(driver.get().getMobileNumber());
				messageLogsRepo.save(log);
			}
		} 
		else if (templates!=null && driverConductorFlg.equals("C")) {
			Optional<ConductorMaster> conductor = conductorMasterRepo.findById(driverId);
			String message = smsText(templates.getMessage_content(), conductor.get().getConductorName(),date,conductor.get().getConductorCode());
			output = smsController.sendSingleSMS(userName, pass, message, senderId, conductor.get().getMobileNumber(), secureId, templates.getMessage_id());
			if(output.equals("sent")) {
				log = new MessageLogs();
				log.setMessage(message);
				log.setLog_date(new Date());
				log.setResponse(output);
				log.setMessageId(templates);
				log.setPhoneNumber(conductor.get().getMobileNumber());
				messageLogsRepo.save(log);
			}}
		return output;
	}
	
	private String smsText(String templateContent,String driverName,Date date,String code) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
		Matcher m = Pattern.compile("#var#").matcher(templateContent);
		StringBuffer sb = new StringBuffer();
		for( int i = 1; m.find(); i++){
			if(i==1) {
		    m.appendReplacement(sb, driverName);
			} else if(i==2) {
			    m.appendReplacement(sb, code);
			} else if(i==3) {
			    m.appendReplacement(sb, sdf.format(date));
			}
			
		}
		m.appendTail(sb);
		String s = sb.toString().replace("{", "");
		s=s.replace("}","");
		return s;
	}
}
