package mBeans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollerEvent;

import jpa.TbCategory;
import jpa.TbCodeType;
import jpa.TbVehicle;
import security.UserLogged;
import sessionVar.SessionUtil;
import util.Vehicles;
import ejb.BrandManager;
import ejb.Category;
import ejb.Device;
import ejb.User;
import ejb.Vehicle;

/**
 * Bean of vehicles for admin
 * 
 * @author jromero
 */
public class AdminVehicleAdmin implements Serializable {

	private static final long serialVersionUID = 7032048764316281149L;

	@EJB(mappedName = "ejb/Vehicle")
	private Vehicle vehicleEJB;

	@EJB(mappedName = "ejb/Category")
	private Category categoryEJB;

	@EJB(mappedName = "ejb/User")
	private User userEJB;

	@EJB(mappedName = "ejb/BrandManager")
	private BrandManager brandEJB;

	@EJB(mappedName = "ejb/Device")
	private Device deviceEJB;

	// /Filter fields
	private List<SelectItem> codeTypesList;
	private Long codeType = 0L;
	private String numberDoc = "";
	private String firstName = "";
	private String lastName = "";
	private Long categoryType = 0L;
	private String placaFil = "";
	private String ad1Fil = "";
	private String ad2Fil = "";
	private String ad3Fil = "";
	private boolean showCreate = false;

	private UserLogged usrs;

	private Long newColorId = 0L;
	private Long newCatId = 0L;

	private Long vehicleID = 0L;
	private String vehicleChassis;
	private String vehiclePlate = "";
	private String newBrand1 = "";
	private Long newCategoryId = 0L;
	private String vehicleInternalCode = "";
	private String line = "";
	private Timestamp vehicleBeginingDate;
	private String aditional1 = "";
	private String aditional2 = "";
	private String aditional3 = "";
	private Long brandId;
	private String observation = "";
	private Long userId;
	private Long userIdCreate;
	private boolean especialForm = false;
	private boolean showObservation = false;
	private Long plateLength = 6L;

	private List<Vehicles> listVehicles;
	private List<Integer> listaScroll;

	private boolean info = false;
	private boolean showModalConD;
	private boolean showModalConC;
	private boolean showModalConM;
	private boolean showModal;
	private boolean showModalInfo;
	private boolean showNew;
	private boolean showEdit;
	private String msg;

	private int page=1;
	private int valuesFor;

	public AdminVehicleAdmin() {
		super();
		usrs = (UserLogged) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("user");
		newColorId = new Long(0);
		newCatId = new Long(0);
		brandId = new Long(0);
		showNew = false;
	}

	public void changeEspecial(ValueChangeEvent event) {
		if (((Boolean) event.getNewValue())) {
			this.setShowObservation(true);
			this.setEspecialForm(true);
			this.setPlateLength(12L);
			this.setObservation("");
			System.out.println("Ingrse a ChangeEspecial true");
		} else {
			this.setShowObservation(false);
			this.setEspecialForm(false);
			this.setPlateLength(6L);
			this.setObservation("");
			System.out.println("Ingrse a ChangeEspecial false");
		}
	}

	public void changeEspecialMod(ValueChangeEvent event) {
		boolean change = false;
		if (((Boolean) event.getNewValue())) {
			if (vehicleEJB.isEspecialPlate(vehiclePlate.toUpperCase())) {
				change = true;
			} else {
				change = false;
				this.setMsg("El vehículo no tiene una placa especial, "
						+ "por favor no seleccione la casilla Especial.");
				this.setShowModal(true);
			}
			if (change) {
				this.setShowObservation(true);
				this.setEspecialForm(true);
			}
			System.out.println("Ingrse a ChangeEspecial true");
		} else {
			if (vehicleEJB.isEspecialPlate(vehiclePlate.toUpperCase())) {
				change = false;
				this.setMsg("El vehículo tiene una placa especial, "
						+ "por favor seleccione la casilla Especial y escriba una observación.");
				this.setShowModal(true);
			} else {
				change = true;
			}
			if (change) {
				this.setShowObservation(false);
				this.setEspecialForm(false);
				this.setObservation(this.getObservation());
			}
			System.out.println("Ingrse a ChangeEspecial false");
		}
	}

	public String clear() {
		this.setMsg("");
		this.setInfo(false);
		this.setShowModal(false);
		this.setShowModalInfo(false);
		this.setShowModalConC(false);
		this.setShowModalConD(false);
		this.setShowModalConM(false);
		this.setShowEdit(false);
		this.setShowNew(false);
		return null;
	}

	public String hideModalAll() {
		this.setMsg("");
		this.setShowModal(false);
		this.setInfo(false);
		this.setShowModalInfo(false);
		this.setShowModalConC(false);
		this.setShowModalConD(false);
		this.setShowModalConM(false);
		this.setUserId(-1L);
		this.setAditional1("");
		this.setAditional2("");
		this.setAditional3("");
		this.setObservation("");
		this.setLine("");
		this.setNewBrand1("");
		this.setNewCategoryId(0L);
		this.setNewCatId(0L);
		this.setNewColorId(0L);
		this.setBrandId(0L);
		this.setShowEdit(false);
		this.setShowNew(false);
		this.setShowObservation(false);
		this.setEspecialForm(false);
		this.setVehicleBeginingDate(null);
		this.setVehicleChassis("");
		this.setVehicleInternalCode("");
		this.setVehiclePlate("");
		this.setPlateLength(6L);
		return null;
	}

	public String hideModal() {
		this.setMsg("");
		this.setShowModal(false);
		this.setInfo(false);
		this.setShowModalInfo(false);
		return null;
	}

	public String hideModalConD() {
		this.setMsg("");
		this.setShowModalConD(false);
		return null;
	}

	public String hideModalConC() {
		this.setMsg("");
		this.setShowModalConC(false);
		return null;
	}

	public String hideModalConM() {
		this.setMsg("");
		this.setShowModalConM(false);
		return null;
	}

	public String hideModalNew() {
		return null;
	}

	public String hideModalEdit() {
		return null;
	}

	public void searchFilter() {
		hideModalAll();
		if (postValidationSearch()) {
			setPage(1);
				List<Vehicles> listfilter = null;
				listfilter = this.getListVehicles();
				if (listfilter.size() <= 0) {

					if ((codeType == -1L) && (numberDoc.equals(""))
							&& (firstName.equals("")) && (lastName.equals(""))) {
						userIdCreate = 0L;
					} else {
						userIdCreate = userEJB.getUserFromFiltersVehicle(
								codeType, numberDoc, firstName, lastName, "","");
					}
					if (userIdCreate.equals(0L)) {
						clearFilter();
						this.setMsg("No se encontraron resultados para la búsqueda.");
						this.setShowModal(true);
					} else if (userIdCreate.equals(-1L)) {
						clearFilter();
						this.setMsg("Ocurrió un error al momento de realizar la búsqueda.");
						this.setShowModal(true);
					} else if (userIdCreate >= 1L) {
						System.out
								.println("Hay cliente sin vehículos pero puede crearlos.");
						this.setShowCreate(true);
					} else if (userIdCreate.equals(-2L)) {
						this.setShowCreate(false);
						this.setMsg("Hay más de un resultado en la consulta, "
								+ "por favor agregue otro campo en el filtro.");
						this.setShowModal(true);
					}
				} else {
					userIdCreate = vehicleEJB.getUserByFiltersVehicle(codeType,
							numberDoc, firstName, lastName, categoryType,
							placaFil, ad1Fil, ad2Fil, ad3Fil, 0L);
					System.out.println("Search.userId:" + userIdCreate);
					if (userIdCreate >= 0L) {
						this.setShowCreate(true);
					} else if (userIdCreate.equals(-2L)) {
						this.setShowCreate(false);
						this.setMsg("Hay más de un resultado en la consulta, "
								+ "por favor agregue otro campo en el filtro.");
						this.setShowModal(true);
					}
				}
		} else {
			clearFilter();
		}
	}

	private boolean postValidationSearch() {
		if ((codeType == -1L) && (numberDoc.equals(""))
				&& (firstName.equals("")) && (lastName.equals(""))
				&& (categoryType == -1L || categoryType == 0L)
				&& (placaFil.equals("")) && (ad1Fil.equals(""))
				&& (ad2Fil.equals("")) && (ad3Fil.equals(""))) {
			this.setMsg("No existen datos para realizar la búsqueda.");
			this.setShowModal(true);
			return false;
		} else if (numberDoc != "" && !numberDoc.matches("([0-9])+")) {
			this.setMsg("El campo No.Identificación tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		} else if (firstName != ""
				&& !firstName
						.matches("([a-z]|[A-Z]|[áéíóúü]|[ÁÉÍÓÚÜ]|[0-9]|[&/]|[_-]|[.]|[ñÑ]|\\s)+")) {
			this.setMsg("El campo Nombre tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		} else if (lastName != ""
				&& !lastName
						.matches("([a-z]|[A-Z]|[áéíóúü]|[ÁÉÍÓÚÜ]|[ñÑ]|\\s)+")) {
			this.setMsg("El campo Apellidos tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		} else if (placaFil != "" && !placaFil.matches("([0-9]|[a-z]|[A-Z])+")) {
			this.setMsg("El campo Placa tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		} else if (ad1Fil != ""
				&& !ad1Fil
						.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
			this.setMsg("El campo Adicional 1 tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		} else if (ad2Fil != ""
				&& !ad2Fil
						.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
			this.setMsg("El campo Adicional 2 tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		} else if (ad3Fil != ""
				&& !ad3Fil
						.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
			this.setMsg("El campo Adicional 3 tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}
		return true;
	}

	public void clearFilter() {
		userId = -1L;
		userIdCreate = -1L;
		codeType = -1L;
		numberDoc = "";
		firstName = "";
		lastName = "";
		categoryType = -1L;
		placaFil = "";
		ad1Fil = "";
		ad2Fil = "";
		ad3Fil = "";
		this.setPage(1);
		this.setShowCreate(false);
		this.getListVehicles();
	}

	public boolean validateModifiedField() {
		boolean validate = false;
		if (vehicleEJB.isEspecialPlate(vehiclePlate.toUpperCase()) == true
				&& especialForm == false) {
			this.setMsg("El vehículo tiene una placa especial,"
					+ " por favor seleccione la casilla Especial y escriba una observación.");
			return false;
		}
		if (vehicleEJB.isEspecialPlate(vehiclePlate.toUpperCase()) == false
				&& especialForm == true) {
			this.setMsg("El vehículo no tiene una placa especial, "
					+ "por favor no seleccione la casilla Especial.");
			return false;
		}
		if (vehicleChassis == null || vehicleChassis.equals("")) {
			validate = true;
		} else {
			if (vehicleChassis.length() > 50) {
				validate = false;
			} else {
				validate = true;
			}
		}
		if (validate) {
			if (newBrand1 == null || newBrand1.equals("")) {
				validate = true;
				brandId = null;
			} else {
				if (newBrand1.length() > 50) {
					validate = false;
				} else {
					brandId = brandEJB.getBrandIdByName(newBrand1);
					validate = true;
				}
			}
			if (validate) {
				if (newCategoryId == null || newCategoryId == 0L) {
					this.setMsg("Falta seleccionar una categoria para el Vehiculo.");
					return false;
				} else {
					if (vehicleInternalCode == null
							|| vehicleInternalCode.equals("")) {
						validate = true;
					} else {
						if (vehicleInternalCode.length() > 50) {
							validate = false;
						} else {
							validate = true;
						}
					}
					if (validate) {
						if (line == null || line.trim().equals("")) {
							this.setMsg("La línea es requerida.");
							return false;
						} else {
							if (!line
									.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
								this.setMsg("La línea tiene caracteres inválidos.");
								return false;
							} else {
								if (line.length() > 100) {
									this.setMsg("La línea debe tener menos de 100 caracteres.");
									return false;
								} else {
									if (aditional1 == null
											|| aditional1.equals("")) {
										validate = true;
									} else {
										if (!aditional1
												.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
											validate = false;
											this.setMsg("El adicional1 tiene caracteres inválidos.");
										} else {
											if (aditional1.length() > 100) {
												validate = false;
												this.setMsg("El adicional1 debe tener menos de 100 caracteres.");
											} else {
												validate = true;
											}
										}
									}
									if (validate) {
										if (aditional2 == null
												|| aditional2.equals("")) {
											validate = true;
										} else {
											if (!aditional2
													.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
												validate = false;
												this.setMsg("El adicional2 tiene caracteres inválidos.");
											} else {
												if (aditional2.length() > 100) {
													this.setMsg("El adicional2 debe tener menos de 100 caracteres.");
													validate = false;
												} else {
													validate = true;
												}
											}
										}
										if (validate) {
											if (aditional3 == null
													|| aditional3.equals("")) {
												validate = true;
											} else {
												if (!aditional3
														.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
													validate = false;
													this.setMsg("El adicional3 tiene caracteres inválidos.");
												} else {
													if (aditional3.length() > 100) {
														validate = false;
														this.setMsg("El adicional1 debe tener menos de 100 caracteres.");
													} else {
														validate = true;
													}
												}
											}
											if (validate) {
												if (especialForm) {
													if (observation == null
															|| observation
																	.trim()
																	.equals("")) {
														this.setMsg("El campo observación es"
																+ " requerido si selecciona la opción especial,"
																+ " por favor verifique.");
														return false;
													} else {
														if (observation
																.length() > 200) {
															this.setMsg("El campo observación "
																	+ "debe tener menos de 200 caracteres.");
															return false;
														} else {
															if (!observation
																	.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
																this.setMsg("La observación tiene caracteres inválidos.");
																return false;
															} else {
																return true;
															}
														}
													}
												} else {
													return true;
												}
											} else {
												return false;
											}
										} else {
											return false;
										}
									} else {
										return false;
									}
								}
							}
						}
					} else {
						this.setMsg("El código interno debe tener menos de 50 caracteres.");
						return false;
					}
				}
			} else {
				this.setMsg("La marca debe tener menos de 50 caracteres.");
				return false;
			}
		} else {
			this.setMsg("El chasis debe tener menos de 50 caracteres.");
			return false;
		}
	}

	public boolean validateField() {
		boolean validate = false;
		Long typeRelation = 0L;
		Long userIdVehicle = -1L;
		this.setInfo(false);
		if (vehiclePlate == null || vehiclePlate.trim().equals("")) {
			this.setMsg("La placa es requerida.");
			return false;
		} else {
			System.out.println("AdminVehicleAdmin.userId: " + userId);
			typeRelation = vehicleEJB.typeRelationTag(vehiclePlate
					.toUpperCase());
			System.out.println("validateField.typeRelation: " + typeRelation);
			userIdVehicle = vehicleEJB
					.getUserIdByVehicleId((vehicleEJB
							.getVehicleByPlate(vehiclePlate.toUpperCase()) == null ? -1L
							: vehicleEJB.getVehicleByPlate(
									vehiclePlate.toUpperCase()).getVehicleId()));
			System.out.println("validateField.userIdVehicle: " + userIdVehicle);
			if (especialForm) {
				if (vehiclePlate.length() > 12) {
					this.setMsg("La placa no puede ser mayor de "
							+ "12 (doce) caracteres.");
					return false;
				} else {
					if (!vehiclePlate.matches("([a-z]|[A-Z]|[0-9])+")) {
						this.setMsg("La placa debe estar compuesta letras y números. Verifique "
								+ "la placa digitada.");
						return false;
					} else {
						if ((vehicleEJB.plateExit(vehiclePlate.toUpperCase()))
								&& ((typeRelation == 1L)
										|| (typeRelation == 0L) || ((typeRelation == -1L) && (userId
										.equals(userIdVehicle))))) {
							this.setMsg("El vehículo "
									+ vehiclePlate.toUpperCase()
									+ " ya se encuentra registrado en el sistema.");
							return false;
						} else if (((vehicleEJB.plateExit(vehiclePlate
								.toUpperCase())) && (typeRelation == -1L))
								|| (vehicleEJB.plateExit(vehiclePlate
										.toUpperCase()) == false)) {
							if (vehicleEJB.isPlacaValida(vehiclePlate
									.toUpperCase())) {
								this.setMsg("Si la placa del vehículo a ingresar está compuesta por tres letras y "
										+ "tres números (AAA000) o por dos letras y cuatro números (AB1234), "
										+ "por favor no seleccione la casilla de verificación ESPECIAL e ingrese nuevamente "
										+ "la placa.");
								this.setInfo(true);
								return false;
							} else {
								System.out
										.println("Pasó validación placa especial");
							}
						}
					}
				}
			} else {
				if (vehicleEJB.isPlacaValida(vehiclePlate.toUpperCase())) {
					if ((vehicleEJB.plateExit(vehiclePlate.toUpperCase()))
							&& ((typeRelation == 1L) || (typeRelation == 0L) || ((typeRelation == -1L) && (userId
									.equals(userIdVehicle))))) {
						this.setMsg("El vehículo " + vehiclePlate.toUpperCase()
								+ " ya se encuentra registrado en el sistema.");
						return false;
					} else {
						System.out.println("Paso validación placa normal");
					}
				} else {
					this.setMsg("Si la placa del vehículo a ingresar no está compuesta por tres letras y "
							+ "tres números (AAA000) o por dos letras y cuatro números (AB1234), "
							+ "por favor de clic en la casilla de verificación ESPECIAL e ingrese nuevamente "
							+ "la placa, adicionalmente en el campo OBSERVACIÓN mencione las características "
							+ "del vehículo.");
					this.setInfo(true);
					return false;
				}
			}
			if (vehicleChassis == null || vehicleChassis.equals("")) {
				validate = true;
			} else {
				if (vehicleChassis.length() > 50) {
					validate = false;
				} else {
					validate = true;
				}
			}
			if (validate) {
				if (newBrand1 == null || newBrand1.equals("")) {
					validate = true;
					brandId = null;
				} else {
					if (newBrand1.length() > 50) {
						validate = false;
					} else {
						brandId = brandEJB.getBrandIdByName(newBrand1);
						validate = true;
					}
				}
				if (validate) {
					if (newCategoryId == null || newCategoryId == 0L) {
						this.setMsg("Falta seleccionar una categoria para el Vehiculo.");
						return false;
					} else {
						if (vehicleInternalCode == null
								|| vehicleInternalCode.equals("")) {
							validate = true;
						} else {
							if (vehicleInternalCode.length() > 50) {
								validate = false;
							} else {
								validate = true;
							}
						}
						if (validate) {
							if (line == null || line.trim().equals("")) {
								this.setMsg("La línea es requerida.");
								return false;
							} else {
								if (!line
										.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
									this.setMsg("La línea tiene caracteres inválidos.");
									return false;
								} else {
									if (line.length() > 100) {
										this.setMsg("La línea debe tener menos de 100 caracteres.");
										return false;
									} else {
										if (aditional1 == null
												|| aditional1.equals("")) {
											validate = true;
										} else {
											if (!aditional1
													.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
												validate = false;
												this.setMsg("El adicional1 tiene caracteres inválidos.");
											} else {
												if (aditional1.length() > 100) {
													validate = false;
													this.setMsg("El adicional1 debe tener menos de 100 caracteres.");
												} else {
													validate = true;
												}
											}
										}
										if (validate) {
											if (aditional2 == null
													|| aditional2.equals("")) {
												validate = true;
											} else {
												if (!aditional2
														.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
													validate = false;
													this.setMsg("El adicional2 tiene caracteres inválidos.");
												} else {
													if (aditional2.length() > 100) {
														this.setMsg("El adicional2 debe tener menos de 100 caracteres.");
														validate = false;
													} else {
														validate = true;
													}
												}
											}
											if (validate) {
												if (aditional3 == null
														|| aditional3
																.equals("")) {
													validate = true;
												} else {
													if (!aditional3
															.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
														validate = false;
														this.setMsg("El adicional3 tiene caracteres inválidos.");
													} else {
														if (aditional3.length() > 100) {
															validate = false;
															this.setMsg("El adicional1 debe tener menos de 100 caracteres.");
														} else {
															validate = true;
														}
													}
												}
												if (validate) {
													if (especialForm) {
														if (observation == null
																|| observation
																		.trim()
																		.equals("")) {
															this.setMsg("El campo observación es"
																	+ " requerido si selecciona la opción especial,"
																	+ " por favor verifique.");
															return false;
														} else {
															if (observation
																	.length() > 200) {
																this.setMsg("El campo observación "
																		+ "debe tener menos de 200 caracteres.");
																return false;
															} else {
																if (!observation
																		.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")) {
																	this.setMsg("La observación tiene caracteres inválidos.");
																	return false;
																} else {
																	return true;
																}
															}
														}
													} else {
														return true;
													}
												} else {
													return false;
												}
											} else {
												return false;
											}
										} else {
											return false;
										}
									}
								}
							}
						} else {
							this.setMsg("El código interno debe tener menos de 50 caracteres.");
							return false;
						}
					}
				} else {
					this.setMsg("La marca debe tener menos de 50 caracteres.");
					return false;
				}
			} else {
				this.setMsg("El chasis debe tener menos de 50 caracteres.");
				return false;
			}
		}
	}

	public String storeNew() {
		System.out.println("Storing new vehicle");
		Long result = 0L;
		String mensaje = "";
		boolean exist = false;
		boolean corr=true;
		try {
			System.out.println("AVD.userId:" + userId);
			if (userId.equals(-1L)) {
				mensaje = "Ocurrió un error al tratar de guardar el vehículo, "
						+ "por favor inténtelo más tarde.";
			} else {
				if (vehicleEJB.plateExit(vehiclePlate.toUpperCase())) {
					exist = true;
					if(vehicleEJB.getLastInactiveRelationTag(vehicleEJB.getVehicleByPlate(vehiclePlate.toUpperCase())
							.getVehicleId())!=-1L){
						result = vehicleEJB.reasignVehicle(
								SessionUtil.sessionUser().getUserId(),
								SessionUtil.ip(),
								vehicleEJB.getVehicleByPlate(
										vehiclePlate.toUpperCase()).getVehicleId(),
								userId);
					}else{
						corr=false;
					}
					
				} else {
					exist = false;
					result = vehicleEJB.createVehicle(SessionUtil.sessionUser()
							.getUserId(), SessionUtil.ip(), vehiclePlate,
							vehicleChassis, vehicleInternalCode, newColorId,
							brandId, newCategoryId, aditional1, aditional2,
							aditional3, line, vehicleBeginingDate, observation,
							userId);
				}
				
				if(corr!=false){
				if (result == -1L) {
					mensaje = "El vehículo " + vehiclePlate.toUpperCase()
							+ " ya se encuentra registrado en el sistema.";
					System.out.println("El vehículo con placa "
							+ vehiclePlate.toUpperCase()
							+ " ya existe en la base de datos");
				} else if (result == 0L) {
					mensaje = "Ocurrió un error al momento de crear el vehículo.";
					System.out.println("Ocurrio un error al momento "
							+ "de crear el vehículo");
				} else {
					if (exist) {
						boolean result1 = vehicleEJB.alterVehicle(
								SessionUtil.sessionUser().getUserId(),
								SessionUtil.ip(),
								vehicleEJB.getVehicleByPlate(
										vehiclePlate.toUpperCase())
										.getVehicleId(), vehicleChassis,
								vehicleInternalCode, newColorId, brandId,
								newCategoryId, aditional1, aditional2,
								aditional3, line, vehicleBeginingDate,
								observation, especialForm, userId);
						System.out
								.println("Actualización información - AdminVehicleAdmin: "
										+ result1);
					}

					String messDetail = "Se creó en el sistema, 1 vehículo de forma manual, "
							+ "placa " + vehiclePlate.toUpperCase() + ".";
					vehicleEJB.createProcessForVehicles(SessionUtil
							.sessionUser().getUserId(), SessionUtil.ip(),
							messDetail, "Error Crear Vehículo", userId, 0L);

					mensaje = "El vehículo ha sido creado con éxito.";
				}
				//}
				}else{
					mensaje = "Ocurrió un error al tratar de guardar el vehículo, "
									+ "por favor inténtelo más tarde.";
				}
			}
			hideModalAll();
			clearFilter();
			System.out.println("the result:" + result);
			this.setMsg(mensaje);
			this.setShowModal(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error AdminVehicle.storeNew ] ");
		}
		return null;
	}

	public String showStoreNew() {
		System.out.println("vehPlateC:" + vehiclePlate);
		System.out.println("user_id:" + userId);
		if (validateField()) {
			clear();

			try {

				if (userId.equals(-1L)) {
					this.setMsg("Ocurrió un error al tratar de guardar el vehículo, "
							+ "por favor inténtelo más tarde.");
					this.setShowModalInfo(false);
					this.setShowModal(true);
				} else {
					if (vehicleEJB.plateExit(vehiclePlate.toUpperCase())) {
						Long rad = vehicleEJB
								.getLastInactiveRelationTag(vehicleEJB
										.getVehicleByPlate(
												vehiclePlate.toUpperCase())
										.getVehicleId());
						if (rad == -1L) {
							this.setMsg("Ocurrió un error al tratar de guardar el vehículo, "
									+ "por favor inténtelo más tarde.");
							this.setShowModalInfo(false);
							this.setShowModal(true);
						} else {
							String names = "";
							if (deviceEJB.getReAccountDeviceById(rad)
									.getTbAccount().getTbSystemUser()
									.getTbCodeType().getCodeTypeId() == 1L) {
								names = deviceEJB.getReAccountDeviceById(rad)
										.getTbAccount().getTbSystemUser()
										.getUserNames()
										+ " "
										+ deviceEJB.getReAccountDeviceById(rad)
												.getTbAccount()
												.getTbSystemUser()
												.getUserSecondNames();
							} else {
								names = deviceEJB.getReAccountDeviceById(rad)
										.getTbAccount().getTbSystemUser()
										.getUserNames();
							}
							this.setMsg("El vehículo de placas "
									+ vehiclePlate.toUpperCase()
									+ " se encuentra "
									+ "con una relación inactiva con el dipositivo "
									+ deviceEJB.getReAccountDeviceById(rad)
											.getTbDevice().getDeviceFacialId()
									+ " de la cuenta FacilPass "
									+ deviceEJB.getReAccountDeviceById(rad)
											.getTbAccount().getAccountId()
									+ " del cliente "
									+ names
									+ " con número de identificación "
									+ deviceEJB.getReAccountDeviceById(rad)
											.getTbAccount().getTbSystemUser()
											.getUserCode() + ".");
							this.setShowModalConC(true);
						}
					} else {
						this.setMsg("¿Está seguro de crear el vehículo con placa "
								+ vehiclePlate.toUpperCase() + "?");
						this.setShowModalConC(true);
					}
				}
			} catch (Exception e) {
				this.setMsg("Error, " + "por favor inténtelo más tarde.");

				e.getStackTrace();
			}
		} else {
			if (info) {
				this.setShowModal(false);
				this.setShowModalInfo(true);
			} else {
				this.setShowModalInfo(false);
				this.setShowModal(true);
			}
		}
		return null;
	}

	public String deleteVehicle() {
		System.out.println("AdminVehicleAdmin.deleteVehicle");
		System.out.println("vehid3:" + vehicleID);
		userId = vehicleEJB.getUserIdByVehicleId(vehicleID);
		System.out.println("DV.userId:" + userId);
		boolean resul = false;
		String mensaje = "";
		if (userId.equals(-1L)) {
			mensaje = "Ocurrió un error al tratar de eliminar el vehículo, "
					+ "por favor inténtelo más tarde.";
		} else {
			resul = vehicleEJB.deleteVehicle(SessionUtil.sessionUser()
					.getUserId(), SessionUtil.ip(), vehiclePlate, userId);
			if (resul) {
				mensaje = "Se eliminó correctamente el vehículo con placa "
						+ vehiclePlate.toUpperCase() + ".";
				String messDetail = "Se eliminó correctamente el vehículo con placa "
						+ vehiclePlate.toUpperCase() + ".";

				vehicleEJB.createProcessForVehicles(SessionUtil.sessionUser()
						.getUserId(), SessionUtil.ip(), messDetail,
						"Error Eliminar Vehículo", userId, 0L);

			} else {
				if (vehicleEJB.haveRelationTag(vehicleID)) {
					mensaje = "No se pudo eliminar el vehículo con placa "
							+ vehiclePlate.toUpperCase()
							+ ", tiene tags registrados.";
				} else {
					mensaje = "No se pudo eliminar el vehículo con placa "
							+ vehiclePlate.toUpperCase() + ".";
				}
			}
		}
		hideModalAll();
		clearFilter();
		this.setMsg(mensaje);
		this.setShowModal(true);
		return null;
	}

	public String showDeleteVehicle() {
		hideModalAll();
		vehiclePlate = vehicleEJB.getVehicle(vehicleID).getVehiclePlate();
		System.out.println("vehPlateD:" + vehiclePlate);
		this.setMsg("¿Está seguro de eliminar el vehículo con placa "
				+ vehiclePlate.toUpperCase() + "?");
		this.setShowModalConD(true);
		return null;
	}

	public String showModalEdit() {
		hideModalAll();
		System.out.println("vehidM:" + vehicleID);
		TbVehicle ve = vehicleEJB.getVehicle(vehicleID);
		if (ve != null) {
			this.setAditional1(ve.getAditional1() == null ? "" : ve
					.getAditional1());
			this.setAditional2(ve.getAditional2() == null ? "" : ve
					.getAditional2());
			this.setAditional3(ve.getAditional3() == null ? "" : ve
					.getAditional3());
			this.setLine(ve.getLine() == null ? "" : ve.getLine());
			this.setNewBrand1(ve.getTbBrand() == null ? "" : ve.getTbBrand()
					.getBrandName());
			this.setNewCategoryId(ve.getTbCategory().getCategoryId());
			this.setNewCatId(0L);
			this.setNewColorId(ve.getTbColor().getColorId());
			this.setBrandId(ve.getTbBrand() == null ? null : ve.getTbBrand()
					.getBrandId());
			this.setVehicleBeginingDate(ve.getVehicleBeginingDate());
			this.setVehicleChassis(ve.getVehicleChassis() == null ? "" : ve
					.getVehicleChassis());
			this.setVehicleInternalCode(ve.getVehicleInternalCode() == null ? ""
					: ve.getVehicleInternalCode());
			this.setVehiclePlate(ve.getVehiclePlate() == null ? "" : ve
					.getVehiclePlate());
			this.setObservation(ve.getVehicleObservation() == null ? "" : ve
					.getVehicleObservation());
			if (ve.getVehicleEspecialPlate() == null
					|| ve.getVehicleEspecialPlate() == 0L) {
				this.setEspecialForm(false);
				this.setShowObservation(false);
			} else if (ve.getVehicleEspecialPlate() == 1L) {
				this.setEspecialForm(true);
				this.setShowObservation(true);
			}
			this.setShowEdit(true);
		} else {
			this.setMsg("Ocurrió un error al momento de modificar el vehículo.");
			this.setShowModal(true);
		}
		return null;
	}

	public String storeEdited() {
		System.out.println("AdminVehicleAdmin.sotreEdited");
		System.out.println("vehid2:" + vehicleID);
		System.out.println("palte2:" + vehiclePlate);
		String mensaje = "";
		boolean result = false;
		try {
			userId = vehicleEJB.getUserIdByVehicleId(vehicleID);
			System.out.println("ModV.userId:" + userId);
			if (userId.equals(-1L)) {
				mensaje = "Ocurrió un error al tratar de modificar el vehículo, "
						+ "por favor inténtelo más tarde.";
			} else {
				result = vehicleEJB.alterVehicle(SessionUtil.sessionUser()
						.getUserId(), SessionUtil.ip(), vehicleID,
						vehicleChassis, vehicleInternalCode, newColorId,
						brandId, newCategoryId, aditional1, aditional2,
						aditional3, line, vehicleBeginingDate, observation,
						especialForm, userId);
				if (result) {
					mensaje = "El vehículo con la Placa "
							+ vehiclePlate.toUpperCase()
							+ " se ha modificado correctamente.";
					System.out.println("El vehículo con la Placa "
							+ vehiclePlate.toUpperCase()
							+ " se ha modificado correctamente.");

					String messDetail = "Se modificó correctamente el vehículo con placa "
							+ vehiclePlate + ".";

					vehicleEJB.createProcessForVehicles(SessionUtil
							.sessionUser().getUserId(), SessionUtil.ip(),
							messDetail, "Error Modificar Vehículo", userId, 0L);
				} else {
					mensaje = "Ocurrió un error al momento de modificar el vehículo.";
					System.out
							.println("Ocurrio un error al momento de modificar el vehículo.");
				}
			}
			hideModalAll();
			clearFilter();
			System.out.println("the result:" + result);
			this.setMsg(mensaje);
			this.setShowModal(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error AdminVehicle.storeEdited ] ");
		}
		return null;
	}

	public String showStoreEdited() {
		System.out.println("vehPlateM:" + vehiclePlate);
		if (validateModifiedField()) {
			clear();
			this.setMsg("¿Está seguro de modificar el vehículo con placa "
					+ vehiclePlate.toUpperCase() + "?");
			this.setShowModalConM(true);
		} else {
			this.setShowModal(true);
		}
		return null;
	}

	public List<Vehicles> getListVehicles() {
		if(listVehicles == null) {
			listVehicles = new ArrayList<Vehicles>();			
	    }
			this.getDataForScroll();
			listVehicles = vehicleEJB.getUserVehiclesByFilters(codeType, numberDoc,
					firstName, lastName, categoryType, placaFil, ad1Fil, ad2Fil, ad3Fil, 0L, page, 15);			
		return listVehicles;
	}
	
	
	public void getDataForScroll(){
		try {
			this.setValuesFor(Integer.parseInt(String.valueOf(vehicleEJB.getUserVehiclesByFilters(codeType, numberDoc,
					firstName, lastName, categoryType, placaFil, ad1Fil, ad2Fil, ad3Fil, 0L, 0, 15).get(0))));
			listaScroll=new ArrayList<Integer>();
			for(int i=0;i<getValuesFor();i++){	
				listaScroll.add(i);
			}
		} catch (Exception e) {
	     	e.printStackTrace();
		}
	}
	
	public void dataScroller(ActionEvent event)throws AbortProcessingException {
		DataScrollerEvent events=(DataScrollerEvent)event;
		page = events.getPage();
		setPage(1);
	}

	public void setNewColorId(Long newColorId) {
		this.newColorId = newColorId;
	}

	public Long getNewColorId() {
		return newColorId;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModal() {
		return showModal;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setShowNew(boolean showNew) {
		this.showNew = showNew;
	}

	public boolean isShowNew() {
		return showNew;
	}

	public void addVehicle() {
		hideModalAll();
		userId = userIdCreate;
		System.out.println("Addvehicle Userid:" + userId);
		this.setShowNew(true);
	}

	public void hideModalAdd() {
		showNew = false;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(Long vehicleID) {
		this.vehicleID = vehicleID;
	}

	public boolean isShowEdit() {
		return showEdit;
	}

	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	public Long getNewCatId() {
		return newCatId;
	}

	public void setNewCatId(Long newCatId) {
		this.newCatId = newCatId;
	}

	public String getVehiclePlate() {
		return vehiclePlate;
	}

	public void setVehiclePlate(String vehiclePlate) {
		this.vehiclePlate = vehiclePlate;
	}

	public String getNewBrand1() {
		return newBrand1;
	}

	public void setNewBrand1(String newBrand1) {
		this.newBrand1 = newBrand1;
	}

	public String getVehicleInternalCode() {
		return vehicleInternalCode;
	}

	public void setVehicleInternalCode(String vehicleInternalCode) {
		this.vehicleInternalCode = vehicleInternalCode;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public Timestamp getVehicleBeginingDate() {
		return vehicleBeginingDate;
	}

	public void setVehicleBeginingDate(Timestamp vehicleBeginingDate) {
		this.vehicleBeginingDate = vehicleBeginingDate;
	}

	public String getAditional1() {
		return aditional1;
	}

	public void setAditional1(String aditional1) {
		this.aditional1 = aditional1;
	}

	public String getAditional2() {
		return aditional2;
	}

	public void setAditional2(String aditional2) {
		this.aditional2 = aditional2;
	}

	public String getAditional3() {
		return aditional3;
	}

	public void setAditional3(String aditional3) {
		this.aditional3 = aditional3;
	}

	public UserLogged getUsrs() {
		return usrs;
	}

	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}

	public Long getNewCategoryId() {
		return newCategoryId;
	}

	public void setNewCategoryId(Long newCategoryId) {
		this.newCategoryId = newCategoryId;
	}

	public String getVehicleChassis() {
		return vehicleChassis;
	}

	public void setVehicleChassis(String vehicleChassis) {
		this.vehicleChassis = vehicleChassis;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public boolean isShowModalConD() {
		return showModalConD;
	}

	public void setShowModalConD(boolean showModalConD) {
		this.showModalConD = showModalConD;
	}

	public boolean isShowModalConC() {
		return showModalConC;
	}

	public void setShowModalConC(boolean showModalConC) {
		this.showModalConC = showModalConC;
	}

	public boolean isShowModalConM() {
		return showModalConM;
	}

	public void setShowModalConM(boolean showModalConM) {
		this.showModalConM = showModalConM;
	}

	public Long getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(Long categoryType) {
		this.categoryType = categoryType;
	}

	public String getPlacaFil() {
		return placaFil;
	}

	public void setPlacaFil(String placaFil) {
		this.placaFil = placaFil;
	}

	public String getAd1Fil() {
		return ad1Fil;
	}

	public void setAd1Fil(String ad1Fil) {
		this.ad1Fil = ad1Fil;
	}

	public String getAd2Fil() {
		return ad2Fil;
	}

	public void setAd2Fil(String ad2Fil) {
		this.ad2Fil = ad2Fil;
	}

	public String getAd3Fil() {
		return ad3Fil;
	}

	public void setAd3Fil(String ad3Fil) {
		this.ad3Fil = ad3Fil;
	}

	public void setListVehicles(List<Vehicles> listVehicles) {
		this.listVehicles = listVehicles;
	}

	public List<SelectItem> getListCategories() {
		List<TbCategory> categories = categoryEJB.getCategories();
		List<SelectItem> list = new ArrayList<SelectItem>();
		list.add(new SelectItem(-1L, "-- Seleccione Uno --"));
		for (TbCategory cat : categories) {
			list.add(new SelectItem(cat.getCategoryId(), cat.getCategoryName()));
		}
		return list;
	}

	public boolean isShowObservation() {
		return showObservation;
	}

	public void setShowObservation(boolean showObservation) {
		this.showObservation = showObservation;
	}

	public boolean isEspecialForm() {
		return especialForm;
	}

	public void setEspecialForm(boolean especialForm) {
		this.especialForm = especialForm;
	}

	public Long getPlateLength() {
		return plateLength;
	}

	public void setPlateLength(Long plateLength) {
		this.plateLength = plateLength;
	}

	public void setCodeTypesList(List<SelectItem> codeTypesList) {
		this.codeTypesList = codeTypesList;
	}

	public List<SelectItem> getCodeTypesList() {
		if (codeTypesList == null) {
			codeTypesList = new ArrayList<SelectItem>();
		} else {
			codeTypesList.clear();
		}
		codeTypesList.add(new SelectItem(-1L, "-- Seleccione Uno --"));
		for (TbCodeType c : userEJB.getCodeTypes()) {
			codeTypesList.add(new SelectItem(c.getCodeTypeId(), c
					.getCodeTypeAbbreviation().toUpperCase()));
		}
		return codeTypesList;
	}

	public Long getCodeType() {
		return codeType;
	}

	public void setCodeType(Long codeType) {
		this.codeType = codeType;
	}

	public String getNumberDoc() {
		return numberDoc;
	}

	public void setNumberDoc(String numberDoc) {
		this.numberDoc = numberDoc;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isShowCreate() {
		return showCreate;
	}

	public void setShowCreate(boolean showCreate) {
		this.showCreate = showCreate;
	}

	public Long getUserIdCreate() {
		return userIdCreate;
	}

	public void setUserIdCreate(Long userIdCreate) {
		this.userIdCreate = userIdCreate;
	}

	public boolean isShowModalInfo() {
		return showModalInfo;
	}

	public void setShowModalInfo(boolean showModalInfo) {
		this.showModalInfo = showModalInfo;
	}

	public boolean isInfo() {
		return info;
	}

	public void setInfo(boolean info) {
		this.info = info;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}

	public void setValuesFor(int valuesFor) {
		this.valuesFor = valuesFor;
	}

	public int getValuesFor() {
		return valuesFor;
	}
	
	public void setListaScroll(List<Integer> listaScroll) {
		this.listaScroll = listaScroll;
	}

	public List<Integer> getListaScroll() {
		return listaScroll;
	}

}