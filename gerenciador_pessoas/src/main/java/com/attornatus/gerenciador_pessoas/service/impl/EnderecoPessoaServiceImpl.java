package com.attornatus.gerenciador_pessoas.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attornatus.gerenciador_pessoas.entity.EnderecoPessoa;
import com.attornatus.gerenciador_pessoas.entity.EnderecoPessoaKey;
import com.attornatus.gerenciador_pessoas.repository.EnderecoPessoaRepository;
import com.attornatus.gerenciador_pessoas.service.EnderecoPessoaService;

@Service
public class EnderecoPessoaServiceImpl implements EnderecoPessoaService{
	
	@Autowired
	private EnderecoPessoaRepository enderecoPessoaRepository;
	
	public EnderecoPessoa salvar(EnderecoPessoa enderecoPessoa) {
		return enderecoPessoaRepository.save(enderecoPessoa);
	}
	
	public List<EnderecoPessoa> listarTodosEnderecoPessoa(){
		return enderecoPessoaRepository.findAll();
	}
	
	public Optional<EnderecoPessoa> buscarPorId(EnderecoPessoaKey id) {
		return enderecoPessoaRepository.findByPessoaIdAndEnderecoId(id.getPessoaId(), id.getEnderecoId());
	}
	
	public void removerPorId(EnderecoPessoaKey id) {
		enderecoPessoaRepository.deleteByPessoaIdAndEnderecoId(id.getPessoaId(), id.getEnderecoId());
	}
	
	public List<EnderecoPessoa> listarEnderecosPorIdPessoa(Long id) {
		return enderecoPessoaRepository.findByPessoaId(id);
	}
	
	public void atualizarEnderecoPrincipal(EnderecoPessoa enderecoPessoa) {
		enderecoPessoaRepository.setTodosEnderecoPrincipalFalseByIdPessoa(enderecoPessoa.getId().getPessoaId());
		
		enderecoPessoaRepository.setEnderecoPrincipalById(enderecoPessoa.getId().getPessoaId(), enderecoPessoa.getId().getEnderecoId());
	}
}
