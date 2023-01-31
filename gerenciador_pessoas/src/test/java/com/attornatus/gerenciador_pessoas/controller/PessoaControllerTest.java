package com.attornatus.gerenciador_pessoas.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.Mockito;

import com.attornatus.gerenciador_pessoas.entity.Pessoa;
import com.attornatus.gerenciador_pessoas.filtros.PessoaFiltro;
import com.attornatus.gerenciador_pessoas.http.controller.PessoaController;
import com.attornatus.gerenciador_pessoas.service.impl.PessoaServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PessoaController.class)
public class PessoaControllerTest {
		
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PessoaServiceImpl pessoaService;
	
	private ObjectMapper objectMapper;

	private Pessoa pessoa;
	
	@BeforeEach
	public void setUp() {
		pessoa = new Pessoa();
		pessoa.setId((long) 1);
		pessoa.setNome("Pessoa Teste");
		
		objectMapper = new ObjectMapper();
	}
	
	@Test
	@DisplayName("Deve retornar uma lista com todas as pessoas")
	public void listarPessoasSemUsarFiltroComSucesso() throws Exception {
		
		when(pessoaService.listarTodasPessoas()).thenReturn(List.of(pessoa));
		
		this.mockMvc.perform(get("/people"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Pessoa Teste")))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("["+objectMapper.writeValueAsString(pessoa)+"]"));
		
		verify(pessoaService, times(1)).listarTodasPessoas();
		verify(pessoaService, times(0)).listarPorFiltro(Mockito.any(PessoaFiltro.class));
		
	}
	
	
	@Test
	@DisplayName("Deve retornar uma lista com pessoas ao receber um filtro")
	public void listarPessoasComFiltroComSucesso() throws Exception {
		
		PessoaFiltro filtro = new PessoaFiltro();
		filtro.setNome("Pessoa Teste");
		
		when(pessoaService.listarPorFiltro(Mockito.any(PessoaFiltro.class))).thenReturn(List.of(pessoa));
		
		this.mockMvc.perform(get("/people")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(filtro)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("["+objectMapper.writeValueAsString(pessoa)+"]"));
		
		verify(pessoaService, times(0)).listarTodasPessoas();
		verify(pessoaService, times(1)).listarPorFiltro(Mockito.any(PessoaFiltro.class));
	}
	
	@Test
	@DisplayName("Buscar pessoa por Id e deve retornar uma pessoa e ter como resultado o sucesso")
	public void buscarPessoaPorIdComSucesso() throws Exception {
		
		Optional<Pessoa> resultadoBusca = Optional.of(pessoa);
		
		when(pessoaService.buscarPorId(Mockito.anyLong())).thenReturn(resultadoBusca);
		
		this.mockMvc.perform(get("/people/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(pessoa)));
		
		verify(pessoaService, times(1)).buscarPorId(Mockito.anyLong());
	}
	
	@Test
	@DisplayName("Buscar pessoa por Id e deve retornar o codigo de nao encontrado e retornar um erro ao informar id que nao existe")
	public void buscarPessoaPorIdQueNaoExiste() throws Exception {
		
		Optional<Pessoa> resultadoBusca = Optional.empty();
		
		when(pessoaService.buscarPorId((long) 1)).thenReturn(resultadoBusca);
		
		
		this.mockMvc.perform(get("/people/1"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist())
				.andExpect(result -> Assertions.assertThat(result.getResolvedException() instanceof ResponseStatusException));
				
		verify(pessoaService, times(1)).buscarPorId(Mockito.anyLong());
	}
	
	@Test
	@DisplayName("Cadastrar uma pessoa, retornar status de criado e retornar o objeto criado")
	public void cadastrarPessoaComSucesso() throws Exception {
		
		when(pessoaService.salvar(Mockito.any(Pessoa.class))).thenReturn(pessoa);
		
		this.mockMvc.perform(post("/people")
				.content(objectMapper.writeValueAsString(pessoa))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(pessoa)));
		
		verify(pessoaService, times(1)).salvar(Mockito.any(Pessoa.class));
	}
	
	@Test
	@DisplayName("Tentar atualizar uma pessoa com id que não existe, deve retornar erro e o codigo de notfound")
	public void atualizarPessoaComIdNaoExistente() throws Exception {

		Optional<Pessoa> resultadoBusca = Optional.empty();
		
		when(pessoaService.buscarPorId(Mockito.anyLong())).thenReturn(resultadoBusca);
		
		
		this.mockMvc.perform(put("/people/1")
				.content(objectMapper.writeValueAsString(pessoa))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist())
				.andExpect(result -> Assertions.assertThat(result.getResolvedException() instanceof ResponseStatusException));
				
		verify(pessoaService, times(1)).buscarPorId(Mockito.anyLong());
		verify(pessoaService, times(0)).salvar(Mockito.any(Pessoa.class));
	}
	
	@Test
	@DisplayName("Atualizar uma pessoa com sucesso")
	public void atualizarPessoaComSucesso() throws Exception {
		
		Optional<Pessoa> resultadoBusca = Optional.of(pessoa);
		
		when(pessoaService.buscarPorId(Mockito.anyLong())).thenReturn(resultadoBusca);
		
		
		this.mockMvc.perform(put("/people/1")
				.content(objectMapper.writeValueAsString(pessoa))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
				
		verify(pessoaService, times(1)).buscarPorId(Mockito.anyLong());
		verify(pessoaService, times(1)).salvar(Mockito.any(Pessoa.class));
	}
	
	
	@Test
	@DisplayName("Tentar remover uma pessoa com id que não existe, deve retornar erro e o codigo de notfound")
	public void removerPessoaComIdNaoExistente() throws Exception {
		
		Optional<Pessoa> resultadoBusca = Optional.empty();
		
		when(pessoaService.buscarPorId((long) 1)).thenReturn(resultadoBusca);
		
		this.mockMvc.perform(delete("/people/1"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist())
				.andExpect(result -> Assertions.assertThat(result.getResolvedException() instanceof ResponseStatusException));
				
		verify(pessoaService, times(1)).buscarPorId(Mockito.anyLong());
		verify(pessoaService, times(0)).removerPorId(Mockito.anyLong());
	}
	
	@Test
	@DisplayName("Remover uma pessoa com sucesso")
	public void removerPessoaComSucesso() throws Exception {
		
		Optional<Pessoa> resultadoBusca = Optional.of(pessoa);
		
		when(pessoaService.buscarPorId(Mockito.anyLong())).thenReturn(resultadoBusca);
		
		this.mockMvc.perform(delete("/people/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
				
		verify(pessoaService, times(1)).buscarPorId(Mockito.anyLong());
		verify(pessoaService, times(1)).removerPorId(Mockito.anyLong());
	}
}
