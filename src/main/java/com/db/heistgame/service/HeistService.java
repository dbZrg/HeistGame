package com.db.heistgame.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.db.heistgame.dto.EligibleMembers;
import com.db.heistgame.enums.HeistStatus;
import com.db.heistgame.enums.StatusType;
import com.db.heistgame.mapper.HeistMapper;
import com.db.heistgame.model.Heist;
import com.db.heistgame.model.Member;
import com.db.heistgame.model.ReqSkill;
import com.db.heistgame.repository.HeistRepo;
import com.db.heistgame.repository.MemberRepo;
import com.db.heistgame.repository.ReqSkillRepo;
import com.db.heistgame.tasks.EndHeistTask;
import com.db.heistgame.tasks.StartHeistTask;
import com.db.heistgame.util.EmailSender;
import com.db.heistgame.util.Helper;

@Service
public class HeistService {
	
	@Autowired
	Helper helper;
	
	@Autowired
	EmailSender emailSender;
	
	@Autowired
	HeistRepo heistRepo;
	
	@Autowired
	ReqSkillRepo reqSkillRepo;
	
	@Autowired
	MemberRepo memberRepo;
	
	@Autowired
	HeistMapper mapper;
	
	@Value("${heist.secondsToLevelUp}")
	private Long secondsToLevel;
	
	private static final String NO_HEIST_MSG = "Heist doesnt exist";  
	
	
	
	public Heist addHeist(Heist heist) {
		
		if(!helper.areAllUniqueReq(heist.getSkills())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate skill names and levels");
		}
		if(heistRepo.existsByName(heist.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Heist name already exists");
		}
		if(heist.getStartTime().isAfter(heist.getEndTime())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time is after end time");
		}
		if(ZonedDateTime.now().isAfter(heist.getEndTime())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End time is in the past");
		}
		
		
		for (ReqSkill skill : heist.getSkills()) {
			skill.setHeist(heist);
		}
		heist.setStatus(HeistStatus.PLANNING);
		heist.setStartTime(heist.getStartTime().minusHours(1));
		heist.setEndTime(heist.getEndTime().minusHours(1));
		heistRepo.save(heist);

		return heist;
	}
	

	public Heist addOrUpdateReqSkills(long id, List<ReqSkill> reqSkills) {
		
		Heist heist = heistRepo.findById(id);
		
		if(heist == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NO_HEIST_MSG);
		}
		if(heist.getStatus() == HeistStatus.IN_PROGRESS) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Heist has already started");
		}
		if(!helper.areAllUniqueReq(reqSkills)){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate skill names and levels");
		}
		
		
		for (ReqSkill reqSkill : reqSkills) {
			ReqSkill dbReqSkill = reqSkillRepo.findByNameAndLevelAndHeist(reqSkill.getName(), reqSkill.getLevel(), heist);
			if(dbReqSkill != null) {
				reqSkill.setIdReqSkill(dbReqSkill.getIdReqSkill());
			}
			reqSkill.setHeist(heist);
			reqSkillRepo.save(reqSkill);
		}
		
		return heist;
	}
	
	
	public EligibleMembers getEligibleMembers(long id){
		
		Heist heist = heistRepo.findById(id);
		
		if(heist == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NO_HEIST_MSG);
		}
		if(!heist.getConfirmedMembers().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Members have already been confirmed");
		}
		
		
		List<Member> eligibleMembers = new ArrayList<>();			
		
		for (ReqSkill reqSkill : heist.getSkills()) {
			List<Member> eMembers = memberRepo.findEliglibleMembers(reqSkill.getName(), reqSkill.getLevel().length());
			eMembers.forEach(eligibleMembers::add);
		}	
		
		eligibleMembers = eligibleMembers.stream().distinct().collect(Collectors.toList());
		EligibleMembers output = new EligibleMembers();
		output.setSkills(mapper.toReqSkillDtoList(heist.getSkills()));
		output.setMembers(mapper.toMemberDtoList(eligibleMembers));
		
		return output;
		
	}
	
	
	public Heist confirmMembers(long id, List<String> memberNames) {
		
		Heist heist = heistRepo.findById(id);
		List<Member> confirmedMembers = new ArrayList<Member>();
		
		if(heist == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NO_HEIST_MSG);
		}
		if(heist.getStatus() != HeistStatus.PLANNING) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Heist in not in PLANNING");
		}
		
		for (String name : memberNames) {
			List<Member> members = memberRepo.findByName(name);
			
			if(members == null || members.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member dosnt exit");
			}
			
			for (Member member : members) {
				
				if(member.getStatus() != StatusType.AVAILABLE && member.getStatus() != StatusType.RETIRED) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member is not AVAILABLE or RETIRED");
				}
				if(member.getHeist() != null) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member is already on a heist");
				}
				if(!helper.hasRequriedSkill(member.getSkills(),heist.getSkills())) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member doesnt have required skills for heist");
				}
				
				confirmedMembers.add(member);		
				member.setHeist(heist);
			}
			
			
			
			
		}
		
		emailSender.sendEmails("Confirmed for heist", "You have been confirmed to participate in heist", confirmedMembers);
		heist.setConfirmedMembers(confirmedMembers);
		heist.setStatus(HeistStatus.READY);
		heistRepo.save(heist);
		
		
		//start tasks after members confirmation
		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

		long startIn = heist.getStartTime().toInstant().toEpochMilli() - ZonedDateTime.now().toInstant().toEpochMilli();
		service.schedule(new StartHeistTask(heistRepo, heist, emailSender), startIn, TimeUnit.MILLISECONDS);
		
		long stopIn = heist.getEndTime().toInstant().toEpochMilli() - ZonedDateTime.now().toInstant().toEpochMilli();
		service.schedule(new EndHeistTask(heistRepo,memberRepo, heist, secondsToLevel, emailSender), stopIn, TimeUnit.MILLISECONDS);
		
		return heist;
	}
	
	
	public Heist startHeist(long id) {
		
		Heist heist = heistRepo.findById(id);
		
		if(heist == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NO_HEIST_MSG);
		}
		if(heist.getStatus() != HeistStatus.READY) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Heist is not READY");
		}
		
		heist.setStatus(HeistStatus.IN_PROGRESS);
		heistRepo.save(heist);
		
		return heist;
	}
	
	
	public Heist getHeist(long id){	
		
		Heist heist = heistRepo.findById(id);
		
		if(heist == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Heist dont exist");
		}
		return heist;
		
	}
	
	
	public List<Heist> getHeists(){	
		return StreamSupport.stream(heistRepo.findAll().spliterator(), false)
                			.collect(Collectors.toList());
	}
}
