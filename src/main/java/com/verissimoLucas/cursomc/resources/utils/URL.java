package com.verissimoLucas.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URL {

	
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	/*
	 * Recebe string no seguinte formato: "1,2,3,4"
	 * Retorna lista de Integer contendo os 4 números
	 * */
	public static List<Integer> decodeIntList(String s){
		/*
		String [] vet = s.split(",");
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < vet.length; i++) {
			list.add(Integer.parseInt(vet[i]));
		}
		return list;
		*/
		
		return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	
	}
}
