package com.teste.cursomc.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.teste.cursomc.domain.Categoria;
import com.teste.cursomc.domain.Cidade;
import com.teste.cursomc.domain.Cliente;
import com.teste.cursomc.domain.Endereco;
import com.teste.cursomc.domain.Estado;
import com.teste.cursomc.domain.ItemPedido;
import com.teste.cursomc.domain.Pagamento;
import com.teste.cursomc.domain.PagamentoComBoleto;
import com.teste.cursomc.domain.PagamentoComCartao;
import com.teste.cursomc.domain.Pedido;
import com.teste.cursomc.domain.Produto;
import com.teste.cursomc.domain.enums.EstadoPagamento;
import com.teste.cursomc.domain.enums.Perfil;
import com.teste.cursomc.domain.enums.TipoCliente;
import com.teste.cursomc.repositories.CategoriaRepository;
import com.teste.cursomc.repositories.CidadeRepository;
import com.teste.cursomc.repositories.ClienteRepository;
import com.teste.cursomc.repositories.EnderecoRepository;
import com.teste.cursomc.repositories.EstadoRepository;
import com.teste.cursomc.repositories.ItemPedidoRepository;
import com.teste.cursomc.repositories.PagamentoRepository;
import com.teste.cursomc.repositories.PedidoRepository;
import com.teste.cursomc.repositories.ProdutoRepository;

@Service
public class DBService {

	@Autowired
	CategoriaRepository categoriaRepository;

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	EstadoRepository estadoRepository;

	@Autowired
	CidadeRepository cidadeRepository;

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	EnderecoRepository enderecoRepository;

	@Autowired
	PedidoRepository pedidoRepository;

	@Autowired
	PagamentoRepository pagamentoRepository;

	@Autowired
	ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private BCryptPasswordEncoder pe;

	public void instantieteTestDatabase() throws ParseException {

		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		Categoria cat3 = new Categoria(null, "Cama, mesa e banho");
		Categoria cat4 = new Categoria(null, "Eletônicos");
		Categoria cat5 = new Categoria(null, "Jardinagem");
		Categoria cat6 = new Categoria(null, "Decoração");
		Categoria cat7 = new Categoria(null, "Perfumaria");
		Categoria cat8 = new Categoria(null, "Teste");

		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		Produto p4 = new Produto(null, "Mesa de Escritório", 300.00);
		Produto p5 = new Produto(null, "Toalha", 50.00);
		Produto p6 = new Produto(null, "Colcha", 200.00);
		Produto p7 = new Produto(null, "Tv true color", 1200.00);
		Produto p8 = new Produto(null, "Roçadeira", 800.00);
		Produto p9 = new Produto(null, "Abajour", 100.00);
		Produto p10 = new Produto(null, "Pendente", 180.00);
		Produto p11 = new Produto(null, "shampoo", 90.00);

		List<Produto> listaProdutos = new ArrayList<>();
		listaProdutos.addAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11));

		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2, p4));
		cat3.getProdutos().addAll(Arrays.asList(p5, p6));
		cat4.getProdutos().addAll(Arrays.asList(p1, p2, p3, p7));
		cat5.getProdutos().addAll(Arrays.asList(p8));
		cat6.getProdutos().addAll(Arrays.asList(p9, p10));
		cat7.getProdutos().addAll(Arrays.asList(p11));

		p1.getCategorias().addAll(Arrays.asList(cat1, cat4));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2, cat4));
		p3.getCategorias().addAll(Arrays.asList(cat1, cat4));
		p4.getCategorias().addAll(Arrays.asList(cat2));
		p5.getCategorias().addAll(Arrays.asList(cat3));
		p6.getCategorias().addAll(Arrays.asList(cat3));
		p7.getCategorias().addAll(Arrays.asList(cat4));
		p8.getCategorias().addAll(Arrays.asList(cat5));
		p9.getCategorias().addAll(Arrays.asList(cat6));
		p10.getCategorias().addAll(Arrays.asList(cat6));
		p11.getCategorias().addAll(Arrays.asList(cat7));

		for (int i = 0; i < 100; i++) {
			Produto p = new Produto(null, "Produto " + i, 10.00);
			p.getCategorias().add(cat1);
			cat1.getProdutos().add(p);
			listaProdutos.add(p);
		}

		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7, cat8));
		produtoRepository.saveAll(listaProdutos);


		Estado[] estados = getEstados();
		estadoRepository.saveAll(Arrays.asList(estados));
		for (Estado estado :  getEstados()) {
			Cidade[] cidades = getCidades(estado);
			for (Cidade cidade : cidades) {
				cidade.setEstado(estado);
			}
			estado.getCidades().addAll(Arrays.asList(cidades));
			cidadeRepository.saveAll(Arrays.asList(cidades));
		}

		estadoRepository.saveAll(Arrays.asList(estados));


		Cidade c1 = cidadeRepository.findById(3170206).get();//"Uberlandia"
		Cidade c2 = cidadeRepository.findById(3550308).get();//"São Paulo"
		Cidade c3 = cidadeRepository.findById(3509502).get();//"Campinas" 
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "vitor.tonhao2@gmail.com", "36378912377",
				TipoCliente.PESSOAFISICA, pe.encode("123"));
		cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));

		Cliente cli2 = new Cliente(null, "Vitor Sousa", "vitor.tonhao@gmail.com", "05077231044",
				TipoCliente.PESSOAFISICA, pe.encode("123"));
		cli2.addPerfis(Perfil.ADMIN);
		cli2.getTelefones().addAll(Arrays.asList("27363321", "93838391"));

		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "38220834", cli1, c1);

		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);

		Endereco e3 = new Endereco(null, "Rua Flores", "310", "Apto 302", "Jardim", "38220834", cli2, c1);

		Endereco e4 = new Endereco(null, "Avenida Matos", "115", "Sala 801", "Centro", "38777012", cli2, c3);

		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));

		cli2.getEnderecos().addAll(Arrays.asList(e3, e4));

		clienteRepository.save(cli1);

		clienteRepository.save(cli2);

		enderecoRepository.saveAll(Arrays.asList(e1, e2, e3, e4));

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);

		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"),
				null);
		ped2.setPagamento(pagto2);

		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));

		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.0, 1, 2000.0);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.0, 2, 80.0);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 0.0, 2, 800.0);

		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().add(ip3);

		p1.getItems().add(ip1);
		p2.getItems().add(ip3);
		p3.getItems().add(ip1);

		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
		 
		
	}

	private Estado[] getEstados() {
		final String uri = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/";

		RestTemplate restTemplate = new RestTemplate();
		Estado[] result = restTemplate.getForObject(uri, Estado[].class);
		return result;
	}
	
	private Cidade[] getCidades(Estado estado) {
		final String uri = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/"+estado.getId()+"/municipios";
		RestTemplate restTemplate = new RestTemplate();
		Cidade[] result = restTemplate.getForObject(uri, Cidade[].class);
		
		return result;
	}

}
