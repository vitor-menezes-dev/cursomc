package com.teste.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.teste.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	@Transactional
	Cliente findByEmail(String email);
	
	@Transactional
	Cliente findByCpfOuCnpj(String cpfOuCnpj);
	
}
