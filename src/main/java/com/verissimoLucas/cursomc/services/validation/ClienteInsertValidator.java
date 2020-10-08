package com.verissimoLucas.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.verissimoLucas.cursomc.domain.Cliente;
import com.verissimoLucas.cursomc.domain.enums.TipoCliente;
import com.verissimoLucas.cursomc.dto.ClienteNewDTO;
import com.verissimoLucas.cursomc.repositories.ClienteRepository;
import com.verissimoLucas.cursomc.resources.exceptions.FieldMessage;
import com.verissimoLucas.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	ClienteRepository repo; 
	
	@Override
	public void initialize(ClienteInsert ann) {

	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		if (objDto.getTipo() == null) {
			list.add(new FieldMessage("Tipo", "Tipo não pode ser nulo"));
		} else {

			if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
				list.add(new FieldMessage("CpfOuCnpj", "Cpf Inválido"));
			}
			
			if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
				list.add(new FieldMessage("CpfOuCnpj", "Cnpj Inválido"));
			}
		}
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		
		if (aux != null) {
			list.add(new FieldMessage("Email", "Email já existente"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}

}
