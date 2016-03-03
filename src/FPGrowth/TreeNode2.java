package FPGrowth;

import java.util.ArrayList;
import java.util.List;


/**
 * FPTree节点类
 *
 */
public class TreeNode2 implements Comparable<TreeNode2>{

  private String name; // 节点名称
  private Double count; // 计数
  private TreeNode2 parent; // 父节点
  private List<TreeNode2> children; // 子节点
  private TreeNode2 nextHomonym; // 下一个同名节点
  
  public TreeNode2() {
  
  }
  /**
   * 获取结点的名称
   *@return 返回节点名称
   */
  public String getName() {
    return name;
  }
 /**
   * 设置结点名称
   * @param name 节点名称
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
    * 获取结点相关元素的统计值
    * @return 返回统计值 
    */
  public Double getCount() {
    return count;
  }
  /**
   *为count赋值
   * @param count 统计值
   * 设置结点统计值
   */
  public void setCount(Double count) {
    this.count = count;
  }
  
  /**
   * 计算统计结果
   * @param count 统计值
   * 
   */
  public void Sum(Double count) {
    this.count =this.count+count;
  }
  
  /**
   * 获取父亲结点
   * @return 返回父结点 
   */
  public TreeNode2 getParent() {
    return parent;
  }
  /**
   * 设置父亲结点
   * @param parent 父结点
   */
  public void setParent(TreeNode2 parent) {
    this.parent = parent;
  }

  /**
   * 获取孩子结点
   * @return 返回孩子结点
   */
  public List<TreeNode2> getChildren() {
    return children;
  }
 /**
   * 设置孩子结点
   * @param children 孩子结点
   */
  public void setChildren(List<TreeNode2> children) {
    this.children = children;
  }

  /**
   * 获取下一个同value值的结点
   * @return 返回下一个同value值的结点
   */
  public TreeNode2 getNextHomonym() {
    return nextHomonym;
  }

  /**
   * 指向的下一个同value值的结点
   * @param nextHomonym  下一个同value值的结点
   */
  public void setNextHomonym(TreeNode2 nextHomonym) {
    this.nextHomonym = nextHomonym;
  }
  /**
   * 添加一个结点
   * @param child 孩子结点 
   */
  public void addChild(TreeNode2 child) {
    if (this.getChildren() == null) {
      List<TreeNode2> list = new ArrayList<TreeNode2>();
      list.add(child);
      this.setChildren(list);
    } else {
      this.getChildren().add(child);
    }
  }
  /**
   * 是否存在着该结点,存在返回该结点，不存在返回空
   * @param name 结点名称
   * @return 通过名字找结点，存在返回该结点，否则返回空
   */
  public TreeNode2 findChild(String name) {
    List<TreeNode2> children = this.getChildren();
    if (children != null) {
      for (TreeNode2 child : children) {
        if (child.getName().equals(name)) {
          return child;
        }
      }
    }
    return null;
  }


  @Override
  public int compareTo(TreeNode2 arg0) {
    // TODO Auto-generated method stub
    double count0 = arg0.getCount();
    // 跟默认的比较大小相反，导致调用Arrays.sort()时是按降序排列
    return (int) (count0 - this.count);
  }
}