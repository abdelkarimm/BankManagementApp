package com.ikhaa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ikhaa.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
