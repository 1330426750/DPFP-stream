package PatternTree;
import java.util.ArrayList;


public class AssociaRules {
  private ArrayList<String> ls;
  
  private double count;
  /** 
	 * 构造函数
	 * @param ls   ArrayList集合
	 * @param count  个数
	 * 
	 */ 
  public AssociaRules(ArrayList<String> ls,double count){
	  setLs(ls);
	  setCount(count);
  }
  /** 
	 * 构造函数，将每个项加入集合中，并且给count赋值
	 * @param str   包含了项集和个数，二者之间用“\t”隔开，项与项之间用“，”隔开
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
	 * 获取ArrayList集合 
	 *  
	 * @return  返回ArrayList集合
	 */
  public ArrayList<String> getLs() {
	  return ls;
  }
	/**
	 * 为ArrayList对象赋值
	 * @param ls 存放String类型的ArrayList集合
	 * 
	 */
  public void setLs(ArrayList<String> ls) {
	  this.ls = new ArrayList<String>();
	  this.ls.addAll(ls);
  }
	/**
	 * 获取count值 
	 *  
	 * @return  返回ArrayList集合
	 */ 
  public double getCount() {
      return count;
  }
	/**
	 * 为count赋值
	 * @param count  项集个数
	 * 
	 */
  public void setCount(double count) {
	  this.count = count;
  }
	/** 
	 * 满足置信度要求时，得出关联规则
	 * @param ar   ArrayList集合
	 * @param confidence  置信度
	 * @return 返回关联规则，格式为{I1，I2} --> {I3}
	 */ 
  public String isAssociated(AssociaRules ar, double confidence) {
	  String str_out = "";
	  if(ls.containsAll(ar.getLs())) {
		  if(count/ar.getCount()<confidence) {
			  
		  }
		  else {
			  ArrayList<String> temp = new ArrayList<String>();
			  temp.addAll(ls);
			//从temp集合中去除ar.getLs()包含的元素
			  temp.removeAll(ar.getLs());
			  str_out = "{";
			//输出第一个到倒数第二个元素
			  for(int i=0;i<ar.getLs().size()-1;i++){
				  str_out=str_out+ar.getLs().get(i)+",";
			  }
			  //输出最后一个元素
			  str_out=str_out+ar.getLs().get(ar.getLs().size()-1)+"} → {";
			//输出第一个到倒数第二个元素
			  for(int i=0;i<temp.size()-1;i++) {
				  str_out=str_out+temp.get(i)+",";
			  }
			  //输出最后一个元素
			  str_out=str_out+temp.get(temp.size()-1)+"}\n";
		  }
	  }
	  return str_out;
  }

}
