
/**
 * @description  当未为输入符号找到下一个状态时，供在State状态类中使用。
 * @author nyccc
 */
public class InvalidEdgeException extends Exception {
  private String m;
  public InvalidEdgeException()
  {
    super("状态转换中开始状态和状态ID不匹配");
  }
  public String message()
  {
    return(m);
  }
}
