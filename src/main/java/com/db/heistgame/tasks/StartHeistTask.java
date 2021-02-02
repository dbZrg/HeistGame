package com.db.heistgame.tasks;

import javax.transaction.Transactional;

import com.db.heistgame.enums.HeistStatus;
import com.db.heistgame.model.Heist;
import com.db.heistgame.repository.HeistRepo;
import com.db.heistgame.util.EmailSender;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class StartHeistTask implements Runnable {
	
	
	private HeistRepo heistRepo;

	private Heist heist;
	
	private EmailSender emailSender;
	
	public StartHeistTask(HeistRepo heistRepo, Heist heist, EmailSender emailSender) {
		super();
		this.heistRepo = heistRepo;
		this.heist = heist;
		this.emailSender = emailSender;
	}
	
	@Transactional
	@Override
	public void run() {
		log.info("Task started");
		
		//String msg ="Heist started on : " +  heist.getStartTime().toLocalDateTime().toString(); 
		//emailSender.sendEmails("Heist ended", msg , heist.getConfirmedMembers());
		
		heist.setStatus(HeistStatus.IN_PROGRESS);
		heistRepo.save(heist);		
	}

	
	
	
	

}
