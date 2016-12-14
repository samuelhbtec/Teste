package br.com.federacao.core;

import java.io.Serializable;
import java.util.List;

import br.com.federacao.core.entity.EntityAbstract;
import br.com.federacao.core.utils.Pagination;

public interface Service<T extends EntityAbstract<?>> extends Serializable {

	public T save(T entity);

	public void saveAll(List<T> entity);
	
	public List<T> findAll();
	
	public T findById(Long id);

	public void deleteById(List<Long> ids);
	
	public Pagination<T> findAllPaged(Pagination<T> pafination);

}
