package com.attornatus.gerenciador_pessoas.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.attornatus.gerenciador_pessoas.entity.Pessoa;
import com.attornatus.gerenciador_pessoas.filtros.PessoaFiltro;
import com.attornatus.gerenciador_pessoas.repository.PessoaRepository;
import com.attornatus.gerenciador_pessoas.service.PessoaService;

@Service
public class PessoaServiceImpl implements PessoaService{
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa salvar(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}
	
	public List<Pessoa> listarTodasPessoas(){
		return pessoaRepository.findAll();
	}
	
	public Optional<Pessoa> buscarPorId(Long id) {
		return pessoaRepository.findById(id);
	}
	
	public void removerPorId(Long id) {
		pessoaRepository.deleteById(id);
	}
	
	public List<Pessoa> listarPorFiltro(PessoaFiltro pessoaFiltro){
		
		Pessoa pessoa = new Pessoa();
		pessoa.setId(pessoaFiltro.getId());
		pessoa.setNome(pessoaFiltro.getNome());
		pessoa.setDataNascimento(pessoaFiltro.getDataNascimento());
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withIgnoreCase()
				.withIgnoreNullValues()
				.withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
		
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
		
		return pessoaRepository.findAll(example);
		
	}
}
