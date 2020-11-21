import java.util.*;

/**
 * @description : 此类定义任何有限状态机的状态。
 *   状态具有名称，描述和一组零个或多个输出边缘。
 *   有几种有用的方法可以在给定状态的情况下提取此信息。
 * @author nyccc
 */
public class State {
  /**
   * 给每一个状态定义一个独一无二的ID
   */
  private final int  ID;
  /**
   * 状态的描述
   */
  String description;
  /**
   * 出度边的集合
   */
  private final HashSet< Object > outGoingEdges;

  /**
   * 构造函数： 定义一个状态
   * @param stateID 输入状态的ID
   */
  public State(int stateID){
    ID = stateID;
    description = "暂无描述.";
    //将传出边集初始化为空。
    outGoingEdges=new HashSet< Object > ();
    //此状态的UIO序列。
    //尚未找到UIO序列。
    //状态标记在某些算法中很有用。
  }
  // Set (true), unset(false), and get mark.

  // Add to visits, initialize visits, return visits.

  /**
   *  增加一个边到这个状态
   *  注意：e开始状态一定是相同的ID
   * @param e 边的信息
   * @throws InvalidEdgeException 如果没有这个边，抛出异常
   */
  public void addEdge(Edge e) throws InvalidEdgeException{
    if (e.head()!=ID) {
      throw new InvalidEdgeException();
    }
    outGoingEdges.add(e);
  }

  /**
   *
   * @return 获取出度的边
   */
  public HashSet< Object > getEdgeSet(){
    return(outGoingEdges);
  }

  /**
   *
   * @return 状态的ID
   */
  public int getID(){
    return ID;
  }

  /**
   *
   * @return 下一状态
   *  当收到输入的字符后，
   */
  public  Edge getNextState(String inputSymbol) throws  NoNextStateException{
    for ( Object outGoingEdge : outGoingEdges ) {
      Edge temp = ( Edge ) outGoingEdge;
      if ( temp.input ().equals (inputSymbol) ) {
        return (temp);
      }
    }
    //当没有对应的下一个状态的时候。抛出异常
    throw new NoNextStateException(inputSymbol);
  }


}// End of class State
