package com.db.heistgame.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EligibleMembers {
	
	@JsonView(View.EligibleMembers.class)
	List<ReqSkillDto> skills;
	
	@JsonView(View.EligibleMembers.class)
	List<MemberDto> members;
}
