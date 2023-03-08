package prepare;

import javax.xml.bind.DatatypeConverter;

/*
补位时，将末尾32位作为其长度信息，原文64位
 */
public class SHA {
    public static String F(String input){
        int a = 0x6a09e667;
        int b = 0xbb67ae85;
        int c = 0x3c6ef372;
        int d = 0xa54ff53a;
        int e = 0x510e527f;
        int f = 0x9b05688c;
        int g = 0x1f83d9ab;
        int h = 0x5be0cd19;

        int[] k = {0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
                0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
                0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
                0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
                0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
                0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
                0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
                0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
        };

        byte[] input_byte = input.getBytes();
        int input_length = input_byte.length + 1;
        int data_length;
        if(input_length % 64 > 56){
            data_length = (int)(Math.ceil(input_length / 64.0) + 1) * 64;
        }else{
            data_length = (int)Math.ceil(input_length / 64.0) * 64;
        }
        byte[] data = new byte[data_length];
        System.arraycopy(input_byte, 0, data, 0, input_byte.length);
        data[input_byte.length] = (byte)0x80;
        byte[] msgLen = intToByte4(input_byte.length * 8);
        System.arraycopy(msgLen,0,data,data_length - 4,4);

        //以64个(512位)为一个块进行循环
        for(int i = 0;i < data_length;i = i + 64){
            int[] w = new int[64];
            for(int j = 0;j < 16;j++){
                w[j] = byteToInt(data,i + j * 4,i + (j + 1) * 4);
            }
            for(int j = 16;j < 64;j++){
                int s0 = rightRotate(w[j - 15],7) ^ rightRotate(w[j - 15],18) ^ (w[j - 15] >>> 3);
                int s1 = rightRotate(w[j - 2],17) ^ rightRotate(w[j - 2],19) ^ (w[j - 2] >>> 10);
                w[j] = w[j - 16] + s0 + w[j - 7] + s1;
            }

            for(int j = 0;j < 64;j++){
                int S1 = rightRotate(e,6) ^ rightRotate(e,11) ^ rightRotate(e,25);
                int ch = (e & f) ^ ((~e) & g);
                int temp1 = h + S1 + ch + k[j] + w[j];
                int S0 = rightRotate(a,2) ^ rightRotate(a,13) ^ rightRotate(a,22);
                int maj = (a & b) ^ (a & c) ^ (b & c);
                int temp2 = S0 + maj;

                h = g;
                g = f;
                f = e;
                e = d + temp1;
                d = c;
                c = b;
                b = a;
                a = temp1 + temp2;
            }
        }
        StringBuilder result = new StringBuilder(256);
        result.append(intToBinary32(a));
        result.append(intToBinary32(b));
        result.append(intToBinary32(c));
        result.append(intToBinary32(d));
        result.append(intToBinary32(e));
        result.append(intToBinary32(f));
        result.append(intToBinary32(g));
        result.append(intToBinary32(h));

        return result.toString();
    }
    public static String SHA256(String input){
        int a = 0x6a09e667;
        int b = 0xbb67ae85;
        int c = 0x3c6ef372;
        int d = 0xa54ff53a;
        int e = 0x510e527f;
        int f = 0x9b05688c;
        int g = 0x1f83d9ab;
        int h = 0x5be0cd19;

        int[] k = {0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
                0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
                0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
                0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
                0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
                0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
                0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
                0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
        };

        byte[] input_byte = input.getBytes();
        int input_length = input_byte.length + 1;
        int data_length;
        if(input_length % 64 > 56){
            data_length = (int)(Math.ceil(input_length / 64.0) + 1) * 64;
        }else{
            data_length = (int)Math.ceil(input_length / 64.0) * 64;
        }
        byte[] data = new byte[data_length];
        System.arraycopy(input_byte, 0, data, 0, input_byte.length);
        data[input_byte.length] = (byte)0x80;
        byte[] msgLen = intToByte4(input_byte.length * 8);
        System.arraycopy(msgLen,0,data,data_length - 4,4);

        //以64个(512位)为一个块进行循环
        for(int i = 0;i < data_length;i = i + 64){
            int[] w = new int[64];
            for(int j = 0;j < 16;j++){
                w[j] = byteToInt(data,i + j * 4,i + (j + 1) * 4);
            }
            for(int j = 16;j < 64;j++){
                int s0 = rightRotate(w[j - 15],7) ^ rightRotate(w[j - 15],18) ^ (w[j - 15] >>> 3);
                int s1 = rightRotate(w[j - 2],17) ^ rightRotate(w[j - 2],19) ^ (w[j - 2] >>> 10);
                w[j] = w[j - 16] + s0 + w[j - 7] + s1;
            }

            for(int j = 0;j < 64;j++){
                int S1 = rightRotate(e,6) ^ rightRotate(e,11) ^ rightRotate(e,25);
                int ch = (e & f) ^ ((~e) & g);
                int temp1 = h + S1 + ch + k[j] + w[j];
                int S0 = rightRotate(a,2) ^ rightRotate(a,13) ^ rightRotate(a,22);
                int maj = (a & b) ^ (a & c) ^ (b & c);
                int temp2 = S0 + maj;

                h = g;
                g = f;
                f = e;
                e = d + temp1;
                d = c;
                c = b;
                b = a;
                a = temp1 + temp2;
            }
        }
        byte[] result = new byte[32];
        System.arraycopy(intToByte4(a), 0, result, 0, 4);
        System.arraycopy(intToByte4(b), 0, result, 4, 4);
        System.arraycopy(intToByte4(c), 0, result, 8, 4);
        System.arraycopy(intToByte4(d), 0, result, 12, 4);
        System.arraycopy(intToByte4(e), 0, result, 16, 4);
        System.arraycopy(intToByte4(f), 0, result, 20, 4);
        System.arraycopy(intToByte4(g), 0, result, 24, 4);
        System.arraycopy(intToByte4(h), 0, result, 28, 4);
        return DatatypeConverter.printHexBinary(result);
    }
    public static String G(String input){
        int a = 0x6a09e667;
        int b = 0xbb67ae85;
        int c = 0x3c6ef372;
        int d = 0xa54ff53a;
        int e = 0x510e527f;
        int f = 0x9b05688c;
        int g = 0x1f83d9ab;
        int h = 0x5be0cd19;

        int[] k = {0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
                0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
                0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
                0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
                0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
                0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
                0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
                0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
        };

        byte[] input_byte = input.getBytes();
        int input_length = input_byte.length + 1;
        int data_length;
        if(input_length % 64 > 56){
            data_length = (int)(Math.ceil(input_length / 64.0) + 1) * 64;
        }else{
            data_length = (int)Math.ceil(input_length / 64.0) * 64;
        }
        byte[] data = new byte[data_length];
        System.arraycopy(input_byte, 0, data, 0, input_byte.length);
        data[input_byte.length] = (byte)0x80;
        byte[] msgLen = intToByte4(input_byte.length * 8);
        System.arraycopy(msgLen,0,data,data_length - 4,4);

        //以64个(512位)为一个块进行循环
        for(int i = 0;i < data_length;i = i + 64){
            int[] w = new int[64];
            for(int j = 0;j < 16;j++){
                w[j] = byteToInt(data,i + j * 4,i + (j + 1) * 4);
            }
            for(int j = 16;j < 64;j++){
                int s0 = rightRotate(w[j - 15],7) ^ rightRotate(w[j - 15],18) ^ (w[j - 15] >>> 3);
                int s1 = rightRotate(w[j - 2],17) ^ rightRotate(w[j - 2],19) ^ (w[j - 2] >>> 10);
                w[j] = w[j - 16] + s0 + w[j - 7] + s1;
            }

            for(int j = 0;j < 64;j++){
                int S1 = rightRotate(e,6) ^ rightRotate(e,11) ^ rightRotate(e,25);
                int ch = (e & f) ^ ((~e) & g);
                int temp1 = h + S1 + ch + k[j] + w[j];
                int S0 = rightRotate(a,2) ^ rightRotate(a,13) ^ rightRotate(a,22);
                int maj = (a & b) ^ (a & c) ^ (b & c);
                int temp2 = S0 + maj;

                h = g;
                g = f;
                f = e;
                e = d + temp1;
                d = c;
                c = b;
                b = a;
                a = temp1 + temp2;
            }
        }
        StringBuilder result = new StringBuilder(256);
        result.append(intToBinary32(a));
        result.append(intToBinary32(b));
        result.append(intToBinary32(c));
        result.append(intToBinary32(d));
        result.append(intToBinary32(e));

        return result.toString();
    }

    public static byte[] intToByte4(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 0xFF);
        targets[2] = (byte) (i >> 8 & 0xFF);
        targets[1] = (byte) (i >> 16 & 0xFF);
        targets[0] = (byte) (i >> 24 & 0xFF);
        return targets;
    }
    public static int byteToInt(byte[] buf,int start,int end) {
        int result = 0;
        for(int i = start;i < end;i++){
            result = (result << 8) | (0x000000FF & ((int)buf[i]));
        }
        return result;
    }
    public static int rightRotate(int input,int n){
        return (input >>> n) | (input << (32 - n));
    }


    public static String intToBinary32(int i){
        String binaryStr = Integer.toBinaryString(i);
        while(binaryStr.length() < 32){
            binaryStr = "0" + binaryStr;
        }
        return binaryStr;
    }
    public static void main(String[] args) {
        long endTime1 = System.currentTimeMillis();
        String resultF = SHA.F("21312314");
        System.out.println(resultF);
        long endTime2 = System.currentTimeMillis();
        System.out.println("消耗时间" + (endTime2 - endTime1) + "ms");

//        long endTime5 = System.currentTimeMillis();
//        String resultG = SHA.G("E650AAEA5220E4112E1BA700644CBD4D9005190EADB0C68F95CB5FF036BAA59C");
//        long endTime6 = System.currentTimeMillis();
//        System.out.println("消耗时间" + (endTime6 - endTime5) + "ms");
//
//        long endTime3 = System.currentTimeMillis();
//        String resultSHA256 = SHA.SHA256("E650AAEA5220E4112E1BA700644CBD4D9005190EADB0C68F95CB5FF036BAA59C");
//        long endTime4 = System.currentTimeMillis();
//        System.out.println("消耗时间" + (endTime4 - endTime3) + "ms");
//        System.out.println(resultF);
//        System.out.println(resultG);
//        System.out.println(resultSHA256);
    }

    //1000000101111011010010101010110001111000111001110101101010101101001100110000000111000001111101100100100000010000001001011110110011111000010001001000101100011110011111010100001101011101111101010110001110111011010000010110101111001100100001110100010000011111
    //1000000101111011010010101010110001111000111001110101101010101101001100110000000111000001111101100100100000010000001001011110110011111000010001001000101100011110011111010100001101011101111101010110001110111011010000010110101111001100100001110100010000011111

    //1000000101111011010010101010110001111000111001110101101010101101001100110000000111000001111101100100100000010000001001011110110011111000010001001000101100011110
    //1000000101111011010010101010110001111000111001110101101010101101001100110000000111000001111101100100100000010000001001011110110011111000010001001000101100011110

}
