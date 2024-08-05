package com.idms.base.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.idms.base.api.v1.SMSController;
import com.idms.base.api.v1.model.dto.AlertDto;
import com.idms.base.api.v1.model.dto.BookReadingClosingDto;
import com.idms.base.api.v1.model.dto.DipChartReadingsDto;
import com.idms.base.api.v1.model.dto.DipReadingMasterDto;
import com.idms.base.api.v1.model.dto.DipReadingMasterDto2;
import com.idms.base.api.v1.model.dto.DispensingUnitMasterDto;
import com.idms.base.api.v1.model.dto.ExcessShortDto;
import com.idms.base.api.v1.model.dto.FuelTankFormLoadDto;
import com.idms.base.api.v1.model.dto.FuelTankMasterDto;
import com.idms.base.api.v1.model.dto.ReadingMasterDto;
import com.idms.base.api.v1.model.dto.TankCapacityMasterDto;
import com.idms.base.dao.entity.BookReadingClosingMaster;
import com.idms.base.dao.entity.DUReadingHistory;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DipChartReadings;
import com.idms.base.dao.entity.DispensingUnitMaster;
import com.idms.base.dao.entity.Document;
import com.idms.base.dao.entity.ExplosiveLicenseDetails;
import com.idms.base.dao.entity.FuelTankCleaningHistory;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.MessageLogs;
import com.idms.base.dao.entity.MessageTemplates;
import com.idms.base.dao.entity.ReadingMaster;
import com.idms.base.dao.entity.TankCapacityMaster;
import com.idms.base.dao.entity.User;
import com.idms.base.dao.repository.BookreadingClosingRepo;
import com.idms.base.dao.repository.DUReadingHistoryRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DipChartReadingsRepository;
import com.idms.base.dao.repository.DipReadingMasterRepository;
import com.idms.base.dao.repository.DispensingUnitRepository;
import com.idms.base.dao.repository.DocumentRepository;
import com.idms.base.dao.repository.ExplosiveLicenseRepo;
import com.idms.base.dao.repository.FuelTankCleaningHistoryRepository;
import com.idms.base.dao.repository.FuelTankMasterRepository;
import com.idms.base.dao.repository.MessageLogsRepository;
import com.idms.base.dao.repository.MesssageTemplatesRepository;
import com.idms.base.dao.repository.ReadingMasterRepository;
import com.idms.base.dao.repository.SystemSettingsRepository;
import com.idms.base.dao.repository.TankCapacityMasterRepository;
import com.idms.base.dao.repository.UserRepository;
import com.idms.base.service.FuelTankMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.util.FuelManagementAndCalculationUtility;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FuelTankMasterServiceImpl implements FuelTankMasterService {

	@Autowired
	DipReadingMasterRepository dipReadingMasterRepository;

	@Autowired
	TankCapacityMasterRepository tankCapacityMasterRepository;

	@Autowired
	FuelTankMasterRepository fuelTankMasterRepository;

	@Autowired
	DocumentRepository documentRepository;

	@Autowired
	DepotMasterRepository depotRepository;

	@Autowired
	ExplosiveLicenseRepo explosiveLicenseRepo;

	@Autowired
	FuelTankCleaningHistoryRepository fuelTankCleaningRepo;

	@Autowired
	DipChartReadingsRepository dipChartReadingRepo;

	@Autowired
	UserRepository userRepository;

	@Value("${file.path}")
	private String filePath;

	@Autowired
	SMSController smsController;

	@Autowired
	MesssageTemplatesRepository messageTemplatesRepo;

	@Autowired
	MessageLogsRepository messageLogsRepo;

	@Autowired
	SystemSettingsRepository systemSettingsRepo;
	
	@Autowired
	ReadingMasterRepository readingMasterRepository;
	
	@Autowired
	DispensingUnitRepository dispensingUnitRepository;
	
	@Autowired
	FuelManagementAndCalculationUtility fuelManagementAndCalculationUtility;
	
	@Autowired
	DUReadingHistoryRepository duReadingHistoryRepository;
	
	@Autowired
	BookreadingClosingRepo bookreadingClosingRepo;
	
	
	@Override
	public FuelTankFormLoadDto fuelTankMasterFormOnLoad() {
		log.info("Entering into fuelTankMasterFormOnLoad service");
		FuelTankFormLoadDto fuelTankFormLoadDto = new FuelTankFormLoadDto();
		try {

			List<DipReadingMasterDto> dipReadingList = dipReadingMasterRepository.findAllByStatus(true).stream()
					.map(dipReadingMasterDto -> new DipReadingMasterDto(dipReadingMasterDto.getId(),
							dipReadingMasterDto.getReadingName()))
					.collect(Collectors.toList());
			if (dipReadingList != null && dipReadingList.size() > 0)
				fuelTankFormLoadDto.setDipReadingList(dipReadingList);

			List<TankCapacityMasterDto> tankList = tankCapacityMasterRepository.findAllByStatus(true).stream()
					.map(tankCapacityMasterDto -> new TankCapacityMasterDto(tankCapacityMasterDto.getId(),
							tankCapacityMasterDto.getName(), tankCapacityMasterDto.getCapacity()))
					.collect(Collectors.toList());
			if (tankList != null && tankList.size() > 0)
				fuelTankFormLoadDto.setTankList(tankList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fuelTankFormLoadDto;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveFuelTankMaster(FuelTankMaster fuelTankMaster, MultipartFile uploadFile) {
		log.info("Entering into saveFuelTankMaster service");
		String pattern = "ddMMyyyy";
		String currentDate = new SimpleDateFormat(pattern).format(new Date());
		Document uploadDocument = null;
		ExplosiveLicenseDetails explosiveDetails = null;
		FuelTankCleaningHistory cleaning = null;
		try {
			if (fuelTankMaster.getId() == null) {
				if (uploadFile != null && !uploadFile.isEmpty()) {
					File dir = new File(filePath + File.separator + "fuel" + File.separator + currentDate);
					log.info(dir.toPath());
					if (!dir.exists())
						dir.mkdirs();
					uploadDocument = new Document();
					Files.copy(uploadFile.getInputStream(), dir.toPath().resolve(uploadFile.getOriginalFilename()),
							StandardCopyOption.REPLACE_EXISTING);
					uploadDocument.setContentType(uploadFile.getContentType());
					uploadDocument.setDocumentName(uploadFile.getOriginalFilename());
					uploadDocument.setDocumentPath(filePath + File.separator + "fuel" + File.separator + currentDate
							+ File.separator + uploadDocument.getDocumentName());
					uploadDocument = documentRepository.save(uploadDocument);
				}
				if (uploadDocument != null && uploadDocument.getId() != null) {
					explosiveDetails = new ExplosiveLicenseDetails();
					fuelTankMaster.setExplosiveLicence(uploadDocument);
					explosiveDetails.setDocumentId(uploadDocument);
					explosiveDetails.setExplosiveValidity(fuelTankMaster.getExplosiveLicenceValidity());
					explosiveLicenseRepo.save(explosiveDetails);
					fuelTankMaster.setExplosiveDetails(explosiveDetails);

				}
				DepotMaster depotMaster = depotRepository.findByDepotCode(fuelTankMaster.getDepotCode());
				fuelTankMaster.setDepot(depotMaster);
				fuelTankMaster.setIsDeleted(false);
				// String capacity = fuelTankMaster.getCapacity().getId() ==1 ?
				// "20000" : "9000";
				fuelTankMaster.setTankUniqueId(
						"TANK" + new SimpleDateFormat(pattern).format(fuelTankMaster.getInstallationDate())
								+ getUniqueTankCode());
				cleaning = new FuelTankCleaningHistory();
				cleaning.setTankUniqueId(fuelTankMaster.getTankUniqueId());
				cleaning.setCleaningDate(fuelTankMaster.getCleaningDate());
				fuelTankCleaningRepo.save(cleaning);
				fuelTankMaster.setFueltankcleaninghistory(cleaning);
				TankCapacityMaster tankCapacity = null;
				List<TankCapacityMaster> capacityMasterList = tankCapacityMasterRepository
						.findTankCapacity(fuelTankMaster.getCapacity().getCapacity());
				if (capacityMasterList != null && capacityMasterList.size() > 0) {
					fuelTankMaster.setCapacity(capacityMasterList.get(0));
				} else {
					tankCapacity = new TankCapacityMaster();
					tankCapacity.setCapacity(fuelTankMaster.getCapacity().getCapacity());
					tankCapacity.setName(fuelTankMaster.getTankCode());
					tankCapacityMasterRepository.save(tankCapacity);
					fuelTankMaster.setCapacity(tankCapacity);

				}
				fuelTankMasterRepository.save(fuelTankMaster);
				return new ResponseEntity<>(new ResponseStatus(fuelTankMaster.getTankUniqueId(), HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return null;
	}

	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateFuelTankMasterStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateFuelTankMasterStatusFlag service");
		try {
			int i = fuelTankMasterRepository.updateFuelTankMasterStatusFlag(flag, id);
			if (i == 1)
				return new ResponseEntity<>(new ResponseStatus("Status has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			else
				return new ResponseEntity<>(
						new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	// public List<FuelTankMaster> listOfAllFuelTankMaster(String depotCode) {
	public List<FuelTankMasterDto> listOfAllFuelTankMaster(String depotCode) {
		log.info("Entering into listOfAllFuelTankMaster service");

		List<FuelTankMaster> fuelMasterList = null;
		DepotMaster depotMaster = null;
		List<FuelTankMasterDto> dto = new ArrayList<FuelTankMasterDto>();

		try {

			depotMaster = depotRepository.findByDepotCode(depotCode);
			// fuelMasterList = fuelTankMasterRepository.findAllByStatus(true);
			// fuelMasterList = fuelTankMasterRepository.findAll();
			// dto =
			// fuelTankMasterRepository.findAllByDepotId(depotMaster.getId())
			// .stream().map(tank ->
			// new FuelTankMasterDto(tank.getId(),
			// tank.getTankName(),
			// tank.getTankCode(),
			// tank.getCapacity(),
			// tank.getDiameter(),
			// tank.getLength(),
			// tank.getRadius(),
			// tank.getDeadBoards(),
			// tank.getInstallationDate(),
			// tank.getTankValidity(),
			// tank.getReorderLevel(),
			// tank.getExplosiveLicenceValidity(),
			// tank.getCurrentValue(),
			// tank.getCleaningDate(),
			// tank.getStatus(),
			// tank.getDip()
			// )).collect(Collectors.toList());
			//
			fuelMasterList = fuelTankMasterRepository.findAllByDepotIdAndisDeletedFlag(depotMaster.getId(), false);
			for (FuelTankMaster fuelTankMaster : fuelMasterList) {
				FuelTankMasterDto masterDto = new FuelTankMasterDto();
				masterDto.setId(fuelTankMaster.getId());
				masterDto.setTankName(fuelTankMaster.getTankName());
				masterDto.setTankCode(fuelTankMaster.getTankCode());
				masterDto.setCapacity(fuelTankMaster.getCapacity());
				masterDto.setDiameter(fuelTankMaster.getDiameter());
				masterDto.setLength(fuelTankMaster.getLength());
				masterDto.setRadius(fuelTankMaster.getRadius());
				masterDto.setDeadBoards(fuelTankMaster.getDeadBoards());
				masterDto.setInstallationDate(fuelTankMaster.getInstallationDate());
				masterDto.setTankValidity(fuelTankMaster.getTankValidity());
				masterDto.setReorderLevel(fuelTankMaster.getReorderLevel());
				masterDto.setExplosiveLicenceValidity(fuelTankMaster.getExplosiveLicenceValidity());
				if (fuelTankMaster.getCurrentValue() == null) {
					masterDto.setCurrentValue(0.00);
				} else {
					masterDto.setCurrentValue(fuelTankMaster.getCurrentValue());
				}
				masterDto.setStatus(fuelTankMaster.getStatus());
				DipReadingMasterDto2 dipReadingMasterDto2 = new DipReadingMasterDto2(fuelTankMaster.getDip().getId(),
						fuelTankMaster.getDip().getReadingName(), fuelTankMaster.getDip().getDiameter(),
						fuelTankMaster.getDip().getRadius(), fuelTankMaster.getDip().getLength(),
						fuelTankMaster.getDip().getDeadboard());
				masterDto.setDip(dipReadingMasterDto2);
				masterDto.setCleaningDate(fuelTankMaster.getCleaningDate());
				masterDto.setTankUniqueId(fuelTankMaster.getTankUniqueId());
				masterDto.setExplosiveCertificate(fuelTankMaster.getExplosiveDetails() != null
						&& fuelTankMaster.getExplosiveDetails().getDocumentId() != null
						&& fuelTankMaster.getExplosiveDetails().getDocumentId().getDocumentName() != null
								? fuelTankMaster.getExplosiveDetails().getDocumentId().getDocumentName() : " ");
				if (fuelTankMaster.getExplosiveDetails() != null
						&& fuelTankMaster.getExplosiveDetails().getDocumentId() != null)
					masterDto.setExplosiveCertificateId(fuelTankMaster.getExplosiveDetails().getDocumentId().getId());
				
				 String lastCleaningDate = fuelTankMasterRepository.lastCleaningDate(fuelTankMaster.getId());
				 
				 
				 if(lastCleaningDate!=null)
				 {
					 masterDto.setLastCleaningDate(lastCleaningDate);
					 
				 }
				 else
				 {
					 masterDto.setLastCleaningDate(""); 
				 }
				 /*if(lastCleaningDate !=null){
				 masterDto.setLastCleaningDate(lastCleaningDate.getLastCleaningDate());
				 }else{
					 masterDto.setLastCleaningDate("0");
				 }*/
				/*if(!lastCleaningDate.isEmpty())
				{
				for ( Object[] ob : lastCleaningDate )
				{
					
					if(ob[0]!=null)
					{
					masterDto.setLastCleaningDate(ob[0].toString());
					}
					
				}//for
				}
				else
				{
					
				masterDto.setLastCleaningDate("0");
					
				}*/
				dto.add(masterDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
		// return fuelMasterList ;
	}

	private String getUniqueTankCode() {
		SimpleDateFormat sd = new SimpleDateFormat("hhmmss");
		return sd.format(new Date());
	}

	public ResponseEntity<ResponseStatus> updateExplosiveLicense(Integer id, Date explosiveRenewalDate,
			MultipartFile uploadFile) {
		log.info("Entering into updateExplosiveLicense service");
		String pattern = "ddMMyyyy";
		String currentDate = new SimpleDateFormat(pattern).format(new Date());
		Document uploadDocument = null;
		ExplosiveLicenseDetails explosiveDetails = null;
		FuelTankMaster fuelTankMaster = fuelTankMasterRepository.findById(id).get();
		try {
			if (fuelTankMaster.getId() != null) {
				if (uploadFile != null && !uploadFile.isEmpty()) {
					File dir = new File(filePath + File.separator + "fuel" + File.separator + currentDate);
					log.info(dir.toPath());
					if (!dir.exists())
						dir.mkdirs();
					uploadDocument = new Document();
					Files.copy(uploadFile.getInputStream(), dir.toPath().resolve(uploadFile.getOriginalFilename()),
							StandardCopyOption.REPLACE_EXISTING);
					uploadDocument.setContentType(uploadFile.getContentType());
					uploadDocument.setDocumentName(uploadFile.getOriginalFilename());
					uploadDocument.setDocumentPath(filePath + File.separator + "fuel" + File.separator + currentDate);
					uploadDocument = documentRepository.save(uploadDocument);
				}
				if (uploadDocument != null && uploadDocument.getId() != null) {
					explosiveDetails = new ExplosiveLicenseDetails();
					fuelTankMaster.setExplosiveLicence(uploadDocument);
					explosiveDetails.setDocumentId(uploadDocument);
					explosiveDetails.setExplosiveValidity(explosiveRenewalDate);
					explosiveLicenseRepo.save(explosiveDetails);
					fuelTankMaster.setExplosiveDetails(explosiveDetails);
					fuelTankMaster.setExplosiveLicence(uploadDocument);
					fuelTankMaster.setExplosiveLicenceValidity(explosiveRenewalDate);
				}
				fuelTankMaster.setExplosiveDetails(explosiveDetails);
				fuelTankMasterRepository.save(fuelTankMaster);
				return new ResponseEntity<>(
						new ResponseStatus("explosive details data has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
				HttpStatus.OK);
	}

	public List<AlertDto> explosiveCertificateExpiry() throws ParseException {
		List<AlertDto> msgs = new ArrayList<>();
		for (FuelTankMaster mst : fuelTankMasterRepository.findAll()) {
			if (mst != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				AlertDto msg = new AlertDto();
				long timeDiff = timeDiff(sdf.format(mst.getExplosiveLicenceValidity()), sdf.format(new Date()));
				if (timeDiff < 30 && timeDiff > 0) {
					msg.setMsg("Certificate will expire in " + timeDiff + " days - " + mst.getTankUniqueId());
					msgs.add(msg);
				} else if (timeDiff == 0) {
					msg.setMsg("Certificate will expire today - " + mst.getExplosiveLicenceValidity() + " - "
							+ mst.getTankUniqueId());
					msgs.add(msg);
				} else if (timeDiff < 1) {
					msg.setMsg("Certificate has expired on " + mst.getExplosiveLicenceValidity() + " - "
							+ mst.getTankUniqueId());
					msgs.add(msg);
				}

			}
		}
		msgs.remove(null);
		return msgs;
	}

	private long timeDiff(String s1, String s2) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = sdf.parse(s1);
		Date d2 = sdf.parse(s2);
		long diff = d.getTime() - d2.getTime();
		long diffInDays = diff / (24 * 60 * 60 * 1000);
		return diffInDays;

	}

	/*public ResponseEntity<ResponseStatus> saveDipChartReadings(DipChartReadingsDto input) {
		DipChartReadings dipReading = new DipChartReadings();
		float currentValue = 0;
		float volume = 0;
		float difference = 0;
		try {
			DipChartReadings existingDip = dipChartReadingRepo.findExistingRecord(input.getTankId());

			if (input != null) {
				if ((existingDip != null && existingDip.getCreatedOn() != null) || existingDip == null) {
					Optional<User> userDetails = null;
					DipChartReadings todaysRecord = null;
					if (existingDip != null) {
						userDetails = userRepository.findById(existingDip.getCreatedBy());
						User userHistory = userRepository.findByUserName(input.getUser());
						todaysRecord = dipChartReadingRepo.findByDailyuserRecords(input.getTankId(),
								userHistory.getId());
					}
					if (((existingDip != null && userDetails != null
							&& !userDetails.get().getUsername().equals(input.getUser())
							&& existingDip.getCreatedOn().toLocalDateTime().toLocalDate().equals(LocalDate.now()))
							&& todaysRecord == null && existingDip.getEvengDateTime() != null) || existingDip == null) {
						if (input.getComplete() && input.getEvengDateTime() == null) {
							dipReading.setDipReadingBeforeSupply(input.getDipReadingBeforesupply());
							dipReading.setExcessShort(input.getExcessShort() != null ? input.getExcessShort() : 0);
							dipReading.setVariation(input.getVariation() != null ? input.getVariation() : 0);
							dipReading.setVolumeBeforeSupply(input.getVolume());
							dipReading.setFuelTankId(input.getTankId());
							dipReading.setIncomplete(input.getComplete());
							dipReading.setCurrentDieselPrice(input.getCurrentDieselPrice());
							DepotMaster depot = depotRepository.findByDepotCode(input.getDepotCode());
							dipReading.setDepotId(depot);
							FuelTankMaster tankMaster = fuelTankMasterRepository.findById(input.getTankId()).get();
							volume = input.getVolume().floatValue();
							currentValue = tankMaster.getCurrentValue().floatValue();
							if (currentValue != 0 && volume != 0) {
								difference = currentValue - volume;
								float percentage = difference / currentValue * 100;
								if (percentage < 4) {
									tankMaster.setCurrentValue(input.getVolume());
									fuelTankMasterRepository.save(tankMaster);
								}
								if (difference < 0 && currentValue != 0) {
									float diff = Math.abs(difference);
									float percent = diff / currentValue * 100;
									if (percent < 4) {
										tankMaster.setCurrentValue(input.getVolume());
										fuelTankMasterRepository.save(tankMaster);
									}
								}
							}
							dipChartReadingRepo.save(dipReading);
							return new ResponseEntity<>(
									new ResponseStatus("dip chart data has been saved successfully.", HttpStatus.OK),
									HttpStatus.OK);
						}
					}
				}
				if (!input.getComplete()) {
					DipChartReadings prevReadings = dipChartReadingRepo.findById(input.getDipId()).get();
					Optional<User> userDetails = userRepository.findById(prevReadings.getCreatedBy());
					if (userDetails.get().getUsername().equals(input.getUser())) {
						prevReadings.setEvengDateTime(new Date());
						prevReadings.setEvengDipReadingBeforeSupply(input.getEveningDipReading());
						prevReadings.setEvengVarition(input.getEveningVariation());
						prevReadings.setEvengVolumeBeforeSupply(input.getEveningVolume());
						prevReadings.setEveningCurrentDieselPrice(input.getEveningCurrentDieselPrice());
						prevReadings.setIncomplete(input.getComplete());
						FuelTankMaster tankMaster = fuelTankMasterRepository.findById(input.getTankId()).get();
						volume = input.getEveningVolume().floatValue();
						currentValue = tankMaster.getCurrentValue().floatValue();
						if (currentValue != 0 && volume != 0) {
							difference = currentValue - volume;
							float percentage = difference / currentValue * 100;
							if (percentage < 4) {
								tankMaster.setCurrentValue(input.getEveningVolume());
								fuelTankMasterRepository.save(tankMaster);
							}
							if (difference < 0 && currentValue != 0) {
								float diff = Math.abs(difference);
								float percent = diff / currentValue * 100;
								if (percent < 4) {
									tankMaster.setCurrentValue(input.getEveningVolume());
									fuelTankMasterRepository.save(tankMaster);
								}
							}
						}
						dipChartReadingRepo.save(prevReadings);
						return new ResponseEntity<>(
								new ResponseStatus("Dip chart data has been saved successfully.", HttpStatus.OK),
								HttpStatus.OK);
					}
					return new ResponseEntity<>(new ResponseStatus(
							"The user who inserted the morning details only can enter the eveng details.",
							HttpStatus.OK), HttpStatus.OK);
				}
			}
			if (existingDip != null) {
				Optional<User> userDetails = userRepository.findById(existingDip.getCreatedBy());
				if (!input.getUser().equals(userDetails.get().getUsername())) {
					return new ResponseEntity<>(new ResponseStatus("Previous User has not entered the evening details.",
							HttpStatus.FORBIDDEN), HttpStatus.OK);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}

		return new ResponseEntity<>(
				new ResponseStatus("Record for current date is already entered. please look for earlier records",
						HttpStatus.FORBIDDEN),
				HttpStatus.OK);
	}*/

	public ExcessShortDto calculateExcessShort(Integer tankId, Double volume) {
		ExcessShortDto dto = new ExcessShortDto();
		DipChartReadings input2 = dipChartReadingRepo.orderByDate(tankId);
		/*if (input2 != null && input2.getEvengVolumeBeforeSupply() != null) {
			Double excess = volume - input2.getEvengVolumeBeforeSupply();
			dto.setExcessShort(excess);
			double result = 0;
			result = ((volume - input2.getEvengVolumeBeforeSupply()) * 100) / input2.getEvengVolumeBeforeSupply();
			dto.setVariation(result);
		} else {
			dto.setExcessShort(0.0);
			dto.setVariation(0.0);
		}*/
		return dto;
	}

	public ExcessShortDto calculateExcessShort2(Integer tankId, Double volume) {
		ExcessShortDto dto = new ExcessShortDto();
		/*DipChartReadings input2 = dipChartReadingRepo.orderByDate(tankId);
		if (input2 != null && input2.getVolumeBeforeSupply() != null) {
			Double excess = volume - input2.getVolumeBeforeSupply();
			dto.setExcessShort(excess);
			double result = 0;
			result = ((volume - input2.getVolumeBeforeSupply()) * 100) / input2.getVolumeBeforeSupply();
			dto.setVariation(result);
		}*/
		return dto;
	}

	public List<DipChartReadingsDto> getAllDipChartReadings(String userName, String depot) {
		List<DipChartReadingsDto> output = new ArrayList<>();
		FuelTankMasterDto tankMasterOneDto = null;
		FuelTankMasterDto tankMasterSecDto = null;
		DispensingUnitMasterDto dipReadingMasterOneDto = null;
		DispensingUnitMasterDto dipReadingMasterSecDto = null;
		ReadingMasterDto readingMasterOneDto = null;
		ReadingMasterDto readingMasterSecDto = null;
		try {
			DepotMaster depotId = depotRepository.findByDepotCode(depot);
			User userDetails = userRepository.findByUserName(userName);
			for (DipChartReadings input : dipChartReadingRepo.findByDepotAndUserName(depotId.getId(),
					userDetails.getId())) {
				DipChartReadingsDto dipReading = new DipChartReadingsDto();
				FuelTankMaster tankMasterOne = fuelTankMasterRepository.findById(input.getFuelTankMasterOne().getId()).get();
				tankMasterOneDto = new FuelTankMasterDto();
				tankMasterOneDto.setId(tankMasterOne.getId());
				tankMasterOneDto.setTankName(tankMasterOne.getTankName());
				dipReading.setFuelTankMasterOne(tankMasterOneDto);
				FuelTankMaster tankMasterSec = fuelTankMasterRepository.findById(input.getFuelTankMasterSec().getId()).get();
				tankMasterSecDto = new FuelTankMasterDto();
				tankMasterSecDto.setId(tankMasterSec.getId());
				tankMasterSecDto.setTankName(tankMasterSec.getTankName());
				dipReading.setFuelTankMasterSec(tankMasterSecDto);
				DispensingUnitMaster dipReadingMasterOne = dispensingUnitRepository.findById(input.getDipReadingMasterOne().getId()).get();
				dipReadingMasterOneDto = new DispensingUnitMasterDto();
				dipReadingMasterOneDto.setId(dipReadingMasterOne.getId());
				dipReadingMasterOneDto.setDisUnitName(dipReadingMasterOne.getDisUnitName());
				dipReading.setDispensingMasterOne(dipReadingMasterOneDto);
				DispensingUnitMaster dipReadingMasterSec = dispensingUnitRepository.findById(input.getDipReadingMasterSec().getId()).get();
				dipReadingMasterSecDto = new DispensingUnitMasterDto();
				dipReadingMasterSecDto.setId(dipReadingMasterSec.getId());
				dipReadingMasterSecDto.setDisUnitName(dipReadingMasterSec.getDisUnitName());
				dipReading.setDispensingMasterSec(dipReadingMasterSecDto);
				dipReading.setFuelVolumeTankOne(input.getFuelVolumeTankOne());
				dipReading.setFuelVolumeTankSecond(input.getFuelVolumeTankSecond());
				dipReading.setTotalOpeningFuelVolume(input.getTotalOpeningFuelVolume());
				dipReading.setDispensingUnitReadingOne(input.getDipReadingOne());
				dipReading.setDispensingUnitReadingSec(input.getDipReadingSec());
				dipReading.setDispenseFuelMorning(input.getDispenseFuelMorning());
				dipReading.setVariationMorning(input.getVariationMorning());
				dipReading.setVariationMorningFlag(input.getVariationMorningFlag());
				ReadingMaster readingMasterOne = readingMasterRepository.findById(input.getReadingMasterOne().getId()).get();
				readingMasterOneDto = new ReadingMasterDto();
				readingMasterOneDto.setId(readingMasterOne.getId());
				readingMasterOneDto.setReading(readingMasterOne.getReading());
				dipReading.setReadingMasterOne(readingMasterOneDto);
				ReadingMaster readingMasterSec = readingMasterRepository.findById(input.getReadingMasterSec().getId()).get();
				readingMasterSecDto = new ReadingMasterDto();
				readingMasterSecDto.setId(readingMasterSec.getId());
				readingMasterSecDto.setReading(readingMasterSec.getReading());
				dipReading.setReadingMasterSec(readingMasterSecDto);
				dipReading.setMorningDate(input.getCreatedOn());
				dipReading.setEvengDateTime(input.getEvengDateTime());
				dipReading.setId(input.getDip_id());
				dipReading.setIncomplete(input.getIncomplete());
				dipReading.setFuelRateMorning(input.getFuelRateMorning());
				dipReading.setFuelRateMorningTank2(input.getFuelRateMorningTank2());
				DUReadingHistory historyOne = duReadingHistoryRepository.findMaxRecord(dipReadingMasterOne.getId());
				DUReadingHistory historySec =  duReadingHistoryRepository.findMaxRecord(dipReadingMasterSec.getId());
				if(historyOne != null && historyOne.getDuEndReading() != null)
				dipReading.setCloseDispensingUnitReadingOne(historyOne.getDuEndReading());
				else
					dipReading.setCloseDispensingUnitReadingOne(0.0);
				if(historySec != null && historySec.getDuEndReading() != null)
				dipReading.setCloseDispensingUnitReadingSec(historySec.getDuEndReading());
				else
					dipReading.setCloseDispensingUnitReadingSec(0.0);
				Float eveningRateOne = mixtureCostCalculation(tankMasterOne.getId());
				Float eveningRateSec = mixtureCostCalculation(tankMasterSec.getId());
				if(eveningRateOne != null)
				dipReading.setFuelRateEvening((double)eveningRateOne);
				if(eveningRateSec != null)
				dipReading.setFuelRateEveningTank2((double)eveningRateSec);
				//DepotMaster depotObj = depotRepository.findByDepotCode(depot);
				//dipReading.setDepotId(depotObj.getDepotName());
				output.add(dipReading);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;
	}

	public ResponseEntity<ResponseStatus> addTankCapacity(TankCapacityMasterDto input) {
		try {
			if (input != null && input.getCapacity() != null) {
				TankCapacityMaster tankCapacity = new TankCapacityMaster();
				tankCapacity.setCapacity(input.getCapacity());
				tankCapacity.setName(input.getName());
				tankCapacityMasterRepository.save(tankCapacity);
				return new ResponseEntity<>(
						new ResponseStatus("tank capacity data has been saved successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
				HttpStatus.OK);
	}

	public ResponseEntity<ResponseStatus> updateCleaningDate(Integer id, Date cleaingRenewalDate) {
		log.info("Entering into update cleaning date service");
		FuelTankMaster fuelTankMaster = fuelTankMasterRepository.findById(id).get();
		try {
			if (fuelTankMaster.getId() != null) {
				FuelTankCleaningHistory cleaning = new FuelTankCleaningHistory();
				//cleaning.setCleaningDate(cleaingRenewalDate);
				cleaning.setTankUniqueId(fuelTankMaster.getTankUniqueId());
				cleaning.setCleaningDate(fuelTankMaster.getCleaningDate());
				cleaning.setFuelTankMaster(fuelTankMaster);
				fuelTankCleaningRepo.save(cleaning);
				fuelTankMaster.setCleaningDate(cleaingRenewalDate);
				fuelTankMasterRepository.save(fuelTankMaster);
				return new ResponseEntity<>(
						new ResponseStatus("cleaning renewal data has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
				HttpStatus.OK);
	}

	public List<TankCapacityMasterDto> findAllTankCapacityMaster() {
		List<TankCapacityMasterDto> output = new ArrayList<>();
		for (TankCapacityMaster tankCapacityList : tankCapacityMasterRepository.findAll()) {
			TankCapacityMasterDto dto = new TankCapacityMasterDto();
			dto.setCapacity(tankCapacityList.getCapacity());
			dto.setName(tankCapacityList.getName());
			dto.setId(tankCapacityList.getId());
			output.add(dto);
		}
		return output;
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> deleteFuelTankMasterStatusFlag(Integer id) {
		log.info("Entering into deleteFuelTankMasterStatusFlag service");
		try {
			int i = fuelTankMasterRepository.deleteFuelTankMasterStatusFlag(true, id);
			if (i == 1)
				return new ResponseEntity<>(
						new ResponseStatus("Fuel Tank has been deleted successfully.", HttpStatus.OK), HttpStatus.OK);
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
	public FuelTankMasterDto checkTankCode(String tankCode) {
		log.info("Entering into checkTankCode service");
		FuelTankMasterDto tankMasterDto = null;
		try {
			tankMasterDto = new FuelTankMasterDto();
			FuelTankMaster fuelTankMaster = fuelTankMasterRepository.checkTankCode(tankCode);

			if (fuelTankMaster != null) {
				tankMasterDto.setCheckTankCode(true);
			} else {
				tankMasterDto.setCheckTankCode(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tankMasterDto;
	}

	@Scheduled(cron = "0 0/5 * * * * ")
	@Override
	public String sendSms() throws Exception {
		log.info("Entering into SmsCodeService service");
		String output = "";
		MessageTemplates templates = messageTemplatesRepo.findByCode("Absent");
		Object[] settings = systemSettingsRepo.findCredentials();
		String userName = "";
		String pass = "";
		String secureKey = "";
		String senderId = "";
		if (settings[0] != null)
			userName = settings[0].toString();
		if (settings[1] != null)
			pass = settings[1].toString();
		if (settings[2] != null)
			secureKey = settings[2].toString();
		if (settings[3] != null)
			senderId = settings[3].toString();

		MessageLogs log = null;

		Date date = new Date();
		String message = "";

		String mobilenumber = "9850507085";

		try {
			List<FuelTankMaster> findAll = fuelTankMasterRepository.findAll();
			for (FuelTankMaster ob : findAll) {
				Integer id = ob.getId();
				FuelTankMaster fuelTankObj = fuelTankMasterRepository.findById(id).get();
				DepotMaster depot = depotRepository.findById(fuelTankObj.getDepot().getId()).get();

				Double currentValue = fuelTankObj.getCurrentValue();

				Float reorderLevel = fuelTankObj.getReorderLevel();
				if (currentValue != null && reorderLevel != null) {

					if (currentValue < reorderLevel) {

						List<Object[]> obj = messageLogsRepo.findByMessageDtls(fuelTankObj.getId(), depot.getId(),
								date);

						if (!obj.isEmpty())
							continue;

						if (templates != null)
							message = smsText(templates.getMessage_content(), date);
						output = smsController.sendSingleSMS(userName, pass, message, senderId, mobilenumber, secureKey,
								templates.getMessage_id());

						if (output.equals("sent")) {

							log = new MessageLogs();
							log.setMessage(message);
							log.setLog_date(new Date());
							log.setResponse(output);
							log.setMessageId(templates);
							log.setPhoneNumber(mobilenumber);
							log.setDepot(depot);
							log.setFuelTank(fuelTankObj);
							log.setCreatedOn(new Date());
							log.setUpdatedOn(new Date());
							messageLogsRepo.save(log);

						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;
	}

	private String smsText(String templateContent, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
		Matcher m = Pattern.compile("#var#").matcher(templateContent);
		StringBuffer sb = new StringBuffer();
		for (int i = 1; m.find(); i++) {
			if (i == 1) {
				m.appendReplacement(sb, sdf.format(date));
			}

		}
		m.appendTail(sb);
		String s = sb.toString().replace("{", "");
		s = s.replace("}", "");
		return s;
	}
	
	public ResponseEntity<ResponseStatus> saveDipChartReadings(DipChartReadingsDto input) {
		DipChartReadings dipReading = new DipChartReadings();
		try {
			if (input != null) {
						if (input.getIncomplete()) {
							DipChartReadings dipObj = dipChartReadingRepo.findMaxRecord();
							if(dipObj != null && dipObj.getIncomplete() != null && dipObj.getIncomplete()){
								return new ResponseEntity<>(new ResponseStatus("Previous User has'nt completed his shift.",
										HttpStatus.FORBIDDEN), HttpStatus.OK);
							}
							FuelTankMaster tankMasterOne = fuelTankMasterRepository.findById(input.getFuelTankMasterOne().getId()).get();
							dipReading.setFuelTankMasterOne(tankMasterOne);
							FuelTankMaster tankMasterSec = fuelTankMasterRepository.findById(input.getFuelTankMasterSec().getId()).get();
							dipReading.setFuelTankMasterSec(tankMasterSec);
							DispensingUnitMaster dipReadingMasterOne = dispensingUnitRepository.findById(input.getDispensingMasterOne().getId()).get();
							dipReading.setDipReadingMasterOne(dipReadingMasterOne);
							DispensingUnitMaster dipReadingMasterSec = dispensingUnitRepository.findById(input.getDispensingMasterSec().getId()).get();
							dipReading.setDipReadingMasterSec(dipReadingMasterSec);
							dipReading.setFuelVolumeTankOne(input.getFuelVolumeTankOne());
							dipReading.setFuelVolumeTankSecond(input.getFuelVolumeTankSecond());
							dipReading.setTotalOpeningFuelVolume(input.getTotalOpeningFuelVolume());
							dipReading.setDipReadingOne(input.getDispensingUnitReadingOne());
							dipReading.setDipReadingSec(input.getDispensingUnitReadingSec());
							dipReading.setDispenseFuelMorning(input.getDispenseFuelMorning());
							dipReading.setVariationMorning(input.getVariationMorning());
							dipReading.setVariationMorningFlag(input.getVariationMorningFlag());
							dipReading.setFuelRateMorning(input.getFuelRateMorning());
							dipReading.setFuelRateMorningTank2(input.getFuelRateMorningTank2());
							ReadingMaster readingMasterOne = readingMasterRepository.findById(input.getReadingMasterOne().getId()).get();
							dipReading.setReadingMasterOne(readingMasterOne);
							ReadingMaster readingMasterSec = readingMasterRepository.findById(input.getReadingMasterSec().getId()).get();
							dipReading.setReadingMasterSec(readingMasterSec);
							DepotMaster depot = depotRepository.findByDepotCode(input.getDepotCode());
							dipReading.setDepotId(depot);
							dipReading.setIncomplete(input.getIncomplete());
							tankMasterOne.setCurrentValue(input.getFuelVolumeTankOne());
							fuelTankMasterRepository.save(tankMasterOne);
							tankMasterSec.setCurrentValue(input.getFuelVolumeTankSecond());
							fuelTankMasterRepository.save(tankMasterSec);
							dipChartReadingRepo.save(dipReading);
							
							DUReadingHistory duHistoryObj = new DUReadingHistory();
							duHistoryObj.setDispensingUnit(dipReadingMasterOne);
							duHistoryObj.setFuelTank(tankMasterOne);
							duHistoryObj.setDuStartReading(input.getDispensingUnitReadingOne());
							//duHistoryObj.setDuEndReading(input.getDispensingUnitReadingOne());
							duReadingHistoryRepository.save(duHistoryObj);
							DUReadingHistory duHistoryObjSec = new DUReadingHistory();
							duHistoryObjSec.setDispensingUnit(dipReadingMasterSec);
							duHistoryObjSec.setFuelTank(tankMasterSec);
							duHistoryObjSec.setDuStartReading(input.getDispensingUnitReadingSec());
							//duHistoryObjSec.setDuEndReading(input.getDispensingUnitReadingSec());
							duReadingHistoryRepository.save(duHistoryObjSec);
							
							return new ResponseEntity<>(
									new ResponseStatus("dip chart data has been saved successfully.", HttpStatus.OK),
									HttpStatus.OK);
						}
					//}
				//}
				if (!input.getIncomplete()) {
					//DipChartReadings prevReadings = dipChartReadingRepo.findById(input.getId()).get();
					//.Optional<User> userDetails = userRepository.findById(prevReadings.getCreatedBy());
					//if (userDetails.get().getUsername().equals(input.getUser())) {
					DipChartReadings prevReadings = dipChartReadingRepo.findById(input.getId()).get();
					FuelTankMaster tankMasterOne = fuelTankMasterRepository.findById(input.getCloseFuelTankMasterDtoOne().getId()).get();
					prevReadings.setCloseFuelTankMasterOne(tankMasterOne);
					FuelTankMaster tankMasterSec = fuelTankMasterRepository.findById(input.getCloseFuelTankMasterSec().getId()).get();
					prevReadings.setCloseFuelTankMasterSec(tankMasterSec);
					DispensingUnitMaster dipReadingMasterOne = dispensingUnitRepository.findById(input.getClosedispensingMasterOne().getId()).get();
					prevReadings.setCloseDipReadingMasterOne(dipReadingMasterOne);
					DispensingUnitMaster dipReadingMasterSec = dispensingUnitRepository.findById(input.getClosedispensingMasterSec().getId()).get();
					prevReadings.setCloseDipReadingMasterSec(dipReadingMasterSec);
					prevReadings.setCloseFuelVolumeTankOne(input.getCloseFuelVolumeTankOne());
					prevReadings.setCloseFuelVolumeTankSecond(input.getCloseFuelVolumeTankSecond());
					prevReadings.setCloseTotalOpeningFuelVolume(input.getCloseTotalOpeningFuelVolume());
					prevReadings.setCloseDipReadingOne(input.getCloseDispensingUnitReadingOne());
					prevReadings.setCloseDipReadingSec(input.getCloseDispensingUnitReadingSec());
					prevReadings.setDispenseFuelEvening(input.getDispenseFuelEvening());
					prevReadings.setVariationEvening(input.getVariationEvening());
					prevReadings.setVariationEveningFlag(input.getVariationEveningFlag());
					prevReadings.setFuelRateEvening(input.getFuelRateEvening());
					prevReadings.setFuelRateEveningTank2(input.getFuelRateEveningTank2());
					prevReadings.setCurrentFuelAllTank(input.getCurrentFuelAllTank());
					ReadingMaster readingMasterOne = readingMasterRepository.findById(input.getCloseReadingMasterOne().getId()).get();
					prevReadings.setCloseReadingMasterOne(readingMasterOne);
					ReadingMaster readingMasterSec = readingMasterRepository.findById(input.getCloseReadingMasterSec().getId()).get();
					prevReadings.setCloseReadingMasterSec(readingMasterSec);
					if(input.getClosingObj() != null && input.getClosingObj().getId() != null){
						BookReadingClosingMaster closeMaster = bookreadingClosingRepo.findById(input.getClosingObj().getId()).get();
					    prevReadings.setClosingObj(closeMaster);
					}
					prevReadings.setIncomplete(input.getIncomplete());
					tankMasterOne.setCurrentValue(input.getCloseFuelVolumeTankOne());
					prevReadings.setEvengDateTime(new Date());
					fuelTankMasterRepository.save(tankMasterOne);
					tankMasterSec.setCurrentValue(input.getCloseFuelVolumeTankSecond());
					fuelTankMasterRepository.save(tankMasterSec);
						dipChartReadingRepo.save(prevReadings);
						return new ResponseEntity<>(
								new ResponseStatus("Dip chart data has been saved successfully.", HttpStatus.OK),
								HttpStatus.OK);
					//}
				}	
					/*return new ResponseEntity<>(new ResponseStatus(
							"The user who inserted the morning details only can enter the eveng details.",
							HttpStatus.OK), HttpStatus.OK);*/
				}
			//}
			/*if (existingDip != null) {
				Optional<User> userDetails = userRepository.findById(existingDip.getCreatedBy());
				if (!input.getUser().equals(userDetails.get().getUsername())) {
					return new ResponseEntity<>(new ResponseStatus("Previous User has'nt completed his shift.",
							HttpStatus.FORBIDDEN), HttpStatus.OK);
				}
			}*/
		
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}

		return new ResponseEntity<>(
				new ResponseStatus("Record for current date is already entered. please look for earlier records",
						HttpStatus.FORBIDDEN),
				HttpStatus.OK);
	}

	@Override
	public Float mixtureCostCalculation(Integer tankId) {
		Float currentCost = 0.0f;
		try{
			currentCost = fuelManagementAndCalculationUtility.mixtureFuelCostCalculation(tankId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return currentCost;
	}

	@Override
	public DipChartReadingsDto viewDipChartObjectById(Integer id) {
		FuelTankMasterDto tankMasterOneDto = null;
		FuelTankMasterDto tankMasterSecDto = null;
		DispensingUnitMasterDto dipReadingMasterOneDto = null;
		DispensingUnitMasterDto dipReadingMasterSecDto = null;
		ReadingMasterDto readingMasterOneDto = null;
		ReadingMasterDto readingMasterSecDto = null;
		DipChartReadingsDto dipReading = new DipChartReadingsDto();
		DipChartReadingsDto dipObj = null;
		try {
			DipChartReadings input = dipChartReadingRepo.findById(id).get();
			FuelTankMaster tankMasterOne = fuelTankMasterRepository.findById(input.getFuelTankMasterOne().getId())
					.get();
			tankMasterOneDto = new FuelTankMasterDto();
			tankMasterOneDto.setId(tankMasterOne.getId());
			tankMasterOneDto.setTankName(tankMasterOne.getTankName());
			dipReading.setFuelTankMasterOne(tankMasterOneDto);
			FuelTankMaster tankMasterSec = fuelTankMasterRepository.findById(input.getFuelTankMasterSec().getId())
					.get();
			tankMasterSecDto = new FuelTankMasterDto();
			tankMasterSecDto.setId(tankMasterSec.getId());
			tankMasterSecDto.setTankName(tankMasterSec.getTankName());
			dipReading.setFuelTankMasterSec(tankMasterSecDto);
			DispensingUnitMaster dipReadingMasterOne = dispensingUnitRepository
					.findById(input.getDipReadingMasterOne().getId()).get();
			dipReadingMasterOneDto = new DispensingUnitMasterDto();
			dipReadingMasterOneDto.setId(dipReadingMasterOne.getId());
			dipReadingMasterOneDto.setDisUnitName(dipReadingMasterOne.getDisUnitName());
			dipReading.setDispensingMasterOne(dipReadingMasterOneDto);
			DispensingUnitMaster dipReadingMasterSec = dispensingUnitRepository
					.findById(input.getDipReadingMasterSec().getId()).get();
			dipReadingMasterSecDto = new DispensingUnitMasterDto();
			dipReadingMasterSecDto.setId(dipReadingMasterSec.getId());
			dipReadingMasterSecDto.setDisUnitName(dipReadingMasterSec.getDisUnitName());
			dipReading.setDispensingMasterSec(dipReadingMasterSecDto);
			dipReading.setFuelVolumeTankOne(input.getFuelVolumeTankOne());
			dipReading.setFuelVolumeTankSecond(input.getFuelVolumeTankSecond());
			dipReading.setTotalOpeningFuelVolume(input.getTotalOpeningFuelVolume());
			dipReading.setDispensingUnitReadingOne(input.getDipReadingOne());
			dipReading.setDispensingUnitReadingSec(input.getDipReadingSec());
			dipReading.setDispenseFuelMorning(input.getDispenseFuelMorning());
			dipReading.setVariationMorning(input.getVariationMorning());
			dipReading.setVariationMorningFlag(input.getVariationMorningFlag());
			dipReading.setFuelRateMorning(input.getFuelRateMorning());
			dipReading.setFuelRateMorningTank2(input.getFuelRateEveningTank2());
			dipReading.setCurrentFuelAllTank(input.getCurrentFuelAllTank());
		    BookReadingClosingDto closingDto = new BookReadingClosingDto();
		    if(input.getClosingObj() != null && input.getClosingObj().getId() != null){
		    	closingDto.setId(input.getClosingObj().getId());
		    closingDto.setName(input.getClosingObj().getName());
		    }
		    dipReading.setClosingObj(closingDto);
			ReadingMaster readingMasterOne = readingMasterRepository.findById(input.getReadingMasterOne().getId())
					.get();
			readingMasterOneDto = new ReadingMasterDto();
			readingMasterOneDto.setId(readingMasterOne.getId());
			readingMasterOneDto.setReading(readingMasterOne.getReading());
			dipReading.setReadingMasterOne(readingMasterOneDto);
			ReadingMaster readingMasterSec = readingMasterRepository.findById(input.getReadingMasterSec().getId())
					.get();
			readingMasterSecDto = new ReadingMasterDto();
			readingMasterSecDto.setId(readingMasterSec.getId());
			readingMasterSecDto.setReading(readingMasterSec.getReading());
			dipReading.setReadingMasterSec(readingMasterSecDto);
			dipReading.setMorningDate(input.getCreatedOn());
			dipReading.setEvengDateTime(input.getEvengDateTime());
			dipReading.setId(input.getDip_id());
			dipReading.setIncomplete(input.getIncomplete());
			dipReading.setDepotCode(input.getDepotId().getDepotCode());
			 dipObj = viewDipChartEveningObjectById(dipReading,input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dipObj;
	}
	
	private DipChartReadingsDto viewDipChartEveningObjectById(DipChartReadingsDto dipReading, DipChartReadings input) {
		FuelTankMasterDto tankMasterOneDto = null;
		FuelTankMasterDto tankMasterSecDto = null;
		DispensingUnitMasterDto dipReadingMasterOneDto = null;
		DispensingUnitMasterDto dipReadingMasterSecDto = null;
		ReadingMasterDto readingMasterOneDto = null;
		ReadingMasterDto readingMasterSecDto = null;
		try {
			if (input.getCloseFuelTankMasterOne() != null) {
				FuelTankMaster tankMasterOne = fuelTankMasterRepository
						.findById(input.getCloseFuelTankMasterOne().getId()).get();
				tankMasterOneDto = new FuelTankMasterDto();
				tankMasterOneDto.setId(tankMasterOne.getId());
				tankMasterOneDto.setTankName(tankMasterOne.getTankName());
				dipReading.setCloseFuelTankMasterDtoOne(tankMasterOneDto);
			}
			if (input.getCloseFuelTankMasterSec() != null) {
				FuelTankMaster tankMasterSec = fuelTankMasterRepository
						.findById(input.getCloseFuelTankMasterSec().getId()).get();
				tankMasterSecDto = new FuelTankMasterDto();
				tankMasterSecDto.setId(tankMasterSec.getId());
				tankMasterSecDto.setTankName(tankMasterSec.getTankName());
				dipReading.setCloseFuelTankMasterSec(tankMasterSecDto);
			}
			if (input.getCloseDipReadingMasterOne() != null) {
				DispensingUnitMaster dipReadingMasterOne = dispensingUnitRepository
						.findById(input.getCloseDipReadingMasterOne().getId()).get();
				dipReadingMasterOneDto = new DispensingUnitMasterDto();
				dipReadingMasterOneDto.setId(dipReadingMasterOne.getId());
				dipReadingMasterOneDto.setDisUnitName(dipReadingMasterOne.getDisUnitName());
				dipReading.setClosedispensingMasterOne(dipReadingMasterOneDto);
			}
			if (input.getCloseDipReadingMasterSec() != null) {
				DispensingUnitMaster dipReadingMasterSec = dispensingUnitRepository
						.findById(input.getCloseDipReadingMasterSec().getId()).get();
				dipReadingMasterSecDto = new DispensingUnitMasterDto();
				dipReadingMasterSecDto.setId(dipReadingMasterSec.getId());
				dipReadingMasterSecDto.setDisUnitName(dipReadingMasterSec.getDisUnitName());
				dipReading.setClosedispensingMasterSec(dipReadingMasterSecDto);
			}
			dipReading.setCloseFuelVolumeTankOne(input.getCloseFuelVolumeTankOne());
			dipReading.setCloseFuelVolumeTankSecond(input.getCloseFuelVolumeTankSecond());
			dipReading.setCloseTotalOpeningFuelVolume(input.getCloseTotalOpeningFuelVolume());
			dipReading.setCloseDispensingUnitReadingOne(input.getCloseDipReadingOne());
			dipReading.setCloseDispensingUnitReadingSec(input.getCloseDipReadingSec());
			dipReading.setDispenseFuelEvening(input.getDispenseFuelEvening());
			dipReading.setVariationEvening(input.getVariationEvening());
			dipReading.setVariationEveningFlag(input.getVariationEveningFlag());
			dipReading.setFuelRateEvening(input.getFuelRateEvening());
			dipReading.setFuelRateEveningTank2(input.getFuelRateEveningTank2());
			if (input.getCloseReadingMasterOne() != null) {
				ReadingMaster readingMasterOne = readingMasterRepository
						.findById(input.getCloseReadingMasterOne().getId()).get();
				readingMasterOneDto = new ReadingMasterDto();
				readingMasterOneDto.setId(readingMasterOne.getId());
				readingMasterOneDto.setReading(readingMasterOne.getReading());
				dipReading.setCloseReadingMasterOne(readingMasterOneDto);
			}
			if (input.getCloseReadingMasterSec() != null) {
				ReadingMaster readingMasterSec = readingMasterRepository
						.findById(input.getCloseReadingMasterSec().getId()).get();
				readingMasterSecDto = new ReadingMasterDto();
				readingMasterSecDto.setId(readingMasterSec.getId());
				readingMasterSecDto.setReading(readingMasterSec.getReading());
				dipReading.setCloseReadingMasterSec(readingMasterSecDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dipReading;
	}

	@Override
	public ResponseEntity<ResponseStatus> validateDuplicateCode(String tankCode) {
		List<FuelTankMaster> tankMasterList = fuelTankMasterRepository.validateTankCode(tankCode);
		if (tankMasterList != null && tankMasterList.size() > 0) {

			return new ResponseEntity<>(
					new ResponseStatus("Tank code already exists, please enter other code", HttpStatus.FORBIDDEN),
					HttpStatus.OK);

		} else {
			return new ResponseEntity<>(new ResponseStatus("", HttpStatus.OK), HttpStatus.OK);
		}

	}

}
