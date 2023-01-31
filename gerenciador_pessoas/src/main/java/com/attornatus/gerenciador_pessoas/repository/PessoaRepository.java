package com.attornatus.gerenciador_pessoas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.attornatus.gerenciador_pessoas.entity.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
}
