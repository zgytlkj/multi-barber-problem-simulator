/**
 * 单链表节点:
 * 空构造体初始化为首节点
 * 其余节点包含:id、name
 */
public class ProcNode {
    public long id;//节点中存储id信息
    public String name;//节点中存储name信息
    public ProcNode next;//使用类做指针,指向下一个节点

    public ProcNode(){
        id = -1;
        name = null;
        next = null;
    }
    public ProcNode(long id,String name){
        this.id = id;
        this.name = name;
    }

    /**
     * 链表中的方法调用此方法显示节点信息
     * @return String
     */
    public String toShow(){
        return "id:"+id+",name:"+name+"=";
    }
}
