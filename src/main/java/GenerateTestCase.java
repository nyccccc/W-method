import java.util.Vector;

/**
 * @author : YiChen Niu (yniu7@sheffield.ac.uk)
 * @version : 0.1
 * @program :W-method
 * @description :生成测试用例
 * @url :
 * @create :2020-11-22
 */
public class GenerateTestCase {
    /**
     *
     * @param tree 测试树
     * @param tableManager P表
     * @return 测试用例
     */
    public static Vector<String> generateTests( TestingTree tree, pTableManager tableManager){
      Vector pVector;
      Vector wVector;
      //用于存储生成的测试用例
      Vector<String> testCases = new Vector<String>();
      //获取过渡覆盖集（P）
      pVector = tree.getPValues();
      //获取W-set
      wVector = tableManager.getW();
      //过渡覆盖集包含空字符串。
      pVector.add("");

      /*
       * 统计计算 P×W 从而生成测试用例
       * 我们假设 m = n，
       *         m是被测程序的FSM中的状态数
       *         n是设计FSM中的状态数。
       * 在集合乘法过程中可能会重复一些测试用例。
       * 计算的过程可以参考：软件测试基础教程的 136页
       * 链接：https://pan.baidu.com/s/1LLBbonc4Nt6uyOR8vclZ5w 提取码：Niuy
       */
      for ( Object s : pVector ) {
         for ( Object o : wVector ) {
           String wValue = ( String ) o;
           String testCase = s + wValue;
           if ( ! Wmethod.existsInVector (testCase, testCases) ) {
             testCases.add (testCase);
           }
         }
       }
       return testCases;
     }
}
