package com.attornatus.gerenciador_pessoas.controller;

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

import com.attornatus.gerenciador_pessoas.entity.Endereco;
import com.attornatus.gerenciador_pessoas.entity.EnderecoPessoa;
import com.attornatus.gerenciador_pessoas.entity.EnderecoPessoaKey;
import com.attornatus.gerenciador_pessoas.entity.Pessoa;
import com.attornatus.gerenciador_pessoas.http.controller.EnderecoPessoaController;
import com.attornatus.gerenciador_pessoas.service.impl.EnderecoPessoaServiceImpl;
import com.attornatus.gerenciador_pessoas.service.impl.EnderecoServiceImpl;
import com.attornatus.gerenciador_pessoas.service.impl.PessoaServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EnderecoPessoaController.class)
public class EnderecoPessoaControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EnderecoPessoaServiceImpl enderecoPessoaService;
	
	@MockBean
	private PessoaServiceImpl pessoaService;
	
	@MockBean
	private EnderecoServiceImpl enderecoService;
	
	private ObjectMapper objectMapper;
	
	private Pessoa pessoa;
	
	private Endereco endereco;
	
	private EnderecoPessoa enderecoPessoa;
	
	@BeforeEach
	public void setUp() {
		pessoa = new Pessoa();
		pessoa.setId((long) 1);
		pessoa.setNome("Pessoa Teste");
		
		endereco = new Endereco();
		endereco.setId((long) 1);
		endereco.setCidade("Cidade");
		
		enderecoPessoa = new EnderecoPessoa(pessoa, endereco);
		
		objectMapper = new ObjectMapper();
	}
	
	@Test
	@DisplayName("Buscar enderecos de uma pessoa pelo seu Id, deve retornar notfound e um erro")
	public void listarEnderecosPeloIdPessoaQueNaoExiste() throws Exception {
		
		when(enderecoPessoaService.listarEnderecosPorIdPessoa(Mockito.anyLong())).thenReturn(null);
		
		this.mockMvc.perform(get("/personaladdress/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
				
		verify(enderecoPessoaService, times(1)).listarEnderecosPorIdPessoa(Mockito.anyLong());
	}
	
	@Test
	@DisplayName("Buscar enderecos de uma pessoa pelo seu Id e receber uma lista com os endereco dessa pessoa")
	public void listarEnderecosPeloIdComSucesso() throws Exception {
		
		when(enderecoPessoaService.listarEnderecosPorIdPessoa(Mockito.anyLong())).thenReturn(List.of(enderecoPessoa));
		
		this.mockMvc.perform(get("/personaladdress/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("["+objectMapper.writeValueAsString(enderecoPessoa)+"]"));
				
		verify(enderecoPessoaService, times(1)).listarEnderecosPorIdPessoa(Mockito.anyLong());
	}
	
	@Test
	@DisplayName("Listar uma lista como todos enderecos de pessoas com sucesso")
	public void listarTodosEnderecosComSucesso() throws Exception {
		
		when(enderecoPessoaService.listarTodosEnderecoPessoa()).thenReturn(List.of(enderecoPessoa));
		
		this.mockMvc.perform(get("/personaladdress"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("["+objectMapper.writeValueAsString(enderecoPessoa)+"]"));
				
		verify(enderecoPessoaService, times(1)).listarTodosEnderecoPessoa();
	}
	
	@Test
	@DisplayName("Tentar colocar endereco de uma pessoa como principal mas ele nao existe, deve retornar erro e o codigo de notfound")
	public void atualizarEnderecoPessoaPrincipalComIdNaoExistente() throws Exception {
		
		Optional<EnderecoPessoa> enderecoPessoaBusca = Optional.empty();
		
		when(enderecoPessoaService.buscarPorId(Mockito.any(EnderecoPessoaKey.class))).thenReturn(enderecoPessoaBusca);
		
		
		this.mockMvc.perform(put("/personaladdress")
				.content(objectMapper.writeValueAsString(enderecoPessoa))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist())
				.andExpect(result -> Assertions.assertThat(result.getResolvedException() instanceof ResponseStatusException));
				
		verify(enderecoPessoaService, times(1)).buscarPorId(Mockito.any(EnderecoPessoaKey.class));
		verify(enderecoPessoaService, times(0)).atualizarEnderecoPrincipal(Mockito.any(EnderecoPessoa.class));
	}
	
	@Test
	@DisplayName("Colocar endereco de uma pessoa como principal com sucesso")
	public void atualizarEnderecoPessoaPrincipal() throws Exception {
		
		Optional<EnderecoPessoa> enderecoPessoaBusca = Optional.of(enderecoPessoa);
		
		when(enderecoPessoaService.buscarPorId(Mockito.any(EnderecoPessoaKey.class))).thenReturn(enderecoPessoaBusca);
		
		
		this.mockMvc.perform(put("/personaladdress")
				.content(objectMapper.writeValueAsString(enderecoPessoa))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
				
		verify(enderecoPessoaService, times(1)).buscarPorId(Mockito.any(EnderecoPessoaKey.class));
		verify(enderecoPessoaService, times(1)).atualizarEnderecoPrincipal(Mockito.any(EnderecoPessoa.class));
	}
	
	@Test
	@DisplayName("Tentar cadastrar um endereco pra uma pessoa com id que nao existe, deve retornar erro e o codigo de notfound")
	public void cadastrarEnderecoPraPessoaComIdNaoExistente() throws Exception {
		
		Optional<Pessoa> pessoaBusca = Optional.empty();
		
		when(pessoaService.buscarPorId(Mockito.anyLong())).thenReturn(pessoaBusca);
		when(enderecoPessoaService.listarEnderecosPorIdPessoa(Mockito.anyLong())).thenReturn(List.of(enderecoPessoa));
		
		this.mockMvc.perform(post("/personaladdress/1")
				.content(objectMapper.writeValueAsString(endereco))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist())
				.andExpect(result -> Assertions.assertThat(result.getResolvedException() instanceof ResponseStatusException));
		
		verify(pessoaService, times(1)).buscarPorId(Mockito.anyLong());
		verify(enderecoPessoaService, times(0)).listarEnderecosPorIdPessoa(Mockito.anyLong());
		verify(enderecoPessoaService, times(0)).salvar(Mockito.any(EnderecoPessoa.class));
	}
	
	@Test
	@DisplayName("Cadastrar um endereco para uma pessoa com sucesso")
	public void cadastrarEnderecoPraPessoaComSucesso() throws Exception {
		
		Optional<Pessoa> pessoaBusca = Optional.of(pessoa);
		
		when(pessoaService.buscarPorId(Mockito.anyLong())).thenReturn(pessoaBusca);
		when(enderecoPessoaService.listarEnderecosPorIdPessoa(Mockito.anyLong())).thenReturn(List.of(enderecoPessoa));
		
		this.mockMvc.perform(post("/personaladdress/1")
				.content(objectMapper.writeValueAsString(endereco))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$").doesNotExist());
		
		verify(pessoaService, times(1)).buscarPorId(Mockito.anyLong());
		verify(enderecoPessoaService, times(1)).listarEnderecosPorIdPessoa(Mockito.anyLong());
		verify(enderecoPessoaService, times(1)).salvar(Mockito.any(EnderecoPessoa.class));
	}
	
	@Test
	@DisplayName("Tentar remover endereco de uma pessoa mas ele nao existe, deve retornar erro e o codigo de notfound")
	public void removerEnderecoPessoaComIdNaoExistente() throws Exception {
		
		Optional<EnderecoPessoa> enderecoPessoaBusca = Optional.empty();
		
		when(enderecoPessoaService.buscarPorId(Mockito.any(EnderecoPessoaKey.class))).thenReturn(enderecoPessoaBusca);
		
		
		this.mockMvc.perform(delete("/personaladdress")
				.content(objectMapper.writeValueAsString(enderecoPessoa))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist())
				.andExpect(result -> Assertions.assertThat(result.getResolvedException() instanceof ResponseStatusException));
				
		verify(enderecoPessoaService, times(1)).buscarPorId(Mockito.any(EnderecoPessoaKey.class));
		verify(enderecoPessoaService, times(0)).removerPorId(Mockito.any(EnderecoPessoaKey.class));
	}
	
	@Test
	@DisplayName("Remover endereco pessoa com sucesso")
	public void removerEnderecoPessoa() throws Exception {
		
		Optional<EnderecoPessoa> enderecoPessoaBusca = Optional.of(enderecoPessoa);
		
		when(enderecoPessoaService.buscarPorId(Mockito.any(EnderecoPessoaKey.class))).thenReturn(enderecoPessoaBusca);
		
		this.mockMvc.perform(delete("/personaladdress")
				.content(objectMapper.writeValueAsString(enderecoPessoa))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
				
		verify(enderecoPessoaService, times(1)).buscarPorId(Mockito.any(EnderecoPessoaKey.class));
		verify(enderecoPessoaService, times(1)).removerPorId(Mockito.any(EnderecoPessoaKey.class));
	}
}
