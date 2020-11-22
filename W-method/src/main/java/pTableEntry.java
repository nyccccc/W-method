import java.util.Arrays;
import java.util.HashSet;
/**
 * @author nyccc
 */
public class pTableEntry{
  /**
   * 定义现在特征集的数量
   */
  public int currentGroup;
  /**
   * 定义现在状态的数量
   */
  public int currentState;
  /**
   * 定义状态迁移的输出的数组
   */
  public String[] outputs;
  /**
   * 定义状态迁移后的下一个状态
   */
  public String[] nextStates;
  /**
   * 定义可能输入的数组
   */
  public String[] possibleInputs;
  /**
   * 定义下一个特征集的数组
   */
  public String[] nextGroups;

  /**
   * @description :构造函数
   * @param currentGroup 现在用于的等价划分表的数
   * @param currentState 现在的状态数
   * @param output 输出的字符串
   * @param nextStates 下一个的可能的状态的集合
   * @param posInput 可能的输入
   * @param nextGroups 下个等价划分表的分组
   */
  public pTableEntry(int currentGroup, int currentState,  String [] output, String [] nextStates, String [] posInput, String [] nextGroups){
    this.currentGroup = currentGroup;
    this.currentState = currentState;
    this.outputs = output;
    this.nextStates = nextStates;
    this.possibleInputs = posInput;
    this.nextGroups = nextGroups;
  }
  
  /**
   *  @description 构造函数
   *  @param s 可能的输入的字符串
   *  @param inputArray 输入的字符
   */
  public pTableEntry(State s, String[] inputArray){
    Utilities.debugPtable("开始");
    //这个状态下所有可能的输入的数组
    possibleInputs = inputArray;
    //所有可能的输入构成的下一个状态的数组
    nextStates = new String [possibleInputs.length];
    //所有可能的输入的状态迁移过程中的输出数组
    outputs = new String [possibleInputs.length];
    //判定是否存在重复的状态迁移的边的集合
    HashSet edgeSet = new HashSet(s.getEdgeSet());
    //所有状态迁移后的所有特征集
    nextGroups = new String [possibleInputs.length];
    //初始化数组
    initializeArrays();
    Utilities.debugPtable("初始化完成.");
    currentGroup = 1;
    currentState = s.getID();
    //迭代所有的边
    for ( Object o : edgeSet ) {
      Edge e = ( Edge ) o;
      //状态迁移所在的输入
      String input = e.input ();
      //状态迁移的输出
      String output = e.output ();
      //状态迁移的下一个状态
      int nState = e.tail ();
      Utilities.debugPtable ("处理的边" + input + "/" + output);
      for ( int i = 0; i < outputs.length; i++ ) {
        //如果可能的输入和存在的输入相同
        if ( possibleInputs[i].equals (input) ) {
          Utilities.debugPtable ("添加 " + output + " 到" + input);
          outputs[i] = output;
        }
      }
      for ( int j = 0; j < nextStates.length; j++ ) {
        if ( possibleInputs[j].equals (input) ) {
          Utilities.debugPtable ("添加下一个状态 " + nState + " 到 " + input);
          nextStates[j] = Integer.toString (nState);
        }
      }
    }
  }

  /**
   * 初始化数组
   */
  public void initializeArrays(){
    Arrays.fill (nextStates, "");
    Arrays.fill (outputs, "");
    Arrays.fill (nextGroups, "");
  }

  /**
   *  打印实体
   */
  public void printEntry(){
    System.out.println("等价划分表的条目");
    System.out.println("状态: " + currentState);
    System.out.println("等价划分表: " + currentGroup);
    System.out.println("数组数据");
    System.out.print("可能的输入: ");
    for ( String possibleInput : possibleInputs ) {
      if ( possibleInput != null ) {
        System.out.print (possibleInput + " ");
      }
    }
    System.out.println();
    System.out.print("下一个状态: ");
    for ( String nextState : nextStates ) {
      if ( nextState != null ) {
        System.out.print (nextState + " ");
      }
    }
    System.out.println();
    System.out.print ("输出: ");
    for ( String output : outputs ) {
      if ( output != null ) {
        System.out.print (output + " ");
      }
    }
    System.out.println("");
    System.out.print ("下一个等价划分表: ");
    for ( String nextGroup : nextGroups ) {
      if ( nextGroup != null ) {
        System.out.print (nextGroup + " ");
      }
    }
    System.out.println();
  }
  

  public pTableEntry returnCopy(){
    String [] newOutputs = new String[outputs.length];
    String [] newNextStates = new String[nextStates.length];
    String [] newNextGroups = new String[nextGroups.length];
    int newCurrentGroup = currentGroup;
    int newCurrentState = currentState;
    System.arraycopy (outputs, 0, newOutputs, 0, outputs.length);
    System.arraycopy (nextGroups, 0, newNextGroups, 0, nextGroups.length);
    System.arraycopy (nextStates, 0, newNextStates, 0, nextStates.length);
    return new pTableEntry(newCurrentGroup, newCurrentState, newOutputs, newNextStates, possibleInputs, newNextGroups);
  }
}
