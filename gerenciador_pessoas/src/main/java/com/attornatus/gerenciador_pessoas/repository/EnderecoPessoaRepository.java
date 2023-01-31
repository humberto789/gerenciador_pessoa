package com.attornatus.gerenciador_pessoas.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.attornatus.gerenciador_pessoas.entity.EnderecoPessoa;

import jakarta.transaction.Transactional;

public interface EnderecoPessoaRepository extends JpaRepository<EnderecoPessoa, Long>{
	
	public List<EnderecoPessoa> findByPessoaId(Long pessoaId);
	
	public Optional<EnderecoPessoa> findByPessoaIdAndEnderecoId(Long pessoaId, Long enderecoId);
	
	@Modifying
	@Transactional
	@Query("delete from endereco_pessoa ep where ep.pessoa.id = :pessoa_id and ep.endereco.id = :endereco_id")
	public int deleteByPessoaIdAndEnderecoId(@Param("pessoa_id") Long pessoaId, @Param("endereco_id") Long enderecoId);
	
	@Modifying
	@Transactional
	@Query("update endereco_pessoa ep set ep.principal = true where ep.pessoa.id = :pessoa_id and ep.endereco.id = :endereco_id")
	public int setEnderecoPrincipalById(@Param("pessoa_id") Long pessoaId, @Param("endereco_id") Long enderecoId);
	
	@Modifying
	@Transactional
	@Query("update endereco_pessoa ep set ep.principal = false where ep.pessoa.id = :id")
	public int setTodosEnderecoPrincipalFalseByIdPessoa(@Param("id") Long pessoaId);
}
