package DO;
import prepare.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Setup {
    int P = 16;
    int parameter;
    String key_setup = "1010001011110001011101001001110001001100111110110100101000000101000011110001010110100110100010001000000111001000001010111000011011101111001101001001110010111110010001000010001000110100100011001000000101110001110010010010000001110000111001110010110000001011";
    String key_add = "0100011000000100001010111101000000001110110000000110010111010101011001100110111110011111111111001101010011000010110101000110110010111000110001111111010100100111010101101110000100000111010110111000110101110010011110001010111000011000111001100101110000010011";
    String key_delete = "1111000110011011101111001100110011100101100001010011101111100010000100111101111010101110001010000011100110101100100110010010000101101011000000110000011010110110111111111110110010101111110011011100100100001011100101010111001110110001100000101110010110001111";
    List<Encrypted_data> EDB;
    Map<Integer,Integer> sigma = new HashMap<>();
    //初始化三个密钥
    public Setup(int parameter, Map<Integer,int[]> keywordIdPair){
        this.parameter = parameter;
        generateEDB(keywordIdPair);
    }
    public void generateEDB(Map<Integer,int[]> keywordIdPair){
        List<Encrypted_data> EDB = new ArrayList<>();
        String implement = "1111111111111111";
        //对每个关键字进行加密
        for(int keyword = 0;keyword < keywordIdPair.size();keyword++){
            //关键字最大位100000，需要比特位20位
            String K1 = SHA.F(key_setup + 1 + Util.intToBinary(keyword,20));
            String K2 = SHA.F(key_setup + 2 + Util.intToBinary(keyword,20));
            int[] keywordId = keywordIdPair.get(keyword + 1);
            //把DB(w)分成 α 块，每个块有p个关键字
            for(int c = 0;c < (int)Math.ceil(((double)keywordId.length) / P);c++) {
                StringBuilder id = new StringBuilder(P * 16);
                for (int i = 0; i < P; i++) {
                    if (c * 16 + i >= keywordId.length) {
                        id.append(implement);
                    } else {
                        //ID最多为10000，比特位16位即可
                        id.append(Util.intToBinary(keywordId[c * 16 + i], 16));
                    }
                }
                Encrypted_data cryData = new Encrypted_data();
                String bitString_r = Util.generate(parameter);
                cryData.r = new BigInteger(bitString_r,2);
                cryData.d = new BigInteger(Util.Xor(id.toString(), SHA.F(K2 + bitString_r)),2);
                cryData.l = new BigInteger(SHA.F(K1 + c),2);
                EDB.add(cryData);
            }
        }
        this.EDB = EDB;
    }

    public SearchKeyType generateSearchKey(int w, int c){
        SearchKeyType key = new SearchKeyType();
        key.c = c;
        key.K1 = SHA.F(this.key_setup + 1 + Util.intToBinary(w,20));
        key.K2 = SHA.F(this.key_setup + 2 + Util.intToBinary(w,20));
        key.K1_A = SHA.F(this.key_add + 1 + Util.intToBinary(w,20));
        key.K2_A = SHA.F(this.key_add + 2 + Util.intToBinary(w,20));
        key.K1_D = SHA.F(this.key_delete + Util.intToBinary(w,20));
        return key;
    }

    public List<AddDataType> add(int id, int[] id_w){
        List<AddDataType> result = new ArrayList<>();
        for(int i = 0;i < id_w.length;i++){
            String K1_A = SHA.F(key_add + 1 + Util.intToBinary(id_w[i],20));
            String K2_A = SHA.F(key_add + 2 + Util.intToBinary(id_w[i],20));
            String K1_D = SHA.F(key_delete + Util.intToBinary(id_w[i],20));

            int c = 0;
            if(sigma.get(id_w[i]) != null){
                c = sigma.get(id_w[i]);
            }
            AddDataType add_data = new AddDataType();
            String bitString_r = Util.generate(parameter);
            add_data.r = new BigInteger(bitString_r,2);
            add_data.l = new BigInteger(SHA.F(K1_A + c),2);
            add_data.d = new BigInteger(Util.Xor(Util.intToBinary(id,16), SHA.F(K2_A + bitString_r).substring(0,16)),2);
            add_data.id_del = new BigInteger(SHA.F(K1_D + Util.intToBinary(id, 16)),2);
            result.add(add_data);
        }
        return result;
    }


    public List<BigInteger> delete(int id,int[] id_w){
        List<BigInteger> result = new ArrayList<>();
        for (int i : id_w) {
            String K1_D = SHA.F(key_delete + Util.intToBinary(i, 20));
            BigInteger id_del = new BigInteger(SHA.F(K1_D + Util.intToBinary(id, 16)), 2);
            result.add(id_del);
        }
        return result;
    }

    public void writeEDBToLocal(String path){
        try {
            File writeName = new File(path);
            writeName.createNewFile();
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                for(int i = 0;i < this.EDB.size();i++){
                    Encrypted_data round = EDB.get(i);
                    out.write(round.l + " ");
                    out.write(round.d + " ");
                    out.write(round.r.toString());
                    out.write("\r\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
//        long endTime1 = System.currentTimeMillis();
//        long endTime2 = System.currentTimeMillis();
//        System.out.println("消耗时间" + (endTime2 - endTime1) + "ms");
    }

}
