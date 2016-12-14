package br.com.federacao.controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.federacao.core.AbstractController;
import br.com.federacao.core.Service;
import br.com.federacao.core.utils.Pagination;
import br.com.federacao.entity.TreinaTelephone;
import br.com.federacao.service.TreinaTelephoneService;

@Path("telephone")
public class TreinaTelephoneController extends AbstractController<TreinaTelephone> {

	@Inject
	private TreinaTelephoneService treinaTelephoneService;
	
	@Override
	public Service<TreinaTelephone> getRootService() {
		return treinaTelephoneService;
	}

	@Override
	public TreinaTelephone newEntity() {
		return new TreinaTelephone();
	}
	
	@GET
	@Path("findAllPaged/{page}")
	@Produces(MediaType.APPLICATION_JSON)
	public Pagination<TreinaTelephone> findAll(@PathParam("page") Integer page) throws Exception {

		Pagination<TreinaTelephone> pagination = new Pagination<TreinaTelephone>();

		pagination.setPageSize(5);
		pagination.setPage(page);

		return treinaTelephoneService.findTelephones(pagination);
	}


}