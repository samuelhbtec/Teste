package br.com.federacao.service;

import br.com.federacao.core.Service;
import br.com.federacao.core.utils.Pagination;
import br.com.federacao.entity.TreinaTelephone;

public interface TreinaTelephoneService extends Service<TreinaTelephone> {

	public static String JNDI = "TreinaTelephoneService";
	
	Pagination<TreinaTelephone> findTelephones(Pagination<TreinaTelephone> pagination);
	
}