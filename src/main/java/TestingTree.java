import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

/**
 * 生成测试树
 * @author nyccc
 */
public class TestingTree{
  /**
   * 在测试树中所有可能的状态
   */
  State [] allStates;
  /**
   * 测试树中所有的状态数
   */
  int numberOfStates;
  /**
   * 根节点
   */
  TestingTreeNode root;
  /**
   * 使用BFS进行搜索的时候，最大的顶点的数量
   */
  int vertexNums;
  /**
   * BFS树的队列
   */
  Vector BFSTreeQueue = new Vector ();
  /**
   * 为P集找到的所有值的向量
   */
  Vector pValues = new Vector ()  ;
  /**
   * 列出树中当前的所有状态
   */
  Vector statesInTree =new Vector();
  /**
   * 构造测试树的方法
   * @param stateArray 所有状态的数组集合
   * @param numOfStates 状态的数量
   */
  public TestingTree(State [] stateArray, int numOfStates) {
    //初始化所有的变量
    vertexNums = 1;
    allStates = stateArray;
    numberOfStates = numOfStates;
    root = new TestingTreeNode (stateArray[1], vertexNums);
    vertexNums++;
    //在测试树中添加根节点的状态
    statesInTree.add (root.currentState.getID ());
    //初始化根节点，构造BFS树
    BFSBuild (root);
    //遍历树并收集过渡覆盖集(P)的元素。
    testTraverse (root, "");
    //打印过渡覆盖集(P)
    if ( Utilities.transitionCoverSetDebugSw ) {
      printPValues ();
    }
  }

  /**
   * 如果节点已经在树数组中的状态中
   * @param stateNumber 状态数
   * @return 否在BFS期间已“查看”该状态
   */
  public boolean foundAlready(int stateNumber){
    int count = 0;
    while(count < statesInTree.size()){
      Integer b = (Integer) statesInTree.get(count);
      if( b == stateNumber){
        return false;
      }
      count++;
    }
    return true;
  }

  /**
   * 打印在转换覆盖集(P)中所有的值
   */
  public void printPValues(){
    int i = 0;
    System.out.println("\n转换覆盖集P有 "+ (pValues.size()+1)+"个输入. 分别为：");
    System.out.print("Empty " );
    Collections.sort(pValues);
    while(i < pValues.size()){
      System.out.print(pValues.get(i).toString()+" ");
      i++; 
    }   
    System.out.println();
  }

  /**
   *
   * @return 转换覆盖集(P)的值
   */
  public Vector getPValues(){
    return pValues;
  }

  /**
   *  构造测试树
   * @param startState 测试树的开始状态
   */
  public void BFSBuild(TestingTreeNode startState){
    startState.level = 1;
    //将BFS查找树队列添加开始的状态
    BFSTreeQueue.add(startState);
    //当BFS树的队列不为空的时候
    while(!BFSTreeQueue.isEmpty()){
      //选取队列中第一个节点，并将BFS的队列中移除
      TestingTreeNode currentNode = (TestingTreeNode) BFSTreeQueue.firstElement();
      BFSTreeQueue.removeElementAt(0);
      Utilities.debugTestingTree("目前调试:" + currentNode.currentState.getID());
      //如果这个状态存在，添加到测试树中
      if( foundAlready (currentNode.currentState.getID ()) ) {
        statesInTree.add(currentNode.currentState.getID ());
      }
      HashSet currentNodeEdges = new HashSet(currentNode.currentState.getEdgeSet());
      for ( Object currentNodeEdge : currentNodeEdges ) {
        Edge nextEdge = ( Edge ) currentNodeEdge;
        State nextState = allStates[nextEdge.tail ()];
        TestingTreeNode nextNode = new TestingTreeNode (nextState,
                currentNode.level + 1);
        vertexNums++;
        //这是另一个“内部节点”，将其展开
        if ( foundAlready (nextNode.currentState.getID ()) ) {
          currentNode.branchVector.add (new TestingTreeBranch (nextEdge.input (),
                  nextNode));
          statesInTree.add (nextNode.currentState.getID ());
          Utilities.debugTestingTree ("添加到队列:" + nextNode.currentState.getID ()
                  + " at level: " + nextNode.level);
          Utilities.debugTestingTree ("从 " + currentNode.currentState.getID ()
                  + " 分支到 " + nextNode.currentState.getID ());
          Utilities.debugTestingTree ("分支vector的大小: " +
                  currentNode.branchVector.size ());
          BFSTreeQueue.add (nextNode);
        } else {
          //这将是一片叶子
          currentNode.branchVector.add (new TestingTreeBranch (nextEdge.input (),
                  nextNode));
          Utilities.debugTestingTree ("从叶子" + currentNode.currentState.getID ()
                  + "状态到 " +nextNode.currentState.getID ()+"状态");
          Utilities.debugTestingTree ("分支的大小: " + currentNode.branchVector.size ());
        }
      }
    }
  }

  /**
   * 遍历树并收集P集。
   * @param root 输入需要遍历的树
   * @param currentString 目前的P-set
   */
  public void testTraverse(TestingTreeNode root, String currentString){
    int count = 0;
    while(count < root.branchVector.size()){
      //访问每个分支，打印其值
      TestingTreeBranch currentBranch = (TestingTreeBranch) root.branchVector.get(count);
      currentString = currentString.concat(currentBranch.inputValue);
      //在P表中添加目前的状态
      pValues.add(currentString);
      //测试遍历目前的状态
      testTraverse(currentBranch.nextState, currentString);
      currentString = currentString.substring(0, currentString.length() - 1);
      count++;
    } 
  }
}