/**
 * 在有限状态机中定义边。
 * 边是一对（源，目标），其中是源节点，而j是目的节点。
 * 边也具有标签a / b，其中a是输入符号，b是输出符号。
 * @author nyccc
 */

public class Edge {
  /**
   * 定义状态机一个状态迁移的初始状态
   */
  private final int sourceState;
  /**
   * 定义状态机一个状态迁移的结束状态
   */
  private final int targetState;
  /**
   * 定义了状态机一个状态迁移的一个边的信息
   */
  private final String label;
  /**
   * 定义状态机的输入符号
   */
  private final String inputSymbol;
  /**
   * 定义了状态机的输出符号
   */
  private final String outputSymbol;

  /**
   * Edge的构造函数
   * @param sourceID 开始状态的ID
   * @param targetID 结束状态的ID
   * @param inputS   输入的字符
   * @param outputS  输出的字符
   */
  public Edge(int sourceID, int targetID, String inputS, String outputS){
    sourceState=sourceID;
    targetState=targetID;
    inputSymbol=inputS;
    outputSymbol=outputS;
    label=inputSymbol+"/"+outputSymbol;
  }

  /**
   *
   * @return 在一个状态迁移的过程中的开始状态
   */
  public int head(){
    return(sourceState);
  }

  /**
   *
   * @return 在一个状态迁移的过程中的结束状态
   */
  public int tail(){
    return(targetState);
  }

  /**
   *
   * @return 在一个状态迁移的过程中的边
   */
  public String getLabel(){
    return(label);
  }

  /**
   *
   * @return 状态迁移的过程中输入的字符
   */
  public String input(){
    return(inputSymbol);
  }

  /**
   *
   * @return 状态迁移的过程中的输出的字符
   */
  public String output(){
    return(outputSymbol);
  }
}
