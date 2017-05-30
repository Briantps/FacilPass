/**
 * 
 */
package util.device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.SocketException;
import java.net.URL;

/**
 * @author Frances Zucchet
 * 
 */
public class ServiceCommand implements Serializable {
	private static final long serialVersionUID = 6645468238827908937L;

	private URL url;
	
	private String ip;
	
	private String port;

	/**
	 * Constructor.
	 * @param ip 
	 * @param port
	 */
	public ServiceCommand(String ip, String port) {
		this.ip = ip;
		this.port = port;
	}

	/**
	 * Send a command 
	 * @param codCommand
	 * @return
	 */
	public String sendCommand(String codCommand) {
		String response = "";
		try {
			url = new URL("http://" + ip + ":" + port + "/t00000" + codCommand);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openConnection().getInputStream()));
			response = reader.readLine();
			reader.close();
		} catch (SocketException e) {
			if (!e.getMessage().contains("Unexpected")) {
				response = "*-1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = "*-2";
		}
		return response;
	}

	/**
	 * 
	 * @param codCommand
	 * @param idTisc
	 * @param newBalance
	 * @return
	 */
	public String sendReload(String codCommand, String idTisc, String newBalance) {
		String response = "";
		try {
			url = new URL("http://" + ip + ":" + port + "/t00000" + codCommand
					+ "/" + idTisc + "?" + newBalance);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openConnection().getInputStream()));
			response = reader.readLine();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			response = "-2";
		}
		return response;
	}

	/**
	 * 
	 * @param codCommand
	 * @param date
	 * @param idOldCard
	 * @param company
	 * @param prevBalance
	 * @param recharge
	 * @param currBalance
	 * @param idNewCard
	 * @param facial
	 * @return
	 */
	public String sendPrint(String codCommand, String date, String idOldCard,
			String company, String prevBalance, String recharge,
			String currBalance, String idNewCard, String facial) {
		String response = "";
		BufferedReader reader = null;
		try {
			url = new URL("http://" + ip + ":" + port + "/p00000" + codCommand
					+ "?" + date + ";" + idOldCard + ";" + company + ";"
					+ prevBalance + ";" + recharge + ";" + currBalance + ";"
					+ idNewCard + ";" + facial);
			reader = new BufferedReader(new InputStreamReader(url
					.openConnection().getInputStream()));
			response = reader.readLine();
			reader.close();
		} catch (Exception e) {
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	
	public String sendPrintBalance(String date, String currBalance,
			String idNewCard) {
		String response = "";
		BufferedReader reader = null;
		try {
			url = new URL("http://" + ip + ":" + port + "/p000002" + "?" + date
					+ ";" + currBalance + ";" + idNewCard);
			reader = new BufferedReader(new InputStreamReader(url
					.openConnection().getInputStream()));
			response = reader.readLine();
			reader.close();
		} catch (Exception e) {
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip  the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
}