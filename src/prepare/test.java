package prepare;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import DO.*;
public class test {
    public static void main(String[] args) throws Exception {
        //建立关键字到文档id的倒排索引
        Map<Integer, int[]> keywordIdPair = BuildKeywordIdPair.build("./src/test1.txt");

        //产生密钥并产生相应密文
        Setup setup = new Setup(256, keywordIdPair);

        //将密文写到本地
        setup.writeEDBToLocal("./src/EDBTest1.txt");

        //输入需要删除的文件id，以及其id包含有的关键字
        List<BigInteger> Del = setup.delete(8136,new int[]{0});

        //输入需要插入的文件id，以及其id包含有的关键字
        List<AddDataType> add_data = setup.add(10000,new int[]{0});
        System.out.println(Del);

        //输入关键字，产生搜索密钥
        SearchKeyType key = setup.generateSearchKey(0,0);

        System.out.println("********************************");
    }
}
//28860578869374466694940246811443027127185758187284536253442856891160343135559
