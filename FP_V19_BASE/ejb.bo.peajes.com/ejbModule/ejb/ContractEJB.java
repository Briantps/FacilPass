package ejb;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbAcceptsContract;
import jpa.TbDocument;
import jpa.TbSignatureParameter;
import jpa.TbStamp;
import jpa.TbSystemUser;
import jpa.TbTypeDocument;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import util.Email;
import util.FileUtil;
import co.com.certicamara.commons.ProcessResponseSign;
import co.com.certicamara.commons.response.MessageResponse;
import co.com.certicamara.kronos.TSAAuthentication;
import co.com.certicamara.kronos.TSAProperties;
import co.com.certicamara.kronos.authentications.TSACertAuthentication;
import co.com.certicamara.sign.Sign;
import co.com.certicamara.sign.SignFactory;
import co.com.certicamara.sign.SignatureParameters;
import co.com.certicamara.sign.certificate.CertificateConfiguration;
import co.com.certicamara.sign.certificate.CertificateFromBytes;
import co.com.certicamara.sign.pdf.PdfParameters;
import co.com.certicamara.sign.utils.UtilsSign;
import co.com.certicamara.verify.certificates.revocation.RevocationVerify;
import co.com.certicamara.verify.certificates.revocation.VerifyType;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;

import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import ejb.email.Outbox;

/**
 * Class ContractEJB
 * @author jromero
 */

@Stateless(mappedName = "ejb/Contract")
public class ContractEJB implements Contract{

	@PersistenceContext(unitName = "bo") 
	EntityManager em;

	@EJB(mappedName ="ejb/sendMail")
	private SendMail sendMail;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	/**
	 * EJB que se encarga de enviar la notificacion por el gestor de correos
	 */
	@EJB(mappedName = "ejb/email/Outbox")
	private Outbox outbox;
	
	private String msgEmail;
	private String msgLog;
	
	private String p12Certificate;
    private String certificatePassword;
    private String tsaCertificate;
    private String tsacertificatePassword;    
    private String pdfSignPath;
    private String pdfSignImagePath;
    private String signReason;
    private String signLocation;
    private String signFieldName;
    private String pdfSignedPath;
    
    private int signPage;
    private int lowerLeftX;
    private int lowerLeftY;
    private int upperRightX;
    private int upperRightY;
    
    private String crlPath;
    private String keystorePath;
    
    private String[] urlSigned;

    private String tsaURL;
    private String tsaPolicyOID;
    private String contentSignature;
    private String hashalgorithm;
	private String userTo;
	private String subjectE; 
	
	@SuppressWarnings("unused")
	private static final String ALGORITHM = "MD5";
    @SuppressWarnings("unused")
	private static final String DIGEST_STRING = "HG58YZ3CR9";
    private static final String CHARSET_UTF_8 = "UTF-8";
    @SuppressWarnings("unused")
	private static final String SECRET_KEY_ALGORITHM = "DESede";
    @SuppressWarnings("unused")
	private static final String TRANSFORMATION_PADDING = "DESede/CBC/PKCS5Padding";
	
	@Override
	public boolean signContract(Long userID,Long documentId,String ip) {
		boolean result = false;
        pdfSignedPath = "";
        TbSystemUser user=null;
        Long idDocument=0L;
        //------URL ESTAMPADO DE PRODUCCION---------
//        tsaURL = "http://tsa.certicamara.com:9235";
        //------------------------------------------

      //------URL ESTAMPADO DE PRUEBAS------------
        //tsaURL = "http://190.131.205.170:9235";
        //------------------------------------------
		try{
			user=em.find(TbSystemUser.class, userID);
			TbDocument doc=em.find(TbDocument.class, documentId);
			pdfSignPath = doc.getUrlDocument().replace("\\", "/");
			
			Query q=em.createNativeQuery("select SIGN_PARAM_NAME,SIGN_PARAM_VALUE " +
					"from TB_SIGNATURE_PARAMETER where SIGN_PARAM_STATE=1");
			List<?> list = (List<?>)q.getResultList();
			for (Object obje : list) {
				if (obje != null && obje instanceof Object[]) {
					Object[] datos = (Object[]) obje;
					if(datos[0].equals("p12Certificate")){
						p12Certificate=(String)datos[1];
					}else if(datos[0].equals("certificatePassword")){
						certificatePassword=(String)datos[1]!=null?
								decodePass((String)datos[1]):null;
					}else if(datos[0].equals("tsaCertificate")){
						tsaCertificate=(String)datos[1];
					}else if(datos[0].equals("tsacertificatePassword")){
						tsacertificatePassword=(String)datos[1]!=null?
								decodePass((String)datos[1]):null;
					}else if(datos[0].equals("pdfSignImagePath")){
						pdfSignImagePath=(String)datos[1];
					}else if(datos[0].equals("signReason")){
						signReason=(String)datos[1];
					}else if(datos[0].equals("signLocation")){
						signLocation=(String)datos[1];
					}else if(datos[0].equals("signFieldName")){
						signFieldName=(String)datos[1];
					}else if(datos[0].equals("contentSignature")){
						contentSignature=(String)datos[1]==null?"":(String)datos[1];
					}else if(datos[0].equals("signPage")){
						signPage=Integer.parseInt((String)datos[1]);
					}else if(datos[0].equals("lowerLeftX")){
						lowerLeftX=Integer.parseInt((String)datos[1]);
					}else if(datos[0].equals("upperRightX")){
						upperRightX=Integer.parseInt((String)datos[1]);
					}else if(datos[0].equals("crlPath")){
						crlPath=(String)datos[1];
					}else if(datos[0].equals("keystorePath")){
						keystorePath=(String)datos[1];
					}else if(datos[0].equals("tsaURL")){
						tsaURL=(String)datos[1];
					}else if(datos[0].equals("tsaPolicyOID")){
						tsaPolicyOID=(String)datos[1];
					}else if(datos[0].equals("hashalgorithm")){
						hashalgorithm=(String)datos[1];
					}else if(datos[0].equals("userTo")){
						userTo=(String)datos[1];
					}else if(datos[0].equals("subjectE")){
						subjectE=(String)datos[1];
					}else{
						if(user.getTbCodeType().getCodeTypeId()==3){
							if(datos[0].equals("upperRightY_JURIDICA")){
								upperRightY=Integer.parseInt((String)datos[1]);
							}else if(datos[0].equals("lowerLeftY_JURIDICA")){
								lowerLeftY=Integer.parseInt((String)datos[1]);
							}
						}else{
							if(datos[0].equals("upperRightY_NATURAL")){
								upperRightY=Integer.parseInt((String)datos[1]);
							}else if(datos[0].equals("lowerLeftY_NATURAL")){
								lowerLeftY=Integer.parseInt((String)datos[1]);
							}
						}
					}
				}
			}
			//System.out.println("[certificatePassword]"+certificatePassword);
			//System.out.println("[tsacertificatePassword]"+tsacertificatePassword);
			System.out.println("[upperRightY]"+upperRightY);
			System.out.println("[lowerLeftY]"+lowerLeftY);
            CertificateConfiguration cert = new CertificateFromBytes(UtilsSign.getBytesFromFile(p12Certificate),certificatePassword);
            
            RevocationVerify revocationVerify = new RevocationVerify(VerifyType.CRL_ONLY,null,crlPath,null);
            revocationVerify.setKeyStorePath(keystorePath);
            
            TSAAuthentication autenticacion = new TSACertAuthentication(UtilsSign.getBytesFromFile(tsaCertificate),tsacertificatePassword);
            TSAProperties tsaProperties = new TSAProperties(tsaURL, tsaPolicyOID, hashalgorithm, autenticacion);
            
            PdfParameters signParameters = new PdfParameters(UtilsSign.getBytesFromFile(pdfSignPath)
            		,revocationVerify,cert);
            
            signParameters.setInformation(signReason, signLocation);
            signParameters.setImageSettings(signFieldName, UtilsSign.getBytesFromFile(pdfSignImagePath),new Rectangle(lowerLeftX,lowerLeftY,upperRightX,upperRightY),signPage,false, contentSignature, RenderingMode.DESCRIPTION);
            signParameters.setTimeStampSettings(tsaProperties);
            
            ArrayList<SignatureParameters> lista = new ArrayList<SignatureParameters>();
            lista.add(signParameters);
            
            Sign s = SignFactory.getSigner(SignFactory.PDF, lista);
           
            List<ProcessResponseSign> listaR = s.signData();
            
            for(ProcessResponseSign pp :listaR){            	
                if(pp.isExito()){
                	urlSigned=pdfSignPath.split("/");
        			for(int i=0;i<urlSigned.length-1;i++){
        				pdfSignedPath=pdfSignedPath+urlSigned[i]+"/";
        			}
        			System.out.println("-------------> "+pp.getTimeStamp());
        			System.out.println("------------------->"+pp.getResultado());
        			
        			File uniqueFile = FileUtil.uniqueFile(new File(pdfSignedPath),
        					""+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
            				+"_Contrato_"+user.getTbCodeType().getCodeTypeAbbreviation()
            				+"_"+user.getUserCode()+"_Signed.pdf");
        			System.out.println("[uniqueFile]"+uniqueFile.toString());
                    UtilsSign.saveSignedFile(uniqueFile.toString(), pp.getResultado());
                    msgLog="";
                    if(pp.isTimeStamped()){
                    	msgLog="Fecha de estampa del contrato: "
                    		+new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa").format(pp.getTimeStamp())+".";
                    	System.out.println(msgLog);
                    	log.insertLog(msgLog, LogReference.CLIENT, LogAction.CREATE,
								ip, userID);
                    	idDocument=this.saveRegDocument(userID,6, uniqueFile.toString(), 
                    			new Timestamp(System.currentTimeMillis()), 
                    			new Timestamp(System.currentTimeMillis()));
                    	doc.setProcessDateDocument(new Timestamp(System.currentTimeMillis()));
                    	em.persist(doc);
                    	em.flush();
                    	try {
							TbStamp stm = new TbStamp();							
							stm.setStampDate(pp.getTimeStamp());
							stm.setUserId(em.find(TbSystemUser.class, userID));
							em.persist(stm);
							em.flush();
						} catch (Exception e) {
							System.out.println("Error al momento de generar registro de stampa en base de datos");
							e.printStackTrace();
						}
                    	System.out.println("[ IdDocumento ]"+idDocument);
                    }else{
                    	msgLog="No se pudo realizar la estampa de la fecha sobre el contrato.";
                    	log.insertLog(msgLog, LogReference.CLIENT, LogAction.OPERATIONFAILED,
								ip, userID);
                    }
                    result=true;
                    System.out.println(msgLog);
                }
                else{
                	msgLog="Error al generar firma digital. Mensaje: ";
                	
                	msgEmail="";
                    for(MessageResponse mm : pp.getMessageResponse()){
                    	msgEmail=msgEmail+mm.getCodigo() + " " + mm.getMensaje()+"\n\r";
                    	msgLog=msgLog+mm.getCodigo() + " " + mm.getMensaje()+". ";
                        System.out.println(mm.getCodigo() + " " + mm.getMensaje());
                    }
                    System.out.println(msgEmail);
                    ArrayList<String> parameters=new ArrayList<String>();
        			parameters.add("#ERR="+msgEmail);
        			outbox.insertMessageOutbox(user.getUserId(),
        					EmailProcess.ERROR_DIGITAL_SIGNATURE, null, null, null, null,
        					null, null, null, null, null, user.getTbCodeType()
        							.getCodeTypeId(), null, null, true, parameters);
                    log.insertLog(msgLog, LogReference.CLIENT, LogAction.OPERATIONFAILED,
							ip, userID);
                    result=false;
                }
            }
        }
        catch (Exception e) {
        	msgLog="Error al generar firma digital. Mensaje: ";
        	ArrayList<String> parameters=new ArrayList<String>();
			parameters.add("#ERR="+e.getMessage());
        	outbox.insertMessageOutbox(userID,
					EmailProcess.ERROR_DIGITAL_SIGNATURE, null, null, null, null,
					null, null, null, null, null, user.getTbCodeType()
							.getCodeTypeId(), null, null, true, parameters);
        	msgLog=msgLog+e.toString()+".";
        	
            log.insertLog(msgLog, LogReference.CLIENT, LogAction.OPERATIONFAILED,
					ip, userID);
        	result=false;
        	e.printStackTrace(System.out);
        }
		return result;
	}
	
	@Override
	public void sendMail(String mess,String ip){
		boolean resultado=false;
		String error="";
		try{
			Query q=em.createNativeQuery("select SIGN_PARAM_NAME,SIGN_PARAM_VALUE " +
			"from TB_SIGNATURE_PARAMETER where SIGN_PARAM_STATE=1 " +
			"and SIGN_PARAM_ID in (22,23)");
			List<?> list = (List<?>)q.getResultList();
			for (Object obje : list) {
				if (obje != null && obje instanceof Object[]) {
					Object[] datos = (Object[]) obje;
					if(datos[0].equals("userTo")){
						userTo=(String)datos[1];
					}else if(datos[0].equals("subjectE")){
						subjectE=(String)datos[1];
					}
				}
			}
			Email popEmail = new Email();
			String from = popEmail.getString("mail.smtp.user");
			if(from==null){
				resultado=false;
			}else{
				resultado = (sendMail.sendMailtoOutBox(
						userTo, 
						from,
						"FacilPass", 
						subjectE,
						"1",
						mess));
			}
		}catch(Exception e){
			error=e.toString();
			e.printStackTrace();
			System.out.println("[ Error ContractEJB.sendMail ]");
		}
		System.out.println("resultado=" +resultado);
		if(!resultado){
			log.insertLog("No se pudo enviar correo de error en la carga del documento. " +
					error+".", 
					LogReference.EMAIL, LogAction.OPERATIONFAILED,
					ip, 0L);
		}
	}

	@Override
	public Long getTypeContract(Long userId) {
		Long idDoc=0L;
		Query q=em.createNativeQuery("select DOCUMENT_ID " +
				"FROM (select DOCUMENT_ID " +
				"from TB_DOCUMENT " +
				"where STATE_DOCUMENT=?2 " +
				"and TYPE_DOCUMENT_ID=1 " +
				"and USER_ID=?1 " +
				"and UPPER(URL_DOCUMENT) like '%PDF' " +
				"order by PROCESS_DATE_DOCUMENT desc) " +
				"WHERE ROWNUM<2");
		Query q1=em.createNativeQuery("select DOCUMENT_ID " +
				"FROM (select DOCUMENT_ID " +
				"from TB_DOCUMENT " +
				"where STATE_DOCUMENT=?2 " +
				"and TYPE_DOCUMENT_ID=1 " +
				"and USER_ID=?1 " +
				"and UPPER(URL_DOCUMENT) like '%PDF' " +
				"order by UPLOAD_DATE_DOCUMENT desc) " +
				"WHERE ROWNUM<2");
		try{
			q.setParameter(1, userId);
			q.setParameter(2, 3);
			idDoc=((BigDecimal)q.getSingleResult()).longValue();
			System.out.println("[ Estado 3 ]");
		}catch(NoResultException e){
			try{
				q1.setParameter(1, userId);
				q1.setParameter(2, 4);
				idDoc=((BigDecimal)q1.getSingleResult()).longValue();
				System.out.println("[ Estado 4 ]");
			}catch(NoResultException e1){
				try{
					q.setParameter(1, userId);
					q.setParameter(2, 6);
					idDoc=((BigDecimal)q.getSingleResult()).longValue();
					System.out.println("[ Estado 6 ]");
				}catch(NoResultException e2){
					try{
						q1.setParameter(1, userId);
						q1.setParameter(2, 5);
						idDoc=((BigDecimal)q1.getSingleResult()).longValue();
						System.out.println("[ Estado 5 ]");
					}catch(NoResultException e3){
						System.out.println("[ No resultado ]");
						idDoc=0L;
					}catch(Exception e7){
						e7.printStackTrace();
						System.out.println("[ Error ContractEJB.getTypeContract ]");
						idDoc=-1L;
					}
				}catch(Exception e6){
					e6.printStackTrace();
					System.out.println("[ Error ContractEJB.getTypeContract ]");
					idDoc=-1L;
				}
			}catch(Exception e5){
				e5.printStackTrace();
				System.out.println("[ Error ContractEJB.getTypeContract ]");
				idDoc=-1L;
			}
		}catch(Exception e4){
			e4.printStackTrace();
			System.out.println("[ Error ContractEJB.getTypeContract ]");
			idDoc=-1L;
		}
		return idDoc;
	}

	@Override
	public String[] getContract(Long documentId) {
		String[] result =null; 
		String name="";
		String path="";
		String[] subPath;
		try{
			TbDocument doc=em.find(TbDocument.class, documentId);
			System.out.println("[url]"+doc.getUrlDocument());
			subPath=doc.getUrlDocument().replace("\\", "/").split("/");
			name=subPath[subPath.length-1];
			for(int i=0;i<subPath.length-1;i++){
				path=path+subPath[i]+"/";
			}
			System.out.println("[path]"+path);
			System.out.println("[name]"+name);
			System.out.println("[state]"+doc.getStateDocument());
			result=new String[]{path,name,String.valueOf(doc.getStateDocument())};
		}catch(Exception e){
			result=null;
			e.printStackTrace();
			System.out.println("[ Error ContractEJB.getContract ]");
		}
		return result;
	}

	@Override
	public Long saveRegDocument(Long userId, Integer state,String url,Timestamp uploadDate,
			Timestamp processDate) {
		Long respu=0L;
		try{
			TbDocument doc=new TbDocument();
			doc.setProcessDateDocument(uploadDate);
			doc.setStateDocument(state);
			doc.setTbSystemUser(em.find(TbSystemUser.class, userId));
			doc.setTbTypeDocument(em.find(TbTypeDocument.class, 1L));
			doc.setUploadDateDocument(uploadDate);
			doc.setUrlDocument(url);
			em.persist(doc);
			em.flush();
			respu=doc.getDocumentId();
		}catch (Exception e) {
			respu=0L;
			e.printStackTrace();
			System.out.println("[ Error ContractEJB.saveRegDocument ]");
		}
		return respu;
	}

	@Override
	public boolean isFirstPass() {
		boolean respu=false;
		try{
			Query q=em.createNativeQuery("select SIGN_PARAM_VALUE " +
					"from TB_SIGNATURE_PARAMETER " +
					"where SIGN_PARAM_ID=2");
			String oldPass=(String)q.getSingleResult();
			if(oldPass==null){
				respu=true;
			}else{
				respu=false;
			}
			System.out.println("[respu-]"+respu);
		}catch(Exception e){
			respu=false;
			e.printStackTrace();
			System.out.println("[ Error ContractEJB.isFirstPass ]");
		}
		return respu;
	}

	@Override
	public boolean isOldPass(String pass) {
		boolean respu=false;
		try{
			Query q=em.createNativeQuery("select SIGN_PARAM_VALUE " +
					"from TB_SIGNATURE_PARAMETER " +
					"where SIGN_PARAM_ID=2");
			String oldPass=(String)q.getSingleResult();
			respu=this.veryPass(oldPass, pass);
		}catch(Exception e){
			respu=false;
			e.printStackTrace();
			System.out.println("[ Error ContractEJB.isOldPass ]");
		}
		return respu;
	}

	@Override
	public boolean changePassCrl(String pass, String ip, Long userId) {
		boolean respu=false;
		String messLog="";
		int val=0;
		try{
			String ePass=this.encodePass(pass);
			System.out.println("[Encpass]"+ePass);
			System.out.println("[pass-]"+pass);
			if(ePass!=null){
				Query upd = em.createNativeQuery("update TB_SIGNATURE_PARAMETER " +
					"set SIGN_PARAM_VALUE=?1 where SIGN_PARAM_ID in (2,4)");
				upd.setParameter(1, ePass);
				val=upd.executeUpdate();
				if(val>0){
					respu=true;
				}else{
					respu=false;
				}
			}else{
				respu=false;
			}
		}catch(Exception e){
			respu=false;
			e.printStackTrace();
			System.out.println("[ Error ContractEJB.changePassCrl ]");
		}
		if(respu){
			messLog="Se ha realizado la modificación de " +
					"la contraseña del certificado digital con éxito.";
			log.insertLog(messLog, LogReference.CLIENT, LogAction.OPERATIONFAILED,
					ip, userId);
		}else{
			messLog="Error al tratar de modificar la contraseña del certificado digital.";
			log.insertLog(messLog, LogReference.CLIENT, LogAction.OPERATIONFAILED,
					ip, userId);
		}
		return respu;
	}
	
	public String encodePass(String message){
		try{
			return new String(new BASE64Encoder().encode(message.getBytes(CHARSET_UTF_8)));
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("[ Error ContractEJB.encodePass ]");
			return null;
		}
	}
	
	public boolean veryPass(String eString,String pString){
		if(eString.equals(this.encodePass(pString))){
			return true;
		}else{
			return false;
		}
	}
	
	public String decodePass(String messageE){
		try {
			return new String(new BASE64Decoder().decodeBuffer(messageE), CHARSET_UTF_8); 
		} catch (Exception e) {
			System.out.println("[ Error ContractEJB.decodePass ]");
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String getParameterValueById(Long idParameter) {
		try{
			TbSignatureParameter tsp = em.find(TbSignatureParameter.class, idParameter);
			if(tsp != null){
				return tsp.getSignParamValue();
			}else{
				return null;
			}
		}catch(NoResultException n){
			System.out.println("Error en ContractEJB.getParameterValueById");
			n.printStackTrace();
			return null;
		}catch (NullPointerException n){
			System.out.println("Error en ContractEJB.getParameterValueById");
			n.printStackTrace();
			return null;
		}
	}
	
	public boolean saveRegacceptscontrac(long userId, String ip, boolean aceptcient){
		boolean res = false;
		System.out.println("aceptcient: " + aceptcient);
		try {
			if (aceptcient==true){
				TbAcceptsContract acc = new TbAcceptsContract();				
				acc.setAcceptsDate(new Timestamp(System.currentTimeMillis()));
				acc.setDeviceIp(ip);
				acc.setUserId(em.find(TbSystemUser.class, userId));
				acc.setCheckUser(1L);				
				acc.setStampId(getStampUserId(userId));								
				em.persist(acc);
				em.flush();			
				res = true;
			}else{
				System.out.println("El cliente no acepto el Contrato");
				res = false;
			}
		} catch (Exception e) {
			System.out.println("-------> Error en ContractEJB.saveRegacceptscontrac");
			e.printStackTrace();
			res = false;
		}
		return res;
	}
	
	public Long getStampUserId (Long userId){
		Long resp = null;
		try {
			Query q = em.createNativeQuery("select stamp_id from tb_stamp where user_id = ?1");
			q.setParameter(1, userId);
			resp = ((BigDecimal) q.getSingleResult()).longValue();			
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al obtener IdStamp ContractEJB.getStampUserId");
		}
		return resp;
	}
	
	@Override
	public Long getStateDocument(Long userId){
		try{
			Long doc=this.getTypeContract(userId);
			if(doc<=0L){
				return doc;
			}else{
				return em.find(TbDocument.class, doc).getStateDocument().longValue();
			}
		}catch (Exception e) {
			System.out.println("[ Error ContractEJB.getStateDocument ]");
			return -1L;
		}
	}
}
