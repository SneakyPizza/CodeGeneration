package io.swagger.utils;

import java.util.Random;

public class ibanGenerator {
    
    public String GenerateIban(){
        StringBuilder s = new StringBuilder();
        Random r = new Random();
        s.append("NL");
        s.append(r.nextInt(0, 9));
        s.append(r.nextInt(0, 9));
        s.append("INHO0");
        for(int i = 0; i < 10;i++){
            s.append(r.nextInt(0,9));
        }
        return s.toString();
    }

    public boolean ValidateIban(String iban){
        return false;
    }
}
