package io.swagger.utils;

//generate start method
class Start extends Thread
{
    public void run()
    {
        String originalString = "1234";

        AES AES = new AES();
        String encryptedString = AES.encrypt(originalString) ;
        String decryptedString = AES.decrypt(encryptedString) ;

        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);
    }
    public static void main(String args[])
    {
        Start t1=new Start();
        // this will call run() method
        t1.start();
    }
}
