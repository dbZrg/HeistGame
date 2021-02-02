package com.db.heistgame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.db.heistgame.model.Member;

@Repository
public interface MemberRepo extends CrudRepository<Member, Long> {
	boolean existsByEmailIgnoreCase(@Param("email")String email);
	Member findById(@Param("id")long id);
	List<Member> findByName(@Param("name")String name);

	@Query("FROM Member AS mem LEFT JOIN mem.skills AS sk WHERE (mem.status = 'AVAILABLE' OR mem.status= 'RETIRED') AND sk.name = ?1 AND LENGTH(sk.level) >= ?2 AND mem.heist = NULL") 
    List<Member> findEliglibleMembers(String skillName, Integer level);
}
