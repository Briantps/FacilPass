package ejb.crud;

import java.util.List;

import javax.ejb.Remote;

import jpa.TbBank;
import util.BankTb;
import util.UListbank;

@Remote
public interface Bank {
	
	public List<TbBank> getBanks();
		
	public List<BankTb> getBankTb();
	
	public List<TbBank> getBanksAval();
	
	public Long getOtherBanks(Long bankId);
	
	public String existBank(Long bankId, String bankName, String bankLetter, int operation);
	
	public String insertBank(Long bankCode, String bankName, String bankLetter,
			Long bankAval, String ip, Long creationUser);
	
	public String editBank(Long bankId, String bankName, Long bankAval, String ip, Long creationUser);

	public String haveBankName(Long bankId);
	
	public String checAval (Long bankId);
	
    //Recarga Online  -  Administrar Convenio
	public List<BankTb> getListAllAgreement();
	
	public boolean insertAgreement(Long codeAgreement, String nameAgreement, Long paymentMethodId, 
			String descriptionAgreement, Long bankId, Long active, Long rechargeValue, String ip, Long creationUser);

	public boolean updateAgreement(Long idAgreement, Long idAgreementBank, Long codeAgreement, String nameAgreement, Long paymentMethodId, 
			Long bankId, String descriptionAgreement, Long idState, Long active, Long rechargeValue, String ip, Long creationUser);

	public boolean updateEstateAgreement(Long idAgreement, Long idAgreementBank, Long idState, String ip, Long creationUser);

	public boolean deleteAgreement(Long idAgreement, Long idAgreementBank, String ip, Long userId);

	public boolean validateRelationAgreementBank(Long idAgreement, Long codeAgreement, Long bankId);
	
	public String validateAgreement(Long idAgreementBank, Long codeAgreement, String nameAgreement,
			Long bankId, String descriptionAgreement, int i);
	
	public List<UListbank> getListBankAutomaticProgramming();
	
	public boolean setListBankAutomaticProgramming(List<UListbank> bankListSend, String modifyEntities, Long userId, String ip) throws Exception;
	
	public Long getPlaymentMethodAccount(Long accountId);
	
	public Long existAgreement(Long idAgreementBank, Long codeAgreement, int i);
	
	//BM
	public boolean getpermission(Long userId, String nameperm);
}
