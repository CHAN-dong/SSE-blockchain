package prepare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class BuildKeywordIdPair {
        public static Map<Integer, int[]> build(String path) throws Exception{
            Map<Integer, int[]> pair = new HashMap<>();
            File file = new File(path);
            if(file.isFile()&&file.exists()){
                InputStreamReader fla = new InputStreamReader(new FileInputStream(file));
                BufferedReader scr = new BufferedReader(fla);
                String str;
                while((str = scr.readLine()) != null){
                    String[] data = str.split(" ");
                    int keyword = Integer.parseInt(data[0]);
                    int[] id = new int[data.length - 1];
                    for(int i = 3;i < data.length;i++){
                        id[i - 3] = Integer.parseInt(data[i]);
                    }
                    pair.put(keyword,id);
                }
                scr.close();
                fla.close();
            }
            return pair;
        }
}

