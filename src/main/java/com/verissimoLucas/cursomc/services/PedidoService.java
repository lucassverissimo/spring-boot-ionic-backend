package com.verissimoLucas.cursomc.services;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.verissimoLucas.cursomc.domain.Cliente;
import com.verissimoLucas.cursomc.domain.ItemPedido;
import com.verissimoLucas.cursomc.domain.PagamentoComBoleto;
import com.verissimoLucas.cursomc.domain.Pedido;
import com.verissimoLucas.cursomc.domain.enums.EstadoPagamento;
import com.verissimoLucas.cursomc.repositories.ItemPedidoRepository;
import com.verissimoLucas.cursomc.repositories.PagamentoRepository;
import com.verissimoLucas.cursomc.repositories.PedidoRepository;
import com.verissimoLucas.cursomc.security.UserSS;
import com.verissimoLucas.cursomc.services.exceptions.AuthorizationException;
import com.verissimoLucas.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	
	@Autowired
	private ItemPedidoRepository itemPedidoRespository;

	@Autowired
	private boletoService boletoService;

	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
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
			boletoService.preencherPagamentoComBoleto(pagto,obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRespository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationEmail(obj);
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null){
			throw new AuthorizationException("Acesso negado!!");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}

}
