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
@Table(name="skills")
public class Skill {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idSkill;
	
	private String name;
	
	private String level;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "robber_id",referencedColumnName = "id")
	private Member robber;
	
	private Long exp = 0L;

	public Skill(String name, String level, Member robber, Long exp) {
		super();
		this.name = name;
		this.level = level;
		this.robber = robber;
		this.exp = exp;
	}
	
	public void addExp(Long seconds, Long toLevel) {
		exp = exp + seconds;
		if(exp > toLevel && level.length()<10) {
			level = level.concat("*");
			exp = exp % toLevel;
		}
	}

	

	
	
}
