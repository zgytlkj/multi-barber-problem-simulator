/**
 * 链表操作:
 * 添加节点,获取头节点(空),获取第一个(首)节点,删除节点,展示节点
 */
public class LinkProcNode {
    private ProcNode headNode = new ProcNode();//头节点

    /**
     * 取头节点
     * @return ProcNode
     */
    public ProcNode getHeadNode(){
        return headNode;
    }

    /**
     * 获取第一个节点
     * @return ProcNode
     */
    public ProcNode getFirst(){
        ProcNode temp = headNode.next;
        if (temp == null){
            System.out.println("空节点");
            return temp;
        } else {
            headNode.next = headNode.next.next;//将第一个节点移出链表
            return temp;
        }
    }

    /**
     * 根据id删除节点
     * @param id
     */
    public void delete(int id){
        ProcNode temp = headNode;
        boolean flag = false;//是否找到的标志
        while(true){
            if (temp.next == null){
                break;
            }
            if (temp.next.id == id){
                flag = true;//找到所需要的id的节点
                break;
            }
            temp = temp.next;//没找到就继续往下找
        }
        if (flag){//如果找到该节点
            temp.next = temp.next.next;
        }else {
            System.out.println("没有该节点");
        }
    }

    /**
     * 添加节点到链尾
     * @param procNode
     */
    public void add(ProcNode procNode){
        ProcNode temp = headNode;//从头节点开始
        while (true){
            if (temp.next == null){//获取到当前最后一个节点
                break;
            }else {//不是最后一个节点则继续往下
                temp = temp.next;
            }
        }
        temp.next = procNode;
    }

    /**
     * 显示链表
     */
    public void show(){
        if (headNode == null){
            System.out.println("链表为空");
        }else {
            ProcNode temp = headNode.next;
            while (temp != null){
                System.out.print(temp.toShow());
                temp = temp.next;
            }
        }
    }
}
