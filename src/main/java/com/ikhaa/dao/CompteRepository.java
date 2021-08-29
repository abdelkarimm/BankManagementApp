package com.ikhaa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ikhaa.entities.Compte;

public interface CompteRepository 
extends JpaRepository<Compte, String>{

}
