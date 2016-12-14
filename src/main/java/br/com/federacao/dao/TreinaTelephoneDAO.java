package br.com.federacao.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.federacao.core.DAO;
import br.com.federacao.core.utils.Pagination;
import br.com.federacao.entity.TreinaTelephone;

public class TreinaTelephoneDAO extends DAO<Long, TreinaTelephone> {

	public Pagination<TreinaTelephone> findTelephones(Pagination<TreinaTelephone> pagination) {
		EntityManager entityManager = super.getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<TreinaTelephone> query = cb.createQuery(TreinaTelephone.class);
		Root<TreinaTelephone> root = query.from(TreinaTelephone.class);
		query.select(root);
		
		List<TreinaTelephone> list = entityManager.createQuery(query)
				.setFirstResult(pagination.getFirstResult())
				.setMaxResults(pagination.getPageSize())
				.getResultList();
		
		pagination.setItems(list);
		pagination.setCountResults(countItensPages().intValue());
		return pagination;
	}
	
	public Long countItensPages() {
		CriteriaBuilder cb = super.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<TreinaTelephone> root = query.from(TreinaTelephone.class);
		query.select(cb.count(root));
		return super.getEntityManager().createQuery(query).getSingleResult();
	}

}