package com.db.heistgame.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.db.heistgame.dto.AddSkillsDto;
import com.db.heistgame.model.Member;
import com.db.heistgame.model.Skill;
import com.db.heistgame.repository.MemberRepo;
import com.db.heistgame.repository.SkillRepo;
import com.db.heistgame.util.EmailSender;
import com.db.heistgame.util.Helper;

@Service
public class MemberService {
	
	@Autowired
	EmailSender emailSender;
	
	@Autowired
	MemberRepo memberRepo;
	
	@Autowired
	SkillRepo skillRepo;
	
	@Autowired
	Helper helper;
	
	
	
	public Member addMember(Member member) {
			
		if(!helper.areAllUnique(member.getSkills())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate skill names");
		}
		if(memberRepo.existsByEmailIgnoreCase(member.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate email");
		}
			
		
		for (Skill skill : member.getSkills()) {
			skill.setRobber(member);
			skill.setExp(0L);
		}
		
		//emailSender.sendEmail(member.getEmail(), "Added as member", "You have been added as a member");
		return memberRepo.save(member);
	}
	
	
	public Member addOrUpdateSkills(long id, AddSkillsDto skills) {
		
		Member member = memberRepo.findById(id);
		
		if(member == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member dont exist");
		}
		if(skills.getSkills() == null && skills.getMainSkill() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Add main skill or list of skills");
		}
		if(!helper.areAllUnique(skills.getSkills())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate skill names");
		}
		
		if(skills.getSkills() != null){
			for (Skill skill : skills.getSkills()) {
				
				Skill dbSkill = skillRepo.findByNameAndRobber(skill.getName(), member);
				if(dbSkill != null) {
					skill.setIdSkill(dbSkill.getIdSkill());
					skill.setExp(0L);
				}
				skill.setRobber(member);			
				skillRepo.save(skill);
			}
		}
		
		Member newMember = memberRepo.findById(id);
		if(skills.getMainSkill() != null) {
			
			if(!helper.containsSkill(member.getSkills(), skills.getMainSkill())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Main skill doesnt have match in skills array");
			}
			
			newMember.setMainSkill(skills.getMainSkill());
			memberRepo.save(newMember);
		}
		return newMember;
	}
	
	
	public void deleteSkill(String name, long id) {	
		if(memberRepo.findById(id) == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
		}
		if(skillRepo.deleteSkill(name, id) == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No skill found");
		}
		
	}
	
	
	public Member getMember(long id) {
		Member member = memberRepo.findById(id);
		if(member == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member dont exist");
		}
		return member;
	}
	
	
	public List<Member> getMembers(){	
		return StreamSupport.stream(memberRepo.findAll().spliterator(), false)
                			.collect(Collectors.toList());
	}
	
}
