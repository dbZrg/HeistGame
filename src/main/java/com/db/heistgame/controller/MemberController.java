package com.db.heistgame.controller;

import java.net.URI;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.db.heistgame.dto.AddSkillsDto;
import com.db.heistgame.dto.MemberDto;
import com.db.heistgame.dto.View;
import com.db.heistgame.mapper.HeistMapper;
import com.db.heistgame.model.Member;
import com.db.heistgame.service.MemberService;
import com.fasterxml.jackson.annotation.JsonView;

@CrossOrigin("http://localhost:3000")
@RestController
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	HeistMapper mapper;
	
	
	
	@PostMapping(value = "/member", consumes = "application/json")
	public ResponseEntity<String> addMember(@Valid @RequestBody MemberDto memberdto) {

		Member mem = memberService.addMember(mapper.toMember(memberdto));	
		
		URI location = UriComponentsBuilder
				.newInstance()
                .path("/member/{id}")
                .buildAndExpand(mem.getId())
                .toUri();
	
		return ResponseEntity.created(location).build();
	}
	
	
	@PutMapping(value = "/member/{id}/skills", consumes = "application/json")
	public ResponseEntity<String> addOrUpdateSills(@Valid @RequestBody AddSkillsDto addSkillsDto, @PathVariable("id")long id) {
	
		memberService.addOrUpdateSkills(id, addSkillsDto);
		
		String location = UriComponentsBuilder
				.newInstance()
                .path("/member/{id}/skills")
                .buildAndExpand(id)
                .toString();
		
		return ResponseEntity.noContent().header("Content-Location", location).build();
	}
	
	
	@DeleteMapping(value = "/member/{id}/skills/{skillName}")
	public ResponseEntity<String> deleteSkill(@PathVariable("id")long id, @PathVariable("skillName")String name) {
		
		memberService.deleteSkill(name, id);
		
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/member/{id}")
	public MemberDto getMember(@PathVariable("id")long id){		
		return mapper.toMemberDto(memberService.getMember(id));
	}
	
	
	@JsonView(View.Skills.class)
	@GetMapping("/member/{id}/skills")
	public MemberDto getMemberSkills(@PathVariable("id")long id){		
		return mapper.toMemberDto(memberService.getMember(id));
	}
	
	
	@GetMapping("/members")
	public List<MemberDto> getMembers(){		
		return mapper.toMemberDtoList(memberService.getMembers());
	}
}
