package FPGrowth;

import java.io.Serializable;
import java.util.ArrayList;

public class frequent implements Serializable{
	private ArrayList<String> item =new ArrayList<String>();
	private double count;
	/** 
	 * ���캯��
	 * 
	 */
	public frequent()
	{

	}
	/** 
	 * ���캯��
	 * @param item   ArrayList����,�洢String���͵�Ԫ�أ�Ƶ���
	 * @param count  Ƶ�����Ӧ����
	 * 
	 */
	public frequent(ArrayList<String> item,double count)
	{
		this.item=item;
		this.count=count;
		
	}
	/**
	 * ��ȡArrayList���� 
	 *  
	 * @return  ����ArrayList����
	 */
	public ArrayList<String> getItem()
	{
		return this.item;
		
	}
	/**
	 * ΪArrayList����ֵ,Ƶ���item
	 * @param item ���String���͵�ArrayList����
	 * 
	 */
	public void setItem(ArrayList<String> item)
	{
	this.item=item;
	}
	/**
	 * ��ȡcount
	 * @return ����countֵ
	 * 
	 */
	public double getCount()
	{
		return count;
	}
	/**
	 * ����countֵ 
	 */
	public void setCount(double count)
	{
		this.count=count;
	}
	/**
	 * ��дToString()����
	 * @return  ����ArrayList�����е�Ԫ�غ���Ӧ��countֵ
	 */
	public String toString()
	{
		return "item:"+item+"count:"+count;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj==null)return false;
		else
		{
			if(obj instanceof frequent)
			{
				frequent f=(frequent)obj;
				if(f.count==this.count&&f.item==this.item)
				{
					return true;
				}
			}
		}
		return false;
	}

}
