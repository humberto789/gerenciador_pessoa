package com.attornatus.gerenciador_pessoas.http.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
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

import com.attornatus.gerenciador_pessoas.entity.Pessoa;
import com.attornatus.gerenciador_pessoas.filtros.PessoaFiltro;
import com.attornatus.gerenciador_pessoas.service.impl.PessoaServiceImpl;

@RestController
@RequestMapping("/people")
public class PessoaController {

	@Autowired
	private PessoaServiceImpl pessoaService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping(produces = "application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.CREATED)
	public Pessoa cadastrar(@RequestBody Pessoa pessoa) {
		return pessoaService.salvar(pessoa);
	}
	
	@GetMapping(produces = "application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.OK)
	public List<Pessoa> listarPessoaPorFiltro(@RequestBody(required = false) PessoaFiltro pessoaFiltro){
		List<Pessoa> listaPessoa;
		
		if(pessoaFiltro == null) {
			listaPessoa = pessoaService.listarTodasPessoas();
		}else {
			listaPessoa = pessoaService.listarPorFiltro(pessoaFiltro);
		}
		
		return listaPessoa;
	}
	
	@GetMapping(value="/{id}", produces = "application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.OK)
	public Pessoa buscarPessoaPorId(@PathVariable("id") Long id){
		return pessoaService.buscarPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Essa Pessoa não foi encontrada."));
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void removerPessoa(@PathVariable("id") Long id) {
		pessoaService.buscarPorId(id)
			.map(pessoa -> {
				pessoaService.removerPorId(id);
				return Void.TYPE;
			}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Essa Pessoa não foi encontrada."));
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void atualizarPessoa(@PathVariable("id") Long id, @RequestBody Pessoa pessoa) {
		pessoaService.buscarPorId(id)
			.map(pessoaBanco -> {
				modelMapper.map(pessoa, pessoaBanco);
				pessoaService.salvar(pessoaBanco);
				return Void.TYPE;
			}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Essa Pessoa não foi encontrada."));
	}
}
