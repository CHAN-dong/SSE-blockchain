package prepare;

public class Util {
    public static String intToBinary(int i,int length){
        String binaryStr = Integer.toBinaryString(i);
        while(binaryStr.length() < length){
            binaryStr = "0" + binaryStr;
        }
        if(binaryStr.length() > length){
            binaryStr = binaryStr.substring(0,length);
        }
        return binaryStr;
    }

    public static String Xor(String a,String b){
        if(a.length() != b.length()){
            return null;
        }
        StringBuilder result = new StringBuilder(a.length());
        for(int i = 0;i < a.length();i++){
            if(a.charAt(i) == b.charAt(i)){
                result.append("0");
            }else{
                result.append("1");
            }
        }
        return result.toString();
    }
    public static String generate(int l){
        StringBuilder res = new StringBuilder(l);
        for(int i = 0;i < l;i++){
            res.append(Math.random() < 0.5 ? 0 : 1);
        }
        return res.toString();
    }


    public static void main(String[] args) {
        String a = "1110011";
        String b = "1011011";
        System.out.println(Xor(a,b));
    }
}
