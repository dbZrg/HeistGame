package com.db.heistgame.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.db.heistgame.model.Skill;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddSkillsDto {
	
	@Valid
	private List<Skill> skills;
	
	@Size(min=1)
	private String mainSkill;
}
