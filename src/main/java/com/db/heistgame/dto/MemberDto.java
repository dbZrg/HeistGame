package com.db.heistgame.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.db.heistgame.enums.SexType;
import com.db.heistgame.enums.StatusType;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
	
	@NotNull
	@JsonView({View.EligibleMembers.class,View.HeistMembers.class})
	private String name;
	
	@NotNull
	private SexType sex;
	
	@NotNull
	private String email;
	
	@Valid
	@NotNull
	@JsonView({View.EligibleMembers.class,View.Skills.class,View.HeistMembers.class})
	private List<SkillDto> skills;
	
	@JsonView(View.Skills.class)
	private String mainSkill;
	
	@NotNull
	private StatusType status;

}
