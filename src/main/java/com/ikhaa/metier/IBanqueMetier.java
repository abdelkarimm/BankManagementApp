package com.ikhaa.metier;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ikhaa.entities.Compte;
import com.ikhaa.entities.Operation;

public interface IBanqueMetier {

	public Compte consulterCompte(String codeCpte);
	public void verser(String codeCpte, double montant);
	public void retirer(String codeCpte, double montant);
	public Page<Operation> listOperation(String codeCpte,int page,int size);
	public List<Operation> listAll();

}
