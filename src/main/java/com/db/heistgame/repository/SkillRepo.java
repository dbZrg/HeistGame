package com.db.heistgame.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.db.heistgame.model.Member;
import com.db.heistgame.model.Skill;

@Repository
public interface SkillRepo extends CrudRepository<Skill, Long> {
	Skill findByNameAndRobber(@Param("name")String name,@Param("robber") Member robberId);

	@Transactional
	@Modifying
    @Query(value="delete from skills u where u.name = ?1 and u.robber_id = ?2", nativeQuery = true)
    int deleteSkill(String name, long id);
}

