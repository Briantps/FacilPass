package ejb.taskeng.email;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Cuerpo de un correo electronico. Contiene uno o mas mensajes.
 * @author Mauricio Gil
 */
public interface Body extends Serializable {

	/**
	 * Agrega un nuevo mensaje al cuerpo
	 * @param e
	 */
	public void addMessage(Message e);

	/**
	 * Obtiene un mensaje del cuerpo, con un determinado id.
	 * @param id Identificador
	 * @return Mensaje
	 */
	public Message getMessage(int id);

	/**
	 * Establece el mapa de mensajes asociado a este cuerpo.
	 * @param messages
	 */
	public void setMessages(Map<Integer, Message> messages);

	/**
	 * Recupera el mapa de mensajes asociado a este cuerpo.
	 * @return
	 */
	public Map<Integer, Message> getMessages();

	/**
	 * Agrega un objeto de archivo adjunto a este cuerpo
	 * @param e
	 */
	public void addAttachment(Attachment e);

	/**
	 * Establece la lista de archivos adjuntos a este cuerpo.
	 * @param attachments
	 */
	public void setAttachments(List<Attachment> attachments);

	/**
	 * Obtiene la lista de archivos adjuntos a este cuerpo.
	 * @return
	 */
	public List<Attachment> getAttachments();

}