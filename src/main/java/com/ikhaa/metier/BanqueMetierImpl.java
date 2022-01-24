package com.ikhaa.metier;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ikhaa.dao.CompteRepository;
import com.ikhaa.dao.OperationRepository;
import com.ikhaa.entities.Compte;

import com.ikhaa.entities.Operation;
import com.ikhaa.entities.Retrait;
import com.ikhaa.entities.Versement;

@Service
@Transactional
public class BanqueMetierImpl implements IBanqueMetier {
	
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	
	@Override
	public Compte consulterCompte(String codeCpte) {
		
		Compte cp=compteRepository.findById(codeCpte).orElse(null);
		if(cp==null) throw new RuntimeException("Compte introuvable");
		return cp;
	}

	@Override
	public void verser(String codeCpte, double montant) {
		
		Compte cp=consulterCompte(codeCpte);
		Versement v=new Versement(new Date(),montant,cp);
		operationRepository.save(v);
		cp.setSolde(cp.getSolde()+ montant);
		compteRepository.save(cp);
		
	}

	@Override
	public void retirer(String codeCpte, double montant) {
		Compte cp=consulterCompte(codeCpte);
	
		if(cp.getSolde() < montant)
			throw new RuntimeException("Solde insuffisant");
		Retrait r=new Retrait(new Date(),montant,cp);
		operationRepository.save(r);
		cp.setSolde(cp.getSolde()- montant);
		compteRepository.save(cp);
		
	}

	@Override
	public Page<Operation> listOperation(String codeCpte, int page, int size) {

		return operationRepository.listOperation(codeCpte,PageRequest.of(page,size));
		
	}
	
	@Override
	public List<Operation> listAll() {

		return operationRepository.findAll();
	}

}
