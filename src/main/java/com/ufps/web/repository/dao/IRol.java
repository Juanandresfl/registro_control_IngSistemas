package com.ufps.web.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ufps.web.entities.Rol;

@Repository
public interface IRol extends JpaRepository<Rol, Integer> {

}
