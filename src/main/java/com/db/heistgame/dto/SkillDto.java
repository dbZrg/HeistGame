package com.db.heistgame.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.db.heistgame.model.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SkillDto {

	@NotNull
	@Size(min=1)
	@JsonView({View.EligibleMembers.class,View.Skills.class,View.HeistMembers.class})
	private String name;
	
	@Size(max=10,min=1,message="Level must in range 1 to 10")
	@JsonView({View.EligibleMembers.class,View.Skills.class,View.HeistMembers.class})
	private String level;
	
	@JsonIgnore
	private Member robber;
	
	@JsonIgnore
	private Long exp;
	
	
}
