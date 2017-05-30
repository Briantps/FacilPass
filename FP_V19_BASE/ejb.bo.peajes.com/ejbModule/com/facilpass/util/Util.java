package com.facilpass.util;

import java.net.InetAddress;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.facilpass.util.classes.SimpleTagMessageParser;

import jpa.TbProcessTrack;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import ejb.Process;
import ejb.email.Outbox;

/** @author Cristhian Buchely
 * 
 * */
public class Util
{
 
	public static void doViewProcessAndProcessTrack(Process process,ProcessTrackDetailType processType,String userProcessLogMessage,String ip,Long creationUser) {
		TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
		Long idPTA;

		if (pt==null) {
			idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
		} else {
			idPTA=pt.getProcessTrackId();
		}

		process.createProcessDetail(idPTA,processType, userProcessLogMessage, creationUser, ip, 1, "", null,null,null,null);

		//proceso cliente
		TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,creationUser);
		Long idPTC;
		if (ptc==null) {
			idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
		} else {
			idPTC=ptc.getProcessTrackId();
		}

		process.createProcessDetail(idPTC,processType,  userProcessLogMessage, creationUser, ip, 1, "", null,null,null,null);
	}
	
	public static void doTagParserEmail(Outbox outbox,Long creationUser,EmailParameterBuilder<String> emailCreator)	{
//		Map<String,Object> parameters = new HashMap<String,Object>();
//		Map<Integer,Object> paramMap=emailCreator.getParameters();
//		Set<Integer> keySet=paramMap.keySet();
//		for(Integer key:keySet) {
//			String tagValue=emailCreator.getTagByEmailParameter(key);
//			if (tagValue!=null) {
//				Object emailItem=paramMap.get(key);
//				parameters.put(tagValue,emailItem.toString());
//			}
//
//		}
		
//		MessageParser tagParser=new SimpleTagMessageParser(parameters);
		MessageParser tagParser = new SimpleTagMessageParser(emailCreator.getParameters());
		outbox.insertMessageOutbox2(creationUser, emailCreator.getEmailProcess(), true, tagParser);
	}
	
	
	public static String getWsUrl(EntityManager em,long wsid)
	{
		Query q1=em.createNativeQuery("SELECT tws.url FROM tb_web_services tws WHERE tws.tb_web_services_id=:ws_id");
		q1.setParameter("ws_id", new Long(wsid));
		String result=(String)q1.getSingleResult();
		return result;
	}
	
	public static String getWsMessage(EntityManager em,long wsid,long statusCode,String defaultValue)
	{
		String result=null;
		Query q1=em.createNativeQuery("SELECT twr.description FROM tb_ws_responses twr WHERE twr.code=:sts_code AND twr.tb_web_services_id=:ws_id");
		q1.setParameter("sts_code", new Long(statusCode));
		q1.setParameter("ws_id", new Long(wsid));
		List results=q1.getResultList();
		
		if(results!=null && results.size()>0)
		{
			result=(String)results.get(0);
		}
		else result=defaultValue;
		return result;
	}
	
	public static String getMostNearIp()
	{
		String ip="127.0.0.1";
		try
		{
		 InetAddress ipAddress = InetAddress.getLocalHost();
		 ip=ipAddress.getHostAddress();
		}
		catch(Exception e)
		{ }
		return ip;
	}
	

	
	
	
}
