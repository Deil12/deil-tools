package org.deil.utils.delegate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射方式实现C#的委托
 *
 * Command类是公共接口类，对外开放的接口方法。StartCommand实现一个Command接口。在StartCommand的executeMethod方法的内部实现了方法的转移，把StartCommand里其它方法转移到接口方法executeMethod里，这样设计后，统一方法接口。
 * C#中委托对象delegate与JAVA中Method对象主要区别在于delegate里包含方法的对象，而Method主要是对方法的描述，不包含任务对象信息。
 * JAVA中用invoke方法时可能会产生大量的异常。
 *
 * @DATE 2023/02/15
 * @CODE Deil
 */
interface Command {
    Method executeMethod();
}

class StartComputer implements Command {

    Class classType = StartComputer.class;
    public Method executeMethod() {
        try {
            return classType.getMethod("innerMethod", new Class[]{ int.class });
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void innerMethod(int param) {
        // TODO: something.
        System.out.println("----------------- OUTPUT ------------------");
        System.out.println(param);
    }

}

@SuppressWarnings("unchecked")
class CommandStudy {

    public static Map commandMap = new HashMap();
    static {
        commandMap.put("StartComputer", StartComputer.class);
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException , InvocationTargetException {
        Class startComputerClass = commandMap.get("StartComputer").getClass();
        Command someCommand = (Command)startComputerClass.newInstance();
        Method executeMethod = someCommand.executeMethod();
        executeMethod.invoke(someCommand, new Object[] { new Integer(200) });
    }

}
