package br.com.federacao.service.impl;

import javax.inject.Inject;

import br.com.federacao.core.ServiceImpl;
import br.com.federacao.core.utils.Pagination;
import br.com.federacao.dao.TreinaTelephoneDAO;
import br.com.federacao.entity.TreinaTelephone;
import br.com.federacao.service.TreinaTelephoneService;

public class TreinaTelephoneServiceImpl extends ServiceImpl<TreinaTelephone, TreinaTelephoneDAO> implements TreinaTelephoneService {

	private static final long serialVersionUID = 1L;

	@Inject
	protected TreinaTelephoneServiceImpl(TreinaTelephoneDAO dao) {
		super(dao);
	}

	@Override
	public Pagination<TreinaTelephone> findTelephones(Pagination<TreinaTelephone> pagination) {
		try {
			return dao.findTelephones(pagination);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}