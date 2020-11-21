import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * 此类包含调试开关和几种实用程序方法。
 * @author nyccc
 */
public class Utilities{
  /**
   * 设定是否打印W-set
   */
  public static boolean fsmPrintSw=true;
  /**
   * 设定是否调试P-table
   */
  public static boolean pTableDebugSw=false;
  /**
   * 设定是否调试测试树
   */
  public static boolean testingTreeDebugSw=false;
  /**
   * 设定打印所有的转换覆盖集调试过程中是否存在
   */
  public static boolean transitionCoverSetDebugSw=true;
  /**
   * 设定调试是否创建状态机
   */
  public static boolean fsmCreationDebugSw=false;
  /**
   * 设定调试是否存在状态机
   */
  public static boolean fsmExecutionDebugSw=true;
  /**
   * 设定调试是否存在W-set
   */
  public static boolean WSetDebugSw=true;

  /**
   * 调试P-table
   * @param s
   */
  public static void debugPtable(String s){
    if(pTableDebugSw) {
      System.out.println(s);
    }
  }

  /**
   * 调试测试树
   * @param s
   */
  public static void debugTestingTree(String s){
    if(testingTreeDebugSw) {
      System.out.println(s);
    }
  }

  /**
   * 调试状态机
   * @param s 输入的信息
   */
  public static void debugFSM(String s){
    if(fsmCreationDebugSw) {
      System.out.println(s);
    }
  }

  /**
   * 调试状态机的执行
   * @param s 输入的一条状态转换
   */
  public static void debugFSMExecution(String s){
    if(fsmExecutionDebugSw) {
      System.out.println(s);
    }
  }

  /**
   * 发生异常时打印
   * @param c 异常所在的类
   * @param m 异常所在的方法
   * @param s 异常的信息
   */
  public static void printException(String c, String m, String s){
    System.out.println("\n发生异常. \n类:"+c +"\n方法: "+m+"\n"+s);
    System.exit(0);
  }

  /**
   * 打印所有的测试用例
   * @param testCases 生成的测试用例的集合
   */
  public static void printAllTestCases(Vector<String> testCases){
    System.out.println("\n一共存在: "+ testCases.size()+" 个测试用例");
    Collections.sort(testCases);
    System.out.println("测试用例为: " + testCases);
  }

  /**
   * 运行状态机
   * @param FSM 输入状态机
   * @param stateID 状态的ID
   * @param input 输入的信息
   * @param separator 分离 input
   */
  public static void runFSM(State [] FSM, int stateID, String input, String separator){
    //由FSM生成的输出序列。 非法输入将生成空输出。
    StringBuilder outputPattern= new StringBuilder ();
    //输入序列中的输入符号。
    String token;
    StringTokenizer inputTokens=new StringTokenizer(input, separator);
    //将FSM置于StateID。
    int currentState=stateID;
    Utilities.debugFSMExecution("\nFSM执行开始。 输入值: "+input+" 初始状态: "+stateID);
    if(FSM[stateID]==null){
      Utilities.printException("wAlgorithm", "runFSM", "无效的开始状态. 执行中止.");
      return;
    }
    while(inputTokens.hasMoreTokens()){
      //从输入中获取下一个token
      token=inputTokens.nextToken();
      try{
        Utilities.debugFSMExecution("现在的状态: "+currentState);
        //下一个状态的边
        Edge nextStateEdge=FSM[currentState].getNextState(token);
        //生成状态输出的边
        String outputGenerated=nextStateEdge.output();
        //获取状态转换中的结束状态
        int nextState=nextStateEdge.tail();
        //生成的输出
        outputPattern.append (outputGenerated).append (",");
        Utilities.debugFSMExecution(" 输入: "+token+" 下一个状态: "+nextState+
                " 输出: "+outputGenerated);
        currentState=nextState;
      }catch (NoNextStateException e){
        Utilities.printException("W-method", "runFSM", " 无效的token: "+token);
      }
    }
    Utilities.debugFSMExecution("\nFSM执行完成。 最终状态: "+currentState);
    Utilities.debugFSMExecution("输出模式:"+outputPattern);
  }
}