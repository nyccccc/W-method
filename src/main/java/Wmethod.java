
import java.io.*;
import java.util.*;

/**
 * 这是W-method的主类。包含了定义状态机的数据结构，读取文件等
 * @author nyccc
 */
public class Wmethod {
  /**
   * 在状态机中的最大状态数
   */
  public static final int MAX_STATES =20;
  /**
   * 最大的输入字符数
   */
  public static final int MAX_INPUTS =50;
  /**
   * 最大的输出字符数
   */
  public static final int MAX_OUTPUTS =50;
  /**
   * 最大的转换数
   */
  public static final int MAX_TRANSITIONS =250;
  /**
   * 一个状态机中包含 id，描述，一系列的输出边
   * 最大的存储的状态，使用数组存储
   */
  public static State [] FSM =new State[MAX_STATES];
  /**
   * 不同状态的数量
   */
  public static int numberOfStates;
  /**
   *  状态机的开始状态
   */
  public static int startState;
  public static int numberOfTransitions=0;
  /**
   * 状态机的文件名
   */
  public static String fsmFilename;
  /**
   *  输入/输出的字母表及其数量
   */
  private static final String[] OUTPUT_ARRAY = new String[MAX_OUTPUTS ];
  private static final String[] INPUT_ARRAY = new String[MAX_INPUTS];
  private static int countOutputs = 0;
  private static int countInputs = 0;
  /**
   * 读取目标文件的名称
   */
  private static Scanner fileSource;

  /**
   * 从源文件读取FSM描述的方法。
   * 假设startState为1。
   */
  public static void getFSM (){
    // 一个从输入的转换
    String aTransition;
    // 输入的状态ID（-1是无效ID）
    //到目前为止尚未输入任何状态。
    numberOfStates=0;
    startState=1;
    try{
      //读取存储状态机的文件
      BufferedReader fsmFile= new BufferedReader (new FileReader(fsmFilename));
      aTransition=fsmFile.readLine();
      //指示循环是否终止。
      boolean done=false;
      if (aTransition==null) {
        done=true;
      }

      while (!done){
        //状态机中的转换数量加一
        numberOfTransitions++;
        /* System.out.println("Next transition: "+numberOfTransitions+" "+aTransition); */
        //将读取的转换进行分离，分别为状态，边，输入和输出
        splitTransition(aTransition);
        //读取下一个转换
        aTransition=fsmFile.readLine();
        //设置终止的条件，如果没有下一条的状态转换，终止。
        if (aTransition==null || (numberOfTransitions==MAX_TRANSITIONS)) {
          done=true;
        }
      }
    }catch (FileNotFoundException e){
      //没有找到状态机文件的输出
      System.out.println("文件: "+fsmFilename+" 没有找到");
      System.exit(0);
    }catch (IOException e){
      Utilities.printException("w算法"," 获取FSM","读取时引发了IO异常:"+
              fsmFilename +"未找到");
      Utilities.printException("w算法", "获取FSM", "转换输入： "+numberOfTransitions);
    }
  } //End getFSM()

  /**
   * 例如：
   * 输入为： 3 4 a/0
   * 其中： 3表示为的开始状态
   *       4 表示为结束状态
   *       a 表示为输入的字符
   *       0 表示为输出的字符
   * @param transition 从文件中读取的一条转换的信息，目的是使其分离
   */
  static void splitTransition(String transition){
    String [] token =new String[3];
    String [] edgeToken;
    //临时的存储空间
    String inputLabel, outputLabel, edgeLabel;
    //在转换中的开始状态的ID
    int ID;
    //在转换中的结束状态的ID
    int toID;
    //将字符串进行分解为一个个的单词或者是标记。
    StringTokenizer transitionTokens=new StringTokenizer(transition);
    int tokensFound=0;

    //将转换分为三个部分：源，目标和标签。
    while(transitionTokens.hasMoreTokens()){
      token[tokensFound]=transitionTokens.nextToken();
      Utilities.debugFSM("下一个token: "+token[tokensFound]);
      tokensFound++;
    }// End of while

    //为了防止编译错误
    ID=-1;
    try{
      // 开始状态，将状态的ID转换为整数
      ID=Integer.parseInt(token[0]);
      //检查这个状态时候曾经出现过
      if (newState(ID)){
      numberOfStates++;
      //如果没有出现过，在状态机中创建一个新的状态
      FSM[ID]=new State(ID);
    }
    }catch(NumberFormatException e){
      Utilities.printException("Wmethod", "分离转换", "从状态ID必须是一个数字 "+token[0]);
    }
    //为了避免编译错误的初始换
    inputLabel="";
    outputLabel="";
    toID=-1;
    try{
      //目标状态， 将状态的ID转换为整数
      toID=Integer.parseInt(token[1]);
      //边的标签
      edgeLabel=token[2];
      //将边缘标签拆分为输入和输出标签。
      edgeToken=splitEdgeLabel(edgeLabel);
      //定义输出和输出的标签
      inputLabel=edgeToken[0];
      outputLabel=edgeToken[1];
    }catch(NumberFormatException e){
      Utilities.printException("Wmethod", "分离转换", "从状态ID必须是一个数字 "+token[1]);
      }
    
    // 检查输出的标签是否曾经存在
    int traverser = 0;
    boolean found = false;
    while(traverser < OUTPUT_ARRAY.length){
      //检查符号是否已在输出数组中。
      if(OUTPUT_ARRAY[traverser].equals(outputLabel)){
        found = true;
        break;
      }
      traverser++; 
    }
    // 将新的输出的标签加入数组
    if( ! found && ! "".equals(outputLabel)){
      Utilities.debugFSM("添加 " + outputLabel + " 到输出数组的 " + countOutputs + "位置.");
      OUTPUT_ARRAY[countOutputs] = outputLabel;
      countOutputs++;
    }
    
    // 检查输入标签是否已经出现。
    int traverse2 = 0;
    boolean found2 = false;
    while(traverse2 < INPUT_ARRAY.length){
      //已经存在了标签数组中
      if(INPUT_ARRAY[traverse2].equals(inputLabel)){
        found2 = true;
        break;
      }
      traverse2++;
    }
    
    // 将新的输入符号添加到输入数组。
    if( ! found2 && ! "".equals(inputLabel)){
      Utilities.debugFSM("添加 " + inputLabel + " 到输入数组的 " + countInputs + "位置.");
      INPUT_ARRAY[countInputs] = inputLabel;
      countInputs++;
    }
    try{
      FSM[ID].addEdge(new Edge(ID, toID, inputLabel, outputLabel));
    }catch (InvalidEdgeException e){
      Utilities.debugFSM("尝试添加无效边。 状态ID与边起始ID不匹配");
      Utilities.debugFSM("状态ID:" + ID+ " 边起始ID" + ID);
      System.exit(0);
    }
  } // End splitTransition()

  /**
   * @param edgeToken 将x/y形式的边缘标签拆分为x和y的方法。
   * @return 分离完成的输入和输出数组
   */
  private static String []  splitEdgeLabel(String edgeToken){
    //定义一个新的数组用于存储输入输出字符
    String []token=new String [2];
    Utilities.debugFSM("边的标记:" + edgeToken);
    //按照 / 进行分离
    StringTokenizer ioTokens=new StringTokenizer(edgeToken, "/");
    token[0]=ioTokens.nextToken();
    token[1]=ioTokens.nextToken();
    return token;
  }//End splitEdgeLabel()

  /**
   * @param stateID 输入的是这个状态的ID
   * @return 这个状态是否曾经出现过
   */
  private static boolean newState(int stateID ){
    return FSM[stateID] == null;
  }//End newState()

  /**
   * 打印输入FSM的所有状态和转换的方法。
   * @param inputAlphabet 输入的字母表
   */
  public static void printFSM ( String [] inputAlphabet){
    // 临时变量
    Set edges;
    int stateID;
    
    System.out.println("状态的数量为: "+numberOfStates);
    System.out.println("边的数量为: "+numberOfTransitions);
    System.out.println("输入的字符为: ");
    for ( String i:inputAlphabet ){
      System.out.print (i+" ");
    }
    
    System.out.println("\n输出的字符为:");
    int count = 0;
    Arrays.sort(OUTPUT_ARRAY);
    while(count<MAX_OUTPUTS ){
      if(! "".equals(OUTPUT_ARRAY[count])) {
        System.out.print(OUTPUT_ARRAY[count]+" ");
      }
      count++;
    }
    //输出读取文件中对应的转换关系
    System.out.println("\n开始状态 \t 状态转移 \t 结束状态");
    for (int i=0; i<MAX_STATES; i++){
      if(FSM[i]!=null){
        stateID=FSM[i].getID();
        edges=FSM[i].getEdgeSet();
        for ( Object edge : edges ) {
          Edge e = ( Edge ) edge;
          System.out.println (stateID + "\t\t\t " + e.input () + "/" + e.output () + "\t\t " + e.tail ());
        }
      }
    }
    count = 0;
    //将输入字母表进行排序
    sortInputs();
    while(! "".equals(INPUT_ARRAY[count])){
      Utilities.debugFSM("可能的输入 "+count +": " +INPUT_ARRAY[count]);
      count++;
    }
  }

  /**
   *
   * @return 输入的文件名为
   */
  private static String getFilename(){
    //提示输入FSM的文件
    System.out.print("输入FSM文件名: ");
    return (fileSource.nextLine());
  }

  /**
   * 将输入的字符进行排序
   */
  public static void sortInputs(){
    boolean Swapped = true;
    int csize = countInputs;
    while(Swapped){
      Swapped = false;
      //使用的是冒泡排序的方式
      for(int i = 0; i < csize-1; i++){
        if(INPUT_ARRAY[i].compareTo(INPUT_ARRAY[i+1]) > 0){
          String temp;
          temp = INPUT_ARRAY[i];
          INPUT_ARRAY[i] = INPUT_ARRAY[i+1];
          INPUT_ARRAY[i+1] = temp;
          Swapped = true;
        }
      }
      csize--;
    }
  }

  /**
   *
   * @param searchString 需要查找的字符串
   * @param searchVector 字符串集合所在的位置
   * @return 是否存在
   */
  public static boolean existsInVector( String searchString, Vector< String > searchVector){
      for ( Object o : searchVector ) {
        if ( o.toString ().equals (searchString) ) {
          return true;
        }
      }
      return false;
    }

  /**
   *
   * @param tree 测试树
   * @param tableManager P表
   * @return 测试用例
   */
  public static Vector<String> generateTests(TestingTree tree, pTableManager tableManager){
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
         if ( ! existsInVector (testCase, testCases) ) {
           testCases.add (testCase);
         }
       }
     }
     return testCases;
   }

  /**
   * Main函数
   * @param args .
   */
   public static void main(String [] args){
    /* System.out.println("Test Generation Using the W-method. V2.0. August 1, 2013\n");*/
     fileSource=new Scanner(System.in);
     //获取存储状态机的文件名
     fsmFilename=getFilename();
     // 初始化输出的数组
     Arrays.fill (OUTPUT_ARRAY, "");
     // 初始输入的数组
     Arrays.fill (INPUT_ARRAY, "");
     //获取状态机，决定输入和输出的字母表
     getFSM ();
     String [] realInput = new String [countInputs];
     //实数输入仅包含输入字母的元素。
     if ( realInput.length >= 0 ) {
       System.arraycopy (INPUT_ARRAY, 0, realInput,
                 0, realInput.length);
     }
     Arrays.sort(realInput);
     System.out.println("从文件 "+fsmFilename+" 获取的状态机");
     if(Utilities.fsmPrintSw) {
       printFSM (realInput);
     }
     //生成测试树，见书131页： 生成特征集 ，137页生成测试树
     TestingTree transitionCover = new TestingTree(FSM, numberOfStates); 
     //生成P表和W-set 见书137
     pTableManager w = new pTableManager(FSM, numberOfStates, realInput);
     // 生成测试
     Vector <String> tests=generateTests(transitionCover, w);
     // 打印测试
     Utilities.printAllTestCases(tests);
     
    /*
     * TODO：编写必要的代码以遍历所有测试用例，并使用
     *  Utilities.runFSM（）方法针对FSM运行它们。
     */
     /* Utilities.runFSM(FSM, 1, "a a b a b", " ");*/
     for ( String test : tests ) {
       Utilities.runFSM (FSM, 1, test.replace ("", " ").trim (), " ");
     }
   }
}




