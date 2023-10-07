package org.deil.utils.delegate;

public interface CallBack {
    void doSomeThing(int i);
}

class Observable {

    private CallBack mCallBack;

    public void setCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            if (mCallBack != null) {
                mCallBack.doSomeThing(i);
            }
        }
    }

}

class Observer implements CallBack {

    @Override
    public void doSomeThing(int i) {
        // 具体需要观察者自己做的事情
        System.out.println("打印数字：" + i);
    }

}

class TestCallBack{
    public static void main(String[] args) {
        Observable observable = new Observable();
        observable.setCallBack(new Observer());
        observable.run();
    }
}
