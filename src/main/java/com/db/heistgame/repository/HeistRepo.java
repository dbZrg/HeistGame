package com.db.heistgame.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.db.heistgame.model.Heist;

@Repository
public interface HeistRepo extends CrudRepository<Heist, Long> {
	boolean existsByName(@Param("name")String name);
	Heist findById(@Param("id")long id);
}

