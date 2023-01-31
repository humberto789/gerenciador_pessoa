package com.attornatus.gerenciador_pessoas.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attornatus.gerenciador_pessoas.entity.Endereco;
import com.attornatus.gerenciador_pessoas.repository.EnderecoRepository;
import com.attornatus.gerenciador_pessoas.service.EnderecoService;

@Service
public class EnderecoServiceImpl implements EnderecoService{
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Endereco salvar(Endereco endereco) {
		return enderecoRepository.save(endereco);
	}
	
	public List<Endereco> listarTodosEnderecos(){
		return enderecoRepository.findAll();
	}
	
	public Optional<Endereco> buscarPorId(Long id) {
		return enderecoRepository.findById(id);
	}
	
	public void removerPorId(Long id) {
		enderecoRepository.deleteById(id);
	}
}
