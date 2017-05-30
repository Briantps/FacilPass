package constant;

import jpa.TbTypeConfAutoRechar;;

/**
 * Enum (Mapped IDs fromTable {@link TbTypeConfAutoRechar}
 * 
 * @author JGomez
 * @since 28-02-2017
 */
public enum TypeConfAutomaticRecharge {
	
	START_HELP_MESSAGE(1L),
	FREQUENCY_HELP_MESSAGE_PRE(2L),
	FREQUENCY_HELP_MESSAGE_POS(3L),
	MINIMUMBALANCE_HELP_MESSAGE_PRE(4L),
	MINIMUMBALANCE_HELP_MESSAGE_POS(5L),
	TYPE_PROGRAMMING_HELP_TOOLTIP(6L),
	FREQUENCY_HELP_TOOLTIP_PRE(7L),
	FREQUENCY_HELP_TOOLTIP_POS(8L),
	MINIMUMBALANCE_HELP_TOOLTIP_PRE(9L),
	MINIMUMBALANCE_HELP_TOOLTIP_POS(10L),
	INITIAL_DATE_TOOLTIP(11L),
	RECHARGE_VALUE_TOOLTIP(12L),
	ASSING_VALUE_TOOLTIP(13L),
	LINK_DELETE_TOOLTIP(14L),
	TYPE_PERSON(15L);
	
	 private Long id;

	 /**
	  * 
	  * @param id
	  */
	 private TypeConfAutomaticRecharge(Long id) {
	   this.id = id;
	 }

	 /**
	  * 
	  * @return id
	  */
	 public Long getId() {
	  	 return id;
	 }
}