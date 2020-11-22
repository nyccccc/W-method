import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author : YiChen Niu (yniu7@sheffield.ac.uk)
 * @version : 0.1
 * @program :W-method
 * @description :读取状态机
 * @url :
 * @create :2020-11-22
 */


public class GetFSM {
    /**
     * 从源文件读取FSM描述的方法。
     * 假设startState为1。
     */

    static void getFSM ( ){
      // 一个从输入的转换
      String aTransition;
      // 输入的状态ID（-1是无效ID）
      //到目前为止尚未输入任何状态。
      Wmethod.numberOfStates=0;
      Wmethod.startState=1;
      try{
        //读取存储状态机的文件
        BufferedReader fsmFile= new BufferedReader (new FileReader (Wmethod.fsmFilename));
        aTransition=fsmFile.readLine();
        //指示循环是否终止。
        boolean done=false;
        if (aTransition==null) {
          done=true;
        }

        while (!done){
          //状态机中的转换数量加一
          Wmethod.numberOfTransitions++;
          /* System.out.println("Next transition: "+numberOfTransitions+" "+aTransition); */
          //将读取的转换进行分离，分别为状态，边，输入和输出
          Wmethod.splitTransition(aTransition);
          //读取下一个转换
          aTransition=fsmFile.readLine();
          //设置终止的条件，如果没有下一条的状态转换，终止。
          if (aTransition==null || (Wmethod.numberOfTransitions== Wmethod.MAX_TRANSITIONS)) {
            done=true;
          }
        }
      }catch ( FileNotFoundException e){
        //没有找到状态机文件的输出
        System.out.println("文件: "+ Wmethod.fsmFilename+" 没有找到");
        System.exit(0);
      }catch ( IOException e){
        Utilities.printException("w算法"," 获取FSM","读取时引发了IO异常:"+
                Wmethod.fsmFilename +"未找到");
        Utilities.printException("w算法", "获取FSM", "转换输入： "+ Wmethod.numberOfTransitions);
      }
    }

}
