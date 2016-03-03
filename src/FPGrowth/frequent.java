package FPGrowth;

import java.io.Serializable;
import java.util.ArrayList;

public class frequent implements Serializable{
	private ArrayList<String> item =new ArrayList<String>();
	private double count;
	/** 
	 * 构造函数
	 * 
	 */
	public frequent()
	{

	}
	/** 
	 * 构造函数
	 * @param item   ArrayList集合,存储String类型的元素，频繁项集
	 * @param count  频繁项集对应个数
	 * 
	 */
	public frequent(ArrayList<String> item,double count)
	{
		this.item=item;
		this.count=count;
		
	}
	/**
	 * 获取ArrayList集合 
	 *  
	 * @return  返回ArrayList集合
	 */
	public ArrayList<String> getItem()
	{
		return this.item;
		
	}
	/**
	 * 为ArrayList对象赋值,频繁项集item
	 * @param item 存放String类型的ArrayList集合
	 * 
	 */
	public void setItem(ArrayList<String> item)
	{
	this.item=item;
	}
	/**
	 * 获取count
	 * @return 返回count值
	 * 
	 */
	public double getCount()
	{
		return count;
	}
	/**
	 * 设置count值 
	 */
	public void setCount(double count)
	{
		this.count=count;
	}
	/**
	 * 重写ToString()方法
	 * @return  返回ArrayList集合中的元素和相应的count值
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
