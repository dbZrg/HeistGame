package com.db.heistgame.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.db.heistgame.model.Heist;
import com.db.heistgame.model.ReqSkill;

public interface ReqSkillRepo extends CrudRepository<ReqSkill, Long> {
	ReqSkill findByNameAndLevelAndHeist(@Param("name")String name,@Param("level")String level,@Param("heist") Heist heistId);
}
