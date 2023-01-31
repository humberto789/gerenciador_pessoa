package com.attornatus.gerenciador_pessoas.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class EnderecoPessoaKey  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "pessoa_id")
	private Long pessoaId; 
	
	@Column(name = "endereco_id")
	private Long enderecoId;

	public EnderecoPessoaKey() {
		
	}
	
	public EnderecoPessoaKey(Long pessoaId, Long enderecoId) {
		this.pessoaId = pessoaId;
		this.enderecoId = enderecoId;
	}
	
	public Long getPessoaId() {
		return pessoaId;
	}

	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}

	public Long getEnderecoId() {
		return enderecoId;
	}

	public void setEnderecoId(Long enderecoId) {
		this.enderecoId = enderecoId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(enderecoId, pessoaId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnderecoPessoaKey other = (EnderecoPessoaKey) obj;
		return Objects.equals(enderecoId, other.enderecoId) && Objects.equals(pessoaId, other.pessoaId);
	}
}
