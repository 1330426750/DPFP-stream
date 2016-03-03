package FPGrowth;

import java.io.Serializable;
import java.util.ArrayList;

public class SecPackData implements Serializable{
	private ArrayList<frequent> item =new ArrayList<frequent>();
	private double count;
	private String time;	
	/** 
	 * 构造函数
	 * 
	 */
	public SecPackData()
	{
		
	}
	 /** 
	 * 构造函数
	 * @param item   ArrayList集合,存储frequent类型的元素，所有的频繁项
	 * @param count  1s内数据总个数
	 * @param time   时间戳
	 */
	public SecPackData(ArrayList<frequent> item,double count,String time)
	{
		this.item=item;
		this.count=count;
		this.time=time;
	}
	/** 
	 * 清空数据
	 *
	 */
	public void clear()
	{
		item.clear();
		count=0;
		time=null;
	}
	/** 
	 * 获取ArrayList集合对象item
	 * @return 返回ArrayList集合对象
	 */
	public ArrayList<frequent> getItem()
	{
		return this.item;
	}
	/**
	 * 为ArrayList对象赋值
	 * @param item 存放frequent类型的ArrayList集合
	 * 
	 */
	public void setItem(ArrayList<frequent> item)
	{
		this.item=item;
	}
	/**
	 * 向集合中添加frequent对象
	 * @param fr frequent对象
	 * 
	 */
	public void addItem(frequent fr)
	{
		this.item.add(fr);
	}
	/** 
	 * 获取count值
	 * @return 返回count值
	 */
	public double getCount()
	{
		return count;
	}
	/**
	 * 为count赋值
	 * @param count  个数 
	 */
	public void setCount(double count)
	{
		this.count=count;
	}
	/**
	 * 计算count的总和
	 * @param count  个数 
	 */
	 public void Sum(Double count)
	 {
		    this.count =this.count+count;
	 } 
		/** 
		 * 获取time值
		 * @return 返回time值
		 */
	public String getTime()
	{
		return this.time;
	}
	/**
	 * 为time赋值
	 * @param time  时间
	 */
	public void setTime(String time)
	{
		this.time=time;
	}
	/**
	 * 重写toString()方法
	 * @return 集合中的元素、个数以及时间戳
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
