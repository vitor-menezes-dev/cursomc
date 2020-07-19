package com.teste.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.cursomc.domain.Estado;
import com.teste.cursomc.dto.EstadoDTO;
import com.teste.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository repo;

	public List<Estado> findAll() {
		return repo.findAllByOrderByNome();
	}

	public Estado fromDTO(EstadoDTO objDto) {
		return new Estado(objDto.getId(),objDto.getNome(), objDto.getSigla());
	}
}
