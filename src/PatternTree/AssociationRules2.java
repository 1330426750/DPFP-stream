package PatternTree;

import java.util.ArrayList;
//按支持度计数进行查询关联规则
public class AssociationRules2 extends PatternTree{
	public static ArrayList<String> stringArrayList(PatternTree tree, int time_sec, double supportCount)
	{
		ArrayList<String> list = new ArrayList<String>();

		tree.setSelectTag(tree.getSelectTag()+1);
		if (time_sec<=5)
		{
			list=selectNode_Minus5sec(tree.getRoot(),tree,time_sec,supportCount);  //查找时间为5秒以内，所对应的函数
		}
		
		else if (time_sec<=30)
		{
			list=selectNode_Minus30sec(tree.getRoot(),tree,time_sec,supportCount);  //查找时间为5~30秒以内（5秒的倍数），所对应的函数
		}
		
		else if (time_sec<=300)
		{
			list=selectNode_Minus5min(tree.getRoot(),tree,time_sec,supportCount);   //查找时间为30秒~5分以内（30秒的倍数），所对应的函数
		}
		
		else if (time_sec<=1800)
		{
			list=selectNode_Minus30min(tree.getRoot(),tree,time_sec,supportCount);  //查找时间为5~30分以内（5分的倍数），所对应的函数
		}
		
		else
			System.out.println("Wrong select time!");             //查询时间不符合定义
		return list;
    }
	
	public static double selectMinus5sec(PatternTreeNode ptnode, PatternTree tree, int time_sec, double supportCount)
	{
		int i;
		double num1;
		num1=0;
		if(ptnode.num1secWindow()>=time_sec)
		{
			for(i=0;i<time_sec;i++)
			{
				num1=num1+ptnode.get1secWindow(i);
			}
		}
		else
		{
			for(i=0;i<ptnode.num1secWindow();i++)
			{
				num1=num1+ptnode.get1secWindow(i);
			}
			for(i=0;i<time_sec-ptnode.num1secWindow();i++)
			{
				num1=num1+ptnode.get1secMidWindow(i);
			}
		}
		if(num1>=supportCount)
			return num1;
		return 0;
	}
	
	public static double selectMinus30sec(PatternTreeNode ptnode, PatternTree tree, int time_sec, double supportCount)
	{
		int i;
		double num1;
		num1=0;
		time_sec=time_sec/5;
		if(ptnode.num5secWindow()>=time_sec)
    	{
	    	for(i=0;i<time_sec;i++)
	    	{
	    		num1=num1+ptnode.get5secWindow(i);
	    	}
    	}
    	else
    	{
	    	for(i=0;i<ptnode.num5secWindow();i++)
	    	{
		    	num1=num1+ptnode.get5secWindow(i);
	    	}
	    	for(i=0;i<time_sec-ptnode.num5secWindow();i++)
		    {
	    		num1=num1+ptnode.get5secMidWindow(i);
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
		if(num1>=supportCount)
			return num1;
		return 0;
	}
	
	public static double selectMinus5min(PatternTreeNode ptnode, PatternTree tree, int time_sec, double supportCount)
	{
		int i;
		double num1;
		num1=0;
		time_sec=time_sec/30;
		if(ptnode.num30secWindow()>=time_sec)
    	{
	    	for(i=0;i<time_sec-1;i++)
	    	{
	    		num1=num1+ptnode.get30secWindow(i);
	    	}
    	}
    	else
    	{
	    	for(i=0;i<ptnode.num30secWindow();i++)
	    	{
		    	num1=num1+ptnode.get30secWindow(i);
	    	}
	    	for(i=0;i<time_sec-ptnode.num30secWindow();i++)
		    {
	    		num1=num1+ptnode.get30secMidWindow(i);
	    	}
    	}
				
		if(ptnode.num5secWindow()<3)
		{
			if(ptnode.num30secWindow()<time_sec)
			{
		        num1=num1+ptnode.get30secMidWindow(time_sec-1-ptnode.num30secWindow());
			}
			else
			{
				num1=num1+ptnode.get30secWindow(time_sec-1);
			}
		}		
		else
		{
			num1=num1+ptnode.get30secSupport();
		}
		
		if(num1>=supportCount)
			return num1;
		return 0;
	}
	
	public static double selectMinus30min(PatternTreeNode ptnode, PatternTree tree, int time_sec, double supportCount)
	{
		int i;
		double num1;
		num1=0;
		time_sec=time_sec/300;
		if(ptnode.num5minWindow()>=time_sec)
    	{
	    	for(i=0;i<time_sec-1;i++)
	    	{
	    		num1=num1+ptnode.get5minWindow(i);
	    	}
    	}
    	else
    	{
	    	for(i=0;i<ptnode.num5minWindow();i++)
	    	{
		    	num1=num1+ptnode.get5minWindow(i);
	    	}
	    	for(i=0;i<time_sec-ptnode.num5minWindow();i++)
		    {
	    		num1=num1+ptnode.get5minMidWindow(i);
	    	}
    	}
				
		if(ptnode.num30secWindow()<3)
		{
			if(ptnode.num5minWindow()<time_sec)
			{
		        num1=num1+ptnode.get5minMidWindow(time_sec-1-ptnode.num5minWindow());
			}
			else
			{
				num1=num1+ptnode.get5minWindow(time_sec-1);
			}
		}		
		else
		{
			num1=num1+ptnode.get5minSupport();	
		}
		
		if(num1>=supportCount)
			return num1;
		return 0;
	}
	
	public static ArrayList<String> selectNode_Minus5sec(PatternTreeNode node, PatternTree tree, int time_sec, double supportCount)
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
					count = selectMinus5sec(node.getChildList().get(i),tree,time_sec,supportCount);
					if(count>0)
					{
						String str;
						str = outPutItemSet(node.getChildList().get(i), tree.getRoot());
						str = str + "\t" + count;
						list.add(str);
						list.addAll(selectNode_Minus5sec(node.getChildList().get(i), tree, time_sec,supportCount));
					}
					else {
						node.getChildList().get(i).setSelectTag(tree.getSelectTag());
					}
			    }
			}			
		}
		return list;
	}
	
	public static ArrayList<String> selectNode_Minus30sec(PatternTreeNode node, PatternTree tree, int time_sec,double supportCount)
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
     				count = selectMinus30sec(node.getChildList().get(i),tree,time_sec,supportCount);
	    			if(count>0)
					{
	    				String str;
						str = outPutItemSet(node.getChildList().get(i), tree.getRoot());
						str = str + "\t" + count;
						list.add(str);
						list.addAll(selectNode_Minus30sec(node.getChildList().get(i), tree, time_sec,supportCount));
					}
					else
						node.getChildList().get(i).setSelectTag(tree.getSelectTag());
			    }
			}			
		}
		return list;
	}
	
	public static ArrayList<String> selectNode_Minus5min(PatternTreeNode node, PatternTree tree, int time_sec, double supportCount)
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
					count = selectMinus5min(node.getChildList().get(i),tree,time_sec,supportCount);
					if(count>0)
					{
						String str;
						str = outPutItemSet(node.getChildList().get(i), tree.getRoot());
						str = str + "\t" + count;
						list.add(str);
						list.addAll(selectNode_Minus5min(node.getChildList().get(i), tree, time_sec,supportCount));
					}
					else
						node.getChildList().get(i).setSelectTag(tree.getSelectTag());
			    }
			}			
		}
		return list;
	}
	
	public static ArrayList<String> selectNode_Minus30min(PatternTreeNode node, PatternTree tree, int time_sec,double supportCount)
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
					count = selectMinus30min(node.getChildList().get(i),tree,time_sec,supportCount);
					if(count>0)
					{
						String str;
						str = outPutItemSet(node.getChildList().get(i), tree.getRoot());
						str = str + "\t" + count;
						list.add(str);
						list.addAll(selectNode_Minus30min(node.getChildList().get(i), tree, time_sec,supportCount));
					}
					else
						node.getChildList().get(i).setSelectTag(tree.getSelectTag());
			    }
			}			
		}
		return list;
	}
}
