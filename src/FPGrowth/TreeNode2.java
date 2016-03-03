package FPGrowth;

import java.util.ArrayList;
import java.util.List;


/**
 * FPTree�ڵ���
 *
 */
public class TreeNode2 implements Comparable<TreeNode2>{

  private String name; // �ڵ�����
  private Double count; // ����
  private TreeNode2 parent; // ���ڵ�
  private List<TreeNode2> children; // �ӽڵ�
  private TreeNode2 nextHomonym; // ��һ��ͬ���ڵ�
  
  public TreeNode2() {
  
  }
  /**
   * ��ȡ��������
   *@return ���ؽڵ�����
   */
  public String getName() {
    return name;
  }
 /**
   * ���ý������
   * @param name �ڵ�����
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
    * ��ȡ������Ԫ�ص�ͳ��ֵ
    * @return ����ͳ��ֵ 
    */
  public Double getCount() {
    return count;
  }
  /**
   *Ϊcount��ֵ
   * @param count ͳ��ֵ
   * ���ý��ͳ��ֵ
   */
  public void setCount(Double count) {
    this.count = count;
  }
  
  /**
   * ����ͳ�ƽ��
   * @param count ͳ��ֵ
   * 
   */
  public void Sum(Double count) {
    this.count =this.count+count;
  }
  
  /**
   * ��ȡ���׽��
   * @return ���ظ���� 
   */
  public TreeNode2 getParent() {
    return parent;
  }
  /**
   * ���ø��׽��
   * @param parent �����
   */
  public void setParent(TreeNode2 parent) {
    this.parent = parent;
  }

  /**
   * ��ȡ���ӽ��
   * @return ���غ��ӽ��
   */
  public List<TreeNode2> getChildren() {
    return children;
  }
 /**
   * ���ú��ӽ��
   * @param children ���ӽ��
   */
  public void setChildren(List<TreeNode2> children) {
    this.children = children;
  }

  /**
   * ��ȡ��һ��ͬvalueֵ�Ľ��
   * @return ������һ��ͬvalueֵ�Ľ��
   */
  public TreeNode2 getNextHomonym() {
    return nextHomonym;
  }

  /**
   * ָ�����һ��ͬvalueֵ�Ľ��
   * @param nextHomonym  ��һ��ͬvalueֵ�Ľ��
   */
  public void setNextHomonym(TreeNode2 nextHomonym) {
    this.nextHomonym = nextHomonym;
  }
  /**
   * ���һ�����
   * @param child ���ӽ�� 
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
   * �Ƿ�����Ÿý��,���ڷ��ظý�㣬�����ڷ��ؿ�
   * @param name �������
   * @return ͨ�������ҽ�㣬���ڷ��ظý�㣬���򷵻ؿ�
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
    // ��Ĭ�ϵıȽϴ�С�෴�����µ���Arrays.sort()ʱ�ǰ���������
    return (int) (count0 - this.count);
  }
}