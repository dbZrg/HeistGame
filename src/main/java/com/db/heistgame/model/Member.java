package com.db.heistgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.db.heistgame.enums.SexType;
import com.db.heistgame.enums.StatusType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name="members")
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	private SexType sex;
	
	private String email;
	
	@OneToMany(mappedBy = "robber", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Skill> skills = new ArrayList<>();
	
	private String mainSkill;
	
	@Enumerated(EnumType.STRING)
	private StatusType status;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heist_id",referencedColumnName = "idHeist",nullable = true)
	private Heist heist;
	
	public Member(String name, SexType sex, String email, List<Skill> skills, String mainSkill, StatusType status,
			Heist heist) {
		super();
		this.name = name;
		this.sex = sex;
		this.email = email;
		this.skills = skills;
		this.mainSkill = mainSkill;
		this.status = status;
		this.heist = heist;
	}

	
}
