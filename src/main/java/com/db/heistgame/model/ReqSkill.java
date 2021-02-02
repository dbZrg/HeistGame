package com.db.heistgame.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="req_skills")
public class ReqSkill {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idReqSkill;
	
	private String name;	
	
	private String level;
	
	private Long members;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heist_id",referencedColumnName = "idHeist")
	private Heist heist;

	public ReqSkill(String name, String level, Long members, Heist heist) {
		super();
		this.name = name;
		this.level = level;
		this.members = members;
		this.heist = heist;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReqSkill other = (ReqSkill) obj;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
}
