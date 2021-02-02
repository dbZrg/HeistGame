package com.db.heistgame.dto;


import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddReqSkillsDto {

	@Valid
	@NotNull
	List<ReqSkillDto> skills;
}
