package br.com.federacao.core;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;

import br.com.federacao.core.annotation.UFEntityManager;
import br.com.federacao.core.entity.EntityAbstract;
import br.com.federacao.core.utils.Pagination;

@Any
@Named("DAO")
public class DAO<PK, T extends EntityAbstract<?>> {

	@Inject
	@UFEntityManager
	private EntityManager entityManager;

	/**
	 * Método para INSERIR ou ATUALIZAR linhas na tabela baseado em um objeto.
	 * Se tiver ID, signifca que o objeto deverá ser atualizado, ao contrário,
	 * inserido!
	 * 
	 * @param entity
	 *            Objeto a ser persistido.
	 * @return Retorna o objeto saldo com ID.
	 */
	public T save(T entity) {

		try {
			executeBeforeSave(entity);
			if (entity.getId() == null) {
				entityManager.persist(entity);
			} else {
				entity = entityManager.merge(entity);
			}
			this.executeAfterSave(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	/**
	 * Realiza busca de um objeto baseado em ID
	 * 
	 * @param pk
	 *            ID para busca.
	 * @return Retorna objeto consultado.
	 */
	public T findById(PK pk) {
		return (T) entityManager.find(getTypeClass(), pk);
	}

	/**
	 * Remove um registro da tabela baseado em um objeto.
	 * 
	 * @param entity
	 *            Objeto que deverá ser removido.
	 */
	public void delete(T entity) {
		entityManager.remove(entity);
	}

	/**
	 * Remove vários registros da tabela, baseado em ID.
	 * 
	 * @param pks
	 *            ID
	 */
	public void deleteById(List<PK> pks) {
		String hql = " DELETE FROM " + getTypeClass().getName() + " WHERE id IN (:ids)";
		Query query = entityManager.createQuery(hql);
		query.setParameter("ids", pks);
		query.executeUpdate();
	}

	/**
	 * Realiza busca de um objeto
	 * 
	 * @param hql
	 *            Query a ser executada.
	 * @param params
	 *            Parametros de filtro da Query
	 * @return Retorna um Objeto referente a linha do banco.
	 * 
	 * @NoResultException Ocorrerá um execeção, caso não haja registro com os
	 *                    dados informados, é tratada a exceção e retornado
	 *                    null;
	 */
	protected T findByQuery(String hql, Map<String, Object> params) {
		try {
			Query query = getQuery(hql, params);
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			// Sem resutlado
			return null;
		}
	}

	/**
	 * Centralização para montar Query
	 * 
	 * @param hql
	 *            Query a ser executada
	 * @param params
	 *            Parametros de filtro para query
	 * @return retorna query montada para os demais métodos utilizar.
	 */
	protected Query getQuery(String hql, Map<String, Object> params) {
		Query query = this.entityManager.createQuery(hql);
		if ((params != null) && (params.size() > 0)) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				query.setParameter(key, params.get(key));
			}
		}
		return query;
	}

	/**
	 * 
	 * @param hql
	 *            Consulta a ser executada
	 * @param params
	 *            Lista de parametros para filtro da consulta
	 * 
	 *            Executa comandos DLL
	 * 
	 */

	protected void executeHQL(String hql, Map<String, Object> params) throws Exception {
		Query query = this.getQuery(hql, params);
		query.executeUpdate();
	}

	/**
	 * 
	 * @param hql
	 *            Consulta a ser executada
	 * @param params
	 *            Lista de parametros para filtro da consulta
	 * @return Retorna todas as linhas da tabela, baseado em um objeto.
	 */

	protected <L extends List<?>> L findAllByQuery(String hql, Map<String, Object> params) {
		Query query = this.getQuery(hql, params);
		return (L) query.getResultList();
	}

	protected <L extends List<?>> L executeSQL(String SQL) {
		return (L) ((Session) this.entityManager.getDelegate()).createSQLQuery(SQL).getQueryReturns();
	}

	/**
	 * Método utilizado para retornar todos os registros da tabela, baseado em
	 * uma entidade. Análogo ao * FROM table_name.
	 * 
	 * @return Retorna todas as linhas da tabela, baseado em um objeto.
	 */

	public List<T> findAll() {
		return entityManager.createQuery(("FROM " + getTypeClass().getName())).getResultList();
	}

	/**
	 * Metodo utilizado para execução de procedure.
	 * 
	 * @param proc
	 *            nome da proc a ser executada
	 * @param params
	 *            parametros da proc
	 */
	protected void executeProcedure(String proc, Map<String, Object> params) {
		Query query = ((Session) this.entityManager.getDelegate()).getNamedQuery(proc);
		if (params != null) {
			for (Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		query.executeUpdate();
	}

	/**
	 * Metodo utilizado para busca baseada em procedure
	 * 
	 * @param proc
	 *            nome da proc a ser executada
	 * @param params
	 *            parametros da proc
	 * @return Coleção de objetos encontrados
	 */
	protected List<T> findByProcedure(String proc, Map<String, Object> params) {
		Query query = ((Session) this.entityManager.getDelegate()).createQuery(proc);
		if (params != null) {
			for (Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.getResultList();
	}
	
	/**
	 * Busca todos os registro de formar paginada
	 * 
	 *  @param pagination
	 * 				Pagina a ser retornada (devem conter o número da pagina <@param page> e o tamanho da pagina <@param pageSize> setados)
	 * 
	 * */
	public Pagination<T> findAllPaged(Pagination<T> pagination) {
		Query queryCount = entityManager.createQuery(("SELECT COUNT(*) FROM " + getTypeClass().getName()));
		pagination.setCountResults(queryCount.getFirstResult());
		
		Query query = entityManager.createQuery(("FROM " + getTypeClass().getName()));
		List<T> items = query.setFirstResult(pagination.getFirstResult()).setMaxResults(pagination.getPageSize()).getResultList();
		pagination.setItems(items);
		
		return pagination;
	}
	
	/**
	 * Busca uma pagina da consulta com base nos parametros informados
	 * 
	 * @param query 
	 * 				Query de busca
	 * 
	 * @param pagination
	 * 				Pagina a ser retornada (devem conter o número da pagina <@param page> e o tamanho da pagina <@param pageSize> setados)
	 * 
	 * */
	public Pagination<T> findAllByCriteriaQueryPaged(CriteriaQuery<T> query, Pagination<T> pagination) {
		
		List<T> items = entityManager.createQuery(query).setFirstResult(pagination.getFirstResult()).setMaxResults(pagination.getPageSize()).getResultList();
		pagination.setItems(items);
		
		return pagination;
	}
	
	/**
	 * 
	 * @param pagination Objeto de paginação.
	 * @param query Query para a consulta.
	 * @param params Parâmetros da query.
	 * @return Lista de objetos consultado. 
	 */
	public List<T> findAllByQueryPaged(Pagination<?> pagination, String query, Map<String, Object> params) {
		Query result = entityManager.createQuery(query).setFirstResult(pagination.getFirstResult()).setMaxResults(pagination.getPageSize());
		if (params != null) {
			for (Entry<String, Object> entry : params.entrySet()) {
				result.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return result.getResultList();
	}

	private Class<?> getTypeClass() {
		return (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}

	protected EntityManager getEntityManager() {
		return this.entityManager;
	}
	
	protected void executeAfterSave(T entity){
		
	}
	
	protected void executeBeforeSave(T entity){
		
	}

}