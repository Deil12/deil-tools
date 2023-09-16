package org.deil.utils.delegate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * .net 委托
 *
 * @DATE 2023/02/14
 * @CODE Deil
 */
public abstract class Delegator implements InvocationHandler {

    protected Object obj_orgin = null; //原始对象

    protected Object obj_proxy = null; //代理对象

    /*
     * 完成原始对象和委托对象的实例化
     */
    public  Delegator(Object orgin) {
        this.createProxy(orgin);
    }

    protected Object createProxy(Object orgin) {
        obj_orgin = orgin;
        //下面语句中orgin.getClass().getClassLoader()为加载器， orgin.getClass().getInterfaces()为接口集class对象
        obj_proxy = Proxy.newProxyInstance(orgin.getClass().getClassLoader(), orgin.getClass().getInterfaces(), this);//委托
        return obj_proxy;
    }

    /*
     * 对带有指定参数的指定对象调用由此 Method 对象表示的底层方法，具体请参见
     */
    protected Object invokeSuper(Method method, Object[] args) throws Throwable {
        return method.invoke(obj_orgin, args);
    }

    //--------------实现InvocationHandler接口，要求覆盖------------
    //下面实现的方法是当委托的类调用toString()方法时，操作其他方法而不是该类默认的toString()，这个类的其他方法则不会。

    public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
        // 缺省实现：委托给obj_orgin完成对应的操作
        if(method.getName().equals("toString")) {
            //对其做额外处理
            return this.invokeSuper(method, args) + "$Proxy";
        } else {
            //注意，调用原始对象的方法，而不是代理的（obj==obj_proxy）
            return this.invokeSuper(method, args);
        }
    }

}
