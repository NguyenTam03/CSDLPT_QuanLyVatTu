
package common.method;

import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Formatter {
	// format x.000đ => x000 ( Integer)

	public static Float formatMoneyToFloat(Object money) {
		return Float.parseFloat(((String) money).replaceAll("[^0-9]", ""));
	}

	// format x000 => x.000 đ
	public static String formatObjecttoMoney(Object string) {
		if (string == null)
			return null;
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		return currencyFormat.format(string);
	}
	
	public static String formatterDate(Date date) {
		LocalDate localDate = date.toLocalDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formatterDate = localDate.format(formatter);
		return formatterDate;
	}
}
