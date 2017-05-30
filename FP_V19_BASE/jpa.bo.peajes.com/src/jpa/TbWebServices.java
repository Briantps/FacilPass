package jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the TB_WEB_SERVICES database table.
 * 
 */
@Entity
@Table(name="TB_WEB_SERVICES")
public class TbWebServices implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TB_WEB_SERVICES_ID")
	private Long tbWebServicesId;

	private String description;

	private String name;

	@Column(name="SERVICE_NAME")
	private String service;

	
	@Column(name="URL")
	private String url;

	//bi-directional many-to-one association to TbWsResponses
	@OneToMany(mappedBy="tbWebService")
	private List<TbWsResponses> tbWsResponses;

    public TbWebServices() {
    }

	public Long getTbWebServicesId() {
		return this.tbWebServicesId;
	}

	public void setTbWebServicesId(Long tbWebServicesId) {
		this.tbWebServicesId = tbWebServicesId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public List<TbWsResponses> getTbWsResponses() {
		return this.tbWsResponses;
	}

	public void setTbWsResponses(List<TbWsResponses> tbWsResponses) {
		this.tbWsResponses = tbWsResponses;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}