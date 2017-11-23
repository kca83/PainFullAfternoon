package io.zipcoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class ItemParser {


    public ArrayList<String> parseRawDataIntoStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawData);
        return response;
    }

    public Item parseStringIntoItem(String rawItem) throws ItemParseException{

        ArrayList<String> keyValuePairs = findKeyValuePairsInRawItemData(rawItem);

        String name = keyValuePairs.get(0);
        Pattern namePattern = Pattern.compile("([nN][aA][mM][eE]):([a-zA-z0-9]+)");
        name = checkMatch(namePattern, name);

        String price = keyValuePairs.get(1);
        Pattern pricePattern = Pattern.compile("([pP][rR][iI][cC][eE]):([0-9]+\\.[0-9]+)");
        price = checkMatch(pricePattern, price);

        String type = keyValuePairs.get(2);
        Pattern typePattern = Pattern.compile("([tT][yY][pP][eE]):([a-zA-z]+)");
        type = checkMatch(typePattern, type);

        String expiration = keyValuePairs.get(3);
        Pattern expirationPattern = Pattern.compile("([eE][xX][pP][iI][rR][aA][tT][iI][oO0][nN]):([0-9]+\\/[0-9]+\\/[0-9]+)");
        expiration = checkMatch(expirationPattern, expiration);

        return new Item(name, Double.parseDouble(price), type, expiration);
    }

    public String checkMatch(Pattern pattern, String field) throws ItemParseException {
        if(pattern.matcher(field).matches()) {
            ArrayList<String> keyValue = splitStringWithRegexPattern(":", field);
            field = keyValue.get(1).toUpperCase();
            if(field.contains("C")) {
                field = field.replace("0", "O");
            }
            if(field.length() > 1) {
                field = field.substring(0,1) + field.substring(1).toLowerCase();
            }
        }
        else {
            throw new ItemParseException();
        }
        return field;
    }

    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem){
        String stringPattern = "[^a-zA-Z0-9/.:]";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawItem);
        return response;
    }

    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString){
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }



}
