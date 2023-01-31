package com.attornatus.gerenciador_pessoas.service;

import java.util.List;
import java.util.Optional;

import com.attornatus.gerenciador_pessoas.entity.EnderecoPessoa;
import com.attornatus.gerenciador_pessoas.entity.EnderecoPessoaKey;

public interface EnderecoPessoaService {
	
	public EnderecoPessoa salvar(EnderecoPessoa endereco);
	
	public List<EnderecoPessoa> listarTodosEnderecoPessoa();
	
	public Optional<EnderecoPessoa> buscarPorId(EnderecoPessoaKey id);
	
	public void removerPorId(EnderecoPessoaKey id);
	
	public List<EnderecoPessoa> listarEnderecosPorIdPessoa(Long id);
	
	public void atualizarEnderecoPrincipal(EnderecoPessoa enderecoPessoa);
}
