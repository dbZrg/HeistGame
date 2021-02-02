package com.db.heistgame.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.db.heistgame.dto.AddReqSkillsDto;
import com.db.heistgame.dto.ConfirmedMembersDto;
import com.db.heistgame.dto.EligibleMembers;
import com.db.heistgame.dto.HeistDto;
import com.db.heistgame.dto.MemberDto;
import com.db.heistgame.dto.ReqSkillDto;
import com.db.heistgame.dto.View;
import com.db.heistgame.enums.HeistStatus;
import com.db.heistgame.mapper.HeistMapper;
import com.db.heistgame.model.Heist;
import com.db.heistgame.service.HeistService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class HeistController {
	
	@Autowired
	HeistService heistService;
	
	@Autowired
	HeistMapper mapper;
	
	
	@PostMapping(value = "/heist", consumes = "application/json")
	public ResponseEntity<String> addHeist(@Valid @RequestBody HeistDto heistDto) {
	
		Heist heist = heistService.addHeist(mapper.toHeist(heistDto));
		
		URI location = UriComponentsBuilder
				.newInstance()
                .path("/heist/{id}")
                .buildAndExpand(heist.getIdHeist())
                .toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	
	@PatchMapping(value = "/heist/{id}/skills", consumes = "application/json")
	public ResponseEntity<String> addHeistSkills(@Valid @RequestBody AddReqSkillsDto reqSkillsDto,@PathVariable("id")long id) {
		
		heistService.addOrUpdateReqSkills(id, mapper.toReqSkillList(reqSkillsDto.getSkills()));
		
		String location = UriComponentsBuilder
				.newInstance()
				.path("/heist/{id}/skills")
                .buildAndExpand(id)
                .toString();

		return ResponseEntity.noContent().header("Content-Location", location).build();
	}
	
	
	@PutMapping(value = "/heist/{id}/members", consumes = "application/json")
	public ResponseEntity<String> confirmMembers(@RequestBody ConfirmedMembersDto members,@PathVariable("id")long id){
		
		heistService.confirmMembers(id, members.getMembers());
		
		String location = UriComponentsBuilder
				.newInstance()
                .path("/heist/{id}/members")
                .buildAndExpand(id)
                .toString();

		return ResponseEntity.noContent().header("Content-Location", location).build();
	}
	
	
	@PutMapping(value = "/heist/{id}/start")
	public ResponseEntity<String> startHeist(@PathVariable("id")long id, UriComponentsBuilder b){
		
		heistService.startHeist(id);
		
		URI location = UriComponentsBuilder
				.newInstance()
                .path("/heist/{id}/start")
                .buildAndExpand(id)
                .toUri();
		
		return ResponseEntity.ok().location(location).build();
	}
	
	
	@JsonView(View.EligibleMembers.class)
	@GetMapping(value="/heist/{id}/eligible_members")
	public EligibleMembers getEligibleMembers(@PathVariable("id")long id) {
		return heistService.getEligibleMembers(id);
	}
	
	
	@JsonView(View.HeistSingle.class)
	@GetMapping(value="/heist/{id}")
	public HeistDto getHeist(@PathVariable("id")long id) {
		return mapper.toHeistDto(heistService.getHeist(id));
	}
	
	
	@JsonView(View.HeistMembers.class)
	@GetMapping(value="/heist/{id}/members")
	public List<MemberDto> getHeistMembers(@PathVariable("id")long id) {
		Heist heist = heistService.getHeist(id);
		
		if(heist.getStatus() == HeistStatus.PLANNING) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Heist in not in PLANNING");
		}
		return mapper.toMemberDtoList(heist.getConfirmedMembers());
	}
	
	
	@GetMapping(value="/heist/{id}/skills")
	public List<ReqSkillDto> getHeistSkills(@PathVariable("id")long id) {
		return mapper.toHeistDto(heistService.getHeist(id)).getSkills();
	}
	
	
	@JsonView(View.HeistStatus.class)
	@GetMapping(value="/heist/{id}/status")
	public HeistDto getHeistStatus(@PathVariable("id")long id) {
		return mapper.toHeistDto(heistService.getHeist(id));
	}
	
	
	@JsonView(View.Outcome.class)
	@GetMapping(value="/heist/{id}/outcome")
	public HeistDto getHeistOutcome(@PathVariable("id")long id) {
		Heist heist = heistService.getHeist(id);
		
		if(heist.getStatus() != HeistStatus.FINISHED) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Heist in not FINISHED");
		}
		return mapper.toHeistDto(heistService.getHeist(id));
	}
	
	
	@GetMapping("/heists")
	public List<HeistDto> getHeists(){		
		return mapper.toHeistDtoList(heistService.getHeists());
	}
	
}
