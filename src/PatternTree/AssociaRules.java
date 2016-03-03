package PatternTree;
import java.util.ArrayList;


public class AssociaRules {
  private ArrayList<String> ls;
  
  private double count;
  /** 
	 * ���캯��
	 * @param ls   ArrayList����
	 * @param count  ����
	 * 
	 */ 
  public AssociaRules(ArrayList<String> ls,double count){
	  setLs(ls);
	  setCount(count);
  }
  /** 
	 * ���캯������ÿ������뼯���У����Ҹ�count��ֵ
	 * @param str   ��������͸���������֮���á�\t��������������֮���á���������
	 * 
	 */
  public AssociaRules(String str) {
	  ls = new ArrayList<String>();
	  String[] s1 = str.split("\t");
	  String[] s2 = s1[0].split(",");
	  for(int i=0;i<s2.length;i++) {
		  ls.add(s2[i]);
	  }
	  count = Double.parseDouble(s1[1]);
  }
  /**
	 * ��ȡArrayList���� 
	 *  
	 * @return  ����ArrayList����
	 */
  public ArrayList<String> getLs() {
	  return ls;
  }
	/**
	 * ΪArrayList����ֵ
	 * @param ls ���String���͵�ArrayList����
	 * 
	 */
  public void setLs(ArrayList<String> ls) {
	  this.ls = new ArrayList<String>();
	  this.ls.addAll(ls);
  }
	/**
	 * ��ȡcountֵ 
	 *  
	 * @return  ����ArrayList����
	 */ 
  public double getCount() {
      return count;
  }
	/**
	 * Ϊcount��ֵ
	 * @param count  �����
	 * 
	 */
  public void setCount(double count) {
	  this.count = count;
  }
	/** 
	 * �������Ŷ�Ҫ��ʱ���ó���������
	 * @param ar   ArrayList����
	 * @param confidence  ���Ŷ�
	 * @return ���ع������򣬸�ʽΪ{I1��I2} --> {I3}
	 */ 
  public String isAssociated(AssociaRules ar, double confidence) {
	  String str_out = "";
	  if(ls.containsAll(ar.getLs())) {
		  if(count/ar.getCount()<confidence) {
			  
		  }
		  else {
			  ArrayList<String> temp = new ArrayList<String>();
			  temp.addAll(ls);
			//��temp������ȥ��ar.getLs()������Ԫ��
			  temp.removeAll(ar.getLs());
			  str_out = "{";
			//�����һ���������ڶ���Ԫ��
			  for(int i=0;i<ar.getLs().size()-1;i++){
				  str_out=str_out+ar.getLs().get(i)+",";
			  }
			  //������һ��Ԫ��
			  str_out=str_out+ar.getLs().get(ar.getLs().size()-1)+"} �� {";
			//�����һ���������ڶ���Ԫ��
			  for(int i=0;i<temp.size()-1;i++) {
				  str_out=str_out+temp.get(i)+",";
			  }
			  //������һ��Ԫ��
			  str_out=str_out+temp.get(temp.size()-1)+"}\n";
		  }
	  }
	  return str_out;
  }

}
