package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TB_IMAGE")
public class TbImage implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMAGE_SEQ",sequenceName="TB_IMAGE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMAGE_SEQ")
	@Column(name="IMAGE_ID")
	private Long imageId;
	
	@Column(name="IMAGE_URL")
	private String ImageUrl;
	
	@ManyToOne
	@JoinColumn(name="TYPE_REJECTED_ID")
	private TbCodeImagesRejected typeRejectedId;
	
	public TbImage(){
		super();
	}

	/**
	 * @param imageId the imageId to set
	 */
	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	/**
	 * @return the imageId
	 */
	public Long getImageId() {
		return imageId;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return ImageUrl;
	}

	/**
	 * @param typeRejectedId the typeRejectedId to set
	 */
	public void setTypeRejectedId(TbCodeImagesRejected typeRejectedId) {
		this.typeRejectedId = typeRejectedId;
	}

	/**
	 * @return the typeRejectedId
	 */
	public TbCodeImagesRejected getTypeRejectedId() {
		return typeRejectedId;
	}
	
}
