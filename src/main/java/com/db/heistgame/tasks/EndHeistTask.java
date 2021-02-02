package com.db.heistgame.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import com.db.heistgame.enums.HeistOutcome;
import com.db.heistgame.enums.HeistStatus;
import com.db.heistgame.enums.StatusType;
import com.db.heistgame.model.Heist;
import com.db.heistgame.model.Member;
import com.db.heistgame.model.ReqSkill;
import com.db.heistgame.model.Skill;
import com.db.heistgame.repository.HeistRepo;
import com.db.heistgame.repository.MemberRepo;
import com.db.heistgame.util.EmailSender;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Slf4j
public class EndHeistTask implements Runnable {
	
	private HeistRepo heistRepo;
	private MemberRepo memberRepo;
	private Heist heist;
	private Long secondsToLevel;
	private EmailSender emailSender;
	
	final Random random = new Random();	
	
	public EndHeistTask(HeistRepo heistRepo, MemberRepo memberRepo, Heist heist, Long secondsToLevel , EmailSender emailSender) {
		super();
		this.heistRepo = heistRepo;
		this.memberRepo = memberRepo;
		this.heist = heist;
		this.secondsToLevel = secondsToLevel;
		this.emailSender = emailSender;
	}

	
	@Transactional
	@Override
	public void run() {
		log.info("Heist ending...");
		
		float reqMembersPercent = getReqMembersPercent(heist);
		log.info( "req member percent : {}", reqMembersPercent);
		
		if(reqMembersPercent < 50) {
			heist.setOutcome(HeistOutcome.FAILED);
			updateMembers(heist, "ALL", false);
		}
		else if(reqMembersPercent >= 50 && reqMembersPercent < 75) {	
			if(random.nextBoolean()) {
				heist.setOutcome(HeistOutcome.FAILED);
				updateMembers(heist, "2/3", false);		
			}else {
				heist.setOutcome(HeistOutcome.SUCCEEDED);
				updateMembers(heist, "1/3", false);
			}
		}
		else if(reqMembersPercent >= 75 && reqMembersPercent < 100) {
			heist.setOutcome(HeistOutcome.SUCCEEDED);
			updateMembers(heist, "1/3", true);
		}
		else {
			heist.setOutcome(HeistOutcome.SUCCEEDED);
		}
		
		long heistDurationSeconds = (heist.getEndTime().toInstant().toEpochMilli() - heist.getStartTime().toInstant().toEpochMilli())/1000;
		log.info( "heist duration seconds : {}", heistDurationSeconds);
		
		for(Member member : heist.getConfirmedMembers()) {
			List<Skill> usedSkills = getUsedSkills(member.getSkills(), heist.getSkills());
			
			for (Skill skill : usedSkills) {
				skill.addExp(heistDurationSeconds, secondsToLevel);
			}

			member.setHeist(null);
			memberRepo.save(member);
		}
		
		//String msg ="Outcome of heist : " +  heist.getOutcome().toString(); 
		//emailSender.sendEmails("Heist ended",msg, heist.getConfirmedMembers());
		
		heist.setStatus(HeistStatus.FINISHED);
		heist.setConfirmedMembers(null);
		heistRepo.save(heist);		
		log.info("Heist ended with outcome : {}", heist.getOutcome().toString());
	}

	
	public void updateMembers(Heist heist, String type, boolean onlyIncarcerated) {
		
		float portionToUpdate = 0;
		switch (type) {
		case "ALL":
			portionToUpdate = 1;
			break;
		case "1/3":
			portionToUpdate = 1/3;
			break;
		case "2/3":
			portionToUpdate = 2/3;
			break;
		default:
			break;
		}
			
		List<Member> members = heist.getConfirmedMembers();
		int numOfMemToUpdate = (int)(members.size() * portionToUpdate);
		Collections.shuffle(members);
		
		for(int i = 0 ; i < numOfMemToUpdate; i++) {
			if(random.nextBoolean() || onlyIncarcerated) {
				members.get(i).setStatus(StatusType.INCARCERATED);
			}else {
				members.get(i).setStatus(StatusType.EXPIRED);
			}
		}
	}
	
	
	public float getReqMembersPercent(Heist heist) {
		
		int reqMembersTotal = 0;
		int reqMembersFilled = 0;
		int reqMemForSkill = 0;
		
		for(ReqSkill reqSkill : heist.getSkills()) {
			reqMemForSkill = reqSkill.getMembers().intValue();
			reqMembersTotal = reqMembersTotal + reqMemForSkill;
	
			for(Member member : heist.getConfirmedMembers()) {
				if(reqMemForSkill == 0) {
					break;
				}
				for(Skill skill : member.getSkills()) {
					if(reqMemForSkill == 0) {
						break;
					}
					
					if(reqSkill.getName().equals(skill.getName()) && reqSkill.getLevel().length() <= skill.getLevel().length()) {
						reqMembersFilled++;
						reqMemForSkill--;
						
					}
				}		
			}
		}
		return (reqMembersFilled * 100.0f) / reqMembersTotal;
	}

	
	public List<Skill> getUsedSkills(List<Skill> skills, List<ReqSkill> reqSkills) {
		List<Skill> usedSkills = new ArrayList<Skill>();
		for (ReqSkill reqSkill : reqSkills) {
			for(Skill skill : skills) {
				if(reqSkill.getName().equals(skill.getName())  && reqSkill.getLevel().length() <= skill.getLevel().length()) {
					usedSkills.add(skill);
				}
			}
		}
		return usedSkills;
	}










	
}
