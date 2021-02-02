package com.db.heistgame.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.db.heistgame.model.ReqSkill;
import com.db.heistgame.model.Skill;


@Component
public class Helper {
	
	/**
	 * Returns false if there is two or more skills with same name
	 * @param list List of skills
	 * @return boolean
	 */
	public boolean areAllUnique(List<Skill> list) {
		if(list == null) {
			return true;
		}
        Set<String> set = new HashSet<>();
        return list.stream().allMatch(p-> set.add(p.getName()));
    }	
	
	
	/**
	 * Returns false if there is two or more required skills with same name and level
	 * @param list List of required skills
	 * @return boolean
	 */
	public boolean areAllUniqueReq(List<ReqSkill> list) {
		if(list == null) {
			return true;
		}
        Set<ReqSkill> set = new HashSet<>();
        return list.stream().allMatch(p-> set.add(p));
    }
	
	
	/**
	 * Returns true if list contains skill with given name
	 * @param list List of skills
	 * @param name Skill name
	 * @return boolean
	 */
	public boolean containsSkill(List<Skill> list, String name){
	    return list.stream().anyMatch(o -> o.getName().equals(name));
	}
	
	
	/**
	 * Returns true if at least one of member skills has same name and same or larger level as one of required skills
	 * @param skills List of member skills
	 * @param reqSkills List of required skills for heist
	 * @return
	 */
	public boolean hasRequriedSkill(List<Skill> skills, List<ReqSkill> reqSkills) {
		for (ReqSkill reqSkill : reqSkills) {
			for(Skill skill : skills) {
				if(reqSkill.getName().equals(skill.getName())  && reqSkill.getLevel().length() <= skill.getLevel().length()) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	
}
