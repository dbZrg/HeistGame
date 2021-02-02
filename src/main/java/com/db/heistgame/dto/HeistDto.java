package com.db.heistgame.dto;

import java.time.ZonedDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.db.heistgame.enums.HeistOutcome;
import com.db.heistgame.enums.HeistStatus;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HeistDto {
	
	@NotNull
	@JsonView(View.HeistSingle.class)
	private String name;
	
	@NotNull
	@JsonView(View.HeistSingle.class)
	private String location;
	
	@NotNull
	@JsonView(View.HeistSingle.class)
	private ZonedDateTime startTime;
	
	@NotNull
	@JsonView(View.HeistSingle.class)
	private ZonedDateTime endTime;
	
	@Valid
	@NotNull
	@JsonView(View.HeistSingle.class)
	private List<ReqSkillDto> skills;
	
	@JsonView(View.HeistMembers.class)
	private List<MemberDto> confirmedMembers;
	
	@JsonView({View.HeistSingle.class,View.HeistStatus.class})
	private HeistStatus status;
	
	@JsonView(View.Outcome.class)
	private HeistOutcome outcome;
}
