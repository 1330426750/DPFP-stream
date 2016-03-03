package FPGrowth;

import java.io.Serializable;
import java.util.ArrayList;

public class SecPackData implements Serializable{
	private ArrayList<frequent> item =new ArrayList<frequent>();
	private double count;
	private String time;	
	/** 
	 * ���캯��
	 * 
	 */
	public SecPackData()
	{
		
	}
	 /** 
	 * ���캯��
	 * @param item   ArrayList����,�洢frequent���͵�Ԫ�أ����е�Ƶ����
	 * @param count  1s�������ܸ���
	 * @param time   ʱ���
	 */
	public SecPackData(ArrayList<frequent> item,double count,String time)
	{
		this.item=item;
		this.count=count;
		this.time=time;
	}
	/** 
	 * �������
	 *
	 */
	public void clear()
	{
		item.clear();
		count=0;
		time=null;
	}
	/** 
	 * ��ȡArrayList���϶���item
	 * @return ����ArrayList���϶���
	 */
	public ArrayList<frequent> getItem()
	{
		return this.item;
	}
	/**
	 * ΪArrayList����ֵ
	 * @param item ���frequent���͵�ArrayList����
	 * 
	 */
	public void setItem(ArrayList<frequent> item)
	{
		this.item=item;
	}
	/**
	 * �򼯺������frequent����
	 * @param fr frequent����
	 * 
	 */
	public void addItem(frequent fr)
	{
		this.item.add(fr);
	}
	/** 
	 * ��ȡcountֵ
	 * @return ����countֵ
	 */
	public double getCount()
	{
		return count;
	}
	/**
	 * Ϊcount��ֵ
	 * @param count  ���� 
	 */
	public void setCount(double count)
	{
		this.count=count;
	}
	/**
	 * ����count���ܺ�
	 * @param count  ���� 
	 */
	 public void Sum(Double count)
	 {
		    this.count =this.count+count;
	 } 
		/** 
		 * ��ȡtimeֵ
		 * @return ����timeֵ
		 */
	public String getTime()
	{
		return this.time;
	}
	/**
	 * Ϊtime��ֵ
	 * @param time  ʱ��
	 */
	public void setTime(String time)
	{
		this.time=time;
	}
	/**
	 * ��дtoString()����
	 * @return �����е�Ԫ�ء������Լ�ʱ���
	 */
	public String toString()
	{
		return "item:"+item+"count:"+count+"time:"+time;
	}
@Override
public boolean equals(Object obj) {
	if(obj==null)return false;
	else
	{
		if(obj instanceof SecPackData)
		{
			SecPackData s=(SecPackData)obj;
			if(s.count==this.count&&s.item==this.item&&s.time==this.time)
			{
				return true;
			}
		}
	}
	return false;
}
}
