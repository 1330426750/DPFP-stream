package PatternTree;

import java.io.Serializable;
import java.util.ArrayList;  
import java.util.List;  

public class PatternTreeNode implements Serializable
{
	/** 节点Id*/  
    private String nodeId;
    /** 父亲节点*/
    private PatternTreeNode parent;

	/** 子节点集合*/
	private List<PatternTreeNode> childList;
	/** 倾斜时间窗口*/
	private double[] timewindow_1sec= new double[5];
	private double[] timewindow_5sec= new double[6];
	private double[] timewindow_30sec= new double[10];
	private double[] timewindow_5min= new double[6];
	/** 倾斜时间窗口缓存*/
	private double[] timewindow_1sec_mid= new double[4];
	private double[] timewindow_5sec_mid= new double[5];
	private double[] timewindow_30sec_mid= new double[9];
	private double[] timewindow_5min_mid= new double[5];
	
	private int presentTimeWindow;  //当前时间窗口号， 与PatternTree的当前时间窗口做比较
	private int removeTag;          //剪枝标记，在patternTree进行剪枝操作时标记是否扫描过该结点
	private int selectTag;          //查询标记，在patternTree进行查询操作时与树的查询标记比较，以得出是否查询过该节点
	private int createTag;          //创建标记，用于节点的插入与修改

	/**
	 * 构造函数
	 * 
	 * @param nodeId   节点名
	 */
	public PatternTreeNode(String nodeId)  
	{  
		this.nodeId = nodeId;
	    this.childList = new ArrayList<PatternTreeNode>();
	    /*初始化倾斜时间窗口*/
	    initTimeWindow();
	} 
	
	/**
	 * 构造函数 
	 * 
	 * @param nodeId   节点名
	 * @param parent   父亲节点
	 */
	public PatternTreeNode(String nodeId,PatternTreeNode parent)  
	{  
		this.nodeId = nodeId;
		this.parent = parent;
	    this.childList = new ArrayList<PatternTreeNode>();
	    /*初始化倾斜时间窗口*/
	    initTimeWindow();
	    parent.childList.add(this);
	}
	
	/**
	  * 构造函数
	  * 
	  * @param nodeId  节点名
	  * @param childList   子节点集合
	  */
	public PatternTreeNode(String nodeId, List<PatternTreeNode> childList)  
	{  
		this.nodeId = nodeId; 
		this.childList = childList; 
		/*初始化倾斜时间窗口*/
		initTimeWindow();
	} 
	
	/**
	 * 构造函数 
	 * 
	 * @param nodeId   节点名
	 * @param parent   父亲节点
	 * @param childList   子节点集合
	 */
	public PatternTreeNode(String nodeId, PatternTreeNode parent, List<PatternTreeNode> childList)  
	{  
		this.nodeId = nodeId; 
		this.parent = parent;
		this.childList = childList; 
		/*初始化倾斜时间窗口*/
		initTimeWindow();
	}
	 /**
	 * 获取子节点集合 
	 * 
	 * @return 返回子节点集合
	 * 
	 */   
	public List<PatternTreeNode> getChildList() {  
	    return childList;  
	}  
	 /**
	 * 设置子节点集合 
	 *  
	 */ 
	public void setChildList(List<PatternTreeNode> childList) {  
	    this.childList = childList;  
	}
	
	/**
	 * 
	 * 初始化倾斜窗口
	 * 将1*5s都置-1，清空窗口
	 */
	public void init1secWindow(){
		for(int i=0;i<5;i++)
			this.timewindow_1sec[i]=-1;
	}
	/**
	 * 
	 * 初始化倾斜窗口
	 * 将5s倾斜窗口置-1，清空窗口
	 */
	public void init5secWindow(){
		for(int i=0;i<6;i++)
			this.timewindow_5sec[i]=-1;
	}
	/**
	 * 
	 * 初始化倾斜窗口
	 * 将30s倾斜窗口置-1，清空窗口
	 */
	public void init30secWindow(){
		for(int i=0;i<10;i++)
			this.timewindow_30sec[i]=-1;
	}
	/**
	 * 
	 * 初始化倾斜窗口
	 * 将300s倾斜窗口置-1，清空窗口
	 */
	public void init5minWindow(){
		for(int i=0;i<6;i++)
			this.timewindow_5min[i]=-1;
	}
	/**
	 * 
	 * 初始化倾斜时间窗口缓存
	 * 将1s倾斜时间窗口缓存都置-1
	 */	
	public void init1secMidWindow(){
		for(int i=0;i<4;i++)
			this.timewindow_1sec_mid[i]=-1;
	}
	/**
	 * 
	 * 初始化倾斜时间窗口缓存
	 * 将5s倾斜时间窗口缓存都置-1
	 */
	public void init5secMidWindow(){
		for(int i=0;i<5;i++)
			this.timewindow_5sec_mid[i]=-1;
	}
	/**
	 * 
	 * 初始化倾斜时间窗口缓存
	 * 将30s倾斜时间窗口缓存都置-1
	 */
	public void init30secMidWindow(){
		for(int i=0;i<9;i++)
			this.timewindow_30sec_mid[i]=-1;
	}
	/**
	 * 
	 * 初始化倾斜时间窗口缓存
	 * 将300s倾斜时间窗口缓存都置-1
	 */	
	public void init5minMidWindow(){
		for(int i=0;i<5;i++)
			this.timewindow_5min_mid[i]=-1;
	}
	/**
	 * 初始化倾斜时间窗口
	 * 初始化倾斜时间窗口缓存
	 * 设置当前时间窗口号
	 */	
	public void initTimeWindow(){
		init1secWindow();
		init5secWindow();
		init30secWindow();
		init5minWindow();
		init1secMidWindow();
		init5secMidWindow();
		init30secMidWindow();
		init5minMidWindow();
		setPresentTimeWindow(-1);
	}
	
	/** 
	 * 
	 * @return 返回1s倾斜时间窗口非Null的数目 
	 */
	public int num1secWindow(){
		int num=0;
		for(int i=0;i<5;i++){
			if (this.timewindow_1sec[i]>=0)
				num++;
		}
		return num;
	}
	/** 
	 * 
	 * @return 返回5s倾斜时间窗口非Null的数目 
	 */
	public int num5secWindow(){
		int num=0;
		for(int i=0;i<6;i++){
			if (this.timewindow_5sec[i]>=0)
				num++;
		}
		return num;
	}
	/** 
	 * 
	 * @return 返回30s倾斜时间窗口非Null的数目 
	 */	
	public int num30secWindow(){
		int num=0;
		for(int i=0;i<10;i++){
			if (this.timewindow_30sec[i]>=0)
				num++;
		}
		return num;
	}
	/** 
	 * 
	 * @return 返回300s倾斜时间窗口非Null的数目 
	 */	
	public int num5minWindow(){
		int num=0;
		for(int i=0;i<6;i++){
			if (this.timewindow_5min[i]>=0)
				num++;
		}
		return num;
	}
	
	/**
	 * 向1s时间窗口插入数据
	 * 
	 * @param i      插入的位置
	 * @param value  插入的值
	 */
	public void insert1secWindow(int i, double value){
		this.timewindow_1sec[i]=value;
	}
	/**
	 * 向5s时间窗口插入数据
	 * 
	 * @param i      插入的位置
	 * @param value  插入的值
	 */
	public void insert5secWindow(int i, double value){
		this.timewindow_5sec[i]=value;
	}
	/**
	 * 向30s时间窗口插入数据
	 * 
	 * @param i      插入的位置
	 * @param value  插入的值
	 */
	public void insert30secWindow(int i, double value){
		this.timewindow_30sec[i]=value;
	}
	/**
	 * 向300s时间窗口插入数据
	 * 
	 * @param i      插入的位置
	 * @param value  插入的值
	 */
	public void insert5minWindow(int i, double value){
		this.timewindow_5min[i]=value;
	}
	/**
	 * 向1s时间窗口缓存中插入数据
	 * 
	 * @param i      插入的位置
	 * @param value  插入的值
	 */
	public void insert1secMidWindow(int i, double value){
		this.timewindow_1sec_mid[i]=value;
	}
	/**
	 * 向5s时间窗口缓存中插入数据
	 * 
	 * @param i      插入的位置
	 * @param value  插入的值
	 */
	public void insert5secMidWindow(int i, double value){
		this.timewindow_5sec_mid[i]=value;
	}
	/**
	 * 向30s时间窗口缓存中插入数据
	 * 
	 * @param i      插入的位置
	 * @param value  插入的值
	 */
	public void insert30secMidWindow(int i, double value){
		this.timewindow_30sec_mid[i]=value;
	}
	/**
	 * 向300s时间窗口缓存中插入数据
	 * 
	 * @param i      插入的位置
	 * @param value  插入的值
	 */
	public void insert5minMidWindow(int i, double value){
		this.timewindow_5min_mid[i]=value;
	}
	
	/**
	 * 获取1s时间窗口内指定位置的值
	 * @param i 时间窗口位置
	 * @return  返回指定时间窗口的值
	 */
	public double get1secWindow(int i){
		return this.timewindow_1sec[i];
	}
	/**
	 * 获取5s时间窗口内指定位置的值
	 * @param i 时间窗口位置
	 * @return  返回指定时间窗口的值
	 */
	public double get5secWindow(int i){
		return this.timewindow_5sec[i];
	}
	/**
	 * 获取30s时间窗口内指定位置的值
	 * @param i 时间窗口位置
	 * @return  返回指定时间窗口的值
	 */
	public double get30secWindow(int i){
		return this.timewindow_30sec[i];
	}
	/**
	 * 获取300s时间窗口内指定位置的值
	 * @param i 时间窗口位置
	 * @return  返回指定时间窗口的值
	 */
	public double get5minWindow(int i){
		return this.timewindow_5min[i];
	}
	/**
	 * 获取1s时间窗口缓存内指定位置的值
	 * @param i 时间窗口位置
	 * @return  返回指定时间窗口的值
	 */
	public double get1secMidWindow(int i){
		return this.timewindow_1sec_mid[i];
	}
	/**
	 * 获取5s时间窗口缓存内指定位置的值
	 * @param i 时间窗口位置
	 * @return  返回指定时间窗口的值
	 */
	public double get5secMidWindow(int i){
		return this.timewindow_5sec_mid[i];
	}
	/**
	 * 获取30s时间窗口缓存内指定位置的值
	 * @param i 时间窗口位置
	 * @return  返回指定时间窗口的值
	 */
	public double get30secMidWindow(int i){
		return this.timewindow_30sec_mid[i];
	}
	/**
	 * 获取300s时间窗口缓存内指定位置的值
	 * @param i 时间窗口位置
	 * @return  返回指定时间窗口的值
	 */
	public double get5minMidWindow(int i){
		return this.timewindow_5min_mid[i];
	}
	
	/**
	 * 获取所有倾斜时间窗口
	 * @return 返回所有倾斜时间窗口
	 */
	public String getTimeWindow()
	{
		int i;
        StringBuilder buffer = new StringBuilder();         
        buffer.append(this.get1secWindow(0));
        for (i=1;i<5;i++)
        {
        	buffer.append(","+this.get1secWindow(i));
        }
        buffer.append("\t"+this.get5secWindow(0));
        for (i=1;i<6;i++)
        {
            buffer.append(","+this.get5secWindow(i));
        }
        buffer.append("\t"+this.get30secWindow(0));
        for (i=1;i<10;i++)
        {
            buffer.append(","+this.get30secWindow(i));
        }
        buffer.append("\t"+this.get5minWindow(0));
        for (i=1;i<6;i++)
        {
            buffer.append(","+this.get5minWindow(i));
        }          
        return buffer.toString();
	}
	/**
	 * 获取所有倾斜时间窗口缓存
	 * @return 返回所有倾斜时间窗口缓存
	 */
	public String getMidTimeWindow()
	{
		int i;
        StringBuilder buffer = new StringBuilder();         
        buffer.append(this.get1secWindow(0));
        for (i=1;i<4;i++)
        {
        	buffer.append(","+this.get1secMidWindow(i));
        }
        buffer.append("\t"+this.get5secMidWindow(0));
        for (i=1;i<5;i++)
        {
            buffer.append(","+this.get5secMidWindow(i));
        }
        buffer.append("\t"+this.get30secMidWindow(0));
        for (i=1;i<9;i++)
        {
            buffer.append(","+this.get30secMidWindow(i));
        }
        buffer.append("\t"+this.get5minMidWindow(0));
        for (i=1;i<5;i++)
        {
            buffer.append(","+this.get5minMidWindow(i));
        }          
        return buffer.toString();
	}
	/**
	 * 获取节点号
	 * @return 返回所有节点号
	 */
	public String getNodeId() {  
	    return nodeId;  
	}  
	 /**
	 * 设置节点号
	 * @param  nodeId  节点号
	 */ 
	public void setNodeId(String nodeId) {  
	    this.nodeId = nodeId;  
	}  
	/**
	 * 获取父节点
	 * @return 返回父节点
	 */  
	public PatternTreeNode getParent() {  
	    return parent;  
	}  
	/**
	 * 设置父节点号
	 * @param  parent  父节点
	 */  
	public void setParentId(PatternTreeNode parent) {  
	    this.parent = parent;  
	}  
	/**
	 * 获取当前时间窗口
	 * @return 返回当前时间窗口
	 */
	public int getPresentTimeWindow() {  
	    return this.presentTimeWindow;  
	}  
	/**
	 * 设置当前时间窗口
	 * @param presentTimeWindow 当前时间窗口
	 */ 
	public void setPresentTimeWindow(int presentTimeWindow) {  
	    this.presentTimeWindow = presentTimeWindow;  
	}
	/**
	 * 获取剪枝标记
	 * @return 返回剪枝标记
	 */
	public int getRemoveTag() {  
	    return this.removeTag;  
	}  
	/**
	 * 设置剪枝标记
	 * @param removeTag   剪枝标记
	 */   
	public void setRemoveTag(int removeTag) {  
	    this.removeTag = removeTag;  
	}
	/**
	 * 获取查询标记
	 * @return 返回查询标记
	 */
	public int getSelectTag(){  
	    return this.selectTag;  
	}  
	/**
	 * 设置查询标记
	 * @param selectTag   查询标记
	 */  
	public void setSelectTag(int selectTag){  
	    this.selectTag = selectTag;  
	}
	/**
	 * 获取创建标记
	 * @return 返回创建标记
	 */
	public int getCreateTag(){  
	    return this.createTag;  
	}  
	/**
	 * 设置创建标记
	 *  @param createTag   创建标记
	 */  
	public void setCreateTag(int createTag){  
	    this.createTag = createTag;  
	}
	
	/**
	 * 获取最近5个1秒时间窗口的和
	 * @return 最近5个1秒时间窗口的和
	 */
	public double get5secSupport()
	{
		int i;
		double num;
		num=0;
		for (i=0;i<this.num1secWindow();i++)
			num=num+this.get1secWindow(i);
		//从倾斜时间窗口缓存中读取
		for (i=0;i<5-this.num1secWindow();i++)
			num=num+this.get1secMidWindow(i);
		return num;
	}
	
	/**
	 * 获取最近6个5秒时间窗口的和
	 * @return 最近6个5秒时间窗口的和
	 */
	public double get30secSupport()
	{
		int i;
		double num;
		num=0;
		for (i=0;i<this.num5secWindow();i++)
			num=num+this.get5secWindow(i);
		//从倾斜时间窗口缓存中读取
		for (i=0;i<6-this.num5secWindow();i++)
			num=num+this.get5secMidWindow(i);
		return num;
	}
	
	/**
	 * 获取最近10个30秒时间窗口的和
	 * @return 最近10个30秒时间窗口的和
	 */
	public double get5minSupport()
	{
		int i;
		double num;
		num=0;
		for (i=0;i<this.num30secWindow();i++)
			num=num+this.get30secWindow(i);
		//从倾斜时间窗口缓存中读取
		for (i=0;i<10-this.num30secWindow();i++)
			num=num+this.get30secMidWindow(i);
		return num;
	}
/*	
	public static void main(String[] args){
		PatternTreeNode p1=new PatternTreeNode("a");
		p1.insert1secWindow(0,100);
		p1.insert1secWindow(1,105);
		p1.insert1secWindow(2,130);
		p1.insert1secMidWindow(0,100);
		p1.insert1secMidWindow(1,200);
		p1.insert1secMidWindow(2,150);
		p1.insert1secMidWindow(3,120);
		System.out.println(p1.get5secSupport());
	}
	*/
	
	
}
