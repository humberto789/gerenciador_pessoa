package com.attornatus.gerenciador_pessoas.service;

import java.util.List;
import java.util.Optional;

import com.attornatus.gerenciador_pessoas.entity.Pessoa;
import com.attornatus.gerenciador_pessoas.filtros.PessoaFiltro;

public interface PessoaService {
	
	public Pessoa salvar(Pessoa pessoa);
	
	public List<Pessoa> listarTodasPessoas();
	
	public Optional<Pessoa> buscarPorId(Long id);
	
	public void removerPorId(Long id);
	
	public List<Pessoa> listarPorFiltro(PessoaFiltro pessoaFiltro);
}
