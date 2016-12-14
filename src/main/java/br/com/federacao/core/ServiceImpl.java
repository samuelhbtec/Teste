package br.com.federacao.core;

import java.util.List;

import org.apache.log4j.Logger;

import br.com.federacao.core.annotation.UFTransaction;
import br.com.federacao.core.entity.EntityAbstract;
import br.com.federacao.core.utils.Pagination;

public abstract class ServiceImpl<TEntity extends EntityAbstract<?>, TDao extends DAO<Long, TEntity>> implements Service<TEntity>{

	private static final long serialVersionUID = -559124174134482749L;

	protected static final Logger log = Logger.getLogger(ServiceImpl.class);
	
	protected TDao dao;
	
	protected ServiceImpl(TDao dao) {
		this.dao = dao;
	}

	@UFTransaction
	public TEntity save(TEntity entity) {
		entity = dao.save(entity);
		return entity;
	}
	
	@UFTransaction
	public void saveAll(List<TEntity> entity) {
		for (TEntity tEntity : entity) {
			this.save(tEntity);
		}
	}

	@Override
	@UFTransaction
	public void deleteById(List<Long> ids) {
		dao.deleteById(ids);
	}

	@Override
	public List<TEntity> findAll() {
		return dao.findAll();
	}

	@Override
	public TEntity findById(Long id) {
		return dao.findById(id);
	}
	
	@Override
	public Pagination<TEntity> findAllPaged(Pagination<TEntity> pafination){
		return dao.findAllPaged(pafination);
	}

}