package com.idms.base.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.AddaFeesTollFeeReportDto;
import com.idms.base.api.v1.model.dto.BusStandWiseAdvanceBookingDto;
import com.idms.base.api.v1.model.dto.ComparisonReportDutyRosterDto;
import com.idms.base.api.v1.model.dto.ETMIssuanceReportResponseModel;
import com.idms.base.api.v1.model.dto.ETMStockReportResponseModel;
import com.idms.base.api.v1.model.dto.FuelDispenWiseFuelIssuReportResModel;
import com.idms.base.api.v1.model.dto.KMPLTicketReportDto;
import com.idms.base.api.v1.model.dto.KMReceivedLessthanSchduledKMsDto;
import com.idms.base.api.v1.model.dto.MachineNotRecSchReportDto;
import com.idms.base.api.v1.model.dto.RepairBillReportDto;
import com.idms.base.api.v1.model.dto.TicketBoxMachIssuReportResModel;
import com.idms.base.dao.entity.IssueEtmTicketBoxEntity;
import com.idms.base.dao.repository.IssueEtmTicketBoxRepository;
import com.idms.base.dao.repository.TicketReportRepository;
import com.idms.base.service.TicketReportService;

@Service
public class TicketReportServiceImpl implements TicketReportService {

	@Autowired
	private TicketReportRepository ticketReportRepository;

	@Autowired
	private IssueEtmTicketBoxRepository issueEtmTicketBoxRepository;

	@Override
	public List<List<ETMStockReportResponseModel>> getEtmStockReport(String from, String to, Long depoId) {
		Date startDate = null;
		Date endDate = null;
		List<List<ETMStockReportResponseModel>> obj = new ArrayList<>();
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<ETMStockReportResponseModel> eTMStockReportResponseModel = new ArrayList<ETMStockReportResponseModel>();
		try {
			List<Object[]> depo = ticketReportRepository.getEtmStockReport(startDate, endDate, depoId);
			for (Object[] o : depo) {
				ETMStockReportResponseModel dto = new ETMStockReportResponseModel();
				if (o[0] != null)
					dto.setDepot(o[0].toString());
				if (o[2] != null)
					dto.setEtmNumber(o[2].toString());
				if (o[1] != null)
					dto.setEtmCode(o[1].toString());
				if (o[3] != null)
					dto.setDateOfPurchase(o[3].toString());
				if (o[4] != null)
					dto.setCompanyName(o[4].toString());
				if (o[5] != null)
					dto.setRate(o[5].toString());
				if (o[6] != null)
					dto.setMachineFeatures(o[6].toString());
				if (o[7] != null)
					dto.setTransUnit(o[7].toString());

				eTMStockReportResponseModel.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.add(eTMStockReportResponseModel);

		List<ETMStockReportResponseModel> eTMStockReportResponseModel1 = new ArrayList<ETMStockReportResponseModel>();
		try {
			List<Object[]> depo = ticketReportRepository.etmStocSummarykReport(startDate, endDate, depoId);
			for (Object[] o : depo) {
				ETMStockReportResponseModel dto = new ETMStockReportResponseModel();
				if (o[0] != null)
					dto.setTransUnit1(o[0].toString());
				if (o[1] != null)
					dto.setDepot1(o[1].toString());
				if (o[2] != null)
					dto.setEtmCount(o[2].toString());

				dto.setFromDate(from);

				dto.setToDate(to);

				if (o[5] != null)
					dto.setMake(o[5].toString());

				if (o[6] != null)
					dto.setMachineFeatures1(o[6].toString());

				eTMStockReportResponseModel1.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.add(eTMStockReportResponseModel1);
		return obj;
	}

	@Override
	public List<List<ETMIssuanceReportResponseModel>> getIssuanceReport(String from, String to, Integer depoId,
			Integer etmNo) {
		Date startDate = null;
		Date endDate = null;

		List<List<ETMIssuanceReportResponseModel>> obj = new ArrayList<>();
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<ETMIssuanceReportResponseModel> eTMIssuanceReportResponseModel = new ArrayList<ETMIssuanceReportResponseModel>();
		try {
			List<Object[]> depo = ticketReportRepository.getIssuanceSummaryReport(startDate, endDate, depoId, etmNo);

			for (Object[] o : depo) {
				ETMIssuanceReportResponseModel dto = new ETMIssuanceReportResponseModel();

				if (o[0] != null)
					dto.setDepotName(o[0].toString());
				dto.setFromDate(from);
				dto.setToDate(to);
				if (o[3] != null)
					dto.setEtmCount(o[3].toString());
				if (o[4] != null)
					dto.setRouteCount(o[4].toString());
				if (o[5] != null)
					dto.setTransUnit(o[5].toString());

				eTMIssuanceReportResponseModel.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		obj.add(eTMIssuanceReportResponseModel);

		List<ETMIssuanceReportResponseModel> eTMIssuanceReportResponseModel1 = new ArrayList<ETMIssuanceReportResponseModel>();
		try {
			List<Object[]> depo = ticketReportRepository.getIssuanceReport(startDate, endDate, depoId, etmNo);

			for (Object[] o : depo) {
				ETMIssuanceReportResponseModel dto = new ETMIssuanceReportResponseModel();
				if (o[0] != null)
					dto.setDepotName1(o[0].toString());
				if (o[1] != null)
					dto.setConductorName(o[1].toString());
				if (o[2] != null)
					dto.setConductorNo(o[2].toString());
				if (o[3] != null)
					dto.setMachineNumber(o[3].toString());
				if (o[4] != null)
					dto.setEtmIssueTime(o[4].toString());
				if (o[5] != null)
					dto.setRouteStartTime(o[5].toString());
		        Timestamp etmIssueTime = Timestamp.valueOf(o[4].toString());
		        Timestamp routeStartTime = Timestamp.valueOf(o[5].toString());
				
		        long milliseconds = routeStartTime.getTime() - etmIssueTime.getTime();
				Integer seconds = (int) milliseconds / 1000;

				Integer hours = seconds / 3600;
				Integer minutes = (seconds % 3600) / 60;
				seconds = (seconds % 3600) % 60;
				String h;
				String m;
				String s;
				
              if (hours<10)
              {
            	  h="0"+ hours.toString();
              }
              else
              {
            	  h=hours.toString();
              }
              if(minutes<10)
              {
            	  m="0"+minutes.toString();
              }
              else
              {
            	  m=minutes.toString();
              }
              if(seconds<10)
              {
            	  s="0"+seconds.toString();
              }
              else
              {
            	  s=seconds.toString();
              }
				
				if (o[6] != null)
					dto.setHowEarlyIssued(h+":"+m+":"+s);
				if (o[7] != null)
					dto.setTransUnit1(o[7].toString());

				eTMIssuanceReportResponseModel1.add(dto);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.add(eTMIssuanceReportResponseModel1);
		return obj;
	}

	@Override
	public List<List<ETMIssuanceReportResponseModel>> getNonIssuanceOfETMReport(String from, String to, Integer depoId,
			Integer etmNo) {

		List<List<ETMIssuanceReportResponseModel>> obj = new ArrayList<>();

		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<ETMIssuanceReportResponseModel> eTMIssuanceReportResponseModel = new ArrayList<ETMIssuanceReportResponseModel>();
		
		try {
			List<Object[]> depo = ticketReportRepository.getNonIssuanceOfETMSummaryReport(startDate,endDate,depoId,etmNo);
			for (Object[] o : depo) {
				ETMIssuanceReportResponseModel dto = new ETMIssuanceReportResponseModel();

				if (o[0] != null)
					dto.setTransUnit(o[0].toString());
				if (o[1] != null)
					dto.setDepotName(o[1].toString());

				dto.setFromDate(from);
				dto.setToDate(to);
				if (o[4] != null)
					dto.setEtmCount(o[4].toString());
				if (o[5] != null)
					dto.setRouteCount(o[5].toString());

				eTMIssuanceReportResponseModel.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.add(eTMIssuanceReportResponseModel);

		List<ETMIssuanceReportResponseModel> eTMIssuanceReportResponseModel1 = new ArrayList<ETMIssuanceReportResponseModel>();

		try {
			List<Object[]> depo = ticketReportRepository.getNonIssuanceOfETMReport(depoId);
			for (Object[] o : depo) {
				ETMIssuanceReportResponseModel dto = new ETMIssuanceReportResponseModel();
				if (o[0] != null)
					dto.setDepotName1(o[0].toString());
				if (o[1] != null)
					dto.setConductorName(o[1].toString());
				if (o[2] != null)
					dto.setConductorNo(o[2].toString());
				if (o[3] != null)
					dto.setMachineNumber(o[3].toString());
				if (o[4] != null)
					dto.setEtmIssueTime(o[4].toString());
				if (o[5] != null)
					dto.setRouteStartTime(o[5].toString());
				if (o[6] != null)
					dto.setLastIssuedDate(o[6].toString());
				if (o[7] != null)
					dto.setNoOfDays(o[7].toString());
				if (o[8] != null)
					dto.setTransUnit1(o[8].toString());

				eTMIssuanceReportResponseModel1.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.add(eTMIssuanceReportResponseModel1);
		return obj;
	}

	@Override
	public List<List<TicketBoxMachIssuReportResModel>> ticketBoxAndMachineIssuanceReport(String from, String to,
			Long depoId) {

		List<List<TicketBoxMachIssuReportResModel>> obj = new ArrayList<>();

		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<TicketBoxMachIssuReportResModel> ticketBoxMachIssuReportResModel = new ArrayList<TicketBoxMachIssuReportResModel>();
		try {
			List<Object[]> depo = ticketReportRepository.ticketBoxAndMachineIssuanceSummaryReport(startDate, endDate,
					depoId);
			for (Object[] o : depo) {
				TicketBoxMachIssuReportResModel dto = new TicketBoxMachIssuReportResModel();
				if (o[0] != null)
					dto.setTransUnit(o[0].toString());
				if (o[1] != null)
					dto.setDepotName(o[1].toString());
				dto.setFromDate(from);
				dto.setToDate(to);

				if (o[4] != null)
					dto.setTicketBoxCount(o[4].toString());
				if (o[5] != null)
					dto.setEtmCount(o[5].toString());
				ticketBoxMachIssuReportResModel.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.add(ticketBoxMachIssuReportResModel);

		
		
		List<TicketBoxMachIssuReportResModel> ticketBoxMachIssuReportResModel1 = new ArrayList<TicketBoxMachIssuReportResModel>();
		try {
			List<Object[]> depo = ticketReportRepository.ticketBoxAndMachineIssuanceReport(startDate, endDate, depoId);
			for (Object[] o : depo) {
				TicketBoxMachIssuReportResModel dto = new TicketBoxMachIssuReportResModel();
				if (o[0] != null)
					dto.setDepotName(o[0].toString());
				if (o[1] != null)
					dto.setDate(o[1].toString());
				if (o[2] != null)
					dto.setTicketBoxNumber(o[2].toString());
				if (o[3] != null)
					dto.setEtmNumber(o[3].toString());
				if (o[4] != null)
					dto.setConductorName(o[4].toString());
				if (o[5] != null)
					dto.setConductorNo(o[5].toString());
				if (o[6] != null)
					dto.setTransUnit1(o[6].toString());
				ticketBoxMachIssuReportResModel1.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.add(ticketBoxMachIssuReportResModel1);
		return obj;
	}

	@Override
	public List<TicketBoxMachIssuReportResModel> issuanceSameTicketBoxMachSameCondRep(String from, String to,
			Integer depoId) {
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		TicketBoxMachIssuReportResModel dto = null;
		List<TicketBoxMachIssuReportResModel> ticketBoxMachIssuReportResModel = new ArrayList<TicketBoxMachIssuReportResModel>();
		try {
			List<Object[]> conductorsIds = ticketReportRepository.issuanceSameTicketBoxMachSameCondRep(startDate,
					endDate, depoId);
			for (Object[] o : conductorsIds) {
				dto = new TicketBoxMachIssuReportResModel();
				if (o[0] != null)
					dto.setDepotName(o[0].toString());
				dto.setFromDate(from);
				dto.setToDate(to);
				if (o[3] != null)
					dto.setTicketBoxNumber(o[3].toString());
				if (o[4] != null)
					dto.setTicketBoxCount(o[4].toString());
				if (o[5] != null)
					dto.setEtmNumber(o[5].toString());
				if (o[6] != null)
					dto.setEtmCount(o[6].toString());
				if (o[7] != null)
					dto.setConductorName(o[7].toString());
				if (o[8] != null)
					dto.setConductorNo(o[8].toString());
				if (o[9] != null)
					dto.setTransUnit(o[9].toString());
				ticketBoxMachIssuReportResModel.add(dto);
				// List<IssueEtmTicketBoxEntity> sameIssuedList =
				// issueEtmTicketBoxRepository
				// .getAllByConductorId(conductorId);
				// if (sameIssuedList.size() > 1) {
				// if (sameIssuedList.get(0).getTicketBoxNumber() != null
				// && sameIssuedList.get(0).getEtmMaster() != null
				// && sameIssuedList.get(1).getTicketBoxNumber() != null
				// && sameIssuedList.get(1).getEtmMaster() != null) {
				// if ((sameIssuedList.get(0).getTicketBoxNumber().getId()) ==
				// (sameIssuedList.get(1)
				// .getTicketBoxNumber().getId())
				// && (sameIssuedList.get(0).getEtmMaster().getId()) ==
				// (sameIssuedList.get(0)
				// .getEtmMaster().getId())) {
				// if (sameIssuedList.get(0).getDepoMaster() != null)
				// dto.setDepotName(sameIssuedList.get(0).getDepoMaster().getDepotName());
				// if (sameIssuedList.get(0).getTicketBoxNumber() != null)
				// dto.setTicketBoxNumber(sameIssuedList.get(0).getTicketBoxNumber().getTicketBoxNumber());
				// if (sameIssuedList.get(0).getEtmMaster() != null)
				// dto.setEtmNumbe(sameIssuedList.get(0).getEtmMaster().getEtmNumber());
				// if (sameIssuedList.get(0).getConductorMaster() != null) {
				// dto.setConductorName(sameIssuedList.get(0).getConductorMaster().getConductorName());
				// dto.setConductorNo(sameIssuedList.get(0).getConductorMaster().getConductorCode());
				// }
				// ticketBoxMachIssuReportResModel.add(dto);
				// }
				// }
				//
				// }
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ticketBoxMachIssuReportResModel;
	}

	@Override
	public List<FuelDispenWiseFuelIssuReportResModel> fuelDispenserWiseFuelIssuanceReport(String from, String to,
			Long stateId) {
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<FuelDispenWiseFuelIssuReportResModel> fuelDispenWiseFuelIssuReportResModel = new ArrayList<FuelDispenWiseFuelIssuReportResModel>();
		/*
		 * try { List<Object[]> depo=
		 * ticketReportRepository.fuelDispenserWiseFuelIssuanceReport(startDate,
		 * endDate,stateId); for(Object[] o: depo) {
		 * FuelDispenWiseFuelIssuReportResModel dto = new
		 * FuelDispenWiseFuelIssuReportResModel();
		 * dto.setDepot(o[0].toString()); dto.setState(o[1].toString());
		 * dto.setDenomination(o[2].toString()); dto.setNoBook(o[3].toString());
		 * dto.setValueInRs(o[4].toString());
		 * dto.setRecievedDate(o[5].toString());
		 * fuelDispenWiseFuelIssuReportResModel.add(dto); } }catch(Exception e)
		 * { e.printStackTrace(); }
		 */
		FuelDispenWiseFuelIssuReportResModel dtos = new FuelDispenWiseFuelIssuReportResModel();
		dtos.setDepot("DP-JAL-1");
		dtos.setState("punjab");
		dtos.setDenomination("5");
		dtos.setStartingSrNo("100100");
		dtos.setEndSerialNo("100500");
		dtos.setNoBook("Abc");
		dtos.setValueInRs("100");
		// dtos.setRecievedDate("2022-1-10");
		fuelDispenWiseFuelIssuReportResModel.add(dtos);

		return fuelDispenWiseFuelIssuReportResModel;
	}

	@Override
	public List<FuelDispenWiseFuelIssuReportResModel> depotWisePaperTicketStockReport(String from, String to,
			Long depotId) {

		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<FuelDispenWiseFuelIssuReportResModel> fuelDispenWiseFuelIssuReportResModel = new ArrayList<FuelDispenWiseFuelIssuReportResModel>();
		try {
			List<Object[]> depo = ticketReportRepository.depotWisePaperTicketStockReport(startDate, endDate, depotId);
			for (Object[] o : depo) {
				FuelDispenWiseFuelIssuReportResModel dto = new FuelDispenWiseFuelIssuReportResModel();
				if (o[0] != null)
					dto.setDepot(o[0].toString());
				if (o[1] != null)
					dto.setState(o[1].toString());
				if (o[2] != null)
					dto.setDenomination(o[2].toString());
				if (o[3] != null)
					dto.setNoOfBooks(o[3].toString());
				if (o[4] != null)
					dto.setValueInRs(o[4].toString());
				if (o[5] != null)
					dto.setNoBook(o[5].toString());
				if (o[6] != null)
					dto.setRecievedDate(o[6].toString());
				if (o[7] != null)
					dto.setTransportUnit(o[7].toString());
				if (o[8] != null)
					dto.setSeriesNumber(o[8].toString());

				fuelDispenWiseFuelIssuReportResModel.add(dto);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * FuelDispenWiseFuelIssuReportResModel dto = new
		 * FuelDispenWiseFuelIssuReportResModel(); dto.setDepot("DP-JAL-1");
		 * dto.setState("punjab"); dto.setDenomination("15");
		 * dto.setNoBook("Abc"); dto.setValueInRs("100");
		 * dto.setRecievedDate("2022-1-10");
		 * fuelDispenWiseFuelIssuReportResModel.add(dto);
		 */

		return fuelDispenWiseFuelIssuReportResModel;
	}

	@Override
	public List<List<MachineNotRecSchReportDto>> machineNotReceivedScheduledReport(String from, String to,
			Integer depotId) {
		List<List<MachineNotRecSchReportDto>> obj = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<MachineNotRecSchReportDto> machineNotRecSchReportDto = new ArrayList<MachineNotRecSchReportDto>();

		try {
			List<Object[]> depo = ticketReportRepository.machineNotReceivedScheduledSummaryReport(startDate, endDate,
					depotId);
			for (Object[] o : depo) {
				MachineNotRecSchReportDto dto = new MachineNotRecSchReportDto();
				if (o[0] != null)
					dto.setTransUnit(o[0].toString());
				if (o[1] != null)
					dto.setDepot(o[1].toString());

				dto.setFromDate(from);
				dto.setToDate(to);
				if (o[4] != null)
					dto.setEtmCount(o[4].toString());
				machineNotRecSchReportDto.add(dto);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		obj.add(machineNotRecSchReportDto);

		List<MachineNotRecSchReportDto> machineNotRecSchReportDto1 = new ArrayList<MachineNotRecSchReportDto>();

		try {
			List<Object[]> depo = ticketReportRepository.machineNotReceivedScheduledReport(startDate, endDate, depotId);
			for (Object[] o : depo) {
				MachineNotRecSchReportDto dto = new MachineNotRecSchReportDto();

				if (o[0] != null)
					dto.setDepot(o[0].toString());
				if (o[1] != null)
					dto.setMachineNumber(o[1].toString());
				if (o[2] != null)
					dto.setScheduleArrival(o[2].toString());
				if (o[3] != null)
					dto.setActualArrived(o[3].toString());
				
				
				Timestamp ActualArrived = Timestamp.valueOf(o[3].toString());
		        Timestamp ScheduleArrival= Timestamp.valueOf(o[2].toString());
				
		        long milliseconds =ActualArrived.getTime() -  ScheduleArrival.getTime();
				Integer seconds = (int) milliseconds / 1000;

				Integer hours = seconds / 3600;
				Integer minutes = (seconds % 3600) / 60;
				seconds = (seconds % 3600) % 60;
				String h;
				String m;
				String s;
				
              if (hours<10)
              {
            	  h="0"+ hours.toString();
              }
              else
              {
            	  h=hours.toString();
              }
              if(minutes<10)
              {
            	  m="0"+minutes.toString();
              }
              else
              {
            	  m=minutes.toString();
              }
              if(seconds<10)
              {
            	  s="0"+seconds.toString();
              }
              else
              {
            	  s=seconds.toString();
              }
				
				if (o[4] != null)
					dto.setLateInHours(h+":"+m+":"+s);

				if (o[5] != null)
					dto.setTransUnit(o[5].toString());
				machineNotRecSchReportDto1.add(dto);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.add(machineNotRecSchReportDto1);

		/*
		 * MachineNotRecSchReportDto dto = new MachineNotRecSchReportDto();
		 * dto.setDepot("DP-JAL-1"); dto.setMachineNumber("256256");
		 * dto.setScheduleArrival("10"); dto.setActualArrived("12");
		 * dto.setLateInHours("3"); machineNotRecSchReportDto.add(dto);
		 */
		return obj;
	}

	@Override
	public List<MachineNotRecSchReportDto> frequentlyIssuanceMachineWithTicketBoxReport(String from, String to,
			Integer depotId) {
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<MachineNotRecSchReportDto> machineNotRecSchReportDto = new ArrayList<MachineNotRecSchReportDto>();
		/*
		 * MachineNotRecSchReportDto dto = null; try { List<Integer> etmIds =
		 * ticketReportRepository.frequentlyIssuanceMachineWithTicketBoxReport(
		 * startDate, endDate, depotId); for (Integer etmId : etmIds) { dto =
		 * new MachineNotRecSchReportDto(); List<IssueEtmTicketBoxEntity>
		 * sameIssuedList = issueEtmTicketBoxRepository .getAllByEtmId(etmId);
		 * if (sameIssuedList.size() > 1) { if
		 * (sameIssuedList.get(0).getTicketBoxNumber() != null &&
		 * sameIssuedList.get(1).getTicketBoxNumber() != null) { if
		 * ((sameIssuedList.get(0).getTicketBoxNumber().getId()) ==
		 * (sameIssuedList.get(1) .getTicketBoxNumber().getId())) { if
		 * (sameIssuedList.get(0).getDepoMaster() != null)
		 * dto.setDepot(sameIssuedList.get(0).getDepoMaster().getDepotName());
		 * if (sameIssuedList.get(0).getTicketBoxNumber() != null)
		 * dto.setTicketBox(sameIssuedList.get(0).getTicketBoxNumber().
		 * getTicketBoxNumber()); if (sameIssuedList.get(0).getEtmMaster() !=
		 * null) dto.setMachineNumber(sameIssuedList.get(0).getEtmMaster().
		 * getEtmNumber()); dto.setNumberFrequency(sameIssuedList.size());
		 * machineNotRecSchReportDto.add(dto); } }
		 * 
		 * } }
		 * 
		 * }catch(Exception e) { e.printStackTrace(); }
		 */

		MachineNotRecSchReportDto dto = new MachineNotRecSchReportDto();
		dto.setDepot("DP-JAL-1");
		dto.setMachineNumber("256256");
		dto.setMachineFaulty("not working");
		dto.setTicketBox("2323");
		dto.setNumberFrequency(1212);
		machineNotRecSchReportDto.add(dto);
		return machineNotRecSchReportDto;
	}

	@Override
	public List<KMPLTicketReportDto> kmplReport(String from, String to, Integer conductor) {
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<KMPLTicketReportDto> kMPLTicketReportDto = new ArrayList<KMPLTicketReportDto>();
		try {
			List<Object[]> depo = ticketReportRepository.kmplReport(startDate, endDate, conductor);
			for (Object[] o : depo) {
				KMPLTicketReportDto dto = new KMPLTicketReportDto();
				if (o[0] != null)
					dto.setDepoName(o[0].toString());
				if (o[1] != null)
					dto.setDate(o[1].toString());
				if (o[2] != null)
					dto.setConductorName(o[2].toString());
				if (o[3] != null)
					dto.setConductorNo(o[3].toString());
				if (o[4] != null)
					dto.setRouteName(o[4].toString());
				if (o[5] != null)
					dto.setAmountOnPaperTicketUsed(o[5].toString());
				if (o[6] != null)
					dto.setTransportUnit(o[6].toString());
				kMPLTicketReportDto.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * KMPLTicketReportDto dto = new KMPLTicketReportDto();
		 * dto.setConductorName("RaviKumar"); dto.setConductorNo("12423");
		 * dto.setRouteName("jaldhar"); dto.setAmountOnPaperTicketUsed("12000");
		 * kMPLTicketReportDto.add(dto);
		 */
		return kMPLTicketReportDto;
	}

	@Override
	public List<KMPLTicketReportDto> lateCashDepositReport(String from, String to, Long conductor) {
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<KMPLTicketReportDto> kMPLTicketReportDto = new ArrayList<KMPLTicketReportDto>();
		try {
			List<Object[]> depo = ticketReportRepository.lateCashDepositReport(startDate, endDate, conductor);
			for (Object[] o : depo) {
				KMPLTicketReportDto dto = new KMPLTicketReportDto();
				if (o[0] != null)
					dto.setDepoName(o[0].toString());
				if (o[1] != null)
					dto.setConductorName(o[1].toString());
				if (o[2] != null)
					dto.setConductorNo(o[2].toString());
				if (o[3] != null)
					dto.setRouteName(o[3].toString());
				
				if (o[5] != null)
					dto.setTransportUnit(o[5].toString());
				if (o[6] != null)
					dto.setActualTime(o[6].toString());
				if (o[7] != null)
					dto.setScheduledTime(o[7].toString());
			
				
				Timestamp ActualArrived = Timestamp.valueOf(o[6].toString());
		        Timestamp ScheduleArrival= Timestamp.valueOf(o[7].toString());
				
		        long milliseconds =ActualArrived.getTime() -  ScheduleArrival.getTime();
				Integer seconds = (int) milliseconds / 1000;

				Integer hours = seconds / 3600;
				Integer minutes = (seconds % 3600) / 60;
				seconds = (seconds % 3600) % 60;
				String h;
				String m;
				String s;
				
              if (hours<10)
              {
            	  h="0"+ hours.toString();
              }
              else
              {
            	  h=hours.toString();
              }
              if(minutes<10)
              {
            	  m="0"+minutes.toString();
              }
              else
              {
            	  m=minutes.toString();
              }
              if(seconds<10)
              {
            	  s="0"+seconds.toString();
              }
              else
              {
            	  s=seconds.toString();
              }
				
				if (o[4] != null)
					dto.setLateInHours(h+":"+m+":"+s);

				kMPLTicketReportDto.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * KMPLTicketReportDto dto = new KMPLTicketReportDto();
		 * dto.setConductorName("RaviKumar"); dto.setConductorNo("12121");
		 * dto.setRouteName("jaldhar"); dto.setLateInHours("2");
		 * kMPLTicketReportDto.add(dto);
		 */

		return kMPLTicketReportDto;
	}

	@Override
	public List<AddaFeesTollFeeReportDto> addaFeesAndTollFeeReport(String from, String to, Long depot) {
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<AddaFeesTollFeeReportDto> addaFeesTollFeeReportDto = new ArrayList<AddaFeesTollFeeReportDto>();
		
		  try { 
			  List<Object[]> depo=ticketReportRepository.addaFeesAndTollFeeReport(startDate,endDate,depot);
			  for(Object[] o: depo)
			  { 
				  AddaFeesTollFeeReportDto dto = new AddaFeesTollFeeReportDto();
				  if(o[0]!=null)
				  dto.setTransportUnit(o[0].toString());
				  if(o[1]!=null)
				  dto.setDepotName(o[1].toString());
				  if(o[2]!=null)
				  dto.setDate(o[2].toString());
				  if(o[3]!=null)
		          dto.setRouteName(o[3].toString());
				  if(o[4]!=null)
		          dto.setAddaFees(o[4].toString());
				  if(o[5]!=null)
		          dto.setTollFees(o[5].toString());
				  if(o[6]!=null)
		          dto.setTotalFees(o[6].toString());
		          addaFeesTollFeeReportDto.add(dto);
		          } 
			  }
		  catch(Exception e) 
		  {
		 e.printStackTrace(); 
		 }
		 

		/*AddaFeesTollFeeReportDto dto = new AddaFeesTollFeeReportDto();
		dto.setDepotName("DP-JAL-1");
		dto.setRouteName("Jaldhar");
		dto.setAddaFees("1000");
		dto.setTollFees("20000");
		dto.setTotalFees("200000");
		addaFeesTollFeeReportDto.add(dto);*/
		return addaFeesTollFeeReportDto;
	}

	@Override
	public List<KMReceivedLessthanSchduledKMsDto> kMReceivedLessthanSchduledKMsReport(String from, String to,
			Long route) {
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<KMReceivedLessthanSchduledKMsDto> kMReceivedLessthanSchduledKMsDto = new ArrayList<KMReceivedLessthanSchduledKMsDto>();
		try {
			List<Object[]> depo = ticketReportRepository.kMReceivedLessthanSchduledKMsReport(startDate, endDate, route);
			for (Object[] o : depo) {
				KMReceivedLessthanSchduledKMsDto dto = new KMReceivedLessthanSchduledKMsDto();
				if (o[0] != null)
					dto.setRouteName(o[0].toString());
				if (o[1] != null)
					dto.setScheduledKms(o[1].toString());
				if (o[2] != null)
					dto.setReceivedKMs(o[2].toString());
				if (o[3] != null)
					dto.setVtsKms(o[3].toString());
				if (o[4] != null)
					dto.setDifference(o[4].toString());
				if (o[5] != null)
					dto.setReason(o[5].toString());
				kMReceivedLessthanSchduledKMsDto.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * KMReceivedLessthanSchduledKMsDto dto = new
		 * KMReceivedLessthanSchduledKMsDto(); dto.setRouteName("Jaldhar");
		 * dto.setScheduledKms("230"); dto.setReceivedKMs("38");
		 * dto.setDifference("10"); dto.setReason("ABC");
		 * kMReceivedLessthanSchduledKMsDto.add(dto);
		 */

		return kMReceivedLessthanSchduledKMsDto;
	}

	@Override
	public List<KMReceivedLessthanSchduledKMsDto> nonScheduleKMsReport(String from, String to, Long route) {
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<KMReceivedLessthanSchduledKMsDto> kMReceivedLessthanSchduledKMsDto = new ArrayList<KMReceivedLessthanSchduledKMsDto>();
		try {
			List<Object[]> depo = ticketReportRepository.nonScheduleKMsReport(startDate, endDate, route);
			for (Object[] o : depo) {
				KMReceivedLessthanSchduledKMsDto dto = new KMReceivedLessthanSchduledKMsDto();
				if (o[0] != null)
					dto.setRouteName(o[0].toString());
				if (o[1] != null)
					dto.setScheduledKms(o[1].toString());
				if (o[2] != null)
					dto.setReceivedKMs(o[2].toString());
				if (o[3] != null)
					dto.setVtsKms(o[3].toString());
				if (o[4] != null)
					dto.setDifference(o[4].toString());
				if (o[5] != null)
					dto.setReason(o[5].toString());
				kMReceivedLessthanSchduledKMsDto.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * KMReceivedLessthanSchduledKMsDto dto = new
		 * KMReceivedLessthanSchduledKMsDto(); dto.setRouteName("Jaldhar");
		 * dto.setScheduledKms("200"); dto.setNonScheduleKm("100");
		 * dto.setDifference("20"); dto.setReason("ABC");
		 * kMReceivedLessthanSchduledKMsDto.add(dto);
		 */
		return kMReceivedLessthanSchduledKMsDto;
	}

	@Override
	public List<ComparisonReportDutyRosterDto> comparisonReportDutyRosterKMs(String from, String to, Long depot) {
		Date startDate = null;
		Date endDate = null;
		
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<ComparisonReportDutyRosterDto> comparisonReportDutyRosterDto = new ArrayList<ComparisonReportDutyRosterDto>();
		
		  try { List<Object[]> depo=ticketReportRepository.comparisonReportDutyRosterKMs(startDate,endDate,depot); 
		  for(Object[] o: depo) {
		  ComparisonReportDutyRosterDto dto = new ComparisonReportDutyRosterDto();
		  if(o[0]!=null)
		  dto.setTransportName(o[0].toString());
		  if(o[1]!=null)
		  dto.setDepotName(o[1].toString());
		  if(o[2]!=null)
		  dto.setRouteName(o[2].toString());
		  if(o[3]!=null)
		  dto.setRouteKms(o[3].toString());
		  if(o[4]!=null)
		  dto.setDpaKms(o[4].toString());
		  if(o[5]!=null)
		  dto.setBusRegisterNumber(o[5].toString());
		  if(o[6]!=null)
		  dto.setBusKms(o[6].toString());
		  if(o[7]!=null)
		  dto.setTripCount(o[7].toString());
		  if(o[8]!=null)
		  dto.setDriverName(o[8].toString());
		  if(o[9]!=null)
		  dto.setConductorName(o[9].toString());
		 
		  comparisonReportDutyRosterDto.add(dto);
		  } 
		  }
		  catch(Exception e)
		  {
		  e.printStackTrace(); 
		  }
		 

		/*ComparisonReportDutyRosterDto dto = new ComparisonReportDutyRosterDto();
		dto.setDepotName("DP-JAL-1");
		dto.setDutyRosterKMs("200");
		dto.setDpaKms("100");
		dto.setBusKms("245");
		dto.setDriverKms("200");
		dto.setConductorKms("20");
		dto.setRouteKms("200");
		comparisonReportDutyRosterDto.add(dto);*/

		return comparisonReportDutyRosterDto;
	}

	@Override
	public List<RepairBillReportDto> repairBillReport(String from, String to, Long depot) {
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<RepairBillReportDto> repairBillReportDto = new ArrayList<RepairBillReportDto>();
		/*
		 * try { List<Object[]> depo=
		 * ticketReportRepository.repairBillReport(startDate,endDate,depot);
		 * for(Object[] o: depo) { RepairBillReportDto dto = new
		 * RepairBillReportDto(); dto.setDepot(o[0].toString());
		 * dto.setRepairBill(o[1].toString());
		 * dto.setRepairShopName(o[2].toString());
		 * dto.setDieselTaken(o[3].toString());
		 * dto.setNameofPump(o[4].toString()); repairBillReportDto.add(dto); }
		 * }catch(Exception e) { e.printStackTrace(); }
		 */

		RepairBillReportDto dto = new RepairBillReportDto();
		dto.setDepot("DP-JAL-1");
		dto.setRepairBill("AAA");
		dto.setRepairShopName("KrishanaShop");
		dto.setDieselTaken("100");
		dto.setNameofPump("ABAB");
		repairBillReportDto.add(dto);

		return repairBillReportDto;
	}

	@Override
	public List<BusStandWiseAdvanceBookingDto> busStandWiseAdvanceBookingReport(String from, String to, Long depot) {
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<BusStandWiseAdvanceBookingDto> busStandWiseAdvanceBookingDto = new ArrayList<BusStandWiseAdvanceBookingDto>();
		
		 try { List<Object[]> depo=
		  ticketReportRepository.busStandWiseAdvanceBookingReport(startDate,endDate,depot); for(Object[] o: depo) 
		 {
			 BusStandWiseAdvanceBookingDto dto = new BusStandWiseAdvanceBookingDto();
		  dto.setDepot(o[0].toString()); 
		  dto.setRouteName(o[1].toString());
		  dto.setBusStandName(o[2].toString());
		  dto.setAdvanceBookingAmount(o[3].toString());
		  dto.setCashDepositedAmount(o[4].toString());
		  dto.setConductorName(o[5].toString());
		  dto.setConductorCode(o[6].toString());
		  busStandWiseAdvanceBookingDto.add(dto); 
		  } }
		 catch(Exception e) {
		  e.printStackTrace(); }
		 

		/*BusStandWiseAdvanceBookingDto dto = new BusStandWiseAdvanceBookingDto();
		dto.setDepot("DP-JAL-1");
		dto.setRouteName("JalDhar");
		dto.setBusStandName("Niyapoor");
		dto.setAdvanceBookingAmount("10000");
		dto.setCashDepositedAmount("20000");
		busStandWiseAdvanceBookingDto.add(dto);*/
		return busStandWiseAdvanceBookingDto;
	}

	@Override
	public List<BusStandWiseAdvanceBookingDto> freeTravelingConcessionTravelingReport(String from, String to) {
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<BusStandWiseAdvanceBookingDto> busStandWiseAdvanceBookingDto = new ArrayList<BusStandWiseAdvanceBookingDto>();
		try {
			List<Object[]> depo = ticketReportRepository.freeTravelingConcessionTravelingReport(startDate, endDate);
			for (Object[] o : depo) {
				BusStandWiseAdvanceBookingDto dto = new BusStandWiseAdvanceBookingDto();
				if (o[0] != null)
					dto.setDepot(o[0].toString());
				if (o[1] != null)
					dto.setRouteName(o[1].toString());
				if (o[2] != null)
					dto.setAmount(o[2].toString());
				if (o[3] != null)
					dto.setTransportUnit(o[3].toString());
				if (o[4] != null)
					dto.setPassType(o[4].toString());
				if (o[5] != null)
					dto.setDiscount(o[5].toString());
				
				busStandWiseAdvanceBookingDto.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * BusStandWiseAdvanceBookingDto dto = new
		 * BusStandWiseAdvanceBookingDto(); dto.setDepot("DP-JAL-1");
		 * dto.setRouteName("Jaldhar"); dto.setFreeTicket("1000");
		 * dto.setConcessionTicket("2000"); dto.setTotal("20000");
		 * busStandWiseAdvanceBookingDto.add(dto);
		 */
		return busStandWiseAdvanceBookingDto;
	}

	@Override
	public List<List<BusStandWiseAdvanceBookingDto>> busTypeSubTypeCashDepositReport(String from, String to, Long busType,Integer depotId) {
		Date startDate = null;
		Date endDate = null;
		
		List<List<BusStandWiseAdvanceBookingDto>> busStanddtls= new ArrayList<>();
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<BusStandWiseAdvanceBookingDto> busStandWiseAdvanceBookingDto = new ArrayList<BusStandWiseAdvanceBookingDto>();
		
		  try {
			  List<Object[]> depo= ticketReportRepository.busTypeSubTypeCashDepositSummaryReport(startDate,endDate,busType,depotId); 
			  for(Object[] o: depo) 
			  {
		 BusStandWiseAdvanceBookingDto dto = new BusStandWiseAdvanceBookingDto(); 
		 if(o[0]!=null)
		 dto.setTransportUnit(o[0].toString());
		 if(o[1]!=null)
		 dto.setDepot(o[1].toString());
		 if(o[2]!=null)
		 dto.setBusType(o[2].toString());
		 dto.setFromDate(from);
		 dto.setToDate(to);
		 if(o[5]!=null)
		 dto.setRouteName(o[5].toString());
		 if(o[6]!=null)
		 dto.setBusCount(o[6].toString());
		 if(o[7]!=null)
		 dto.setTotal(o[7].toString());
		  busStandWiseAdvanceBookingDto.add(dto);
		  } }
		  catch(Exception e)
		  {
		 e.printStackTrace();
		 }
		  busStanddtls.add(busStandWiseAdvanceBookingDto);
		  
		  List<BusStandWiseAdvanceBookingDto> busStandWiseAdvanceBookingDto1 = new ArrayList<BusStandWiseAdvanceBookingDto>();
			
		  try {
			  List<Object[]> depo= ticketReportRepository.busTypeSubTypeCashDepositReport(startDate,endDate,busType,depotId); 
			  for(Object[] o: depo) 
			  {
		 BusStandWiseAdvanceBookingDto dto = new BusStandWiseAdvanceBookingDto(); 
		 if(o[0]!=null)
		 dto.setTransportUnit1(o[0].toString());
		 if(o[1]!=null)
		 dto.setDepot1(o[1].toString());
		 if(o[2]!=null)
		 dto.setBusType(o[2].toString());
		 dto.setFromDate(from);
		 dto.setToDate(to);
		 if(o[5]!=null)
		 dto.setRouteName(o[5].toString());
		 if(o[6]!=null)
		 dto.setBusNo(o[6].toString());
		 if(o[7]!=null)
		 dto.setTotal(o[7].toString());
		  busStandWiseAdvanceBookingDto1.add(dto);
		  } }
		  catch(Exception e)
		  {
		 e.printStackTrace();
		 }
		  
		  
		  busStanddtls.add(busStandWiseAdvanceBookingDto1);
		  
		  
		  
		  
		  
		  
		  

		/*BusStandWiseAdvanceBookingDto dto = new BusStandWiseAdvanceBookingDto();
		dto.setBusNo("buspbo390");
		dto.setBusType("Volvo");
		dto.setTotal("10000");
		busStandWiseAdvanceBookingDto.add(dto);*/
		return busStanddtls;
	}

	@Override
	public List<List<MachineNotRecSchReportDto>> machineNotReceivedAsperScheduledAndDelayInhrsReport(String from, String to,
			Integer depotId) {

		List<List<MachineNotRecSchReportDto>> obj= new ArrayList<>();
		Date startDate = null;
		Date endDate = null;
		try {
			if (from != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (to != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<MachineNotRecSchReportDto> machineNotRecSchReportDto = new ArrayList<MachineNotRecSchReportDto>();
		try {
			List<Object[]> depo = ticketReportRepository.machineNotReceivedAsperScheduledAndDelayInhrsSummaryReport(startDate, endDate, depotId);
			for (Object[] o : depo) {
				MachineNotRecSchReportDto dto = new MachineNotRecSchReportDto();
				if (o[0] != null)
					dto.setTransUnit(o[0].toString());
				if (o[1] != null)
					dto.setDepot(o[1].toString());

				dto.setFromDate(from);
				dto.setToDate(to);
				if (o[4] != null)
					dto.setEtmCount(o[4].toString());
				
				machineNotRecSchReportDto.add(dto);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		obj.add(machineNotRecSchReportDto);
		
		
		List<MachineNotRecSchReportDto> machineNotRecSchReportDto1 = new ArrayList<MachineNotRecSchReportDto>();
		try {
			List<Object[]> depo = ticketReportRepository.machineNotReceivedAsperScheduledAndDelayInhrsReport(startDate, endDate, depotId);
			for (Object[] o : depo) {
				MachineNotRecSchReportDto dto = new MachineNotRecSchReportDto();
				if (o[0] != null)
					dto.setDepot(o[0].toString());
				if (o[1] != null)
					dto.setMachineNumber(o[1].toString());
				if (o[2] != null)
					dto.setScheduleArrival(o[2].toString());
				if (o[3] != null)
					dto.setActualArrived(o[3].toString());
				Timestamp ActualArrived = Timestamp.valueOf(o[3].toString());
		        Timestamp ScheduleArrival= Timestamp.valueOf(o[2].toString());
				
		        long milliseconds =ActualArrived.getTime() -  ScheduleArrival.getTime();
				Integer seconds = (int) milliseconds / 1000;

				Integer hours = seconds / 3600;
				Integer minutes = (seconds % 3600) / 60;
				seconds = (seconds % 3600) % 60;
				String h;
				String m;
				String s;
				
              if (hours<10)
              {
            	  h="0"+ hours.toString();
              }
              else
              {
            	  h=hours.toString();
              }
              if(minutes<10)
              {
            	  m="0"+minutes.toString();
              }
              else
              {
            	  m=minutes.toString();
              }
              if(seconds<10)
              {
            	  s="0"+seconds.toString();
              }
              else
              {
            	  s=seconds.toString();
              }
				
				if (o[4] != null)
					dto.setLateInHours(h+":"+m+":"+s);
				if (o[5] != null)
					dto.setTransUnit(o[5].toString());
				

				machineNotRecSchReportDto1.add(dto);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
   obj.add(machineNotRecSchReportDto1);
		return obj;
	}

}
