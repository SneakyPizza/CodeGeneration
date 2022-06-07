package io.swagger.utils;

public class PincodeGenerator {

    // generate 4 number pincode
    public String generatePincode(){
        StringBuilder pincode = new StringBuilder();
        for(int i = 0; i < 4; i++){
            pincode.append((int) (Math.random() * 10));
        }
        return pincode.toString();
    }
}
