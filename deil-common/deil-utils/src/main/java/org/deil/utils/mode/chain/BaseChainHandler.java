package org.deil.utils.mode.chain;

/**
 * 责任链处理器
 *
 * @DATE 2023/03/09
 * @CODE Deil
 */
public abstract class BaseChainHandler<T> {

    /**
     * 下一个责任节点
     */
    protected BaseChainHandler<T> next = null;

    /**
     * 链内逻辑
     *
     * @param t
     * @time 2023/03/09
     */
    protected abstract void handle(T t) ;

    /**
     * 责任链启动入口
     *
     * @param t
     * @time 2023/03/09
     */
    public void startHandle(T t){
        if (isHandle(t)){
            handle(t);
        }
    }

    /**
     * 是否执行本节点及后节点
     *
     * @param t
     * @return boolean
     * @time 2023/03/09
     */
    protected  boolean isHandle(T t){
        return true;
    };

    /**
     * 下一个节点
     *
     * @param handler
     * @time 2023/03/09
     */
    public void addNext(BaseChainHandler<T> handler) {
        this.next = handler;
    }


    public static class Builder<T> {
        private BaseChainHandler<T> head;
        private BaseChainHandler<T> tail;

        public Builder<T> add(BaseChainHandler<T> handler) {
            if (this.head == null) {
                this.head = handler;
            } else {
                this.tail.addNext(handler);
            }
            this.tail = handler;
            return this;
        }

        public Builder<T> base(BaseChainHandler<T> handler) {
            this.add(handler);
            return this;
        }

        public BaseChainHandler<T> build() {
            return this.head;
        }
    }


}