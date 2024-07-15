package controller;

public class NumberToWord {
    private static final String[] unitNumbers = {"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};
    private static final String[] placeValues = {"", "nghìn", "triệu", "tỷ"};

    public static String convertToWords(double inputNumber) {

        // Chuyển số thành chuỗi và bỏ phần thập phân
        String sNumber = String.format("%.0f", inputNumber);
        double number = Double.parseDouble(sNumber);
        if (number < 0) {
            number = -number;
            sNumber = String.format("%.0f", number);
        }

        int ones, tens, hundreds;
        int positionDigit = sNumber.length();
        String result = " ";

        if (positionDigit == 0) {
            result = unitNumbers[0] + result;
        } else {
            int placeValue = 0;

            while (positionDigit > 0) {
                tens = hundreds = -1;
                ones = Integer.parseInt(sNumber.substring(positionDigit - 1, positionDigit));
                positionDigit--;
                if (positionDigit > 0) {
                    tens = Integer.parseInt(sNumber.substring(positionDigit - 1, positionDigit));
                    positionDigit--;
                    if (positionDigit > 0) {
                        hundreds = Integer.parseInt(sNumber.substring(positionDigit - 1, positionDigit));
                        positionDigit--;
                    }
                }

                if ((ones > 0) || (tens > 0) || (hundreds > 0) || (placeValue == 3)) {
                    result = placeValues[placeValue] + " " + result;
                }

                placeValue++;
                if (placeValue > 3) placeValue = 1;

                if ((ones == 1) && (tens > 1)) {
                    result = "mốt " + result;
                } else {
                    if ((ones == 5) && (tens > 0)) {
                        result = "lăm " + result;
                    } else if (ones > 0) {
                        result = unitNumbers[ones] + " " + result;
                    }
                }

                if (tens < 0) {
                    break;
                } else {
                    if ((tens == 0) && (ones > 0)) result = "lẻ " + result;
                    if (tens == 1) result = "mười " + result;
                    if (tens > 1) result = unitNumbers[tens] + " mươi " + result;
                }

                if (hundreds < 0) {
                    break;
                } else {
                    if ((hundreds > 0) || (tens > 0) || (ones > 0)) {
                        result = unitNumbers[hundreds] + " trăm " + result;
                    }
                }
                result = " " + result;
            }
        }
        return result = result.trim();
    }
}
