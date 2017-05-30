package ejb;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.Remote;

import util.Channel;

@Remote
public interface  ChannelI {

	public ArrayList<Channel>getAllChannel();

	public boolean createChannel(Long codeChannel,String nameChannel,String Description,Long active,Long rechargeValue,String ip,Long userCreator);
	public boolean updateChannel(Long channelId,Long codeChannel,String nameChannel,String Description,Long activeRecharge,Long rechargeValue,String ip,Long creationUser);
	public boolean inactiveActiveChannel (Long id_channel,Long codeChannel,String ip,Long creationUser)throws SQLException;
	public boolean deleteChannel(Long idChannel,Long codeChannel,String ip,Long creationUser)throws SQLException;
	public Long existChannel(Long channelId, Long codeChannel, int i);
}
