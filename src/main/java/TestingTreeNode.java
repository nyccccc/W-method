import java.util.Vector;

/**
 * @description 测试树的节点
 * @author nyccc
 */
public class TestingTreeNode {
  /**
   * 目前的状态
   */
  State currentState;
  /**
   * 标记状态
   */
  boolean markedState;
  /**
   * 状态数
   */
  int vertexNum;
  /**
   * 状态所在的层级
   */
  int level;
  /**
   * 定义存储状态树
   */
  Vector branchVector;

  /**
   * 构造测试树的节点
   *
   * @param state        状态
   * @param vertexNumber 节点的数量
   */
  public TestingTreeNode ( State state, int vertexNumber ) {
    //目前所在的状态
    currentState = state;
    branchVector = new Vector ();
    markedState = false;
    vertexNum = vertexNumber;
  }

  /**
   * 打印节点
   */
  public void printNode ( ) {
    System.out.println ("打印节点");
    System.out.println ("目前的状态: " + currentState.getID ());
    if ( branchVector.isEmpty () ) {
      System.out.println ("分支表： " + "EMPTY");
    } else {
      for ( Object o : branchVector ) {
        TestingTreeBranch current = ( TestingTreeBranch ) o;
        current.printBranch ();
      }
    }
  }
}