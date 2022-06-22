package io.swagger.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ibanGenerator {
    
    public String GenerateIban(){
        StringBuilder s = new StringBuilder();
        Random r = new Random();
        s.append("NL");
        s.append(r.nextInt(10));
        s.append(r.nextInt(10));
        s.append("INHO0");
        for(int i = 0; i < 9;i++){
            s.append(r.nextInt(10));
        }
        return s.toString();
    }

    public boolean ValidateIban(String iban){
        Pattern pattern = Pattern.compile("NL[0-9]{2}INHO0[0-9]{9}");
        Matcher matcher = pattern.matcher(iban);
        return matcher.matches();
    }
}
