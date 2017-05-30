package ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import util.ReToolbarOptionActionList;


@Stateless(mappedName = "ejb/TextEditorManager")
public class TextEditorManagerEJB implements TextEditorManager{
	

	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	private final String themeFont = "Andale Mono=andale mono,times;"
			+ "Arial=arial,helvetica,sans-serif;"
			+ "Arial Black=arial black,avant garde;"
			+ "Book Antiqua=book antiqua,palatino;"
			+ "Comic Sans MS=comic sans ms,sans-serif;"
			+ "Courier New=courier new,courier;"
			+ "Georgia=georgia,palatino;" + "Helvetica=helvetica;"
			+ "Impact=impact,chicago;"
			+ "Tahoma=tahoma,arial,helvetica,sans-serif;"
			+ "Terminal=terminal,monaco;"
			+ "Times New Roman=times new roman,times;"
			+ "Trebuchet MS=trebuchet ms,geneva;"
			+ "Verdana=verdana,geneva";
		
	
	
	/*Metodo que modifica los estados de base de datos a Booleano.*/ 
	

	@Override
	public Long getEditorListCount() {
		Long resp;
		
		try {
			System.out.println("Entre a TextEditorManagerEJB.getEditorListCount");
			
			Query q = em.createNativeQuery("select count(*) from re_toolbar_option_action rto " +
					"inner join re_option_action roa on roa.option_action_id = rto.option_action_id " +
					"inner join tb_action ac on ac.action_id = roa.action_id " +
					"left join (select toolbar_state_id as text ,option_action_id as optext  from re_toolbar_option_action where toolbar_editor_id = 1) on optext = roa.option_action_id " +
					"left join (select toolbar_state_id as hyper,option_action_id as ophyper from re_toolbar_option_action where toolbar_editor_id = 2) on ophyper = roa.option_action_id " +
			"left join (select toolbar_state_id as multim,option_action_id as opmultim from re_toolbar_option_action where toolbar_editor_id = 3) on opmultim = roa.option_action_id");

			resp = ((BigDecimal) q.getSingleResult()).longValue();
			
			if (resp==null){
				resp=0L;
			}
			
		}catch (NoResultException ex) {
			resp=0L;	
			System.out.println("Entre a NoResultexeption en TextEditorManagerEJB.getEditorListCount");
		}catch (Exception e) {
			resp=0L;
			System.out.println("Error en NoResultexeption en TextEditorManagerEJB.getEditorListCount");
			e.printStackTrace();
		}
		return resp;
	}
	
	/*Metodo que extrae y verifica si existen datos.*/ 
	
	@Override
	public ArrayList<ReToolbarOptionActionList> getEditorList() {
		ArrayList<ReToolbarOptionActionList> list = new ArrayList<ReToolbarOptionActionList> ();
		
		try {
			System.out.println("Entre a TextEditorManagerEJB.getEditorList");
			
			Query q = em.createNativeQuery("select distinct ac.action_name,roa.option_action_id,NVL(text,3),NVL(hyper,3),NVL(multim,3) from re_toolbar_option_action rto " +
					"inner join re_option_action roa on roa.option_action_id = rto.option_action_id " +
					"inner join tb_action ac on ac.action_id = roa.action_id " +
					"left join (select toolbar_state_id as text ,option_action_id as optext  from re_toolbar_option_action where toolbar_editor_id = 1) on optext = roa.option_action_id " +
					"left join (select toolbar_state_id as hyper,option_action_id as ophyper from re_toolbar_option_action where toolbar_editor_id = 2) on ophyper = roa.option_action_id " +
			"left join (select toolbar_state_id as multim,option_action_id as opmultim from re_toolbar_option_action where toolbar_editor_id = 3) on opmultim = roa.option_action_id");
			
			
			@SuppressWarnings("unchecked")
			List<Object> lis= (List<Object>)q.getResultList();
			System.out.println("Estoy llenando el objeto  getEditorList " + lis.size());
			for(int i=0;i<lis.size();i++){
				Object[] o=(Object[]) lis.get(i);
				ReToolbarOptionActionList r= new ReToolbarOptionActionList();
				
				
				r.setNameTxt(o[0]!=null?o[0].toString():"-");
				r.setIdOpciAcci(o[1]!=null?o[1].toString():"-");
				r.setTextBar(stateValidate(o[2]!=null?o[2].toString():"-"));
				r.setActvText(o[2]!=null?o[2].toString():"-");
				r.setHyperlinkBar(stateValidate(o[3]!=null?o[3].toString():"-"));
				r.setActvHyper(o[3]!=null?o[3].toString():"-");
				r.setMultimediaBar(stateValidate(o[4]!=null?o[4].toString():"-"));				
				r.setActvMultime(o[4]!=null?o[4].toString():"-");
				
							
				list.add(r);
				
			}
			
		}catch (NoResultException ex) {
			System.out.println("Entre a NoResultexeption en TextEditorManagerEJB.getEditorList");
		}catch (Exception e) {
			
			System.out.println("Error en TextEditorManagerEJB.getEditorList");
			e.printStackTrace();
		}
		return list;
		
	}
	
	/*Metodo que modifica los estados de base de datos a Booleano.*/ 
	
	public boolean stateValidate (String State){
		Long StateId;
		try {
			StateId = Long.parseLong(State);
			
			if (StateId == 1L){
				return true;
			}else {
				return false;
			}
			

		}catch (Exception e) {
			System.out.println("Error en TextEditorManagerEJB.stateValidate");
			e.printStackTrace();
		}

		return false;
	}

	/*Metodo que determina el estado de la seleccion por el usuario.*/ 
	@Override
	public String setUpdateBarEditor(ArrayList<ReToolbarOptionActionList> listEditor) {
		String resp = null;
		System.out.println("Entre a TextEditorManagerEJB.setUpdateBarEditor" );
		try {
			
			for (int i = 0; i < listEditor.size(); i++) {
				ReToolbarOptionActionList rto = listEditor.get(i);
				
				if (rto.isTextBar()){
					System.out.println("Se Activara la Barra de texto - Formulario: " + rto.getIdOpciAcci());
					updateToolBarr(rto.getIdOpciAcci(), 1L, 1L);
				}else if (!rto.isTextBar() && Long.parseLong(rto.getActvText())!= 3){
					System.out.println("Se inactivara la Barra de texto - Formulario: " + rto.getIdOpciAcci());
					updateToolBarr(rto.getIdOpciAcci(), 1L, 2L);
				}else {
					System.out.println("Barra de texto Invalida - Formulario: " + rto.getIdOpciAcci());
				}				
				if (rto.isHyperlinkBar()){
					System.out.println("Se Activara la Barra de Hipervinculo - Formulario: " + rto.getIdOpciAcci());
					updateToolBarr(rto.getIdOpciAcci(), 2L, 1L);
				}else if (!rto.isHyperlinkBar() && Long.parseLong(rto.getActvHyper())!= 3){
					System.out.println("Se inactivara la Barra de Hipervinculo - Formulario: " + rto.getIdOpciAcci());
					updateToolBarr(rto.getIdOpciAcci(), 2L, 2L);
				}else {
					System.out.println("Barra de Hipervinculo Invalida - Formulario: " + rto.getIdOpciAcci());
				}
				if (rto.isMultimediaBar()){
					System.out.println("Se Activara la Barra de Multimedia - Formulario: " + rto.getIdOpciAcci());
					updateToolBarr(rto.getIdOpciAcci(), 3L, 1L);
				}else if (!rto.isMultimediaBar() && Long.parseLong(rto.getActvMultime())!= 3){
					System.out.println("Se inactivara la Barra de Multimedia - Formulario: " + rto.getIdOpciAcci());
					updateToolBarr(rto.getIdOpciAcci(), 3L, 2L);
				}else{
					System.out.println("Barra de Multimedia Invalida - Formulario: " + rto.getIdOpciAcci());
				}
			}
			resp = "Se han realizado los cambios con éxito.";
			
		} catch (Exception e) {
			System.out.println("Error en TextEditorManagerEJB.setUpdateBarEditor");
			e.printStackTrace();
			resp = "Error en la transacción Intente nuevamente.";
		}
		
		return resp;
	}
	
	/*Metodo para Realizar los Cambios en los Editores Seleccionados.*/ 
	
	public void updateToolBarr (String idOpci,Long idBarr,Long State){
		try {
			Query q = em.createNativeQuery("UPDATE re_toolbar_option_action SET toolbar_state_id = ?1 WHERE option_action_id = ?2 AND toolbar_editor_id = ?3 ");
			q.setParameter(1, State);
			q.setParameter(2, idOpci);
			q.setParameter(3, idBarr);
			int count = q.executeUpdate();
			
			if(count>0){
				System.out.println("Se realizo el Cambio con exito");
			}else{
				
			}
		} catch (Exception e) {
			System.out.println("Error en TextEditorManagerEJB.updateToolBarr");
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getOptionToolBarrEditorText(int idBarr, String formName){
		ArrayList<String> lista=new ArrayList<String>();
		String resp = "";
		Long stateId = 0L;
		String themeOne = "newdocument,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,bullist,numlist,|,outdent,indent,|,undo,redo,|,fontselect,fontsizeselect,|,forecolor,backcolor";
		String themeTwo = "link,unlink";
		String themeTree = "emotions,image,media";
     	String themeFour="pasteword,|,search,replace,|,sub,sup,|,tablecontrols,|,hr,removeformat,|,insertdate,inserttime,|,preview,print,code"; 
		int barra=0;
		/*if(idBarr==1){barra=idBarr;}else if(idBarr==2){barra=idBarr;}else if(idBarr==3){barra=idBarr;}else
     	if(idBarr==4){
     		barra=1;
     		System.out.println("ENTRO AL VALOR DE LA BARRA CUATRO:::"+idBarr+"---"+barra);
		}*/     	
		try {
			
			Query q = em.createNativeQuery("SELECT rto.toolbar_state_id FROM re_toolbar_option_action rto " +
					"INNER JOIN re_option_action roa ON roa.option_action_id = rto.option_action_id " +
					"WHERE roa.behavior = ?2 " +
			"AND rto.toolbar_editor_id = ?1");
			
			q.setParameter(1, idBarr);
			q.setParameter(2, formName);
			
			stateId = ((BigDecimal) q.getSingleResult()).longValue();
			
			if (stateId == 1L){
				if (idBarr==1){
					resp =themeOne;					
		         	lista.add(resp);
		         	resp = themeFour;
		         	lista.add(resp);
					System.out.println("Texto de editor:: vidBarr==1"+themeOne);
				}else if (idBarr==2){
					System.out.println("Texto de editor:: vidBarr==2"+themeTwo);
					resp=themeTwo;
					lista.add(resp);
				}else if (idBarr==3){
					System.out.println("Texto de editor:: vidBarr==3"+themeTree);
					resp=themeTree;
					lista.add(resp);					
				}else{
						resp = "";
						lista.add(resp);
					}
				
			}else{
				resp = "";
				lista.add(resp);
			}
						
		} catch (Exception e) {
			System.out.println("Error en TextEditorManagerEJB.getOptionToolBarrEditorText");
			e.printStackTrace();
			resp="";
			lista.add(resp);
			
		}
		return lista;
	}

	@Override
	public String getThemefont() {
		return themeFont;
	}

	@Override
	public String getThemeTooltip() {
		String themeTooltip="bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,bullist,numlist,|,outdent,indent,|,undo,redo";
		return themeTooltip;
	}

	
}