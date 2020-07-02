package com.teste.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.teste.cursomc.domain.Cliente;
import com.teste.cursomc.domain.enums.TipoCliente;
import com.teste.cursomc.dto.ClienteNewDTO;
import com.teste.cursomc.repositories.ClienteRepository;
import com.teste.cursomc.resources.exceptions.FieldMessage;
import com.teste.cursomc.services.validation.util.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
// inclua os testes aqui, inserindo erros na lista
		
		if(objDto.getTipo()==null) {
			list.add(new FieldMessage("tipo","Tipo não pode ser nulo"));
		}
		 
		if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj()) ) {
			list.add(new FieldMessage("cpfOuCnpj","CPF Inválido"));
		}
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj()) ) {
			list.add(new FieldMessage("cpfOuCnpj","CNPJ Inválido"));
		}
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if(aux !=null) {
			list.add(new FieldMessage("email","Email já cadastrado"));
		}
		
		aux = repo.findByCpfOuCnpj(objDto.getCpfOuCnpj());
		if(aux !=null) {
			list.add(new FieldMessage("cpfOuCnpj","CPF ou CNPJ já cadastrado"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
