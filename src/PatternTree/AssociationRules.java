package PatternTree;

import java.util.ArrayList;
//按支持度进行查询关联规则
public class AssociationRules extends PatternTree{
	/**
	 * 根据输入的查询时间，调用不同的函数，得到查询的结果 
	 * 
	 * @param tree   模式树
	 * @param time_sec  查询时间
	 * @return  
	 */
	public static ArrayList<String> stringArrayList(PatternTree tree, int time_sec)
	{
		ArrayList<String> list = new ArrayList<String>();

		tree.setSelectTag(tree.getSelectTag()+1);
		if (time_sec<=5)
		{
			list=selectNode_Minus5sec(tree.getRoot(),tree,time_sec);  //查找时间为5秒以内，所对应的函数
		}
		
		else if (time_sec<=30)
		{
			list=selectNode_Minus30sec(tree.getRoot(),tree,time_sec);  //查找时间为5~30秒以内（5秒的倍数），所对应的函数
		}
		
		else if (time_sec<=300)
		{
			list=selectNode_Minus5min(tree.getRoot(),tree,time_sec);   //查找时间为30秒~5分以内（30秒的倍数），所对应的函数
		}
		
		else if (time_sec<=1800)
		{
			list=selectNode_Minus30min(tree.getRoot(),tree,time_sec);  //查找时间为5~30分以内（5分的倍数），所对应的函数
		}
		
		else
			System.out.println("Wrong select time!");             //查询时间不符合定义
		return list;
    }
	/**
	 * 
	 * 在最近5s内指定节点代表的项是否满足最小支持度，
	 * 满足最小支持度就返回时间窗口中存储的数之和，否则返回0.0
	 * @param ptnode   指定节点
	 * @param tree   模式树
	 * @param time_sec  查询时间
	 * @return  满足最小支持度就返回时间窗口中存储的数之和，否则返回0.0
	 */	
	public static double selectMinus5sec(PatternTreeNode ptnode, PatternTree tree, int time_sec)
	{
		int i;
		double num1,num2;
		num1=0;
		num2=0;
		//假如要查询的time_sec小于或者等于当前节点倾斜时间窗口中不为null的数目
		//那么就从当前节点的倾斜时间窗口中取值
		if(ptnode.num1secWindow()>=time_sec)
		{
			for(i=0;i<time_sec;i++)
			{
				num1=num1+ptnode.get1secWindow(i);
				num2=num2+tree.get1secTupleNum(i);
			}
		}
		else//要查询的time_sec大于当前节点倾斜时间窗口中不为null的数目
		{
			//从当前倾斜时间窗口中取出全部非null的值
			for(i=0;i<ptnode.num1secWindow();i++)
			{
				num1=num1+ptnode.get1secWindow(i);
				num2=num2+tree.get1secTupleNum(i);
			}
			//然后再从倾斜时间窗口缓存中取出缺少的部分
			for(i=0;i<time_sec-ptnode.num1secWindow();i++)
			{
				num1=num1+ptnode.get1secMidWindow(i);
				num2=num2+tree.get1secMidTupleNum(i);
			}
		}
		//满足最小支持度时，返回num1
		if(num1>=(tree.getSupport()*num2))
			return num1;
		return 0;
	}
	/**
	 * 
	 * 在最近30s内指定节点代表的项是否满足最小支持度，
	 * 满足最小支持度就返回时间窗口中存储的数之和，否则返回0.0
	 * 否则不是频繁项集
	 * @param ptnode    指定节点
	 * @param tree	    模式树
	 * @param time_sec  查询时间
	 * @return  满足最小支持度就返回时间窗口中存储的数之和，否则返回0.0
	 */	
	public static double selectMinus30sec(PatternTreeNode ptnode, PatternTree tree, int time_sec)
	{
		int i;
		double num1,num2;
		num1=0;
		num2=0;
		time_sec=time_sec/5;
		//假如要查询的time_sec小于或者等于当前节点倾斜时间窗口中不为null的数目
		//那么就从当前节点的倾斜时间窗口中取值
		if(ptnode.num5secWindow()>=time_sec)
    	{
	    	for(i=0;i<time_sec;i++)
	    	{
	    		num1=num1+ptnode.get5secWindow(i);
	     		num2=num2+tree.get5secTupleNum(i);
	    	}
    	}
    	else//要查询的time_sec大于当前节点倾斜时间窗口中不为null的数目
    	{			
    		//从当前倾斜时间窗口中取出全部非null的值
	    	for(i=0;i<ptnode.num5secWindow();i++)
	    	{
		    	num1=num1+ptnode.get5secWindow(i);
		    	num2=num2+tree.get5secTupleNum(i);
	    	}
	    	//然后再从倾斜时间窗口缓存中取出缺少的部分
	    	for(i=0;i<time_sec-ptnode.num5secWindow();i++)
		    {
	    		num1=num1+ptnode.get5secMidWindow(i);
		    	num2=num2+tree.get5secMidTupleNum(i);
	    	}
    	}
/*				
		if(ptnode.num1secWindow()<3)
		{
			if(ptnode.num5secWindow()<time_sec)
			{
		        num1=num1+ptnode.get5secMidWindow(time_sec-1-ptnode.num5secWindow());
		        num2=num2+tree.get5secMidTupleNum(time_sec-1-ptnode.num5secWindow());
			}
			else
			{
				num1=num1+ptnode.get5secWindow(time_sec-1);
				num2=num2+tree.get5secTupleNum(time_sec-1);
			}
		}		
		else
		{
			num1=num1+ptnode.get5secSupport();
			num2=num2+tree.get5secTupleNum();			
		}
		*/
		//满足最小支持度时，返回num1
		if(num1>=(tree.getSupport()*num2))
			return num1;
		return 0;
	}
	/**
	 * 
	 * 在最近300s内指定节点代表的项是否满足最小支持度，
	 * 满足最小支持度就返回时间窗口中存储的数之和，否则返回0.0
	 * @param ptnode    指定节点
	 * @param tree	    模式树
	 * @param time_sec  查询时间
	 * @return  满足最小支持度就返回时间窗口中存储的数之和，否则返回0.0
	 */	
	public static double selectMinus5min(PatternTreeNode ptnode, PatternTree tree, int time_sec)
	{
		int i;
		double num1,num2;
		num1=0;
		num2=0;
		time_sec=time_sec/30;
		//假如要查询的time_sec小于或者等于当前节点倾斜时间窗口中不为null的数目
		//那么就从当前节点的倾斜时间窗口中取值
		if(ptnode.num30secWindow()>=time_sec)
    	{
	    	for(i=0;i<time_sec-1;i++)
	    	{
	    		num1=num1+ptnode.get30secWindow(i);
	     		num2=num2+tree.get30secTupleNum(i);
	    	}
    	}
    	else//要查询的time_sec大于当前节点倾斜时间窗口中不为null的数目
    	{
    		//从当前倾斜时间窗口中取出全部非null的值
	    	for(i=0;i<ptnode.num30secWindow();i++)
	    	{
		    	num1=num1+ptnode.get30secWindow(i);
		    	num2=num2+tree.get30secTupleNum(i);
	    	}
	    	//然后再从倾斜时间窗口缓存中取出缺少的部分
	    	for(i=0;i<time_sec-ptnode.num30secWindow();i++)
		    {
	    		num1=num1+ptnode.get30secMidWindow(i);
		    	num2=num2+tree.get30secMidTupleNum(i);
	    	}
    	}
				
		if(ptnode.num5secWindow()<3)
		{
			if(ptnode.num30secWindow()<time_sec)
			{
		        num1=num1+ptnode.get30secMidWindow(time_sec-1-ptnode.num30secWindow());
		        num2=num2+tree.get30secMidTupleNum(time_sec-1-ptnode.num30secWindow());
			}
			else
			{
				num1=num1+ptnode.get30secWindow(time_sec-1);
				num2=num2+tree.get30secTupleNum(time_sec-1);
			}
		}		
		else
		{
			num1=num1+ptnode.get30secSupport();
			num2=num2+tree.get30secTupleNum();			
		}
		//满足最小支持度时，返回num1
		if(num1>=(tree.getSupport()*num2))
			return num1;
		return 0;
	}
	/**
	 * 
	 * 在最近1800s内指定节点代表的项是否满足最小支持度，
	 * 满足最小支持度就返回时间窗口中存储的数之和，否则返回0.0
	 * @param ptnode    指定节点
	 * @param tree	    模式树
	 * @param time_sec  查询时间
	 * @return  满足最小支持度就返回时间窗口中存储的数之和，否则返回0.0
	 */
	public static double selectMinus30min(PatternTreeNode ptnode, PatternTree tree, int time_sec)
	{
		int i;
		double num1,num2;
		num1=0;
		num2=0;
		time_sec=time_sec/300;
		//假如要查询的time_sec小于或者等于当前节点倾斜时间窗口中不为null的数目
		//那么就从当前节点的倾斜时间窗口中取值
		if(ptnode.num5minWindow()>=time_sec)
    	{
	    	for(i=0;i<time_sec-1;i++)
	    	{
	    		num1=num1+ptnode.get5minWindow(i);
	     		num2=num2+tree.get5minTupleNum(i);
	    	}
    	}
    	else//要查询的time_sec大于当前节点倾斜时间窗口中不为null的数目
    	{
    		//从当前倾斜时间窗口中取出全部非null的值
	    	for(i=0;i<ptnode.num5minWindow();i++)
	    	{
		    	num1=num1+ptnode.get5minWindow(i);
		    	num2=num2+tree.get5minTupleNum(i);
	    	}
	    	//然后再从倾斜时间窗口缓存中取出缺少的部分
	    	for(i=0;i<time_sec-ptnode.num5minWindow();i++)
		    {
	    		num1=num1+ptnode.get5minMidWindow(i);
		    	num2=num2+tree.get5minMidTupleNum(i);
	    	}
    	}
				
		if(ptnode.num30secWindow()<3)
		{
			if(ptnode.num5minWindow()<time_sec)
			{
		        num1=num1+ptnode.get5minMidWindow(time_sec-1-ptnode.num5minWindow());
		        num2=num2+tree.get5minMidTupleNum(time_sec-1-ptnode.num5minWindow());
			}
			else
			{
				num1=num1+ptnode.get5minWindow(time_sec-1);
				num2=num2+tree.get5minTupleNum(time_sec-1);
			}
		}		
		else
		{
			num1=num1+ptnode.get5minSupport();
			num2=num2+tree.get5minTupleNum();			
		}
		
		if(num1>=(tree.getSupport()*num2))
			return num1;
		return 0;
	}
	/** 
	 * 查询时间在5秒内，指定节点及其子节点代表的频繁项集，将其加入List集合中
	 * @param node 指定的节点
	 * @param tree 模式树
	 * @param time_sec 查询的时间
	 * @return  返回包含指定的node节点及其子节点代表的频繁项集的List集合
	 */
	public static ArrayList<String> selectNode_Minus5sec(PatternTreeNode node, PatternTree tree, int time_sec)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		node.setSelectTag(tree.getSelectTag());
		if(node.getChildList()!=null)
		{
			for (int i=0;i<node.getChildList().size();i++)
			{
				node.getChildList().get(i);
				if (node.getChildList().get(i).getSelectTag()!=tree.getSelectTag())
				{
					double count;
					//满足最小支持度时，count=1，否则count=0
					count = selectMinus5sec(node.getChildList().get(i),tree,time_sec);
					if(count>0)
					{
						String str;
						//调用PatternTree.java中的静态函数outPutItemSet,其功能是
						//输出节点代表的频繁项集
						str = outPutItemSet(node.getChildList().get(i), tree.getRoot());
						str = str + "\t" + count;
						list.add(str);
						//继续查询node的孩子节点的频繁项集，并把结果添加到List集合中
						list.addAll(selectNode_Minus5sec(node.getChildList().get(i), tree, time_sec));
					}
					else {
						node.getChildList().get(i).setSelectTag(tree.getSelectTag());
					}
			    }
			}			
		}
		return list;
	}
	/** 
	 * 查询时间在30秒内，指定节点及其子节点代表的频繁项集，将其加入List集合中
	 * @param node 指定的节点
	 * @param tree 模式树
	 * @param time_sec 查询的时间
	 * @return  返回包含指定的node节点及其子节点代表的频繁项集的List集合
	 */	
	public static ArrayList<String> selectNode_Minus30sec(PatternTreeNode node, PatternTree tree, int time_sec)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		node.setSelectTag(tree.getSelectTag());
		if(node.getChildList()!=null)
		{
			for (int i=0;i<node.getChildList().size();i++)
			{
				node.getChildList().get(i);
				if (node.getChildList().get(i).getSelectTag()!=tree.getSelectTag())
				{
					double count;
					//满足最小支持度时，count=1，否则count=0
     				count = selectMinus30sec(node.getChildList().get(i),tree,time_sec);
	    			if(count>0)
					{
	    				String str;
						//调用PatternTree.java中的静态函数outPutItemSet,其功能是
						//输出节点代表的频繁项集
						str = outPutItemSet(node.getChildList().get(i), tree.getRoot());
						str = str + "\t" + count;
						list.add(str);
						//继续查询node的孩子节点的频繁项集，并把结果添加到List集合中
						list.addAll(selectNode_Minus30sec(node.getChildList().get(i), tree, time_sec));
					}
					else
						node.getChildList().get(i).setSelectTag(tree.getSelectTag());
			    }
			}			
		}
		return list;
	}
	/** 
	 * 查询时间在300秒内，指定节点及其子节点代表的频繁项集，将其加入List集合中
	 * @param node 指定的节点
	 * @param tree 模式树
	 * @param time_sec 查询的时间
	 * @return  返回包含指定的node节点及其子节点代表的频繁项集的List集合
	 */	
	public static ArrayList<String> selectNode_Minus5min(PatternTreeNode node, PatternTree tree, int time_sec)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		node.setSelectTag(tree.getSelectTag());
		if(node.getChildList()!=null)
		{
			for (int i=0;i<node.getChildList().size();i++)
			{
				node.getChildList().get(i);
				if (node.getChildList().get(i).getSelectTag()!=tree.getSelectTag())
				{
					double count;	
					//满足最小支持度时，count=1，否则count=0
					count = selectMinus5min(node.getChildList().get(i),tree,time_sec);
					if(count>0)
					{
						String str;
						//调用PatternTree.java中的静态函数outPutItemSet,其功能是
						//输出节点代表的频繁项集
						str = outPutItemSet(node.getChildList().get(i), tree.getRoot());
						str = str + "\t" + count;
						list.add(str);
						//继续查询node的孩子节点的频繁项集，并把结果添加到List集合中
						list.addAll(selectNode_Minus5min(node.getChildList().get(i), tree, time_sec));
					}
					else
						node.getChildList().get(i).setSelectTag(tree.getSelectTag());
			    }
			}			
		}
		return list;
	}
	/** 
	 * 查询时间在1800秒内，指定节点及其子节点代表的频繁项集，将其加入List集合中
	 * @param node 指定的节点
	 * @param tree 模式树
	 * @param time_sec 查询的时间
	 * @return  返回包含指定的node节点及其子节点代表的频繁项集的List集合
	 */
	public static ArrayList<String> selectNode_Minus30min(PatternTreeNode node, PatternTree tree, int time_sec)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		node.setSelectTag(tree.getSelectTag());
		if(node.getChildList()!=null)
		{
			for (int i=0;i<node.getChildList().size();i++)
			{
				node.getChildList().get(i);
				if (node.getChildList().get(i).getSelectTag()!=tree.getSelectTag())
				{
					double count;
					//满足最小支持度时，count=1，否则count=0
					count = selectMinus30min(node.getChildList().get(i),tree,time_sec);
					if(count>0)
					{
						String str;
						//调用PatternTree.java中的静态函数outPutItemSet,其功能是
						//输出节点代表的频繁项集
						str = outPutItemSet(node.getChildList().get(i), tree.getRoot());
						str = str + "\t" + count;
						list.add(str);
						//继续查询node的孩子节点的频繁项集，并把结果添加到List集合中
						list.addAll(selectNode_Minus30min(node.getChildList().get(i), tree, time_sec));
					}
					else
						node.getChildList().get(i).setSelectTag(tree.getSelectTag());
			    }
			}			
		}
		return list;
	}
}
