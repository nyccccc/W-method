/**
 * @description 测试树的分支
 * @author nyccc
 */
public class TestingTreeBranch{
  /**
   *  输入的值
   */
  String inputValue;
  /**
   *  下一个状态
   */
  TestingTreeNode nextState;

  /**
   *  生成测试树的分支
   * @param input 一个状态状态转移
   * @param next 下一个边
   */
  public TestingTreeBranch(String input, TestingTreeNode next){
    inputValue = input;
    nextState = next;
  }

  /**
   *  打印分支
   */
  public void printBranch(){
    System.out.println("打印分支");
    System.out.println("输入的值: " + inputValue);
    System.out.println("下一个状态:");
    nextState.printNode(); 
  }
}