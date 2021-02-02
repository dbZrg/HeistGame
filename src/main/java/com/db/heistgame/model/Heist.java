package com.db.heistgame.model;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.db.heistgame.enums.HeistOutcome;
import com.db.heistgame.enums.HeistStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="heists")
public class Heist {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idHeist;
	
	@Column(unique = true)
	private String name;
	
	private String location;
	
	private ZonedDateTime startTime;
	
	private ZonedDateTime endTime;

	@OneToMany(mappedBy = "heist", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<ReqSkill> skills;

	@OneToMany(mappedBy = "heist", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<Member> confirmedMembers;
	
	@Enumerated(EnumType.STRING)
	private HeistStatus status;
	
	@Enumerated(EnumType.STRING)
	private HeistOutcome outcome;

	public Heist(String name, String location, ZonedDateTime startTime, ZonedDateTime endTime, List<ReqSkill> skills,
			List<Member> confirmedMembers, HeistStatus status, HeistOutcome outcome) {
		super();
		this.name = name;
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
		this.skills = skills;
		this.confirmedMembers = confirmedMembers;
		this.status = status;
		this.outcome = outcome;
	}

	
}
