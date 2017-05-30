/**
 * 
 */
package util;

import java.sql.Timestamp;

/**
 * @author agil
 *
 */
public final class TimeUtil {

	/**
	 * Converts a time to complete task in days to a Timestamp representation
	 * @param adsTime Time in days
	 * @return Time in timestamp format
	 */
	public static Timestamp calculateAds(Integer adsTime) {
		return new Timestamp(System.currentTimeMillis() + 86400000L * adsTime);
	}
}
