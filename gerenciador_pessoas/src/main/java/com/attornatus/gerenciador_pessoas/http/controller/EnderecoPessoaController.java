package com.attornatus.gerenciador_pessoas.http.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.attornatus.gerenciador_pessoas.entity.Endereco;
import com.attornatus.gerenciador_pessoas.entity.EnderecoPessoa;
import com.attornatus.gerenciador_pessoas.service.impl.EnderecoPessoaServiceImpl;
import com.attornatus.gerenciador_pessoas.service.impl.EnderecoServiceImpl;
import com.attornatus.gerenciador_pessoas.service.impl.PessoaServiceImpl;

@RestController
@RequestMapping("/personaladdress")
public class EnderecoPessoaController {
	
	@Autowired
	private EnderecoPessoaServiceImpl enderecoPessoaService;
	
	@Autowired
	private EnderecoServiceImpl enderecoService;
	
	@Autowired
	private PessoaServiceImpl pessoaService;
	
	@PostMapping(value="/{id}", produces = "application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.CREATED)
	public void cadastrarEnderecoPraPessoa(@PathVariable("id") Long id, @RequestBody Endereco endereco) {
		pessoaService.buscarPorId(id)
		.map(pessoa -> {
			
			if(endereco.getId() == null) {
				enderecoService.salvar(endereco);
			}
			
			EnderecoPessoa enderecoPessoa = new EnderecoPessoa(pessoa, endereco);
			
			if(listarEnderecosPeloIdPessoa(pessoa.getId()).isEmpty()) {
				enderecoPessoa.setPrincipal(true);
			}else {
				enderecoPessoa.setPrincipal(false);
			}
			
			enderecoPessoaService.salvar(enderecoPessoa);
			
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Essa Pessoa não foi encontrada."));
	}
	
	@GetMapping(value="/{id}", produces = "application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.OK)
	public List<EnderecoPessoa> listarEnderecosPeloIdPessoa(@PathVariable("id") Long id){
		return enderecoPessoaService.listarEnderecosPorIdPessoa(id);
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public void atualizarEnderecoPessoaPrincipal(@RequestBody EnderecoPessoa enderecoPessoa) {
		enderecoPessoaService.buscarPorId(enderecoPessoa.getId())
		.map(enderecoPessoaBanco -> {
			
			enderecoPessoaService.atualizarEnderecoPrincipal(enderecoPessoaBanco);
			
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse Endereco não existe com essa pessoa."));
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void removerEnderecoPessoa(@RequestBody EnderecoPessoa enderecoPessoa) {
		enderecoPessoaService.buscarPorId(enderecoPessoa.getId())
		.map(enderecoPessoaBanco -> {
			
			enderecoPessoaService.removerPorId(enderecoPessoa.getId());
			
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse Endereco não existe com essa pessoa."));
	}
	
	@GetMapping(produces = "application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.OK)
	public List<EnderecoPessoa> listarTodosEnderecoPessoa(){
		return enderecoPessoaService.listarTodosEnderecoPessoa();
	}
	
}
