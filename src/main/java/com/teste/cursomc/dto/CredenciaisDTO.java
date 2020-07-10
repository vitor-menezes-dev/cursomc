package com.teste.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.teste.cursomc.services.validation.ClienteUpdate;

@ClienteUpdate
public class CredenciaisDTO implements Serializable {
	private static final long serialVersionUID = 1L;


	
	
	@NotEmpty(message = "Preenchimento Obrigatório")
	@Email(message = "Email inválido")
	private String email;
	
	@NotEmpty(message = "Preenchimento Obrigatório")
	private String senha;
	
	public CredenciaisDTO() {
	}

	public CredenciaisDTO(
			@NotEmpty(message = "Preenchimento Obrigatório") @Email(message = "Email inválido") String email,
			@NotEmpty(message = "Preenchimento Obrigatório") String senha) {
		super();
		this.email = email;
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	
}
