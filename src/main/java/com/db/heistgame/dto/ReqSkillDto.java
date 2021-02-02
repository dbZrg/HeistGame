package com.db.heistgame.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.db.heistgame.model.Heist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReqSkillDto {
	
	@NotNull
	@Size(min=1)
	@JsonView({View.EligibleMembers.class,View.HeistSingle.class})
	private String name;
	
	@Size(max=10,min=1,message="Level must in range 1 to 10")
	@JsonView({View.EligibleMembers.class,View.HeistSingle.class})
	private String level;
	
	@NotNull
	@JsonView({View.EligibleMembers.class,View.HeistSingle.class})
	private Long members;
	
	@JsonIgnore
	private Heist heist;
	
}
