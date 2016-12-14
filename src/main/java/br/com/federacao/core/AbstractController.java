package br.com.federacao.core;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import br.com.federacao.core.entity.EntityAbstract;
import br.com.federacao.core.utils.Pagination;

public abstract class AbstractController<T extends EntityAbstract<?>> {

	private static final Logger logger = Logger.getLogger(AbstractController.class);
	
	@Inject
	private HttpServletRequest request;
	
	public abstract Service<T> getRootService();
	
	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Pagination<T> getAllPaged(){
		return getRootService().findAllPaged(getPaginationParameters());
	}
	
	@GET
	@Path("findById/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public T findById(@PathParam("id") Long id){
		return getRootService().findById(id);
	}
	
	@GET
	@Path("newEntity")
	@Produces(MediaType.APPLICATION_JSON)
	public abstract T newEntity();

	@POST
	@Path("save")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	public T save(T entity) throws Exception {
		return getRootService().save(entity);
	}
	
	@POST
	@Path("saveAll")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	public void saveAll(List<T> listEntity) throws Exception {
		getRootService().saveAll(listEntity);
	}

	@DELETE
	@Path("removeAll")
	@Produces(MediaType.APPLICATION_JSON)
	public void remove(@QueryParam("ids") List<Long> ids) throws Exception {
		getRootService().deleteById(ids);
	}

	@DELETE
	@Path("remove")
	@Produces(MediaType.APPLICATION_JSON)
	public void remove(@QueryParam("id") Long id) throws Exception {
		getRootService().deleteById(Arrays.asList(id));
	}
	
	protected Pagination<T> getPaginationParameters() {
		
		Pagination<T> pg = new Pagination<T>();
		
		try {
		
			String pageStr = request.getParameter("page");
			String pageSizeStr = request.getParameter("pageSize");
			
			if((pageStr != null) && (!pageStr.trim().isEmpty())) {
				pg.setPage(Integer.valueOf(pageStr));
			}
			if((pageSizeStr != null) && (!pageSizeStr.trim().isEmpty())) {
				pg.setPageSize(Integer.valueOf(pageSizeStr));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		
		return pg;
	}
	
}
