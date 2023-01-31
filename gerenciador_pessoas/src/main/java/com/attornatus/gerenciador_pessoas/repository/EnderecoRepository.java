package com.attornatus.gerenciador_pessoas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.attornatus.gerenciador_pessoas.entity.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
