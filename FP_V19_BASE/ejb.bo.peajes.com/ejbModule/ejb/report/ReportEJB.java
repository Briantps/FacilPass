package ejb.report;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import util.warehouse.WarehouseTo;

import constant.WarehouseState;

import jpa.ReDeviceWarehouse;
import jpa.TbDevice;
import jpa.TbDeviceCustomization;
import jpa.TbSpecialStation;
import jpa.TbSystemUser;
import jpa.TbUserData;
import jpa.TbWarehouse;

/**
 * Session Bean implementation class ReportBean
 */
@Stateless(mappedName = "ejb/Report")
public class ReportEJB implements Report {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	String QUERY_TRACEABILITY_BY_DEVICE_CODE = "SELECT DISTINCT " +
	  "t.transaction_id, " +
	  "CASE WHEN tsu.code_type_id = 3 THEN tsu.user_names ELSE tsu.user_names || ' ' || tsu.user_second_names  END as company, " + 
	  "CASE WHEN TO_CHAR(tc.category_id) IS  NULL THEN '-'  ELSE TO_CHAR(tc.category_id) END AS categoria,  " +
	  "CASE WHEN tv.vehicle_plate IS NULL THEN '-' ELSE tv.vehicle_plate END AS Placa, "+
	  "t.transaction_time, " +
	  "decode (com.company_name,null,'-',com.company_name) as CONC, "+
	  "CASE  " +
	  "  WHEN s.station_bo_name IS NOT NULL THEN s.station_bo_name " + 
	  "  WHEN sbo.station_bo_name IS NOT NULL THEN sbo.station_bo_name  ELSE '-'  END as station, " + 
	  "CASE WHEN l.lane_code = '0' THEN '-' WHEN l.lane_code IS NULL THEN '-' ELSE l.lane_code END as lane, " + 
	  "t.previous_balance, " +
	  "t.transaction_value,  " +
	  "t.new_balance,  " +
	  "ttype.transaction_type_name AS transaccion, " + 
	  "t.transaction_description, " +
	  "t.account_id as account,  " +
	  "d.device_facial_id as facial, " +
	  "decode(t.manual_transaction,'0','AUTOM햀ICO','1','MANUAL',NULL,'N/A') " +
	  "FROM   " +
	  "tb_transaction t " +
	  "INNER JOIN tb_transaction_type ttype ON ttype.transaction_type_Id = t.transaction_type_id " +
	  "LEFT JOIN tb_device d ON d.device_id = t.device_id  " +
	  "LEFT JOIN tb_vehicle tv on tv.vehicle_id = t.vehicle_id " +  
	  "LEFT JOIN tb_category tc ON tv.category_id = tc.category_id " + 
	  "LEFT JOIN re_account_device rad ON rad.device_id = t.device_id OR t.vehicle_id = rad.vehicle_id " +
	  "LEFT JOIN tb_account ta ON rad.account_id = ta.account_id " +
	  "LEFT JOIN tb_system_user tsu ON tsu.user_id = ta.user_id  " +
	  "LEFT JOIN tb_Lane l ON l.lane_id = t.lane_id  " +
	  "LEFT JOIN tb_station_bo s ON l.station_id = s.station_bo_id " + 
	  "LEFT JOIN tb_station_bo sbo ON sbo.station_bo_id = t.station_bo_id " +
	  "LEFT JOIN tb_company com ON (s.company_id = com.company_id or sbo.company_id = com.company_id) " +
	  "WHERE " +    
	  "d.device_code = ?1 " +
	  "AND t.account_id IS NOT NULL " +
	  "and t.transaction_type_id in (2,1,5,6) " + 
	  "AND t.transaction_time BETWEEN ?2 AND ?3 " +
	  "UNION " + 
	  "SELECT DISTINCT " +
	  "t.transaction_id, " +
	  "'TODOS LOS CLIENTES' as company, " + 
	  "CASE WHEN TO_CHAR(tc.category_id) IS  NULL THEN '-'  ELSE TO_CHAR(tc.category_id) END AS categoria,  " +
	  "CASE WHEN tv.vehicle_plate IS NULL THEN '-' ELSE tv.vehicle_plate END AS Placa, "+
	  "t.transaction_time, " +
	  "decode (com.company_name,null,'-',com.company_name) as CONC, "+
	  "CASE  " +
	  "  WHEN s.station_bo_name IS NOT NULL THEN s.station_bo_name " + 
	  "  WHEN sbo.station_bo_name IS NOT NULL THEN sbo.station_bo_name  ELSE '-'  END as station, " + 
	  "CASE WHEN l.lane_code = '0' THEN '-' WHEN l.lane_code IS NULL THEN '-' ELSE l.lane_code END as lane, " + 
	  "t.previous_balance, " +
	  "t.transaction_value,  " +
	  "t.new_balance,  " +
	  "ttype.transaction_type_name AS transaccion, " + 
	  "t.transaction_description, " +
	  "t.account_id as account,  " +
	  "d.device_facial_id as facial, " +
	  "decode(t.manual_transaction,'0','AUTOM햀ICO','1','MANUAL',NULL,'N/A') " +
	  "FROM   " +
	  "tb_transaction t " +
	  "INNER JOIN tb_transaction_type ttype ON ttype.transaction_type_Id = t.transaction_type_id " +
	  "LEFT JOIN tb_device d ON d.device_id = t.device_id  " +
	  "LEFT JOIN tb_vehicle tv on tv.vehicle_id = t.vehicle_id " +  
	  "LEFT JOIN tb_category tc ON tv.category_id = tc.category_id " + 
	  "LEFT JOIN re_account_device rad ON rad.device_id = t.device_id OR t.vehicle_id = rad.vehicle_id " +
	  "LEFT JOIN tb_account ta ON rad.account_id = ta.account_id " +
	  "LEFT JOIN tb_system_user tsu ON tsu.user_id = ta.user_id  " +
	  "LEFT JOIN tb_Lane l ON l.lane_id = t.lane_id  " +
	  "LEFT JOIN tb_station_bo s ON l.station_id = s.station_bo_id " + 
	  "LEFT JOIN tb_station_bo sbo ON sbo.station_bo_id = t.station_bo_id " +
	  "LEFT JOIN tb_company com ON (s.company_id = com.company_id or sbo.company_id = com.company_id) " +
	  "WHERE " +    
	  "'-1' = ?1 " +
	  "AND t.account_id IS NOT NULL " +
	  "and t.transaction_type_id in (2,1,5,6) " +
	  "AND t.transaction_time BETWEEN ?2 AND ?3 " +
	  "ORDER BY 5 ";
	
	String QUERY_USER_STATEMENT_ACCOUNT = "SELECT "
			+ "td.device_code, "
			+ "CASE WHEN tv.vehicle_plate IS NULL THEN '-' ELSE tv.vehicle_plate END AS Placa, "
			+ "CASE WHEN TO_CHAR(tc.category_id) IS  NULL THEN '-'  ELSE TO_CHAR(tc.category_id) END AS categoria, "
			+ "tt.transaction_time, "
			+ "CASE WHEN ts.station_name IS NULL THEN sbo.station_bo_name ELSE ts.station_name END as station, "
			+ "CASE WHEN tl.lane_code = '0' THEN '-' WHEN tl.lane_code IS NULL THEN '-' ELSE tl.lane_code END as lane, "
			+ "tt.previous_balance, "
			+ "tt.transaction_value, "
			+ "tt.new_balance, "
			+ "CASE WHEN tt.transaction_type = '1' THEN 'DEBITO' ELSE 'CREDITO' END AS transaccion "
			+ "FROM  "
			+ "tb_system_user tsu "
			+ "INNER JOIN tb_account ta ON ta.account_id = tsu.account_id "
			+ "INNER JOIN re_account_device rad ON rad.account_id = ta.account_id "
			+ "LEFT JOIN re_account_device_vehicle radv ON radv.account_device_id = rad.account_device_id "
			+ "LEFT JOIN tb_vehicle tv ON tv.vehicle_id = radv.vehicle_id  "
			+ "LEFT JOIN tb_category tc ON tv.category_id = tc.category_id "
			+ "INNER JOIN tb_device td ON td.device_id =  rad.device_id "
			+ "INNER JOIN tb_transaction tt ON tt.device_id = td.device_id "
			+ "LEFT JOIN tb_lane tl ON tl.lane_id = tt.lane_id "
			+ "LEFT JOIN tb_station ts ON tl.station_id = ts.station_id "
			+ "LEFT JOIN tb_station_bo  sbo ON sbo.station_bo_id = tt.station_bo_id  "
			+ "WHERE " + "tsu.user_code = ?1 " 
			+ "AND tt.transaction_time BETWEEN ?2 AND ?3 "
			+ "ORDER BY "
			+ "tt.device_id,tt.transaction_time "; 
	
	String QUERY_MASTER = "SELECT "
			+    "  td.device_facial_id,  "
			+	"  tv.vehicle_plate,  " 
			+	"  tv.vehicle_chassis, " 
			+	"  tv.vehicle_internal_code, "
			+	"  tset.special_exempt_type_name, "
			+	"  dc.category_concession, "
			+	"  dc.category_invias, " 
			+	"  dc.filing_number, " 
			+	"  dc.filing_date, "
			+	"  CASE WHEN tsu.code_type_id = 3 THEN tsu.user_names ELSE tsu.user_names || ' ' || tsu.user_second_names END AS user_name, "
			+	"  tseo.office_name, "
			+	"  tdp.department_name, " 
			+	"  tc.color_name, "
			+	"  tb.brand_name,  "
			+	"  dc.official_document_number, " 
			+	"  dc.official_document_date,  "
			+	"  dc.pay,  "
			+	"  dc.creation_reposition_user, "
			+	"  dc.creation_reposition_date, "
			+	"  dc.observation,  "
			+	"  tdcs.customization_state_name, " 
			+	"  dc.customization_user,  "
			+	"  dc.customization_date,  "
			+	"  td.device_code, "
			+	"  tr.replacement_cause_name," 
			+	   "dc.device_customization_id  "
			+	"FROM "
			+	"  tb_device_customization dc "
			+	"  LEFT JOIN tb_device td ON td.device_id = dc.device_id "
			+	"  INNER JOIN tb_vehicle tv ON tv.vehicle_id = dc.vehicle_id "
			+	"  INNER JOIN tb_special_exempt_type tset ON tset.special_exempt_type_id = dc.special_exempt_type_id "
			+	"  LEFT JOIN tb_account ta ON ta.account_id = dc.account_id "
			+	"  LEFT JOIN tb_system_user tsu ON tsu.user_id = ta.user_id "
			+	"  LEFT JOIN tb_special_exempt_office tseo ON tseo.special_exempt_office_id = dc.special_exempt_office_id "
			+	"  LEFT JOIN tb_department tdp ON tdp.department_id = dc.department_id "
			+	"  LEFT JOIN tb_color tc ON tc.color_id = tv.color_id "
			+	"  LEFT JOIN tb_brand tb ON tb.brand_id = tv.brand_id "
			+	"  LEFT JOIN tb_device_customization_state tdcs ON tdcs.device_customization_state_id = dc.device_customization_state_id "
			+	"  LEFT JOIN tb_replacement_cause tr ON tr.replacement_cause_id = dc.replacement_cause_id "
			+	"  LEFT JOIN tb_device_type tdt ON tdt.device_type_id = tset.device_type_id ";
	
	String QUERY_TRACEABILITY_BY_PLATE = "SELECT " +
    "DISTINCT (t.transaction_id), " +
    "CASE WHEN tsu.code_type_id = 3 THEN tsu.user_names ELSE tsu.user_names || ' ' || tsu.user_second_names  END as company, " + 
    "CASE WHEN TO_CHAR(tc.category_id) IS  NULL THEN '-'  ELSE TO_CHAR(tc.category_id) END AS categoria,  " +
    "CASE WHEN d.device_facial_id IS NULL THEN '-' ELSE d.device_facial_id END AS Facial,  "+
    "t.transaction_time,  " +
    "decode (com.company_name,null,'-',com.company_name) as CONC, "+
	"	 CASE  " +
    "  WHEN s.station_bo_name IS NOT NULL THEN s.station_bo_name " +  
    "  WHEN sbo.station_bo_name IS NOT NULL THEN sbo.station_bo_name  ELSE '-'  END as station, " + 
    "CASE WHEN l.lane_code = '0' THEN '-' WHEN l.lane_code IS NULL THEN '-' ELSE l.lane_code END as  lane, " + 
  	"t.previous_balance,  " +
    "t.transaction_value,  " +
    "t.new_balance,   " +
    "ttype.transaction_type_name AS transaccion, " + 
    "t.transaction_description,  " +
    "t.account_id, " +
    "tv.vehicle_plate As plate, d.device_code As device, " +
    "decode(t.manual_transaction,'0','AUTOM햀ICO','1','MANUAL',NULL,'N/A') " +
  	"FROM    " +
    "Tb_Transaction t " + 
    "INNER JOIN tb_transaction_type ttype ON ttype.transaction_type_Id = t.transaction_type_id " +
    "LEFT JOIN tb_device d ON d.device_id = t.device_id   " +
    "LEFT JOIN tb_vehicle tv on tv.vehicle_id = t.vehicle_id  " + 
    "LEFT JOIN tb_category tc ON tv.category_id = tc.category_id  " +
    "LEFT JOIN re_account_device rad ON rad.device_id = t.device_id and t.vehicle_id = rad.vehicle_id " +
    "LEFT JOIN tb_account ta ON rad.account_id = ta.account_id  " +
    "LEFT JOIN tb_system_user tsu ON tsu.user_id = ta.user_id  " +
    "LEFT JOIN tb_Lane l ON l.lane_id = t.lane_id    " +
    "LEFT JOIN tb_station_bo s ON l.station_id = s.station_bo_id " + 
    "LEFT JOIN tb_station_bo sbo ON sbo.station_bo_id = t.station_bo_id " +
    "LEFT JOIN tb_company com ON (s.company_id = com.company_id or sbo.company_id = com.company_id) " +
    "WHERE " +    
    "tv.vehicle_plate = ?1 " +
    "AND t.account_id IS NOT NULL " +
    " and t.transaction_type_id in (2,1,5,6) "+
	"AND t.transaction_time BETWEEN ?2 AND (?3+interval '1' day) " +
	"UNION " +
	"SELECT " +
    "DISTINCT (t.transaction_id), " +
    "'TODOS LOS CLIENTES' as company, " + 
    "CASE WHEN TO_CHAR(tc.category_id) IS  NULL THEN '-'  ELSE TO_CHAR(tc.category_id) END AS categoria,  " +
    "CASE WHEN d.device_facial_id IS NULL THEN '-' ELSE d.device_facial_id END AS Facial,  "+
    "t.transaction_time,  " +
    "decode (com.company_name,null,'-',com.company_name) as CONC, "+
	"	 CASE  " +
    "  WHEN s.station_bo_name IS NOT NULL THEN s.station_bo_name " +  
    "  WHEN sbo.station_bo_name IS NOT NULL THEN sbo.station_bo_name  ELSE '-'  END as station, " + 
    "CASE WHEN l.lane_code = '0' THEN '-' WHEN l.lane_code IS NULL THEN '-' ELSE l.lane_code END as  lane, " + 
  	"t.previous_balance,  " +
    "t.transaction_value,  " +
    "t.new_balance,   " +
    "ttype.transaction_type_name AS transaccion, " + 
    "t.transaction_description,  " +
    "t.account_id, " +
    "tv.vehicle_plate As plate, d.device_code As device, " +
    "decode(t.manual_transaction,'0','AUTOM햀ICO','1','MANUAL',NULL,'N/A') " +
  	"FROM    " +
    "Tb_Transaction t " + 
    "INNER JOIN tb_transaction_type ttype ON ttype.transaction_type_Id = t.transaction_type_id " +
    "LEFT JOIN tb_device d ON d.device_id = t.device_id   " +
    "LEFT JOIN tb_vehicle tv on tv.vehicle_id = t.vehicle_id  " + 
    "LEFT JOIN tb_category tc ON tv.category_id = tc.category_id  " +
    "LEFT JOIN re_account_device rad ON rad.device_id = t.device_id and t.vehicle_id = rad.vehicle_id " +
    "LEFT JOIN tb_account ta ON rad.account_id = ta.account_id  " +
    "LEFT JOIN tb_system_user tsu ON tsu.user_id = ta.user_id  " +
    "LEFT JOIN tb_Lane l ON l.lane_id = t.lane_id    " +
    "LEFT JOIN tb_station_bo s ON l.station_id = s.station_bo_id " + 
    "LEFT JOIN tb_station_bo sbo ON sbo.station_bo_id = t.station_bo_id " +
    "LEFT JOIN tb_company com ON (s.company_id = com.company_id or sbo.company_id = com.company_id) " +
    "WHERE " +    
    "'-1' = ?1 " +
    "AND t.account_id IS NOT NULL " +
    " and t.transaction_type_id in (2,1,5,6) "+
	"AND t.transaction_time BETWEEN ?2 AND (?3+interval '1' day) " +
    "ORDER BY 5"; 
	
	
	String QUERY_TRACEABILITY_BY_PLATE_CLIENT = "SELECT " +
    "DISTINCT (t.transaction_id), " +
    "CASE WHEN tsu.code_type_id = 3 THEN tsu.user_names ELSE tsu.user_names || ' ' || tsu.user_second_names  END as company, " + 
    "CASE WHEN TO_CHAR(tc.category_id) IS  NULL THEN '-'  ELSE TO_CHAR(tc.category_id) END AS categoria,  " +
    "CASE WHEN d.device_facial_id IS NULL THEN '-' ELSE d.device_facial_id END AS Facial,  "+
    "t.transaction_time,  " +
    "decode (com.company_name,null,'-',com.company_name) as CONC, "+
	"	 CASE  " +
    "  WHEN s.station_bo_name IS NOT NULL THEN s.station_bo_name " +  
    "  WHEN sbo.station_bo_name IS NOT NULL THEN sbo.station_bo_name  ELSE '-'  END as station, " + 
    "CASE WHEN l.lane_code = '0' THEN '-' WHEN l.lane_code IS NULL THEN '-' ELSE l.lane_code END as  lane, " + 
  	"t.previous_balance,  " +
    "t.transaction_value,  " +
    "t.new_balance,   " +
    "ttype.transaction_type_name AS transaccion, " + 
    "t.transaction_description,  " +
    "t.account_id, " +
    "tv.vehicle_plate As plate, d.device_code As device " +
  	"FROM    " +
    "Tb_Transaction t " + 
    "INNER JOIN tb_transaction_type ttype ON ttype.transaction_type_Id = t.transaction_type_id " +
    "LEFT JOIN tb_device d ON d.device_id = t.device_id   " +
    "LEFT JOIN tb_vehicle tv on tv.vehicle_id = t.vehicle_id  " + 
    "LEFT JOIN tb_category tc ON tv.category_id = tc.category_id  " +
    "LEFT JOIN re_account_device rad ON rad.device_id = t.device_id and t.vehicle_id = rad.vehicle_id " +
    "LEFT JOIN tb_account ta ON rad.account_id = ta.account_id  " +
    "LEFT JOIN tb_system_user tsu ON tsu.user_id = ta.user_id  " +
    "LEFT JOIN tb_Lane l ON l.lane_id = t.lane_id    " +
    "LEFT JOIN tb_station_bo s ON l.station_id = s.station_bo_id " + 
    "LEFT JOIN tb_station_bo sbo ON sbo.station_bo_id = t.station_bo_id " +
    "LEFT JOIN tb_company com ON (s.company_id = com.company_id or sbo.company_id = com.company_id) " +
    "WHERE " +    
    "tv.vehicle_plate = ?1 " +
    "AND tsu.user_id= ?4 " +
    "AND t.account_id IS NOT NULL " +
	"AND t.transaction_time BETWEEN ?2 AND (?3+interval '1' day) " +
	"UNION " +
	"SELECT " +
    "DISTINCT (t.transaction_id), " +
    "CASE WHEN tsu.code_type_id = 3 THEN tsu.user_names ELSE tsu.user_names || ' ' || tsu.user_second_names  END as company, " + 
    "CASE WHEN TO_CHAR(tc.category_id) IS  NULL THEN '-'  ELSE TO_CHAR(tc.category_id) END AS categoria,  " +
    "CASE WHEN d.device_facial_id IS NULL THEN '-' ELSE d.device_facial_id END AS Facial,  "+
    "t.transaction_time,  " +
    "decode (com.company_name,null,'-',com.company_name) as CONC, "+
	"	 CASE  " +
    "  WHEN s.station_bo_name IS NOT NULL THEN s.station_bo_name " +  
    "  WHEN sbo.station_bo_name IS NOT NULL THEN sbo.station_bo_name  ELSE '-'  END as station, " + 
    "CASE WHEN l.lane_code = '0' THEN '-' WHEN l.lane_code IS NULL THEN '-' ELSE l.lane_code END as  lane, " + 
  	"t.previous_balance,  " +
    "t.transaction_value,  " +
    "t.new_balance,   " +
    "ttype.transaction_type_name AS transaccion, " + 
    "t.transaction_description,  " +
    "t.account_id, " +
    "tv.vehicle_plate As plate, d.device_code As device " +
  	"FROM    " +
    "Tb_Transaction t " + 
    "INNER JOIN tb_transaction_type ttype ON ttype.transaction_type_Id = t.transaction_type_id " +
    "LEFT JOIN tb_device d ON d.device_id = t.device_id   " +
    "LEFT JOIN tb_vehicle tv on tv.vehicle_id = t.vehicle_id  " + 
    "LEFT JOIN tb_category tc ON tv.category_id = tc.category_id  " +
    "LEFT JOIN re_account_device rad ON rad.device_id = t.device_id and t.vehicle_id = rad.vehicle_id " +
    "LEFT JOIN tb_account ta ON rad.account_id = ta.account_id  " +
    "LEFT JOIN tb_system_user tsu ON tsu.user_id = ta.user_id  " +
    "LEFT JOIN tb_Lane l ON l.lane_id = t.lane_id    " +
    "LEFT JOIN tb_station_bo s ON l.station_id = s.station_bo_id " + 
    "LEFT JOIN tb_station_bo sbo ON sbo.station_bo_id = t.station_bo_id " +
    "LEFT JOIN tb_company com ON (s.company_id = com.company_id or sbo.company_id = com.company_id) " +
    "WHERE " +    
    "'-1' = ?1 " +
    "AND tsu.user_id= ?4 " +
    "AND t.account_id IS NOT NULL " +
	"AND t.transaction_time BETWEEN ?2 AND (?3+interval '1' day) " +
    "ORDER BY 4"; 
	
    /**
     * Default constructor. 
     */
    public ReportEJB() {
    }

    /*
     * (non-Javadoc)
     * @see ejb.Report#getUserData()
     */
	@Override
	public Object[][] getUserData() {
		Object[][] object = null;
		try{
			Query q = em.createQuery("SELECT us FROM TbSystemUser us");
			List<?> lista = q.getResultList();
			
			object = new  Object[lista.size()][2];
			for(int i = 0; i <lista.size(); i++){
				TbSystemUser u = (TbSystemUser) lista.get(i);
				object[i][0] = u.getUserNames();
				object[i][1] = u.getUserSecondNames();
			}
			
		}catch(Exception e){
			System.out.println(" [ Error en ReportEJB.getUserData ] ");
			e.printStackTrace();
		}
		return object;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Report#getDeviceCodes()
	 */
	@Override
	public List<String> getDeviceCodes() {
		List<String> list = new ArrayList<String>();
		try{
			Query q = em.createQuery("SELECT de FROM TbDevice de");
			for(Object o: q.getResultList()){
				list.add(((TbDevice) o).getDeviceCode());
			}
		}catch(Exception e){
			System.out.println(" [ Error en ReportEJB.getDeviceCodes ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Report#getTraceabilityByDeviceCode(java.util.Date,
	 * java.util.Date, java.lang.String)
	 */
	@Override
	public Object[][] getTraceabilityByDeviceCode(Date begDate, Date endDate,
			String deviceCode) {
		System.out.println("FecFin: "+endDate);
        
        System.out.println("deviceCode: "+deviceCode);
		Object[][] object = {{null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null}};
		if (begDate == null) {
			begDate = new Date();
		}
		if (endDate == null) {
			endDate = new Date();
		}
		try{
			Query q;

			if(deviceCode==""){
			    deviceCode="-1";	
			}
			q = em.createNativeQuery(QUERY_TRACEABILITY_BY_DEVICE_CODE);
			q.setParameter(1, deviceCode);
			q.setParameter(2,new Timestamp(begDate.getTime())).setParameter(3,
							new Timestamp(endDate.getTime() + 86399000));
			
			List<?> list = q.getResultList();	
			if(list.size()>0){
				object = new  Object[list.size()][16];
				for(int i = 0; i <list.size(); i++){
					Object[] obj = (Object[]) list.get(i);
					
					object[i][0] = obj[1] != null ? obj[1].toString(): null ;
					object[i][1] = obj[2] != null ? obj[2].toString(): null;
					object[i][2] = obj[3] != null ? obj[3].toString(): null;
					object[i][3] = (Date)(obj[4]);
					object[i][4] = obj[5] != null ? obj[5].toString(): null;
					object[i][5] = obj[6] != null ? obj[6].toString(): null;
					object[i][6] = obj[7] != null ? obj[7].toString(): null;
					object[i][7] = ((BigDecimal) obj[8]).longValue();
					object[i][8] = ((BigDecimal) obj[9]).longValue();
					object[i][9] = ((BigDecimal) obj[10]).longValue();
					object[i][10] = obj[11] != null ? obj[11].toString(): null;
					object[i][11] = obj[12] != null ? obj[12].toString(): null;
					object[i][12] = obj[13] != null ? obj[13].toString(): null;
					object[i][13] = obj[14] != null ? obj[14].toString(): null;
					object[i][14] = obj[15] != null ? obj[15].toString(): null;
				}
			} else {
				object = null;
			}
		}catch(Exception e){
			System.out.println(" [ Error en ReportEJB.getTraceabilityByDeviceCode ] ");
			e.printStackTrace();
		}
		return object;
	}
	
	@Override
	public Object[][] getTraceabilityByPlate(Date begDate, Date endDate,
			String plate) {
		Object[][] object = {{null,null,null,null,null,null,null,null,null,null}};
		if (begDate == null) {
			begDate = new Date();
		}
		if (endDate == null) {
			endDate = new Date();
		}
		if(plate==""){
			plate="-1";
		}
		try{
			Query q = em.createNativeQuery(QUERY_TRACEABILITY_BY_PLATE)
					.setParameter(1, plate).setParameter(2,
							new Timestamp(begDate.getTime())).setParameter(3,
							new Timestamp(endDate.getTime()));
			
			List<?> list = q.getResultList();	
			if(list.size()>0){
				object = new  Object[list.size()][17];
				for(int i = 0; i <list.size(); i++){
					Object[] obj = (Object[]) list.get(i);
					
					object[i][0] = obj[1] != null ? obj[1].toString(): null ;
					object[i][1] = obj[2] != null ? obj[2].toString(): null;
					object[i][2] = obj[3] != null ? obj[3].toString(): null;
					object[i][3] = (Timestamp)(obj[4]);
					object[i][4] = obj[5] != null ? obj[5].toString(): null;
					object[i][5] = obj[6] != null ? obj[6].toString(): null;
					object[i][6] = obj[7] != null ? obj[7].toString(): null;
					object[i][7] = ((BigDecimal) obj[8]).longValue();
					object[i][8] = ((BigDecimal) obj[9]).longValue();
					object[i][9] = ((BigDecimal) obj[10]).longValue();
					object[i][10] = obj[11] != null ? obj[11].toString(): null;
					object[i][11] = obj[12] != null ? obj[12].toString(): null;
					object[i][12] = obj[13] != null ? obj[13].toString(): null;
					object[i][13] = obj[14] != null ? obj[14].toString(): null;
					object[i][14] = obj[15] != null ? obj[15].toString(): null;
					object[i][15] = obj[16] != null ? obj[16].toString(): null;
				}
			} else {
				object = null;
			}
		}catch(Exception e){
			System.out.println(" [ Error en ReportEJB.getTraceabilityByPlate ] ");
			e.printStackTrace();
		}
		return object;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Report#getUserStatementAccount(java.util.Date, java.util.Date,
	 * java.lang.String)
	 */
	@Override
	public Object[][] getUserStatementAccount(Date begDate, Date endDate,
			String userCode) {
		Object[][] object = {{null,null,null,null,null,null,null,null,null,null}};
		try{
			Query q = em.createNativeQuery(QUERY_USER_STATEMENT_ACCOUNT)
					.setParameter(1, userCode).setParameter(2,
							new Timestamp(begDate.getTime())).setParameter(3,
							new Timestamp(endDate.getTime()));
			
			List<?> list = q.getResultList();	
			if(list.size()>0) {
				object = new  Object[list.size()][10];
			
				for(int i = 0; i <list.size(); i++){
					Object[] obj = (Object[]) list.get(i);
					
					
					object[i][0] = obj[0]!= null ? obj[0].toString(): null ;
					object[i][1] = obj[1]!= null ? obj[1].toString(): null ;
					object[i][2] = obj[2]!= null ? obj[2].toString(): null ;
					object[i][3] = (Date)(obj[3]);
					object[i][4] = obj[4]!= null ? obj[4].toString(): null ;
					object[i][5] = obj[5]!= null ? obj[5].toString(): null ;
					object[i][6] = ((BigDecimal) obj[6]).longValue();
					object[i][7] = ((BigDecimal) obj[7]).longValue();
					object[i][8] = ((BigDecimal) obj[8]).longValue();
					object[i][9] = obj[9]!= null ? obj[9].toString(): null ;
					
				}
			} else {
				object = null;
			}
		}catch(Exception e){
			System.out.println(" [ Error en ReportEJB.getUserStatementAccount ] ");
			e.printStackTrace();
		}
		return object;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Report#getUserCodes()
	 */
	@Override
	public List<String> getUserCodes() {
		List<String> list = new ArrayList<String>();
		try{
			Query q = em.createQuery("SELECT DISTINCT su.tbSystemUser FROM TbAccount su ");
			for(Object o: q.getResultList()){
				list.add(((TbSystemUser) o).getUserCode());
			}
		}catch(Exception e){
			System.out.println(" [ Error en ReportEJB.getUserCodes ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Report#getTbSystemUserByCode(java.lang.String)
	 */
	@Override
	public TbSystemUser getTbSystemUserByCode(String userCode) {
		TbSystemUser user = null;
		try{
			Query q = em.createQuery(
					"SELECT ts FROM TbSystemUser ts WHERE ts.userCode = ?1")
					.setParameter(1, userCode);
			user = (TbSystemUser) q.getSingleResult();
		}catch(Exception e){
			System.out.println(" [ Error en ReportEJB.getTbSystemUserByCode ] ");
			e.printStackTrace();
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.report.Report#getAllClients()
	 */
	@Override
	public Object[][] getAllClients() {
		Object[][] object = null;
		try{
			Query q = em.createQuery("SELECT us FROM TbUserData us");
			List<?> lista = q.getResultList();
			
			object = new  Object[lista.size()][5];
			for(int i = 0; i <lista.size(); i++){
				TbUserData u = (TbUserData) lista.get(i);
				object[i][0] = u.getTbSystemUser().getUserCode();
				if(u.getTbSystemUser().getTbCodeType().equals("3")){
					object[i][1] = u.getTbSystemUser().getUserNames();
				}else {
					object[i][1] = u.getTbSystemUser().getUserNames() + " "  +u.getTbSystemUser().getUserSecondNames();
				}
				
				object[i][2] = u.getUserDataAddress();
				
				if(u.getUserDataPhone()!=null){
					object[i][3] = u.getUserDataPhone().toString();
				} else{
					object[i][3] = null;
				}
				
				if(u.getUserDataOptionalPhone()!=null){
					object[i][4] = u.getUserDataOptionalPhone().toString();
				} else{
					object[i][4] = null;
				}
			}
			
		}catch(Exception e){
			System.out.println(" [ Error en ReportEJB.getUserData ] ");
			e.printStackTrace();
		}
		return object;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.report.Report#getMasterByType(java.lang.Long, boolean)
	 */
	@Override
	public Object[][] getMasterByType(Long deviceTypeId, boolean applyCondition) {
		Object[][] object = {{null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,null,null,null,null,
			null,null,null,null}};
		try{
			String query = QUERY_MASTER;
			
			if (applyCondition) {
				query += " WHERE  tdt.device_type_id = ?1 ";
			}	
			
			query += " ORDER BY dc.creation_reposition_date ";
			
			Query q = em.createNativeQuery(query);
			
			if (applyCondition) {
				q.setParameter(1, deviceTypeId);
			}
			
			List<?> list = q.getResultList();	
			if (list.size() > 0) {
				object = new Object[list.size()][28];
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = (Object[]) list.get(i);
					
					String dep = "";
					String sta = "";
					
					TbDeviceCustomization cus = em.find(
								TbDeviceCustomization.class, Long
										.parseLong(obj[25].toString()));
						
					if(cus.getTbSpecialExemptType().getTbDeviceType().getDeviceTypeId().longValue() != 4L){
						
						Query q1 = em.createQuery("SELECT ss FROM TbSpecialStation ss WHERE ss.tbDeviceCustomization.deviceCustomizationId = ?1");
						q1.setParameter(1, cus.getDeviceCustomizationId());
						
						for(Object ob: q1.getResultList()){
							TbSpecialStation ss = (TbSpecialStation) ob;
							
							dep = ss.getTbStationBO().getTbDepartment().getDepartmentName();
							sta += " " + ss.getTbStationBO().getStationBOName() + " -";
						}
						sta = sta.substring(0, sta.length() - 1);
					}

					object[i][0] = obj[0] != null ? obj[0].toString() : null;
					object[i][1] = obj[1] != null ? obj[1].toString() : null;
					object[i][2] = obj[2] != null ? obj[2].toString() : null;
					object[i][3] = obj[3] != null ? obj[3].toString() : null;
					object[i][4] = obj[4].toString();
					object[i][5] = obj[5].toString();
					object[i][6] = obj[6].toString();
					object[i][7] = obj[7].toString();
					object[i][8] = (Date) (obj[8]);
					object[i][9] = obj[9].toString();
					object[i][10] = obj[10] != null ? obj[10].toString(): null;
		
					if(cus.getTbSpecialExemptType().getTbDeviceType().getDeviceTypeId().longValue() != 4L){
						object[i][11] = dep;
					}else {
						object[i][11] = obj[11].toString();
					}
					object[i][12] = obj[12].toString();
					object[i][13] = obj[13].toString();
					object[i][14] = obj[14].toString();
					object[i][15] = (Date) (obj[15]);
					object[i][16] = obj[16].toString();
					object[i][17] = sta;
					
					TbSystemUser user = em.find(TbSystemUser.class, Long.parseLong(obj[17].toString()));
					
					object[i][18] = user.getUserNames() + " " + user.getUserSecondNames();
					object[i][19] = (Date) (obj[18]);
					object[i][20] = obj[19].toString();
					object[i][21] = null;
					object[i][22] = obj[20].toString();
					object[i][23] = obj[21] != null ? obj[21].toString(): null;
					object[i][24] = (Date) (obj[22]);
					object[i][25] = null;
					object[i][26] = obj[23] != null ? obj[23].toString(): null;
					object[i][27] = obj[24] != null ? obj[24].toString(): null;
				}
			} 
		} catch (Exception e) {
			System.out.println(" [ Error en ReportEJB.getMasterByType ] ");
			e.printStackTrace();
		}
		return object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.report.Report#getEntryWarehouseByDate(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public Object[][] getEntryWarehouseByDate(Date initDate, Date endDate, 
			Long warehouseStateId) {
		Object[][] object = null;
		try {
			String query = "SELECT wa FROM TbWarehouse wa WHERE wa.creationDate BETWEEN ?1 AND ?2 ";
			
			if (warehouseStateId != -1L) {
				query += " AND wa.tbWarehouseState.warehouseStateId = ?3 ";
			}
			
			Query q = em.createQuery(query +	" ORDER BY wa.creationDate, wa.tbWarehouseState.warehouseStateId ");
			q.setParameter(1, new Timestamp(initDate.getTime()));
			q.setParameter(2, new Timestamp(endDate.getTime() + 86399000));
			
			if (warehouseStateId != -1L) {
				q.setParameter(3, warehouseStateId);
			}
			
			List<?> lista = q.getResultList();
			
			if(lista.size() > 0){
				object = new  Object[lista.size()][5];
				for (int i = 0; i < lista.size(); i++) {
					TbWarehouse w = (TbWarehouse) lista.get(i);
					
					object[i][0] = w.getCreationDate();
					object[i][1] = w.getOrderNumber();
					object[i][2] = w.getDeviceQuantity();
					object[i][3] = w.isWarehouseIsCard() ? "Tarjetas" : "Tags";
					object[i][4] = w.getTbWarehouseState().getWarehouseStateName();
				}
			}
		} catch (Exception e) {
			
		}
		return object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.report.Report#getEntryWarehouseByNumber(java.lang.String)
	 */
	@Override
	public WarehouseTo getEntryWarehouseByNumber(String entryNumber) {
		WarehouseTo war = null;
		try {
			Query q = em.createQuery("SELECT wa FROM TbWarehouse wa WHERE wa.orderNumber = ?1  " +
				" AND wa.tbWarehouseState.warehouseStateId NOT IN (?2)").setMaxResults(1);
			
			q.setParameter(1, entryNumber);
			q.setParameter(2, WarehouseState.CANCELED.getId());
			
			TbWarehouse w = (TbWarehouse) q.getSingleResult();
			
			war = new WarehouseTo();
			war.setWarehouse(w);
				
			q= em.createQuery("SELECT re FROM ReDeviceWarehouse re WHERE re.tbWarehouse.warehouseId = ?1 " +
					" ORDER BY re.deviceWarehouseDate ");
			q.setParameter(1, w.getWarehouseId());
			
			List<?> lista = q.getResultList();
			
			if (lista.size() > 0) {
				
				war.setLeft((w.getDeviceQuantity().longValue() - lista.size()));
				
				Object object[][] = new Object[lista.size()][2];
				for (int i = 0; i < lista.size(); i++) {
					ReDeviceWarehouse r = (ReDeviceWarehouse) lista.get(i);

					object[i][0] = r.getTbDevice().getDeviceCode();
					object[i][1] = r.getDeviceWarehouseDate();
				}
				
				war.setObj(object);
			} else {
				war.setLeft(w.getDeviceQuantity());
			}
		
		} catch (NoResultException e2) {
		} catch (Exception e) {
			System.out.println(" [ Error en ReportEJB.getEntryWarehouseByNumber ] ");
			e.printStackTrace();
		}
		return war;
	}


	@Override
	public Object[][] getTraceabilityByPlateClient(Date begDate, Date endDate,
			String plate, Long user) {
		Object[][] object = {{null,null,null,null,null,null,null,null,null,null}};
		if (begDate == null) {
			begDate = new Date();
		}
		if (endDate == null) {
			endDate = new Date();
		}
		try{
			Query q = em.createNativeQuery(QUERY_TRACEABILITY_BY_PLATE_CLIENT)
					.setParameter(1, plate).setParameter(2,
							new Timestamp(begDate.getTime())).setParameter(3,
							new Timestamp(endDate.getTime())).setParameter(4, user);
			
			List<?> list = q.getResultList();	
			if(list.size()>0){
				object = new  Object[list.size()][15];
				for(int i = 0; i <list.size(); i++){
					Object[] obj = (Object[]) list.get(i);
					
					object[i][0] = obj[1] != null ? obj[1].toString(): null ;
					object[i][1] = obj[2] != null ? obj[2].toString(): null;
					object[i][2] = obj[3] != null ? obj[3].toString(): null;
					object[i][3] = (Timestamp)(obj[4]);
					object[i][4] = obj[5] != null ? obj[5].toString(): null;
					object[i][5] = obj[6] != null ? obj[6].toString(): null;
					object[i][6] = obj[7] != null ? obj[7].toString(): null;
					object[i][7] = ((BigDecimal) obj[8]).longValue();
					object[i][8] = ((BigDecimal) obj[9]).longValue();
					object[i][9] = ((BigDecimal) obj[10]).longValue();
					object[i][10] = obj[11] != null ? obj[11].toString(): null;
					object[i][11] = obj[12] != null ? obj[12].toString(): null;
					object[i][12] = obj[13] != null ? obj[13].toString(): null;
					object[i][13] = obj[14] != null ? obj[14].toString(): null;
					object[i][14] = obj[15] != null ? obj[15].toString(): null;
					
					System.out.println("placa " +obj[14] != null ? obj[14].toString(): null);
					System.out.println("dispositivo " +obj[15] != null ? obj[15].toString(): null);
				}
			} else {
				object = null;
			}
		}catch(Exception e){
			System.out.println(" [ Error en ReportEJB.getTraceabilityByPlateClient ] ");
			e.printStackTrace();
		}
		return object;
	}
	
	@Override
	public boolean getExitDataForReportTrasability(String deviceCode, Date begDate,
			Date endDate, Long user) {
		boolean result = false;
		String resultado=null;
		try{
			String qry="select d.device_code " +
					"FROM   " +
					  "tb_transaction t " +
					  "INNER JOIN tb_transaction_type ttype ON ttype.transaction_type_Id = t.transaction_type_id " +
					  "LEFT JOIN tb_device d ON d.device_id = t.device_id  " +
					  "LEFT JOIN tb_vehicle tv on tv.vehicle_id = t.vehicle_id " +  
					  "LEFT JOIN tb_category tc ON tv.category_id = tc.category_id " + 
					  "LEFT JOIN re_account_device rad ON rad.device_id = t.device_id OR t.vehicle_id = rad.vehicle_id " +
					  "LEFT JOIN tb_account ta ON rad.account_id = ta.account_id " +
					  "LEFT JOIN tb_system_user tsu ON tsu.user_id = ta.user_id  " +
					  "LEFT JOIN tb_Lane l ON l.lane_id = t.lane_id  " +
					  "LEFT JOIN tb_station_bo s ON l.station_id = s.station_bo_id " + 
					  "LEFT JOIN tb_station_bo sbo ON sbo.station_bo_id = t.station_bo_id " +
					  "LEFT JOIN tb_company com ON (s.company_id = com.company_id or sbo.company_id = com.company_id) " +
					  "WHERE " +    
					  "tsu.user_id =?4 " +
					  "AND d.device_code = ?1 " +
					  "AND t.account_id IS NOT NULL " +
					  "AND t.transaction_time BETWEEN ?2 AND (?3 +interval '1' day) " +
					  " UNION " +
					  "select d.device_code " +
						"FROM   " +
						  "tb_transaction t " +
						  "INNER JOIN tb_transaction_type ttype ON ttype.transaction_type_Id = t.transaction_type_id " +
						  "LEFT JOIN tb_device d ON d.device_id = t.device_id  " +
						  "LEFT JOIN tb_vehicle tv on tv.vehicle_id = t.vehicle_id " +  
						  "LEFT JOIN tb_category tc ON tv.category_id = tc.category_id " + 
						  "LEFT JOIN re_account_device rad ON rad.device_id = t.device_id OR t.vehicle_id = rad.vehicle_id " +
						  "LEFT JOIN tb_account ta ON rad.account_id = ta.account_id " +
						  "LEFT JOIN tb_system_user tsu ON tsu.user_id = ta.user_id  " +
						  "LEFT JOIN tb_Lane l ON l.lane_id = t.lane_id  " +
						  "LEFT JOIN tb_station_bo s ON l.station_id = s.station_bo_id " + 
						  "LEFT JOIN tb_station_bo sbo ON sbo.station_bo_id = t.station_bo_id " +
						  "LEFT JOIN tb_company com ON (s.company_id = com.company_id or sbo.company_id = com.company_id) " +
						  "WHERE " +    
						  "tsu.user_id =?4 " +
						  "AND '-1' = ?1 " +
						  "AND t.account_id IS NOT NULL " +
						  "AND t.transaction_time BETWEEN ?2 AND (?3 +interval '1' day) ";
			
			Query q = em.createNativeQuery(qry);
			q.setParameter(1, deviceCode);
			q.setParameter(2, begDate);
			q.setParameter(3, endDate);
			q.setParameter(4, user).setMaxResults(1);
			
			resultado= ((String)q.getSingleResult());
			System.out.println("resultado: "+resultado);
			System.out.println("user: "+user);
			System.out.println("deviceCode: "+deviceCode);
			if(resultado !=null){
				result = true;
			}
		}catch(Exception e){
			System.out.println(" [ Error en ReportEJB.getExitDataForReportTrasability ] ");
			e.printStackTrace();			
		}
		return result;
	}
	
	
}