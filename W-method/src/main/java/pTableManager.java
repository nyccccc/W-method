import java.util.*;
/**
 * @author nyccc
 */
public class pTableManager{
  /**
   * 定义P表，将其定义为数组形式，数据结构为pTable
   */
  public pTable[] tableArray;
  /**
   * 定义状态机数组，数据结构为State
   */
  public State[] FSMArray;
  /**
   * 定义现在group的数量
   */
  public int currentNumberOfGroups;
  /**
   * 定义最大的表的数量
   */
  public static int maxTables = 50;
  /**
   * 定义状态的数量
   */
  public int numberOfStates;
  /**
   * 定义特征集的集合
   */
  public Vector  W;
  /**
   * 定义表的数量
   */
  public int tableCount;
  /**
   * 定义P表类型的数据结构为第一个表
   */
  public pTable firstTable;

  /**
   *  构造函数
   * @param stateArray 数组类型，输入的状态的数组
   * @param numStates 状态的数量
   * @param inputArray 状态转换之间的输入数组
   */
  public pTableManager(State [] stateArray, int numStates, String [] inputArray){
    // 创建一个全局变量的数组来存储P表
    tableArray = new pTable[maxTables];
    // 创建一个全局变量的数组存储状态
    FSMArray = stateArray;
    // 创建一个全局变量的整型数存储状态的数量
    numberOfStates = numStates;
    //构造第一个表，调用P表的构造函数
    firstTable = new pTable(stateArray, numStates, inputArray);
    //初始化特征集 W-set
    W = new Vector();
    //存入P表大的第一个的状态
    tableArray[0] = firstTable.getPOne().returnCopy();

    // 如果数组中的第一个元素（即P表的第一状态）的组数和状态数相同
    //该部分参考书的 131页的第3.5节构造K阶等价特征集
    if(tableArray[0].numGroups == numberOfStates){
      Utilities.debugPtable("特殊情况：仅1个P表");
      tableCount = 1;
      currentNumberOfGroups = tableArray[0].numGroups;
      calculateW();
      return;
    }
    // 如果不仅仅一个表，继续从表中读取
    tableArray[1] = firstTable.getPNext().returnCopy();
    //同上
    if(tableArray[1].numGroups == numberOfStates){
      Utilities.debugPtable("特殊情况：仅2个P表");
      tableCount = 2;
      currentNumberOfGroups = tableArray[1].numGroups;
      calculateW();
      return;
    }
    int count = 2;
    Utilities.debugPtable("正常情况：超过了2章表");
    Utilities.debugPtable("状态数为: " + numberOfStates);

    //当分组的数量小于状态的数量的时候，开始循环
    while(currentNumberOfGroups < numberOfStates){
      tableArray[count] = firstTable.getPNext().returnCopy();
      currentNumberOfGroups = tableArray[count].numGroups;
      Utilities.debugPtable("现在分组的数量为: " + currentNumberOfGroups);
      Utilities.debugPtable("状态的数量为:" + numberOfStates);
      Utilities.debugPtable("增加一个表.");
      Utilities.debugPtable("长度为 = " + (count+1));
      count++;
    }

    //将所有的表数赋值给全局变量
    tableCount = count;
    //计算特征集W-set
    calculateW();
  }

  /**
   * 计算特征集
   */
  public void calculateW ( ){
    String z;
    //如果只有一张表，就在这里检测
    if(tableArray[0].numGroups == numberOfStates){
      String last;
      for(int a = 1; a <= numberOfStates; a++){
        for(int b = 1; b <= numberOfStates; b++){
          if(a != b){
            Utilities.debugPtable("A:" + a);
            Utilities.debugPtable("B:" + b);
            last = tableArray[0].OCompare(a, b);
            if(!W.contains(last) && ! "".equals(last)) {
              W.add(last);
            }
          }
        }
      }
      // 如果调试特征集为真，打印特征集
      if (Utilities.WSetDebugSw) {
        printW();
      }
      return;
    }
    for(int i = 1; i <= numberOfStates; i++){
      for(int j = 1; j <= numberOfStates; j++){
        if(i != j){
          for(int k = 0; k < tableCount-1; k++){
            State iState = FSMArray[i];
            State jState = FSMArray[j];
            int iValue = iState.getID ();
            int jValue = jState.getID ();
            if((tableArray[k].findGroup(Integer.toString (iValue)) ==
                    tableArray[k].findGroup(Integer.toString (jValue))) &&
                    (tableArray[k+1].findGroup(Integer.toString (iValue)) !=
                    tableArray[k+1].findGroup(Integer.toString (jValue)))){
              int currentI = iState.getID();
              int currentJ = jState.getID();
              z = "";
              int r = k;
              while( r >= 0 ){
                Utilities.debugPtable("Current r: " + r);
                String nextSymbol = tableArray[r].GCompare(currentI, currentJ);
                z = z.concat(nextSymbol);
                Utilities.debugPtable("Z:" + z);
                try{
                  currentI = Integer.parseInt(tableArray[r].O(currentI, nextSymbol));
                  currentJ = Integer.parseInt(tableArray[r].O(currentJ, nextSymbol));
                } catch(Exception e) { 
                  Utilities.printException("pTableManager", "CalculateW", "THIS SHOULD NOT HAPPEN"); 
                }
                r--;
              }
              String lastSymbol = tableArray[0].OCompare(currentI, currentJ);
              Utilities.debugPtable("Last Symbol: " + lastSymbol);
              z = z.concat(lastSymbol);
              Utilities.debugPtable("Z Adding to W:" + z);
              if(!W.contains(z)){
                W.addElement(z);
              }
            }else{
              Utilities.debugPtable("重置没有找到.");
              String lastSymbol = tableArray[0].OCompare(iValue, jValue);
              if(!W.contains(lastSymbol) && (! "".equals(lastSymbol))){
                if((iValue == 3) && (jValue == 4)) {
                  Utilities.debugPtable("HELLO");
                }
                W.addElement(lastSymbol);
              }              
            }
          }
        }
      }
    }
   // 判断是否能够打印特征集，如果为true，进行打印
    if (Utilities.WSetDebugSw) {
      printW();
    }
  }

  /**
   * 打印特征覆盖集
   */
  public void printW ( ){
    System.out.println("\nW Set中有"+ W.size()+"个特征集. 分别为：");
    //将w-set中的特征集进行排序
    Collections.sort(W);
    for ( Object o : W ) {
      String wElement = ( String ) o;
      System.out.print (wElement + " ");
    }
      System.out.println();
  }

  /**
   * @return 特征覆盖集
   */
  public Vector getW(){
    return W;
  }
}