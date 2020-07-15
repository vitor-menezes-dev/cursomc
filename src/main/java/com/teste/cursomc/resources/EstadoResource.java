package com.teste.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.teste.cursomc.domain.Cidade;
import com.teste.cursomc.domain.Estado;
import com.teste.cursomc.dto.CidadeDTO;
import com.teste.cursomc.dto.EstadoDTO;
import com.teste.cursomc.services.CidadeService;
import com.teste.cursomc.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

	@Autowired
	private EstadoService service;

	@Autowired
	private CidadeService cidadeservice;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> findAll() {
		List<Estado> lista = service.findAll();
		List<EstadoDTO> listaDto =  lista.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaDto);
	}
	
	@RequestMapping(value="/{estadoId}/cidades",method = RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> find(@PathVariable Integer estadoId) {
		List<Cidade> lista = cidadeservice.findByEstado(estadoId);
		List<CidadeDTO> listaDto =  lista.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok(listaDto);
	}
	
}
