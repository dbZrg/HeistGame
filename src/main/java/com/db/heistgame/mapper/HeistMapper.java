package com.db.heistgame.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.db.heistgame.dto.HeistDto;
import com.db.heistgame.dto.MemberDto;
import com.db.heistgame.dto.ReqSkillDto;
import com.db.heistgame.dto.SkillDto;
import com.db.heistgame.model.Heist;
import com.db.heistgame.model.Member;
import com.db.heistgame.model.ReqSkill;
import com.db.heistgame.model.Skill;

@Mapper(componentModel ="spring")
public interface HeistMapper {
	
	Member toMember(MemberDto memberDto);
	MemberDto toMemberDto(Member member);
	List<MemberDto> toMemberDtoList(List<Member> memberList);
	
	List<SkillDto> toSkillDtoList(List<Skill> skills);
	Skill toSkill(SkillDto skillDto);
	List<Skill> toSkillList(List<SkillDto> skillDtoList);
	
	HeistDto toHeistDto(Heist heist);
	Heist toHeist(HeistDto heistDto);
	List<HeistDto> toHeistDtoList(List<Heist> heists);
	
	ReqSkill toReqSkill(ReqSkillDto reqSkillDto);
	List<ReqSkill> toReqSkillList(List<ReqSkillDto> reqSkillDtos);
	List<ReqSkillDto> toReqSkillDtoList(List<ReqSkill> reqSkills);
}
