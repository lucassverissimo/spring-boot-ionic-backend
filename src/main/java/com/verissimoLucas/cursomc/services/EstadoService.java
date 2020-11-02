package com.verissimoLucas.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.verissimoLucas.cursomc.domain.Estado;
import com.verissimoLucas.cursomc.repositories.EstadoRepository;
import com.verissimoLucas.cursomc.services.exceptions.DataIntegrityException;
import com.verissimoLucas.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository repo;

	public Estado find(Integer id) {
		Optional<Estado> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Estado.class.getName()));
	}

	public Estado insert(Estado obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	public Estado update(Estado obj) {
		Estado newObj = find(obj.getId());
		updateData(newObj,obj);
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possua produtos");
		}
	}

	public List<Estado> findAll() {
		return repo.findAll();
	}

	public Page<Estado> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);

	}

	private void updateData(Estado newObj, Estado obj) {
		newObj.setNome(obj.getNome());
	}

}
