package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbAgreement;
import jpa.TbChanel;

import util.ReAgreementChannel;
@Remote
public interface ReAgreementChanelI {
	public ArrayList<ReAgreementChannel> getAllRelationAgreementChannel();
	public List<TbChanel> getChannels();
	public List<TbAgreement> getAgreements();
	public boolean saveReAgreementChannel(Long idChannel,Long idagreement,String ip,Long usercreator);
	public ReAgreementChannel getFindChannelAgreement(Long idChanel, Long idAgreement);
	public boolean deleteRelationChannelAgremeent(Long id_agreementChannel);
	public boolean ActiveInactiveRelationChannelAgremeent(Long id_agreementChannel,String ip,Long UserCreator);

}
