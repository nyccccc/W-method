/**
 *
 当未为输入符号找到下一个状态时，供在State状态类中使用。
 * @author nyccc
 */
public class NoNextStateException extends Exception{
  private String m;

  public NoNextStateException(){
    super("没有找到下一个状态");
  }
  
  public NoNextStateException(String message){
    super(message);
    m=message;
  }
  
  public String message()
  {
    return(m);
  }
}
