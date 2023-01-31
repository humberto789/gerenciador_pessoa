package com.attornatus.gerenciador_pessoas.service;

import java.util.List;
import java.util.Optional;

import com.attornatus.gerenciador_pessoas.entity.Endereco;


public interface EnderecoService {
	
	public Endereco salvar(Endereco endereco);
	
	public List<Endereco> listarTodosEnderecos();
	
	public Optional<Endereco> buscarPorId(Long id);
	
	public void removerPorId(Long id);
}
