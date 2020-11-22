/**
 * @author : YiChen_Niu
 * @program :pTable
 * @description: 构造K阶等价集
 *               详情见《软件测试基础教程》 135页
 * @data: 2020/11/22
 */
public class pTable {
  /**
   * 构造状态的数组，其数据结构为pTableEntry
   */
  public pTableEntry[] state;
  /**
   * 定义一个整型的全局变量为等价划分表的组数
   */
  public int numGroups;
  /**
   * 定义一个之后实体的数组
   */
  public pTableEntry[] backupEntries;
  /**
   * 定义全局变量为之后的等价划分的组数
   */
  public int backupNumGroups;
  /**
   * 定义一个全局变量为的状态数
   */
  public int numberOfStates;

  /**
   * 等价划分表的构造函数
   * @param entryArray 输出的数组
   * @param numberOfGroups 等价划分的组数
   */
 public pTable ( pTableEntry[] entryArray, int numberOfGroups ) {
    state = entryArray;
    numGroups = numberOfGroups;
    backupEntries = state;
    backupNumGroups = numberOfGroups;
  }

  /**
   * 等级划分表的构造函数
   * @param stateArray 状态的数组
   * @param numStates 状态数
   * @param inputArray 状态迁移时的输入数组
   */
  public pTable ( State[] stateArray, int numStates, String[] inputArray ) {
    numGroups = 1;
    state = new pTableEntry[numStates];
    backupEntries = new pTableEntry[numStates];
    numberOfStates = numStates;
    Utilities.debugPtable ("构造等价划分表");

    for ( int i = 0; i < numStates; i++ ) {
      Utilities.debugPtable ("I:" + i);
      Utilities.debugPtable ("构造前");
      state[i] = new pTableEntry (stateArray[i + 1], inputArray);
      Utilities.debugPtable ("生成了一个正确的实体");
    }

    //迭代输入实体
    for ( pTableEntry entry : state ) {
      if ( Utilities.pTableDebugSw ) {
        entry.printEntry ();
      }
    }
    backupEntries = state;
    backupNumGroups = numGroups;
    Utilities.debugPtable ("构造完成.");
  }

  /**
   *  对于所有的实体，检测i 和i+1 在输出方面是否相同
   *  如果是的话，什么都不做
   *  如果不是的话，更新i+1 和特征集的组号
   *  对于每一个状态的下一个状态，在表中都可以找到状态
   * @return 该条目对应的组号
   */
  public pTable getPOne ( ) {
    for ( int i = 0; i < state.length - 1; i++ ) {
      String[] firstOutputs = state[i].outputs;
      String[] secondOutputs = state[i + 1].outputs;
      int sameCount = 0;
      Utilities.debugPtable ("I is currently: " + i);
      for ( int j = 0; j < firstOutputs.length; j++ ) {
        if ( firstOutputs.length != secondOutputs.length ) {
          Utilities.debugPtable ("错误，输入的长度不匹配");
        } else {
          Utilities.debugPtable ("比较 " + firstOutputs[j] + " 和" + secondOutputs[j]);
          if ( firstOutputs[j].equals (secondOutputs[j]) ) {
            Utilities.debugPtable ("比较结束 " + firstOutputs[j] + " 和 " + secondOutputs[j]);
            sameCount++;
            Utilities.debugPtable ("相同的计数: " + sameCount);
          }
        }
      }
      // 如果sameCount和输出的长度不同，我们需要将其进行分离
      if ( sameCount != firstOutputs.length ) {
        Utilities.debugPtable ("增加一个分离在 " + state[i].currentState + " 和 "
                + state[i + 1].currentState+"之间");
        state[i + 1].currentGroup = state[i].currentGroup + 1;
        Utilities.debugPtable ("更新 " + state[i + 1].currentState
                + " 到 " + state[i + 1].currentGroup+"的特征集");
      }
      else {
        //因为是相同的，所以也在同一个特征集中
        Utilities.debugPtable ("状态i的特征集分组: "
                + state[i].currentGroup);
        Utilities.debugPtable ("状态i+1的特征集分组: "
                + state[i + 1].currentGroup);
        state[i + 1].currentGroup = state[i].currentGroup;
      }
    }
    /* 重新命名特征集的表，对于所有条目，对
     * 于每个条目中的所有nextStates， 返回
     * 与该条目相对应的组号，并将其放入
     * nextGroups数组中。
     */
    return getpTable ();
  }

  /**
   * @author :Yichen_Niu
   * @Description :返回特征集的顺序
   * @Date 1:26 
   * @Param 无
   * @return pTable 返回的为状态所在的特征集
  */
  private pTable getpTable ( ) {
    //迭代查找实体
    for ( pTableEntry entry : state ) {
      for ( int l = 0; l < entry.nextStates.length; l++ ) {
        //如果下一个状态不为空，查找该状态所在的特征集
        if ( ! "".equals (entry.nextStates[l]) &&
                ! "-1".equals (entry.nextStates[l]) ) {
          //查找到所在的特征集的组，然后存入数组中
          int group = findGroup (entry.nextStates[l]);
          entry.nextGroups[l] = Integer.toString (group);
        }
      }
    }
    //现在状态所在的特征集
    numGroups = state[numberOfStates - 1].currentGroup;
    Utilities.debugPtable ("等价划分表的组数: " + numGroups);
    return this;
  }

  /**
   * @author :Yichen_Niu
   * @Description :
   * @Date 1:50
   * @Param [nextState] 进行一次状态迁移后的状态转换
   * @return int 状态转换后所在特征集
  */
  public int findGroup ( String nextState ) {
    int stateToFind = 0;
    try {
      stateToFind = Integer.parseInt (nextState);
    } catch ( Exception e ) {
      Utilities.debugPtable ("下一个状态（解析错误）: " + nextState);
      Utilities.debugPtable ("致命错误，无法还原整数");
    }
    //迭代查找状态所在的特征集
    for ( pTableEntry entry : state ) {
      if ( entry.currentState == stateToFind ) {
        return entry.currentGroup;
      }
    }
    //如果没有找到，返回-1
    return - 1;
  }

  /**
   * 对于所有的状态，检测i和i+1是否相同，
   *     如果是：什么都不做
   *     如果不是：更新i+1的特征集和中的特征集；
   * 然后通过检查每一个状态和对应的下一个状态，进行重新标记
   * 使其能够让每个状态的下一个状态都可以找到其特征集
   * @return
   */
  public pTable getPNext ( ) {
    for ( int i = 0; i < state.length - 1; i++ ) {
      String[] firstGroups = state[i].nextGroups;
      String[] secondGroups = state[i + 1].nextGroups;
      int sameCount = 0;
      Utilities.debugPtable ("状态为现在是 " + i);
      for ( int j = 0; j < firstGroups.length; j++ ) {
        if ( firstGroups.length != secondGroups.length ) {
          Utilities.debugPtable ("长度不匹配，特征集出错");
        } else {
          Utilities.debugPtable ("比较 " + firstGroups[j]
                  + " 和 " + secondGroups[j]);
          if ( firstGroups[j].equals (secondGroups[j]) ) {
            Utilities.debugPtable ("比较 " + firstGroups[j] +
                    " 和 " + secondGroups[j]+"完成");
            sameCount++;
            Utilities.debugPtable ("Same count: " + sameCount);
          }
        }
      }

      /*
       * 若状态数和第一个特征集不同，需要分离
       */
      if ( sameCount != firstGroups.length ) {
        Utilities.debugPtable ("增加一个分离在 " + state[i].currentState + " 状态和 " + state[i + 1].currentState+"状态之间");
        state[i + 1].currentGroup = state[i].currentGroup + 1;
        Utilities.debugPtable ("将状态 " + state[i + 1].currentState + "更新到 " + state[i + 1].currentGroup+"特征集");
        Utilities.debugPtable ("更新之前特征集的数量: " + numGroups);
        Utilities.debugPtable ("更新之后特征集的数量: " + numGroups);

      } else {
        //如两个相同就不管
        Utilities.debugPtable ("状态i的特征集 " + state[i].currentGroup);
        Utilities.debugPtable ("状态i+1的特征集: " + state[i + 1].currentGroup);
        state[i + 1].currentGroup = state[i].currentGroup;
      }
    }
    /*
     * 现在重新贴标签,
     * 对于所有状态，对于每个条目中的所有nextStates，
     * 返回对应于该条目的组号并将其放入在nextGroups数组中。
     */
    return getpTable ();
  }


  public pTable returnCopy ( ) {
    pTableEntry[] copyEntries = new pTableEntry[numberOfStates];
    int copyGroups = numGroups;
    for ( int i = 0; i < copyEntries.length; i++ ) {
      copyEntries[i] = state[i].returnCopy ();
    }
    return new pTable (copyEntries, copyGroups);
  }

  /**
   * 特征集比较
   * @param state1 状态1
   * @param state2 状态2
   * @return 状态到这个状态特征集的可能输入
   */
  public String GCompare ( int state1, int state2 ) {
    for ( int i = 0; i < state[state1 - 1].nextGroups.length; i++ ) {
      if ( ! state[state1 - 1].nextGroups[i].equals (state[state2 - 1].nextGroups[i]) ) {
        Utilities.debugPtable ("GCompare: " + state1 + ", " + state2 + " = " + state[state1 - 1].possibleInputs[i]);
        return state[state1 - 1].possibleInputs[i];
      }
    }
    return "";
  }

  /**
   *
   * @param state 现在的状态
   * @param symbol 输出的符号
   * @return 状态迁移的输入
   */
  public String O ( int state, String symbol ) {
    int inputPos = - 1;
    for ( int i = 0; i < this.state[state - 1].possibleInputs.length; i++ ) {
      if ( this.state[state - 1].possibleInputs[i].equals (symbol) ) {
        inputPos = i;
        break;
      }
    }
    Utilities.debugPtable ("O: " + state + ", "
            + symbol + " = " + this.state[state - 1].nextStates[inputPos]);
    return this.state[state - 1].nextStates[inputPos];
  }

  /**
   * 比较两个状态的输出
   * @param state1 状态1
   * @param state2 状态2
   * @return 状态1的输出
   */
  public String OCompare ( int state1, int state2 ) {
    for ( int i = 0; i < state[state1 - 1].outputs.length; i++ ) {
      if ( ! state[state1 - 1].outputs[i].equals (state[state2 - 1].outputs[i]) ) {
        Utilities.debugPtable ("OCompare: " + state1 + ", " + state2 + " = " + state[state1 - 1].possibleInputs[i]);
        return state[state1 - 1].possibleInputs[i];
      }
    }
    return "";
  }
}