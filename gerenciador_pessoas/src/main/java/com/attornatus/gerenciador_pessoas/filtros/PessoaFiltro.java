package com.attornatus.gerenciador_pessoas.filtros;

import java.util.Date;

public class PessoaFiltro {
	
	private Long id;
	
	private String nome;
	
	private Date dataNascimento;
	
	public PessoaFiltro() {
		
	}
	
	public PessoaFiltro(Long id, String nome, Date dataNascimento) {
		this.id = id;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
}
