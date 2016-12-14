package br.com.federacao.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.log4j.Logger;

import br.com.federacao.core.utils.ApplicationUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
public abstract class EntityAbstract<TPKID> implements Serializable {

	private static final Logger logger = Logger.getLogger(EntityAbstract.class.getClass());
	
	private static final long serialVersionUID = 2866402516151725133L;

	@Id
	@GeneratedValue(generator = "HIB_SEQUENCE", strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private TPKID id;

	@PrePersist
	@PreUpdate
	public void encryptData() {
		try {
			ApplicationUtil.getInstance().encryptData(this);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@PostLoad
	@PostUpdate
	public void decrypt() {
		try {
			ApplicationUtil.getInstance().decryptData(this);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
