package ejb;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.swing.table.TableModel;

import jpa.ReAccountDevice;
import jpa.ReUserVehicle;
import jpa.TbBrand;
import jpa.TbCategory;
import jpa.TbColor;
import jpa.TbProcessTrack;
import jpa.TbSystemUser;
import jpa.TbTag;
import jpa.TbVehicle;

import org.hibernate.exception.ConstraintViolationException;

import util.ParserReadFiles;
import util.Vehicles;
import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import crud.ObjectEM;
import ejb.email.Outbox;

/**
 * Bean implementation of Vehicle
 * @author Mauricio Gil
 */
@Stateless(mappedName = "ejb/Vehicle")
public class VehicleEJB implements Vehicle {

	@PersistenceContext(unitName = "bo")
	EntityManager em;
	private Query allVehicles;
	private Query vehiclesByPlate;
	

	@EJB(mappedName = "ejb/Log")
	private Log log;

	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName = "ejb/Device")
	private Device deviceEJB;
	
	public VehicleEJB() {
	}

	/**
	 * Performs initialization to internal queries
	 */
	@PostConstruct	
	public void init() {
		allVehicles = em.createQuery("FROM TbVehicle x WHERE x.vehicleDeleted = 0 ORDER BY x.vehiclePlate");
		vehiclesByPlate = em.createNativeQuery(
				"select * from TB_VEHICLE where VEHICLE_PLATE like :plate",
				TbVehicle.class);	
	}

	/**
	 * Retrieves a vehicle object from database
	 * @param id Vehicle identifier
	 * @return Vehicle object
	 */
	public TbVehicle getVehicle(long id) {
		TbVehicle vehicle = em.find(TbVehicle.class, id);
		return vehicle;
	}

	/**
	 * Removes a vehicle from database
	 * @param vehicle Vehicle object
	 */
	public boolean removeVehicle(TbVehicle vehicle) {
		return new ObjectEM(em).delete(vehicle);
	}

	/**
	 * Inserts a new vehicle object in database
	 * @param vehicle Vehicle object
	 */
	public boolean  addVehicle(TbVehicle vehicle) {
		try{
			boolean b=new ObjectEM(em).create(vehicle);
			return b;
		}catch (ConstraintViolationException v){
			return false;
		}
	}

	/**
	 * Changes a vehicle in database
	 * @param vehicle Vehicle database
	 */
	public boolean updateVehicle(TbVehicle vehicle) {
		return new ObjectEM(em).update(vehicle);
	}

	/**
	 * Retrieves all the available vehicle objects from database
	 * @return List of vehicles
	 */
	@SuppressWarnings("unchecked")
	public List<TbVehicle> getVehicles() {
		return allVehicles.getResultList();
	}

	/**
	 * Retrieves a vehicle list according to plate
	 * @param plate Plate initials or full plate for one object
	 * @return List of vehicles
	 */
	@SuppressWarnings("unchecked")
	public List<TbVehicle> getVehiclesByPlate(String plate) {
		vehiclesByPlate.setParameter("plate", plate+"%");
		return vehiclesByPlate.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @author jromero
	 * @see ejb.Vehicle#createVehicle(java.lang.Long, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	public Long createVehicle(Long creationUser, String ip, 
			String vehiclePlate, String vehicleChassis,
			String vehicleInternalCode, Long colorId, Long brandId,
			Long categoryId,String aditional1,String aditional2,
			String aditional3,String line,Timestamp model,String observ,Long userId) {
		try {	
			Query q = em.createQuery("SELECT ve FROM TbVehicle ve WHERE ve.vehiclePlate = ?1 AND ve.vehicleDeleted = 0");
			q.setParameter(1, vehiclePlate.toUpperCase());

			q.getSingleResult();
			return -1L;

		}catch (NoResultException e) {
			
			Query e1 = em.createNativeQuery("select count(*) from tb_vehicle where VEHICLE_PLATE=?1 and vehicle_deleted = 1");
			e1.setParameter(1, vehiclePlate.toUpperCase());
			
			Long exits=((BigDecimal) e1.getSingleResult()).longValue();
			
			if(observ!=null&&observ!=""){
				observ=replaceGuion(observ);
			}
			if(line!=null&&line!=""){
				line=replaceGuion(line);
			}
			if(aditional1!=null&&aditional1!=""){
				aditional1=replaceGuion(aditional1);
			}
			if(aditional2!=null&&aditional2!=""){
				aditional2=replaceGuion(aditional2);
			}
			if(aditional3!=null&&aditional3!=""){
				aditional3=replaceGuion(aditional3);
			}
			
			if(exits<=0){
				System.out.println("No existe vehiculo: "+vehiclePlate);
				
				TbVehicle v = new TbVehicle();
				v.setTbBrand(brandId==null?null:(em.find(TbBrand.class, brandId)));
				v.setTbCategory(em.find(TbCategory.class, categoryId));
				v.setTbColor(em.find(TbColor.class, colorId));
				v.setVehicleBeginingDate(model);
				v.setVehicleChassis(vehicleChassis);
				v.setVehicleInternalCode(vehicleInternalCode);
				v.setVehicleObservation(observ);
				v.setVehiclePlate(vehiclePlate.toUpperCase());
				v.setAditional1(aditional1);
				v.setAditional2(aditional2);
				v.setAditional3(aditional3);
				v.setLine(line);
				v.setVehicleRegistrationDate(new Timestamp(System.currentTimeMillis()));
				if(this.isEspecialPlate(vehiclePlate.toUpperCase())){
					v.setVehicleEspecialPlate(1L);
				}else{
					v.setVehicleEspecialPlate(0L);
				}
				v.setVehicleDeleted(0L);
				em.persist(v);

				TbSystemUser u=new TbSystemUser();
				u=em.find(TbSystemUser.class, userId);

				ReUserVehicle uv=new ReUserVehicle();
				uv.setDateAssociation(new Timestamp(System.currentTimeMillis()));
				uv.setStateRelation(0);
				uv.setTbSystemUser(u);
				uv.setTbVehicle(v);
				em.persist(uv);

				em.flush();

				log.insertLog(" Creación De Vehículo | Se ha Creado un Vehículo ID: " + v.getVehicleId() + ".", LogReference.VEHICLE, LogAction.CREATE, ip, creationUser);
				return  v.getVehicleId();
			}else{
				System.out.println("Se encontraba eliminado el vehiculo: "+vehiclePlate);
				TbVehicle v = this.getVehicleByPlate(vehiclePlate.toUpperCase());
				if(this.createDeletedVehicle(creationUser, ip, v.getVehicleId(), vehicleChassis,
						vehicleInternalCode, colorId, brandId, categoryId, aditional1,
						aditional2, aditional3, line, model, observ, userId)){
					return v.getVehicleId();
				}else{
					return 0L;
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en VehicleEJB.createVehicle ] ");
			e.printStackTrace();
			return 0L;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Vehicle#getVehicleByPlate(java.lang.String)
	 */
	@Override
	public TbVehicle getVehicleByPlate(String plate) {
		try {
			Query q = em.createQuery("SELECT ve FROM TbVehicle ve WHERE ve.vehiclePlate = ?1");
			q.setParameter(1, plate.toUpperCase());
			return (TbVehicle) q.getSingleResult();
		} catch (NoResultException e) {
		} catch (Exception e) {
			System.out.println(" [ Error en VehicleEJB.getVehicleByPlate ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Vehicle#getVehicleByClient(java.lang.Long)
	 */
	@Override
	public List<TbVehicle> getVehicleByClient(Long idClient) {
		List<TbVehicle> list = new ArrayList<TbVehicle>();
		try {
			Query q = em.createNativeQuery("	select tv.* " +
					"from tb_vehicle tv " + 
					"inner join re_account_device ad ON ad.vehicle_id = tv.vehicle_id " + 
					"inner join tb_account ta ON ta.account_id = ad.account_id " +
					"where ta.user_id = ?1 and ad.relation_state=0", TbVehicle.class); 
			q.setParameter(1, idClient);
			for(Object ob :q.getResultList()){
				list.add((TbVehicle) ob);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en VehicleEJB.getVehicleByClient ] ");
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public List<TbVehicle> getVehicleByAccount(Long userId, Long accountId) {
		List<TbVehicle> list = new ArrayList<TbVehicle>();
		try {
			Query q = em.createNativeQuery("select tv.* " +
					"from tb_vehicle tv " + 
					"inner join re_account_device ad ON ad.vehicle_id = tv.vehicle_id " + 
					"inner join tb_account ta ON ta.account_id = ad.account_id " +
					"where ta.user_id =?1 and ta.account_id=?2 and ad.relation_state=0", TbVehicle.class); 
			q.setParameter(1, userId);
			q.setParameter(2, accountId);
			for(Object ob :q.getResultList()){
				list.add((TbVehicle) ob);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en VehicleEJB.getVehicleByAccount ] ");
			e.printStackTrace();
		}
		return list;
	}
	/** 
	 * @author jgomez 
	 * @return Lista de vehiculos disponibles para realizar la asignacion.
	 */
	@SuppressWarnings("unchecked")	
	public List<TbVehicle> getVehicleNoRegister(){
		System.out.println("Se ingresa a Consultar los vehiculos");
		List<TbVehicle> listVehicle = new ArrayList<TbVehicle>();
		try {
			
			Query vehicle = em.createNativeQuery(
					"select * from tb_vehicle where vehicle_deleted = 0 " +
					"and vehicle_id not in (select vehicle_id from re_account_device " +
					"where (vehicle_id is not null) and relation_state=0)",
					TbVehicle.class);			
			listVehicle = vehicle.getResultList();
		} catch (Exception e) {
			System.out.println("Erro al Consultar los vehiculos");
			e.printStackTrace();
			listVehicle = null;
		}
		return listVehicle;
	}


	@Override
	public ReAccountDevice getAccountByPlate(String plate){
		Long id = null;
		ReAccountDevice rel2 = null;
		try{
			Query q= em.createNativeQuery("select rad.account_device_id from re_account_device rad "
					+ " inner join tb_vehicle tv on rad.vehicle_id=tv.vehicle_id "
					+ " inner join tb_account ta on rad.account_id=ta.account_id "
					+ " where tv.vehicle_plate=?1 and rad.relation_state=0");
			q.setParameter(1,plate);

			BigDecimal cuenta=(BigDecimal) q.getSingleResult();

			if(cuenta!=null){
				id=cuenta.longValue();
				rel2= em.find(ReAccountDevice.class, id);
			}
		}catch(NoResultException ex){
			System.out.println("No se encontro cuenta para la placa" + plate);
		}
		return rel2;
	}

	@Override
	public boolean plateExit(String plate) {
		try{
			Query v = em.createNativeQuery("select count(*) from tb_vehicle where VEHICLE_PLATE=?1 and  vehicle_deleted = 0");
			v.setParameter(1, plate);

			if(v.getSingleResult() != null){
				int cant = ((BigDecimal) v.getSingleResult()).intValue();
				if(cant > 0){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}catch(NoResultException ex){
			System.out.println("No se encontró la placa" + plate);
			return false;
		}
	}
	/**
	 * @author jromero
	 */
	@Override
	public TableModel loadFileVehicles(File file) {
		String name="";
		String ext="";
		try{
			TableModel result=null;
			name=file.getName();
			ext=name.substring(name.length()-3);
			System.out.println("Name:"+name);
			if(ext.toUpperCase().equals("LSX")){
				//EXCEL 2007 EN ADELANTE
				result=ParserReadFiles.parseXLSX(file);
			}else if(ext.toUpperCase().equals("XLS")){
				//EXCEL ANTES DEL 2007
				result=ParserReadFiles.parseXLS(file);
			}else if(ext.toUpperCase().equals("TXT")){
				result=ParserReadFiles.parseTXT(file);
			}else if(ext.toUpperCase().equals("CSV")){
				result=ParserReadFiles.parseCSV(file,",");
				if(result.getColumnCount()==1){
					System.out.println("Delimitador CSV Punto y Coma");
					result=null;
					result=ParserReadFiles.parseCSV(file,";");
				}
			}else{
				result=null;
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("[ Error VehicleEJB.loadFileVehicles ]");
			return null;
		}
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public Long createMassiveVehicles(Long creationUser,String ip,TableModel t,
			String fileName,Long userId,Long type) {
		Long result=-1L;
		String adi1="";
		String adi2="";
		String adi3="";
		String line="";
		String plate="";
		String obser="";
		String messPlate="";
		String sumary="";
		String messDetail="";
		Long catId=0L;
		boolean saveReg=false;
		boolean isEspecial=false;
		Integer savedReg=0;
		for (int x = 0; x < t.getRowCount(); x++) {
			if(t.getValueAt(x, 6)!=null&&(((String)t.getValueAt(x, 6))
					.toUpperCase().trim().equals("SI"))){
				isEspecial=true;
			}else{
				isEspecial=false;
			}
			for (int y = 0; y < t.getColumnCount(); y++) {
				if(y==0){
					saveReg=true;
					plate=(String)t.getValueAt(x, y);
				}
				if(y==1){
					adi1=(String)t.getValueAt(x, y);
				}
				if(y==2){
					adi2=(String)t.getValueAt(x, y);
				}
				if(y==3){
					adi3=(String)t.getValueAt(x, y);
				}
				if(y==4){
					try{
						catId=Long.parseLong((String)t.getValueAt(x, y));
					}catch (Exception e) {
						catId=0L;
					}
				}
				if(y==5){
					line=(String)t.getValueAt(x, y);
				}
				if(y==7){
					obser=(String)t.getValueAt(x, y);
				}
				if(validateMassiveLoad(x,y,(String)t.getValueAt(x, y),
						fileName,userId,isEspecial)){
					if(saveReg){
						saveReg=true;
					}else{
						saveReg=false;
					}
				}else{
					saveReg=false;
				}
			}
			if(saveReg){
				try{
					//String bomUtf="ï»¿";
					plate=new String(plate.getBytes("UTF8"));
					System.out.println("PlacaCreation: "+plate);
					/*if(bomUtf.equals(plate.substring(0, 3))){
						System.out.println("IsBOMUTF-8Creation: "+plate);
						plate=plate.replaceFirst(bomUtf, "");
						System.out.println("NewPlateCreation: "+plate);
					}*/
					Long idVeh=this.createVehicle(creationUser, ip,
							plate, null, null, 
							0L, null, catId,adi1,adi2,adi3,line,null,obser, userId);
					if(idVeh==-1L){
						System.out.println("El vehículo con placa "+plate+
						" ya existe en la base de datos");
					}else if(idVeh==0L){
						System.out.println("Ocurrio un error al momento " +
						"de crear el vehículo");
					}else{
						messPlate=plate.toUpperCase();
						savedReg=savedReg+1;
						System.out.println("Se creó el vehículo con placa: "+plate.toUpperCase());
						sumary="PLACA: "+plate.toUpperCase()+", ADICIONAL1: "+
						(adi1==null?"":adi1)+", ADICIONAL2: "+(adi2==null?"":adi2)+
						", ADICIONAL3: "+(adi3==null?"":adi3)+", CATEGORÍA: "+
						catId+" y LÍNEA: "+(line==null?"":line);
						log.insertLogVehicle(fileName, 2L, String.valueOf(x+1),
								"REGISTRO CORRECTO",sumary,"N/A",
								"N/A", userId,0L);
					}
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("[ Error VehicleEJB.createMassiveVehicles ]");
				}
			}
		}
		if(savedReg==0){
			result=0L;
		}else{
			result=(long)savedReg;

			if(result>1){
				messDetail="Se creó en el sistema, "+result+" vehículo(s) de forma masiva, "+fileName+".";
			}else{
				messDetail="Se creó en el sistema, "+result+" vehículo(s) de forma masiva, "+
				"placa "+messPlate+".";
			}
			ProcessTrackDetailType ty=null;			
			if(type==0L){
				ty=ProcessTrackDetailType.VEHICLES_ADMIN;
			}else if(type==1L){
				ty=ProcessTrackDetailType.VEHICLES;
			}
			//administrador
			TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
			Long idPTA;
			if(pt==null){
				idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
			}
			else{
				idPTA=pt.getProcessTrackId();
			}
			Long detailA=process.createProcessDetail(idPTA,ty, 
					messDetail,
					creationUser, ip,1,"Error Carga Masiva de Vehículos",null,null,null,null);
			System.out.println("DetailCargaMasiva:"+detailA);
			//proceso cliente
			TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
			Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
			}
			else{
				idPTC=ptc.getProcessTrackId();
			}
			Long detailC=process.createProcessDetail(idPTC,ty, 
					messDetail, creationUser, ip, 1, "Error Carga Masiva de Vehículos",null,null,null,null);
			System.out.println("DetailCargaMasivaC:"+detailC);
		}
		return result;
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public boolean isPlacaValida(String plac) {
		Pattern p = Pattern.compile("[a-zA-Z]{2}[0-9]{4}");
		Matcher m = p.matcher(plac);
		if (m.matches()) {
			return true;
		} else {
			p = Pattern.compile("[a-zA-Z]{3}[0-9]{3}");
			m = p.matcher(plac);
			return m.matches();
		}
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public boolean validateMassiveLoad(int row,int column, String value,
			String fileName,Long userId,boolean isEspecial) {
		boolean respu=false;
		try{
			if(column==0){//PLACA
				if(value==null||value.trim().equals("")){
					log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
							"PLACA", value, "La placa es un campo requerido en el archivo.",
							"Escriba una placa válida.", userId,1L);
					respu=false;
				}else{
					//String bomUtf="ï»¿";
					value=new String(value.getBytes("UTF8"));
					System.out.println("PlacaValidation: "+value);
					/*if(bomUtf.equals(value.substring(0, 3))){
						System.out.println("IsBOMUTF-8Validation: "+value);
						value=value.replaceFirst(bomUtf, "");
						System.out.println("NewPlateValidation: "+value);
					}*/
					if(value.trim().toUpperCase().equals("CYP702")){
						log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
								"PLACA", value.toUpperCase(), "La placa CYP702 hace " +
								"parte de la línea de ejemplo.",
								"Por favor elimine la línea de ejemplo del archivo a cargar.", userId,1L);
						respu=false;
					}else{
						if(isEspecial){
							if(value.length()>12){
								log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
										"PLACA", value.toUpperCase(), "La placa no puede ser mayor a " +
										"12 (doce) caracteres.",
										"Corrija el tamaño de la placa.", userId,1L);
								respu=false;
							}else{
								if(!value.matches("([a-z]|[A-Z]|[0-9])+")){
									log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
											"PLACA", value.toUpperCase(), "La Placa " +
											"tiene caracteres inválidos.",
											"Recuerde que los caracteres válidos son letras " +
											"mayúsculas y minúsculas (sin acentos) y números. " +
											"Por favor modifique el " +
											"texto con el fin que cumpla con los caracteres " +
											"válidos.", userId,1L);
									respu=false;
								}else{
									if(plateExit(value.toUpperCase())){
										log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
												"PLACA", value.toUpperCase(), "Ya existe un vehículo " +
												"registrado con esa placa.",
												"Verifique la placa ingresada.", userId,1L);
										respu=false;
									}else{
										if(isPlacaValida(value.toUpperCase())){
											log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
													"PLACA", value.toUpperCase(), "La placa no tiene un formato especial.",
													"La placa del vehículo ingresada está compuesta " +
													"por tres letras y tres números (AAA000) o por dos " +
													"letras y cuatro números (AB1234), por favor no escriba " +
													"nada en la columna ESPECIAL ni en la columna OBSERVACIÓN " +
													"e intente nuevamente, o escriba una placa " +
													"que tenga un formato especial.", userId,1L);
											respu=false;
										}else{
											respu=true;
										}
									}
								}
							}
						}else{
							if(isPlacaValida(value.toUpperCase())){
								if(plateExit(value.toUpperCase())){
									log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
											"PLACA", value.toUpperCase(), "Ya existe un vehículo " +
											"registrado con esa placa.",
											"Verifique la placa ingresada.", userId,1L);
									respu=false;
								}else{
									respu=true;
								}
							}else{
								log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
										"PLACA", value.toUpperCase(), "La placa no tiene un formato " +
										"válido para una placa no especial.",
										"La placa del vehículo ingresada no está compuesta por " +
										"tres letras y tres números (AAA000) o por dos letras y " +
										"cuatro números (AB1234), por favor escriba SI en la columna " +
										"ESPECIAL y en la columna OBSERVACIÓN mencione " +
										"las características del vehículo, o escriba una placa válida.", userId,1L);
								respu=false;
							}
						}
					}
				}
			}else if(column==1){//ADICIONAL 1
				if(value==null||value.equals("")){
					respu=true;
				}else{
					boolean vali=true;
					if(value.length()>100){
						log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
								"ADICIONAL1", value, "El campo Adicional 1 " +
								"no puede tener más de 100 caracteres.",
								"Corrija el tamaño del texto que quiere " +
								"asignar para el campo Adicional 1.", userId,1L);
						vali=false;
					}
					value=replaceGuion(value);
					if(!value.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
						log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
								"ADICIONAL1", value, "El campo Adicional 1 " +
								"tiene caracteres inválidos.",
								"Recuerde que los caracteres válidos son letras " +
								"mayúsculas y minúsculas (sin acentos), números, " +
								"paréntesis [(,)], guion [-], guion bajo [_], barra " +
								"diagonal [/] y el punto [.]. Por favor modifique el " +
								"texto con el fin que cumpla con los caracteres " +
								"válidos.", userId,1L);
						vali=false;
					}
					if(vali){
						respu=true;
					}else{
						respu=false;
					}
				}
			}else if(column==2){//ADICIONAL 2
				if(value==null||value.equals("")){
					respu=true;
				}else{
					boolean vali=true;
					if(value.length()>100){
						log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
								"ADICIONAL2", value, "El campo Adicional 2 " +
								"no puede tener más de 100 caracteres.",
								"Corrija el tamaño del texto que quiere " +
								"asignar para el campo Adicional 2.", userId,1L);
						vali=false;
					}
					value=replaceGuion(value);
					if(!value.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
						log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
								"ADICIONAL2", value, "El campo Adicional 2 " +
								"tiene caracteres inválidos.",
								"Recuerde que los caracteres válidos son letras " +
								"mayúsculas y minúsculas (sin acentos), números, " +
								"paréntesis [(,)], guion [-], guion bajo [_], barra " +
								"diagonal [/] y el punto [.]. Por favor modifique el " +
								"texto con el fin que cumpla con los caracteres " +
								"válidos.", userId,1L);
						vali=false;
					}
					if(vali){
						respu=true;
					}else{
						respu=false;
					}
				}
			}else if(column==3){//ADICIONAL 3
				if(value==null||value.equals("")){
					respu=true;
				}else{
					boolean vali=true;
					if(value.length()>100){
						log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
								"ADICIONAL3", value, "El campo Adicional 3 " +
								"no puede tener más de 100 caracteres.",
								"Corrija el tamaño del texto que quiere " +
								"asignar para el campo Adicional 3.", userId,1L);
						vali=false;
					}
					value=replaceGuion(value);
					if(!value.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
						log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
								"ADICIONAL3", value, "El campo Adicional 3 " +
								"tiene caracteres inválidos.",
								"Recuerde que los caracteres válidos son letras " +
								"mayúsculas y minúsculas (sin acentos), números, " +
								"paréntesis [(,)], guion [-], guion bajo [_], barra " +
								"diagonal [/] y el punto [.]. Por favor modifique el " +
								"texto con el fin que cumpla con los caracteres " +
								"válidos.", userId,1L);
						vali=false;
					}
					if(vali){
						respu=true;
					}else{
						respu=false;
					}
				}
			}else if(column==4){//CATEGORIA
				if(value==null||value.equals("")){
					log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
							"CATEGORÍA", value, "La categoría es un campo requerido en el archivo.",
							"Escriba una categoría válida.", userId,1L);
					respu=false;
				}else{
					try{
						Long cat=Long.parseLong(value);
						if(cat<1){
							log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
									"CATEGORÍA", value, "La categoría debe ser un " +
									"número mayor o igual a 1.",
									"Corrija el valor de la categoría.", userId,1L);
							respu=false;
						}else{
							if(cat>7){
								log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
										"CATEGORÍA", value, "La categoría debe ser un " +
										"número menor o igual a 7.",
										"Corrija el valor de la categoría.", userId,1L);
								respu=false;
							}else{
								respu=true;
							}
						}
					}catch(Exception e){
						log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
								"CATEGORÍA", value, "La categoría es un campo numérico.",
								"Escriba una categoría válida entre 1 y 7.", userId,1L);
						respu=false;
					}
				}
			}else if(column==5){//LINEA
				if(value==null||value.trim().equals("")){
					log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
							"LÍNEA", value, "La línea es un campo requerido en el archivo.",
							"Escriba un texto válido.", userId,1L);
					respu=false;
				}else{
					boolean vali=true;
					if(value.length()>100){
						log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
								"LÍNEA", value, "El campo Línea " +
								"no puede tener más de 100 caracteres.",
								"Corrija el tamaño del texto que quiere " +
								"asignar para el campo Línea.", userId,1L);
						vali=false;
					}
					value=replaceGuion(value);
					if(!value.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
						log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
								"LÍNEA", value, "El campo Línea " +
								"tiene caracteres inválidos.",
								"Recuerde que los caracteres válidos son letras " +
								"mayúsculas y minúsculas (sin acentos), números, " +
								"paréntesis [(,)], guion [-], guion bajo [_], barra " +
								"diagonal [/] y el punto [.]. Por favor modifique el " +
								"texto con el fin que cumpla con los caracteres " +
								"válidos.", userId,1L);
						vali=false;
					}
					if(vali){
						respu=true;
					}else{
						respu=false;
					}
				}
			}else if(column==6){//ESPECIAL NO SE VALIDA PARA INSERCIÓN
				respu=true;
			}else if(column==7){//OBSERVACION
				if(value==null||value.trim().equals("")){
					if(isEspecial){
						log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
								"OBSERVACIÓN", value, "La Observación es un campo requerido en el archivo" +
								" cuando se escribe SI es el campo Especial.",
								"Escriba un texto válido.", userId,1L);
						respu=false;
					}else{
						respu=true;
					}
				}else{
					if(isEspecial==false){
						log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
								"OBSERVACIÓN", value, "El campo Observación debe " +
								"ir vacío si en el campo Especial no está la palabra SI.",
								"Modifique el campo Observación quitando el " +
								"texto o escriba en el campo Especial la palabra SI, " +
								"esto solo aplica si el vehículo tiene una placa especial.", userId,1L);
						respu=false;
					}else{
						if(value.length()>200){
							log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
									"OBSERVACIÓN", value, "El campo Observación " +
									"no puede tener más de 200 caracteres.",
									"Corrija el tamaño del texto que quiere " +
									"asignar para el campo Observación.", userId,1L);
							respu=false;
						}else{
							value=replaceGuion(value);
							if(!value.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
								log.insertLogVehicle(fileName, 2L, String.valueOf(row+1),
										"OBSERVACIÓN", value, "El campo Observación " +
										"tiene caracteres inválidos.",
										"Recuerde que los caracteres válidos son letras " +
										"mayúsculas y minúsculas (sin acentos), números, " +
										"paréntesis [(,)], guion [-], guion bajo [_], barra " +
										"diagonal [/] y el punto [.]. Por favor modifique el " +
										"texto con el fin que cumpla con los caracteres " +
										"válidos.", userId,1L);
								respu=false;
							}else{
								respu=true;
							}
						}
					}
				}
			}else{
				respu=false;
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("[ Error VehicleEJB.validateMassiveLoad ]");
			respu=false;
		}
		return respu;
	}
	
	/**
	 * @author jromero
	 * @param value
	 * @return
	 */
	public String replaceGuion(String value){
		value=value.replaceAll(Character.toString((char)8211), "-");
		value=value.replaceAll(Character.toString((char)8212), "-");
		System.out.println("Se realiza ajuste del caracter guion");
		return value;
	}

	/**
	 * @author jromero
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Vehicles> getVehiclesByUser(Long userId) {
		List<Vehicles> vehicles= new ArrayList<Vehicles>();
		String qr="SELECT DISTINCT VE.VEHICLE_ID,VE.VEHICLE_PLATE ," +
		"VE.VEHICLE_BEGINING_DATE ,VE.VEHICLE_CHASSIS ," +
		"VE.VEHICLE_OBSERVATION ,VE.COLOR_ID ,VE.BRAND_ID ," +
		"VE.CATEGORY_ID ,VE.VEHICLE_INTERNAL_CODE ,VE.LINE ," +
		"VE.ADITIONAL1 ,VE.ADITIONAL2 ,VE.ADITIONAL3,VE.VEHICLE_ESPECIAL_PLATE ," +
		"DECODE(RAD.ACCOUNT_DEVICE_ID,null,0,1) " +
		"FROM RE_USER_VEHICLE REU, TB_VEHICLE VE " +
		"LEFT JOIN RE_ACCOUNT_DEVICE RAD ON VE.VEHICLE_ID = RAD.VEHICLE_ID " +
		"WHERE VE.VEHICLE_ID = REU.VEHICLE_ID " +
		"AND REU.STATE_RELATION=0 " +
		"AND REU.USER_ID=?1 " +
		"ORDER BY VE.VEHICLE_PLATE";

		Query q= em.createNativeQuery(qr);
		q.setParameter(1, userId);

		System.out.println("qr1:"+qr);
		List<Object> lis=(List<Object>) q.getResultList();
		for(int i=0;i<lis.size();i++){
			Object[] obj=(Object[]) lis.get(i);
			Vehicles ve=new Vehicles();	
			if(ve!=null){
				ve.setAditional1(obj[10]==null?"":obj[10].toString());
				ve.setAditional2(obj[11]==null?"":obj[11].toString());
				ve.setAditional3(obj[12]==null?"":obj[12].toString());
				ve.setBrandId(obj[6]!=null?Long.parseLong(obj[6].toString()):null);
				ve.setCategoryId(Long.parseLong(obj[7]!=null?obj[7].toString():"0"));
				ve.setColorId(Long.parseLong(obj[5]!=null?obj[5].toString():"0"));
				if(Long.parseLong(obj[14].toString())==1L){
					ve.setEliminable(false);
				}else if(Long.parseLong(obj[14].toString())==0L){
					ve.setEliminable(true);
				}
				ve.setLine(obj[9]==null?"":obj[9].toString());
				if(obj[13]==null||(Long.parseLong(obj[13].toString())==0L)){
					ve.setEspecialPlate("");
				}else if(Long.parseLong(obj[13].toString())==1L){
					ve.setEspecialPlate("SI");
				}
				if(obj[2]==null){
					ve.setVehicleBeginingDate(null);
				}else{
					Timestamp timestamp = (Timestamp) obj[2];
					ve.setVehicleBeginingDate(timestamp);
				}
				ve.setVehicleChassis(obj[3]==null?"":obj[3].toString());
				ve.setVehicleId(Long.parseLong(obj[0].toString()));
				ve.setVehicleInternalCode(obj[8]==null?"":obj[8].toString());
				ve.setVehicleObservation(obj[4]==null?"":obj[4].toString());
				ve.setVehiclePlate(obj[1]==null?"":obj[1].toString());
				vehicles.add(ve);
			}
		}
		return vehicles;
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public boolean alterVehicle(Long creationUser, String ip,Long vehicleId,
			String vehicleChassis, String vehicleInternalCode, Long colorId,
			Long brandId, Long categoryId, String aditional1,
			String aditional2, String aditional3, String line,
			Timestamp modelVehi,String observation,boolean especial,
			Long userId) {
		boolean result=false;
		boolean modTag=false;
		String vehicleChassisOld=null; 
		String vehicleInternalCodeOld=null;
		String colorOld=null;
		String brandOld=null; 
		String categoryOld=null; 
		String aditional1Old=null;
		String aditional2Old=null; 
		String aditional3Old=null;
		String lineOld=null;
		String modelVehiOld=null;
		String observationOld=null;
		try{
			TbVehicle v= em.find(TbVehicle.class, vehicleId);

			if(!categoryId.equals(v.getTbCategory().getCategoryId())){
				System.out.println("Se actualiza tag, si hay");
				modTag=true;
				categoryOld="Antigua Categoría: "+v.getTbCategory().getCategoryId();
			}else{
				System.out.println("No se actualiza tag");
				modTag=false;
			}
			if(!aditional1.equals(v.getAditional1()==null?"":v.getAditional1())){
				aditional1Old="Antiguo Adicional 1: "+(v.getAditional1()==null?"":v.getAditional1());
			}
			if(!aditional2.equals(v.getAditional2()==null?"":v.getAditional2())){
				aditional2Old="Antiguo Adicional 2: "+(v.getAditional2()==null?"":v.getAditional2());
			}
			if(!aditional3.equals(v.getAditional3()==null?"":v.getAditional3())){
				aditional3Old="Antiguo Adicional 3: "+(v.getAditional3()==null?"":v.getAditional3());
			}
			if(!line.equals(v.getLine()==null?"":v.getLine())){
				lineOld="Antigua Línea: "+(v.getLine()==null?"":v.getLine());
			}
			if(v.getTbBrand()==null&&brandId!=null){
				brandOld="Antigua Marca: ";
			}else if(v.getTbBrand()!=null&&brandId==null){
				brandOld="Antigua Marca: "+v.getTbBrand().getBrandName();
			}else if(v.getTbBrand()!=null&&brandId!=null){
				if(!brandId.equals(v.getTbBrand().getBrandId())){
					brandOld="Antigua Marca: "+v.getTbBrand().getBrandName();
				}
			}
			if(!colorId.equals(v.getTbColor().getColorId())){
				colorOld="Antiguo Color: "+v.getTbColor().getColorName();
			}
			if(modelVehi==null||v.getVehicleBeginingDate()==null){
				if(modelVehi!=v.getVehicleBeginingDate()){
					modelVehiOld="Antiguo Modelo: "+(v.getVehicleBeginingDate()==null?"":new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(v.getVehicleBeginingDate().getTime())));
				}
			}else{
				if(modelVehi.getTime()!=v.getVehicleBeginingDate().getTime()){
					modelVehiOld="Antiguo Modelo: "+(v.getVehicleBeginingDate()==null?"":new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(v.getVehicleBeginingDate().getTime())));
				}
			}
			if(!vehicleChassis.equals(v.getVehicleChassis()==null?"":v.getVehicleChassis())){
				vehicleChassisOld="Antiguo Chasis: "+(v.getVehicleChassis()==null?"":v.getVehicleChassis());
			}
			if(!vehicleInternalCode.equals(v.getVehicleInternalCode()==null?"":v.getVehicleInternalCode())){
				vehicleInternalCodeOld="Antiguo Código Interno: "
					+(v.getVehicleInternalCode()==null?"":v.getVehicleInternalCode());
			}
			if(!observation.equals(v.getVehicleObservation()==null?"":v.getVehicleObservation())){
				observationOld="Antigua Observación: "
					+(v.getVehicleObservation()==null?"":v.getVehicleObservation());
			}

			v.setAditional1(aditional1);
			v.setAditional2(aditional2);
			v.setAditional3(aditional3);
			v.setLine(line);
			v.setTbBrand(brandId==null?null:(em.find(TbBrand.class, brandId)));
			v.setTbCategory(em.find(TbCategory.class, categoryId));
			v.setTbColor(em.find(TbColor.class, colorId));
			v.setVehicleBeginingDate(modelVehi);
			v.setVehicleChassis(vehicleChassis);
			v.setVehicleInternalCode(vehicleInternalCode);
			v.setVehicleObservation(observation);
			if(especial){
				v.setVehicleEspecialPlate(1L);
			}else{
				v.setVehicleEspecialPlate(0L);
			}
			if(this.updateVehicle(v)){
				log.insertLog(" Modificación De Vehículo | Se ha Modificado el Vehículo ID: " + v.getVehicleId() + ".", 
						LogReference.VEHICLE, LogAction.UPDATE, ip, creationUser);

				if(categoryOld!=null){
					log.insertLogVehicle("Se ha Modificado el Vehículo: " + v.getVehiclePlate() + ".", 1L, "1",
							"MODIFICACIÓN CORRECTA",categoryOld+", "+
							"Nuevo Categoría: "+v.getTbCategory().getCategoryId(),"N/A",
							"N/A", userId,0L);
				}
				if(modelVehiOld!=null){
					log.insertLogVehicle("Se ha Modificado el Vehículo: " + v.getVehiclePlate() + ".", 1L, "1",
							"MODIFICACIÓN CORRECTA",modelVehiOld+", "+
							"Nuevo Modelo: "+(v.getVehicleBeginingDate()==null?"":
								new SimpleDateFormat("dd/MM/yyyy")
							.format(new java.util.Date(v.getVehicleBeginingDate().getTime()))),"N/A",
							"N/A", userId,0L);
				}
				if(vehicleChassisOld!=null){
					log.insertLogVehicle("Se ha Modificado el Vehículo: " + v.getVehiclePlate() + ".", 1L, "1",
							"MODIFICACIÓN CORRECTA",vehicleChassisOld+", "+
							"Nuevo Chasis: "+(v.getVehicleChassis()==null?"":v.getVehicleChassis()),"N/A",
							"N/A", userId,0L);
				}
				if(vehicleInternalCodeOld!=null){
					log.insertLogVehicle("Se ha Modificado el Vehículo: " + v.getVehiclePlate() + ".", 1L, "1",
							"MODIFICACIÓN CORRECTA",vehicleInternalCodeOld+", "+
							"Nuevo Código Interno: "+(v.getVehicleInternalCode()==null?"":v.getVehicleInternalCode()),"N/A",
							"N/A", userId,0L);
				}
				if(colorOld!=null){
					log.insertLogVehicle("Se ha Modificado el Vehículo: " + v.getVehiclePlate() + ".", 1L, "1",
							"MODIFICACIÓN CORRECTA",colorOld+", "+
							"Nuevo Color: "+v.getTbColor().getColorName(),"N/A",
							"N/A", userId,0L);
				}
				if(brandOld!=null){
					log.insertLogVehicle("Se ha Modificado el Vehículo: " + v.getVehiclePlate() + ".", 1L, "1",
							"MODIFICACIÓN CORRECTA",brandOld+", "+
							"Nueva Marca: "+(v.getTbBrand()==null?"":v.getTbBrand().getBrandName()),"N/A",
							"N/A", userId,0L);
				}
				if(aditional1Old!=null){
					log.insertLogVehicle("Se ha Modificado el Vehículo: " + v.getVehiclePlate() + ".", 1L, "1",
							"MODIFICACIÓN CORRECTA",aditional1Old+", "+
							"Nuevo Adicional 1: "+(v.getAditional1()==null?"":v.getAditional1()),"N/A",
							"N/A", userId,0L);
				}
				if(aditional2Old!=null){
					log.insertLogVehicle("Se ha Modificado el Vehículo: " + v.getVehiclePlate() + ".", 1L, "1",
							"MODIFICACIÓN CORRECTA",aditional2Old+", "+
							"Nuevo Adicional 2: "+(v.getAditional2()==null?"":v.getAditional2()),"N/A",
							"N/A", userId,0L);
				}
				if(aditional3Old!=null){
					log.insertLogVehicle("Se ha Modificado el Vehículo: " + v.getVehiclePlate() + ".", 1L, "1",
							"MODIFICACIÓN CORRECTA",aditional3Old+", "+
							"Nuevo Adicional 3: "+(v.getAditional3()==null?"":v.getAditional3()),"N/A",
							"N/A", userId,0L);
				}
				if(lineOld!=null){
					log.insertLogVehicle("Se ha Modificado el Vehículo: " + v.getVehiclePlate() + ".", 1L, "1",
							"MODIFICACIÓN CORRECTA",lineOld+", "+
							"Nueva Línea: "+(v.getLine()==null?"":v.getLine()),"N/A",
							"N/A", userId,0L);
				}
				if(observationOld!=null){
					log.insertLogVehicle("Se ha Modificado el Vehículo: " + v.getVehiclePlate() + ".", 1L, "1",
							"MODIFICACIÓN CORRECTA",observationOld+", "+
							"Nueva Observación: "+(v.getVehicleObservation()==null?"":v.getVehicleObservation()),"N/A",
							"N/A", userId,0L);
				}

				if(modTag){
					Query q=em.createNativeQuery("SELECT D.DEVICE_CODE " +
							"FROM RE_ACCOUNT_DEVICE RED,TB_DEVICE D " +
							"WHERE RELATION_STATE=0 " +
							"AND RED.DEVICE_ID = D.DEVICE_ID " +
					"AND VEHICLE_ID=?1");
					q.setParameter(1, v.getVehicleId());
					String devCod="";
					try{
						devCod=(String) q.getSingleResult();
					}catch (NoResultException e) {
						devCod=null;
					}
					System.out.println("devCod:"+devCod);
					if(devCod!=null){
						if(this.updateCategoryTag(devCod,categoryId)){
							log.insertLog(" Modificación De Vehículo | Se ha Modificado el Tag ID: " + devCod + ".", 
									LogReference.VEHICLE, LogAction.UPDATE, ip, creationUser);
						}else{
							log.insertLog(" Modificación De Vehículo | No Se ha Podido Modificar el Tag ID: " + devCod + ".", 
									LogReference.VEHICLE, LogAction.UPDATEFAIL, ip, creationUser);
						}

					}
				}
				result=true;
			}else{
				log.insertLog(" Modificación De Vehículo | No Se ha Podido Modificar el Vehículo ID: " + v.getVehicleId() + ".", 
						LogReference.VEHICLE, LogAction.UPDATEFAIL, ip, creationUser);
				result=false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error VehicleEJB.alterVehicle ] ");
			result =false;
		}
		return result;
	}
	
	/**
	 * @author jromero
	 */
	public boolean deleteUserVehicleRelation(Long userID,Long vehicleId){
		boolean result=false;
		try{
			Query q=em.createNativeQuery("select DISTINCT USER_VEHICLE_ID " +
					"from RE_USER_VEHICLE " +
					"where STATE_RELATION=0 " +
					"AND VEHICLE_ID=?1 " +
			"AND USER_ID=?2");
			q.setParameter(1, vehicleId);
			q.setParameter(2, userID);
			Long uvId=((BigDecimal) q.getSingleResult()).longValue();
			System.out.println("deleteUserVehicleRelation.uvId: "+uvId);
			
			ReUserVehicle uv=em.find(ReUserVehicle.class, uvId);
			uv.setStateRelation(1);
			uv.setDateDisassociation(new Timestamp(System.currentTimeMillis()));
			em.merge(uv);
			em.flush();
			
			result=true;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error vehicleEJB.deleteUserVehicleRelation ] ");
			result=false;
		}
		System.out.println("deleteUserVehicleRelation.result: "+result);
		return result;
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public boolean createDeletedVehicle(Long creationUser, String ip,Long vehicleId,
			String vehicleChassis, String vehicleInternalCode, Long colorId,
			Long brandId, Long categoryId, String aditional1,
			String aditional2, String aditional3, String line,
			Timestamp modelVehi,String observation,Long userId) {
		boolean result=false;
		try{
			TbVehicle v=em.find(TbVehicle.class, vehicleId);
			
			boolean especial=false;
			
			if(this.isEspecialPlate(v.getVehiclePlate().toUpperCase())){
				especial=true;
			}else{
				especial=false;
			}
			if(this.alterVehicle(creationUser, ip, vehicleId, vehicleChassis, vehicleInternalCode,
					colorId, brandId, categoryId, aditional1, 
					aditional2, aditional3, line, modelVehi, 
					observation, especial, userId)){
				v.setVehicleDeleted(0L);
				v.setVehicleRegistrationDate(new Timestamp(System.currentTimeMillis()));
				em.merge(v);
				em.flush();
				
				ReUserVehicle uv=new ReUserVehicle();
				uv.setDateAssociation(new Timestamp(System.currentTimeMillis()));
				uv.setStateRelation(0);
				uv.setTbSystemUser(em.find(TbSystemUser.class, userId));
				uv.setTbVehicle(v);
				em.persist(uv);

				em.flush();
				return true;
			}else{
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error VehicleEJB.createDeletedVehicle ] ");
			result =false;
		}
		return result;
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public boolean deleteVehicle(Long creationUser, String ip,
			String vehiclePlate, Long userId) {
		boolean result=false;
		TbVehicle v=null;
		try{
			if(vehiclePlate==null||vehiclePlate.equals("")){
				result=false;
			}else{
				v=this.getVehicleByPlate(vehiclePlate);
				if(this.haveRelationTag(v.getVehicleId())){
					result=false;
				}else{
					if(this.deleteUserVehicleRelation(userId, v.getVehicleId())){
						v.setVehicleDeleted(1L);
						em.merge(v);
						em.flush();
						
						log.insertLog(" Eliminación De Vehículo | Se ha Eliminado un Vehículo ID: " + v.getVehicleId() + ".", 
								LogReference.VEHICLE, LogAction.DELETE, ip, creationUser);
						result=true;
					}else{
						log.insertLog(" Eliminación De Vehículo | No Se ha Eliminado un Vehículo ID: " + v.getVehicleId() + ".", 
								LogReference.VEHICLE, LogAction.DELETEFAIL, ip, creationUser);
						result=false;
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error VehicleEJB.deleteVehicle ] ");
			result=false;
		}
		return result;
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public boolean haveVehicleCountMoves(Long vehicleId) {
		boolean respu=false;
		try{
			Query q = em.createNativeQuery("select count(TRANSACTION_ID) " +
					"from TB_TRANSACTION " +
					"where TRANSACTION_TYPE_ID=2 " +
					"and ACCOUNT_ID is not null " +
					"and VEHICLE_ID is not null " +
					"and DEVICE_ID is not null " +
					"and USER_ID is not null " +
			"and VEHICLE_ID=?1"); 
			q.setParameter(1, vehicleId);
			Long cant=((BigDecimal) q.getSingleResult()).longValue();
			if(cant<=0L){
				respu=false;
			}else{
				respu=true;
			}
		}catch (Exception e) {
			System.out.println(" [ Error VehicleEJB.getVehicleCountMoves ] ");
			e.printStackTrace();
			respu=false;
		}
		return respu;
	}
	
	/**
	 * @author jromero
	 */
	public boolean removeUserVehicleRelation(Long userID,Long vehicleId){
		boolean result=false;
		try{
			Query q=em.createNativeQuery("select DISTINCT USER_VEHICLE_ID " +
					"from RE_USER_VEHICLE " +
					"where STATE_RELATION=0 " +
					"AND VEHICLE_ID=?1 " +
			"AND USER_ID=?2");
			q.setParameter(1, vehicleId);
			q.setParameter(2, userID);
			Long uvId=((BigDecimal) q.getSingleResult()).longValue();
			System.out.println("removeUserVehicleRelation.uvId: "+uvId);
			result=new ObjectEM(em).delete(em.find(ReUserVehicle.class, uvId));
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error vehicleEJB.removeUserVehicleRelation ] ");
			result=false;
		}
		System.out.println("removeUserVehicleRelation.result: "+result);
		return result;
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public void createProcessForVehicles(Long creationUser, String ip,
			String messDetail, String messError, Long userId, Long type) {

		ProcessTrackDetailType ty=null;			
		if(type==0L){
			ty=ProcessTrackDetailType.VEHICLES_ADMIN;
		}else if(type==1L){
			ty=ProcessTrackDetailType.VEHICLES;
		}
		TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
		Long idPTA;
		if(pt==null){
			idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, 
					ip, creationUser);
		}
		else{
			idPTA=pt.getProcessTrackId();
		}
		Long detailA=process.createProcessDetail(idPTA,ty, 
				messDetail,
				creationUser, ip,1,messError,null,null,null,null);
		System.out.println("DetailVehiculo:"+detailA);
		//proceso cliente
		TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
		Long idPTC;
		if(ptc==null){
			idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, 
					ip, creationUser);
		}
		else{
			idPTC=ptc.getProcessTrackId();
		}
		Long detailC=process.createProcessDetail(idPTC,ty, 
				messDetail, creationUser, ip, 1, messError,null,null,null,null);
		System.out.println("DetailVehiculoC:"+detailC);

	}
	
	/**
	 * @author jromero
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Vehicles> getUserVehiclesByFilters(Long codeType,String code,String firstName,String lastName,Long catId, String placa,
			String adicional1, String adicional2,String adicional3,Long type, int page, int rows){	

		List<Vehicles> vehicles = new ArrayList<Vehicles>();
		String qry1, qry2, qry3= "";
		try {
			qry1 = "SELECT COUNT(*) FROM (";
			
			qry2= "SELECT * FROM (";
			
			qry3= "SELECT a.*, rownum rnum FROM (" +		
				  "SELECT DISTINCT VE.VEHICLE_ID,VE.VEHICLE_PLATE ," +
				  "VE.VEHICLE_BEGINING_DATE ,VE.VEHICLE_CHASSIS ," +
				  "VE.VEHICLE_OBSERVATION ,VE.COLOR_ID ,VE.BRAND_ID ," +
				  "VE.CATEGORY_ID ,VE.VEHICLE_INTERNAL_CODE ,VE.LINE ," +
				  "VE.ADITIONAL1 ,VE.ADITIONAL2 ,VE.ADITIONAL3,VE.VEHICLE_ESPECIAL_PLATE ," +
				  "DECODE(RAD.ACCOUNT_DEVICE_ID,null,0,1) " +
				  "FROM RE_USER_VEHICLE REU, TB_SYSTEM_USER US ,TB_VEHICLE VE " +
				  "LEFT JOIN RE_ACCOUNT_DEVICE RAD ON VE.VEHICLE_ID = RAD.VEHICLE_ID " +
				  "WHERE VE.VEHICLE_ID = REU.VEHICLE_ID " +
				  "AND US.USER_ID = REU.USER_ID " +
				  "AND REU.STATE_RELATION=0 ";
		
			if(codeType!=0L&&codeType!=-1L){
				qry3=qry3+"AND US.CODE_TYPE_ID="+codeType+" ";
			}
			if(!code.equals("")&&type==0L){
				qry3=qry3+"AND US.USER_CODE like '%"+code+"%' ";
			}
			if(!code.equals("")&&type==1L){
				qry3=qry3+"AND US.USER_CODE='"+code+"' ";
			}
			if(!firstName.equals("")&&type==0L){
				qry3=qry3+"AND UPPER(US.USER_NAMES) like '%"+firstName.toUpperCase()+"%' ";
			}
			if(!lastName.equals("")&&type==0L){
				qry2=qry3+"AND UPPER(US.USER_SECOND_NAMES) like '%"+lastName.toUpperCase()+"%' ";
			}
			if(catId!=0L&&catId!=-1L){
				qry3=qry3+"AND VE.CATEGORY_ID="+catId+" ";
			}
			if(!placa.equals("")){
				qry3=qry3+"AND UPPER(VE.VEHICLE_PLATE) like '%"+placa.toUpperCase()+"%' ";
			}
			if(!adicional1.equals("")){
				qry3=qry3+"AND UPPER(VE.ADITIONAL1) like '%"+adicional1.toUpperCase()+"%' ";
			}
			if(!adicional2.equals("")){
				qry3=qry3+"AND UPPER(VE.ADITIONAL2) like '%"+adicional2.toUpperCase()+"%' ";
			}
			if(!adicional3.equals("")){
				qry3=qry3+"AND UPPER(VE.ADITIONAL3) like '%"+adicional3.toUpperCase()+"%' ";
			}

		 if(page!=0){
			qry3 = qry3+ "ORDER BY 2 ASC) a WHERE rownum  <="+(page*rows)+") WHERE rnum > "+((page*rows)-rows)+" ";	
			Query q=em.createNativeQuery(qry2+qry3);
			List<Object> lis=(List<Object>) q.getResultList();
			for(int i=0;i<lis.size();i++){
				Object[] obj=(Object[]) lis.get(i);
				Vehicles ve=new Vehicles();	
				if(ve!=null){
					ve.setAditional1(obj[10]==null?"":obj[10].toString());
					ve.setAditional2(obj[11]==null?"":obj[11].toString());
					ve.setAditional3(obj[12]==null?"":obj[12].toString());
					ve.setBrandId(obj[6]!=null?Long.parseLong(obj[6].toString()):null);
					ve.setCategoryId(Long.parseLong(obj[7]!=null?obj[7].toString():"0"));
					ve.setColorId(Long.parseLong(obj[5]!=null?obj[5].toString():"0"));
					if(Long.parseLong(obj[14].toString())==1L){
						ve.setEliminable(false);
					}else if(Long.parseLong(obj[14].toString())==0L){
						ve.setEliminable(true);
					}
					ve.setLine(obj[9]==null?"":obj[9].toString());
					if(obj[13]==null||(Long.parseLong(obj[13].toString())==0L)){
						ve.setEspecialPlate("");
					}else if(Long.parseLong(obj[13].toString())==1L){
						ve.setEspecialPlate("SI");
					}
					if(obj[2]==null){
						ve.setVehicleBeginingDate(null);
					}else{
						Timestamp timestamp = (Timestamp) obj[2];
						ve.setVehicleBeginingDate(timestamp);
					}
					ve.setVehicleChassis(obj[3]==null?"":obj[3].toString());
					ve.setVehicleId(Long.parseLong(obj[0].toString()));
					ve.setVehicleInternalCode(obj[8]==null?"":obj[8].toString());
					ve.setVehicleObservation(obj[4]==null?"":obj[4].toString());
					ve.setVehiclePlate(obj[1]==null?"":obj[1].toString());
					vehicles.add(ve);
				}
			}
		  }else {
			Query query = em.createNativeQuery(qry1+qry3+")a )");
			vehicles= query.getResultList();
		  }
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getUserVehiclesByFilters ] ");
			e.printStackTrace();
		}	
		return vehicles;
	}
	
	/**
	 * @author jromero
	 */
	public boolean updateCategoryTag(String codeDev,Long cat) {
		boolean result=false;
		try{
			TbTag t=em.find(TbTag.class, codeDev);
			t.setCategoryId(cat);

			em.merge(t);
			em.flush();

			result=true;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error VehicleEJB.updateCategoryTag ] ");
			result=false;
		}
		return result;
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public boolean haveRelationTag(Long vehicleId) {
		boolean result=false;
		try{
			Query q=em.createNativeQuery("SELECT COUNT(*) " +
					"FROM RE_ACCOUNT_DEVICE " +
			"WHERE VEHICLE_ID=?1");
			q.setParameter(1, vehicleId);
			Long cant=((BigDecimal) q.getSingleResult()).longValue();
			if(cant<=0L){
				result=false;
			}else{
				result=true;
			}
		}catch (Exception e) {
			System.out.println(" [ Error VehicleEJB.haveRelationTag ] ");
			e.printStackTrace();
			result=false;
		}
		return result;
	}
	
	/**
	 * @author jromero
	 * @return Long
	 * 			-2: Error
	 * 			-1: Inactive Relation
	 * 			0: No relations
	 * 			1: Active Relation
	 */
	@Override
	public Long typeRelationTag(String vehiclePlate) {
		Long result=-2L;
		Long vehicleId=-1L;
		try{
			vehicleId=this.getVehicleByPlate(vehiclePlate).getVehicleId();
			Query q=em.createNativeQuery("SELECT count(*) " +
					"FROM re_account_device rad,tb_account a " +
					"WHERE rad.account_id=a.account_id " +
					"AND a.user_id=?1 " +
					"AND rad.vehicle_id=?2");
			q.setParameter(1, this.getUserIdByVehicleId(vehicleId));
			q.setParameter(2, vehicleId);
			Long cant=((BigDecimal) q.getSingleResult()).longValue();
			if(cant>0L){
				System.out.println("typeRelationTag.cant: "+cant);
				if(haveActiveRelationTag(vehicleId)==true){
					result=1L;
				}else{
					result=-1L;
				}
			}else{
				result=0L;
			}
		}catch (NullPointerException e) {
			System.out.println(" [ Error VehicleEJB.typeRelationTag ] ");
			e.printStackTrace();
			result=-2L;
		}catch (Exception e) {
			System.out.println(" [ Error VehicleEJB.typeRelationTag ] ");
			e.printStackTrace();
			result=-2L;
		}
		System.out.println("typeRelationTag.result: "+result);
		return result;
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public boolean haveActiveRelationTag(Long vehicleId) {
		boolean result=false;
		try{
			Query q=em.createNativeQuery("SELECT count(*) " +
					"FROM re_account_device rad,tb_account a " +
					"WHERE rad.account_id=a.account_id " +
					"AND a.user_id=?1 " +
					"AND rad.vehicle_id=?2 " +
					"AND rad.relation_state=0");
			q.setParameter(1, this.getUserIdByVehicleId(vehicleId));
			q.setParameter(2, vehicleId);
			Long cant=((BigDecimal) q.getSingleResult()).longValue();
			System.out.println("haveActiveRelationTag.cant: "+cant);
			if(cant<=0L){
				result=false;
			}else{
				result=true;
			}
		}catch (Exception e) {
			System.out.println(" [ Error VehicleEJB.haveActiveRelationTag ] ");
			e.printStackTrace();
			result=false;
		}
		return result;
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public boolean isEspecialPlate(String plate) {
		boolean result=false;
		if(plate.length()!=6){
			result=true;
		}else{
			if(this.isPlacaValida(plate)){
				result=false;
			}else{
				result=true;
			}
		}
		return result;
	}

	/**
	 * @author jromero
	 */
	@Override
	public Long getUserByFiltersVehicle(Long codeType,String code,String firstName,String lastName,Long catId, String placa,
			String adicional1, String adicional2,
			String adicional3,Long type){			
		String qry = "SELECT DISTINCT REU.USER_ID " +
		"FROM RE_USER_VEHICLE REU, TB_SYSTEM_USER US ,TB_VEHICLE VE " +
		"WHERE VE.VEHICLE_ID = REU.VEHICLE_ID " +
		"AND US.USER_ID = REU.USER_ID " +
		"AND REU.STATE_RELATION=0 ";
		String qryEnd="GROUP BY REU.USER_ID";
		try {
			if(codeType!=0L&&codeType!=-1L){
				qry=qry+"AND US.CODE_TYPE_ID="+codeType+" ";
			}
			if(!code.equals("")&&type==0L){
				qry=qry+"AND US.USER_CODE like '%"+code+"%' ";
			}
			if(!code.equals("")&&type==1L){
				qry=qry+"AND US.USER_CODE='"+code+"' ";
			}
			if(!firstName.equals("")&&type==0L){
				qry=qry+"AND UPPER(US.USER_NAMES) like '%"+firstName.toUpperCase()+"%' ";
			}
			if(!lastName.equals("")&&type==0L){
				qry=qry+"AND UPPER(US.USER_SECOND_NAMES) like '%"+lastName.toUpperCase()+"%' ";
			}
			if(catId!=0L&&catId!=-1L){
				qry=qry+"AND VE.CATEGORY_ID="+catId+" ";
			}
			if(!placa.equals("")){
				qry=qry+"AND UPPER(VE.VEHICLE_PLATE) like '%"+placa.toUpperCase()+"%' ";
			}
			if(!adicional1.equals("")){
				qry=qry+"AND UPPER(VE.ADITIONAL1) like '%"+adicional1.toUpperCase()+"%' ";
			}
			if(!adicional2.equals("")){
				qry=qry+"AND UPPER(VE.ADITIONAL2) like '%"+adicional2.toUpperCase()+"%' ";
			}
			if(!adicional3.equals("")){
				qry=qry+"AND UPPER(VE.ADITIONAL3) like '%"+adicional3.toUpperCase()+"%' ";
			}

			System.out.println("qry3:"+qry+qryEnd);
			Query q=em.createNativeQuery(qry+qryEnd);

			Long cant=((BigDecimal) q.getSingleResult()).longValue();
			return cant;
		} catch (NonUniqueResultException e) {
			System.out.println(" [ Error NonUniqueResultException en UserEJB.getUserVehiclesByFilters ] ");
			e.printStackTrace();
			return -2L;
		} catch (NoResultException e) {
			System.out.println(" [ Error NoResultException en UserEJB.getUserVehiclesByFilters ] ");
			e.printStackTrace();
			return -1L;
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getUserVehiclesByFilters ] ");
			e.printStackTrace();
			return -1L;
		} 
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public Long getUserIdByVehicleId(Long vehicleId){			
		String qry = "SELECT DISTINCT REU.USER_ID " +
		"FROM RE_USER_VEHICLE REU " +
		"WHERE REU.VEHICLE_ID = ?1 " +
		"AND REU.STATE_RELATION=0";
		try {

			Query q=em.createNativeQuery(qry);
			q.setParameter(1, vehicleId);

			Long userId=((BigDecimal) q.getSingleResult()).longValue();
			System.out.println("getUserIdByVehicleId.userId: "+userId);
			return userId;
		} catch (NonUniqueResultException e) {
			System.out.println(" [ Error NonUniqueResultException en VehicleEJB.getUserIdByVehicleId ] ");
			e.printStackTrace();
			return -2L;
		} catch (NoResultException e) {
			System.out.println(" [ Error NoResultException en VehicleEJB.getUserIdByVehicleId ] ");
			//e.printStackTrace();
			return -1L;
		} catch (Exception e) {
			System.out.println(" [ Error en VehicleEJB.getUserIdByVehicleId ] ");
			e.printStackTrace();
			return -1L;
		} 
	}

	/**
	 * @author jromero
	 */
	@Override
	public String getVehiclesByReport(Long codeType,String code,String firstName,String lastName,String email,String account,
			Long catId, String placa,String adicional1, String adicional2,String adicional3,Date dateIni,Date dateEnd){
		String qry = "SELECT c.code_type_abbreviation tipo_id,u.user_code identificacion, " +
		"DECODE(u.code_type_id,1,u.user_names||' '||u.user_second_names,u.user_names) nombres,ad.account_id cuenta,b.bank_name banco, " +
		"v.vehicle_plate placa,'Categoría '||v.category_id categoria, " +
		"b.brand_name marca,v.line linea,v.vehicle_internal_code codigo,v.aditional1 ad1,v.aditional2 ad2, " +
		"v.aditional3 ad3,decode(v.vehicle_especial_plate,1,'SI',NULL) especial,v.vehicle_observation observacion,v.vehicle_registration_date fecha " +
		"FROM tb_vehicle v " +
		"LEFT JOIN tb_brand b " +
		"ON v.brand_id = b.brand_id " +
		"INNER JOIN re_user_vehicle uv " +
		"ON v.vehicle_id = uv.vehicle_id " +
		"AND uv.state_relation=0 " +
		"INNER JOIN tb_system_user u " +
		"ON uv.user_id = u.user_id " +
		"INNER JOIN tb_code_type c " +
		"ON u.code_type_id = c.code_type_id " +
		"LEFT JOIN re_account_device ad " +
		"ON v.vehicle_id = ad.vehicle_id " +
		"AND ad.relation_state=0 " +
		"LEFT JOIN re_account_bank ab " +
		"ON ad.account_id = ab.account_id " +
		"AND ab.state_account_bank=1 " +
		"LEFT JOIN tb_bank_account ba " +
		"ON ba.bank_account_id = ab.bank_account_id " +
		"LEFT JOIN tb_bank b " +
		"ON ba.product=b.bank_id " +
		"WHERE 1=1 ";
		String qryEnd="ORDER BY TO_NUMBER(u.user_code),ad.account_id,v.vehicle_plate";
		try {
			if(codeType!=0L&&codeType!=-1L){
				qry=qry+"AND u.code_type_id="+codeType+" ";
			}
			if(!code.equals("")){
				qry=qry+"AND u.user_code LIKE '%"+code+"%' ";
			}
			if(!firstName.equals("")){
				qry=qry+"AND UPPER(u.user_names) LIKE '%"+firstName.toUpperCase()+"%' ";
			}
			if(!lastName.equals("")){
				qry=qry+"AND UPPER(u.user_second_names) LIKE '%"+lastName.toUpperCase()+"%' ";
			}
			if(!email.equals("")){
				qry=qry+"AND UPPER(u.user_email) LIKE '%"+email.toUpperCase()+"%' ";
			}
			if(!account.equals("")){
				qry=qry+"AND ad.account_id LIKE '%"+account+"%' ";
			}
			if(catId!=0L&&catId!=-1L){
				qry=qry+"AND v.category_id="+catId+" ";
			}
			if(!placa.equals("")){
				qry=qry+"AND UPPER(v.vehicle_plate) LIKE '%"+placa.toUpperCase()+"%' ";
			}
			if(!adicional1.equals("")){
				qry=qry+"AND UPPER(v.aditional1) LIKE '%"+adicional1.toUpperCase()+"%' ";
			}
			if(!adicional2.equals("")){
				qry=qry+"AND UPPER(v.aditional2) LIKE '%"+adicional2.toUpperCase()+"%' ";
			}
			if(!adicional3.equals("")){
				qry=qry+"AND UPPER(v.aditional3) LIKE '%"+adicional3.toUpperCase()+"%' ";
			}
			if(dateIni!=null&&dateEnd!=null){
				qry=qry+"AND v.vehicle_registration_date BETWEEN TO_TIMESTAMP('"+new SimpleDateFormat("dd/MM/yyyy").format(dateIni)+
				" 00:00:00,0000','DD/MM/YYYY HH24:MI:SS,FF') AND TO_TIMESTAMP('"+new SimpleDateFormat("dd/MM/yyyy").format(dateEnd)+
				" 23:59:59,9999','DD/MM/YYYY HH24:MI:SS,FF') ";
			}

			qry=qry+qryEnd;

			System.out.println("String query:"+qry);
		} catch (Exception e) {
			System.out.println(" [ Error en VehicleEJB.getVehiclesByReport ] ");
			e.printStackTrace();
		}	
		return qry;
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public ReUserVehicle getReUserVehicleByVehicleId(Long vehicleId){			
		String qry = "SELECT REU.USER_VEHICLE_ID " +
			"FROM RE_USER_VEHICLE REU " +
			"WHERE REU.VEHICLE_ID = ?1 " +
			"AND REU.STATE_RELATION=0";
		try {

			Query q=em.createNativeQuery(qry);
			q.setParameter(1, vehicleId);

			Long userVehicleId=((BigDecimal) q.getSingleResult()).longValue();
			return em.find(ReUserVehicle.class, userVehicleId);
		} catch (NonUniqueResultException e) {
			System.out.println(" [ Error NonUniqueResultException en VehicleEJB.getReUserVehicleByVehicleId ] ");
			e.printStackTrace();
			return null;
		} catch (NoResultException e) {
			System.out.println(" [ Error NoResultException en VehicleEJB.getReUserVehicleByVehicleId ] ");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println(" [ Error en VehicleEJB.getReUserVehicleByVehicleId ] ");
			e.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * @author jromero
	 * @return 0: Error
	 * 			1: Ok
	 */
	@Override
	public Long reasignVehicle(Long creationUser, String ip,Long vehicleId,Long userIdNew) {
		try{
			ReUserVehicle uv=this.getReUserVehicleByVehicleId(vehicleId);
			if(uv!=null){
				System.out.println("uv no vacío");
				Long rad=this.getLastInactiveRelationTag(vehicleId);
				System.out.println("reasignVehicle.rad: "+rad);
				
				uv.setDateDisassociation(new Timestamp(System.currentTimeMillis()));
				uv.setStateRelation(1);
				em.merge(uv);
				
				em.flush();
				
				System.out.println("change uv");
				
				log.insertLogVehicle("Se ha Modificado el Vehículo: " + uv.getTbVehicle().getVehiclePlate() + ".", 1L, "1",
						"MODIFICACIÓN CORRECTA","Se eliminó relación del vehículo "+uv.getTbVehicle().getVehiclePlate()
						+" con el usuario "+uv.getTbSystemUser().getUserCode()
						,"N/A","N/A", uv.getTbSystemUser().getUserId(),0L);
				
				TbSystemUser u=new TbSystemUser();
				u=em.find(TbSystemUser.class, userIdNew);

				ReUserVehicle uv2=new ReUserVehicle();
				uv2.setDateAssociation(new Timestamp(System.currentTimeMillis()));
				uv2.setStateRelation(0);
				uv2.setTbSystemUser(u);
				uv2.setTbVehicle(uv.getTbVehicle());
				em.persist(uv2);

				System.out.println("create new uv");
				
				em.flush();
				
				log.insertLogVehicle("Se ha Modificado el Vehículo: " + uv.getTbVehicle().getVehiclePlate() + ".", 1L, "1",
						"MODIFICACIÓN CORRECTA","Se asignó el vehículo "+uv.getTbVehicle().getVehiclePlate() 
						+" al usuario "+userEJB.getSystemUser(userIdNew).getUserCode()
						,"N/A","N/A", userIdNew,0L);

				log.insertLog(" Creación De Vehículo | Se ha Reasignado el Vehículo ID: " + uv.getTbVehicle().getVehicleId() + ".", 
						LogReference.VEHICLE, LogAction.UPDATE, ip, creationUser);
				
				try{
					ArrayList<String> parameters=new ArrayList<String>();
					parameters.add("#NEWCFP="+userEJB.getLastAccountByUserId(userIdNew));
					parameters.add("#NEWTPCC="+u.getTbCodeType().getCodeTypeId());
					parameters.add("#DATEINAC="+rad);
					System.out.println("parameters: "+parameters.toString());
					outbox.insertMessageOutbox(uv.getTbSystemUser().getUserId(),
							EmailProcess.REASIGN_VEHICLE,
							deviceEJB.getReAccountDeviceById(rad).getTbAccount().getAccountId(),
							null, 
							null, 
							null,
							vehicleId,	
							null,
							null,
							null,
							userIdNew,
							uv.getTbSystemUser().getTbCodeType().getCodeTypeId(),
							null,
							null,
							true,
							parameters);
				}catch (Exception e) {
					System.out.println(" [ Error en VehicleEJB.reasignVehicle Error Al Enviar Correo ] ");
					e.printStackTrace();
				}
				
				return uv2.getUserVehicleId();
			}else{
				return 0L;
			}
		}catch (Exception e) {
			System.out.println(" [ Error en VehicleEJB.reasignVehicle ] ");
			e.printStackTrace();
			return 0L;
		}
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public Long getLastInactiveRelationTag(Long vehicleId) {
		Long rad=-1L;
		try{
			Query q=em.createNativeQuery("SELECT account_device_id FROM " +
					"(SELECT rad.account_device_id " +
					"FROM re_account_device rad, tb_account a " +
					"WHERE rad.account_id = a.account_id " +
					"AND rad.vehicle_id=?1 " +
					"AND a.user_id=?2 " +
					"ORDER BY rad.account_device_id DESC) " +
					"WHERE ROWNUM<2");
			q.setParameter(1, vehicleId);
			q.setParameter(2, this.getUserIdByVehicleId(vehicleId));
			rad=((BigDecimal)q.getSingleResult()).longValue();
			System.out.println("getLastInactiveRelationTag.rad "+rad);
			return rad;
		}catch (Exception e) {
			System.out.println(" [ Error en VehicleEJB.getLastInactiveRelationTag ] ");
			e.printStackTrace();
			return -1L;
		}
	}
	
	public Long getMaxCategoryClient (Long Account){
		Long catVehi = 0L;
		try {
			Query q= em.createNativeQuery("select nvl(max(tv.category_id),0) from re_account_device rd " +
					"inner join tb_device td on td.device_id = rd.device_id " +
					"inner join tb_vehicle tv on tv.vehicle_id = rd.vehicle_id " +
					"where account_id = ?1 " +
					"and rd.relation_state = 0 " +
					"and td.device_state_id in (3,4,6,16)");
			
			q.setParameter(1, Account);
			catVehi = ((BigDecimal) q.getSingleResult()).longValue();
			if(catVehi==0){
				catVehi=-1L;
			}
		} catch (Exception e) {
			System.out.println(" [ Error en VehicleEJB.getMaxCategoryClient ] ");
			e.printStackTrace();
			catVehi = -1L;
		}
		System.out.println("La categoria Maxima del Cliente, para sus vehiculos Activos es: " + catVehi);	
		return catVehi;
	}
	
}