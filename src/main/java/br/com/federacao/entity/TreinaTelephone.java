package br.com.federacao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.federacao.core.entity.EntityAbstract;
import br.com.federacao.core.utils.DatabaseDefinition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "HIB_SEQUENCE", schema = DatabaseDefinition.SCHEMA, sequenceName = "TRN001_ID_SEQ", allocationSize = 1)
@Table(name = "TREINA_TELEFONE")
public class TreinaTelephone extends EntityAbstract<Long> {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "PESSOA")
	private String person;
	
	@Column(name = "TELEFONE")
	private String telephone;

}