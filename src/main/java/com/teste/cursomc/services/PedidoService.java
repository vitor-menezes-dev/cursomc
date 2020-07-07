package com.teste.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teste.cursomc.domain.ItemPedido;
import com.teste.cursomc.domain.PagamentoComBoleto;
import com.teste.cursomc.domain.Pedido;
import com.teste.cursomc.domain.Produto;
import com.teste.cursomc.domain.enums.EstadoPagamento;
import com.teste.cursomc.repositories.ItemPedidoRepository;
import com.teste.cursomc.repositories.PagamentoRepository;
import com.teste.cursomc.repositories.PedidoRepository;
import com.teste.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository; 
	
	@Autowired
	private EmailService emailService;
	
	public Pedido buscar(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getSimpleName()));

	}


	@Transactional
	public Pedido insert(Pedido obj) {

		obj.setId(null);
		obj.setInstante(new Date());
		
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherDataVencimentoPagamentoComBoleto(pagto,obj.getInstante());
		}
		obj = repo.save(obj);
		obj.setPagamento(pagamentoRepository.save(obj.getPagamento()));

		for(ItemPedido ip : obj.getItens()) {
			Produto prod = produtoService.find(ip.getProduto().getId());
			ip.setProduto(prod);
			ip.setPreco(prod.getPreco());
			ip.setPedido(obj);
		}

		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmation(obj);
		return obj;
	}

}
