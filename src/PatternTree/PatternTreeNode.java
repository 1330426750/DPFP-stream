package PatternTree;

import java.io.Serializable;
import java.util.ArrayList;  
import java.util.List;  

public class PatternTreeNode implements Serializable
{
	/** �ڵ�Id*/  
    private String nodeId;
    /** ���׽ڵ�*/
    private PatternTreeNode parent;

	/** �ӽڵ㼯��*/
	private List<PatternTreeNode> childList;
	/** ��бʱ�䴰��*/
	private double[] timewindow_1sec= new double[5];
	private double[] timewindow_5sec= new double[6];
	private double[] timewindow_30sec= new double[10];
	private double[] timewindow_5min= new double[6];
	/** ��бʱ�䴰�ڻ���*/
	private double[] timewindow_1sec_mid= new double[4];
	private double[] timewindow_5sec_mid= new double[5];
	private double[] timewindow_30sec_mid= new double[9];
	private double[] timewindow_5min_mid= new double[5];
	
	private int presentTimeWindow;  //��ǰʱ�䴰�ںţ� ��PatternTree�ĵ�ǰʱ�䴰�����Ƚ�
	private int removeTag;          //��֦��ǣ���patternTree���м�֦����ʱ����Ƿ�ɨ����ý��
	private int selectTag;          //��ѯ��ǣ���patternTree���в�ѯ����ʱ�����Ĳ�ѯ��ǱȽϣ��Եó��Ƿ��ѯ���ýڵ�
	private int createTag;          //������ǣ����ڽڵ�Ĳ������޸�

	/**
	 * ���캯��
	 * 
	 * @param nodeId   �ڵ���
	 */
	public PatternTreeNode(String nodeId)  
	{  
		this.nodeId = nodeId;
	    this.childList = new ArrayList<PatternTreeNode>();
	    /*��ʼ����бʱ�䴰��*/
	    initTimeWindow();
	} 
	
	/**
	 * ���캯�� 
	 * 
	 * @param nodeId   �ڵ���
	 * @param parent   ���׽ڵ�
	 */
	public PatternTreeNode(String nodeId,PatternTreeNode parent)  
	{  
		this.nodeId = nodeId;
		this.parent = parent;
	    this.childList = new ArrayList<PatternTreeNode>();
	    /*��ʼ����бʱ�䴰��*/
	    initTimeWindow();
	    parent.childList.add(this);
	}
	
	/**
	  * ���캯��
	  * 
	  * @param nodeId  �ڵ���
	  * @param childList   �ӽڵ㼯��
	  */
	public PatternTreeNode(String nodeId, List<PatternTreeNode> childList)  
	{  
		this.nodeId = nodeId; 
		this.childList = childList; 
		/*��ʼ����бʱ�䴰��*/
		initTimeWindow();
	} 
	
	/**
	 * ���캯�� 
	 * 
	 * @param nodeId   �ڵ���
	 * @param parent   ���׽ڵ�
	 * @param childList   �ӽڵ㼯��
	 */
	public PatternTreeNode(String nodeId, PatternTreeNode parent, List<PatternTreeNode> childList)  
	{  
		this.nodeId = nodeId; 
		this.parent = parent;
		this.childList = childList; 
		/*��ʼ����бʱ�䴰��*/
		initTimeWindow();
	}
	 /**
	 * ��ȡ�ӽڵ㼯�� 
	 * 
	 * @return �����ӽڵ㼯��
	 * 
	 */   
	public List<PatternTreeNode> getChildList() {  
	    return childList;  
	}  
	 /**
	 * �����ӽڵ㼯�� 
	 *  
	 */ 
	public void setChildList(List<PatternTreeNode> childList) {  
	    this.childList = childList;  
	}
	
	/**
	 * 
	 * ��ʼ����б����
	 * ��1*5s����-1����մ���
	 */
	public void init1secWindow(){
		for(int i=0;i<5;i++)
			this.timewindow_1sec[i]=-1;
	}
	/**
	 * 
	 * ��ʼ����б����
	 * ��5s��б������-1����մ���
	 */
	public void init5secWindow(){
		for(int i=0;i<6;i++)
			this.timewindow_5sec[i]=-1;
	}
	/**
	 * 
	 * ��ʼ����б����
	 * ��30s��б������-1����մ���
	 */
	public void init30secWindow(){
		for(int i=0;i<10;i++)
			this.timewindow_30sec[i]=-1;
	}
	/**
	 * 
	 * ��ʼ����б����
	 * ��300s��б������-1����մ���
	 */
	public void init5minWindow(){
		for(int i=0;i<6;i++)
			this.timewindow_5min[i]=-1;
	}
	/**
	 * 
	 * ��ʼ����бʱ�䴰�ڻ���
	 * ��1s��бʱ�䴰�ڻ��涼��-1
	 */	
	public void init1secMidWindow(){
		for(int i=0;i<4;i++)
			this.timewindow_1sec_mid[i]=-1;
	}
	/**
	 * 
	 * ��ʼ����бʱ�䴰�ڻ���
	 * ��5s��бʱ�䴰�ڻ��涼��-1
	 */
	public void init5secMidWindow(){
		for(int i=0;i<5;i++)
			this.timewindow_5sec_mid[i]=-1;
	}
	/**
	 * 
	 * ��ʼ����бʱ�䴰�ڻ���
	 * ��30s��бʱ�䴰�ڻ��涼��-1
	 */
	public void init30secMidWindow(){
		for(int i=0;i<9;i++)
			this.timewindow_30sec_mid[i]=-1;
	}
	/**
	 * 
	 * ��ʼ����бʱ�䴰�ڻ���
	 * ��300s��бʱ�䴰�ڻ��涼��-1
	 */	
	public void init5minMidWindow(){
		for(int i=0;i<5;i++)
			this.timewindow_5min_mid[i]=-1;
	}
	/**
	 * ��ʼ����бʱ�䴰��
	 * ��ʼ����бʱ�䴰�ڻ���
	 * ���õ�ǰʱ�䴰�ں�
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
	 * @return ����1s��бʱ�䴰�ڷ�Null����Ŀ 
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
	 * @return ����5s��бʱ�䴰�ڷ�Null����Ŀ 
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
	 * @return ����30s��бʱ�䴰�ڷ�Null����Ŀ 
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
	 * @return ����300s��бʱ�䴰�ڷ�Null����Ŀ 
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
	 * ��1sʱ�䴰�ڲ�������
	 * 
	 * @param i      �����λ��
	 * @param value  �����ֵ
	 */
	public void insert1secWindow(int i, double value){
		this.timewindow_1sec[i]=value;
	}
	/**
	 * ��5sʱ�䴰�ڲ�������
	 * 
	 * @param i      �����λ��
	 * @param value  �����ֵ
	 */
	public void insert5secWindow(int i, double value){
		this.timewindow_5sec[i]=value;
	}
	/**
	 * ��30sʱ�䴰�ڲ�������
	 * 
	 * @param i      �����λ��
	 * @param value  �����ֵ
	 */
	public void insert30secWindow(int i, double value){
		this.timewindow_30sec[i]=value;
	}
	/**
	 * ��300sʱ�䴰�ڲ�������
	 * 
	 * @param i      �����λ��
	 * @param value  �����ֵ
	 */
	public void insert5minWindow(int i, double value){
		this.timewindow_5min[i]=value;
	}
	/**
	 * ��1sʱ�䴰�ڻ����в�������
	 * 
	 * @param i      �����λ��
	 * @param value  �����ֵ
	 */
	public void insert1secMidWindow(int i, double value){
		this.timewindow_1sec_mid[i]=value;
	}
	/**
	 * ��5sʱ�䴰�ڻ����в�������
	 * 
	 * @param i      �����λ��
	 * @param value  �����ֵ
	 */
	public void insert5secMidWindow(int i, double value){
		this.timewindow_5sec_mid[i]=value;
	}
	/**
	 * ��30sʱ�䴰�ڻ����в�������
	 * 
	 * @param i      �����λ��
	 * @param value  �����ֵ
	 */
	public void insert30secMidWindow(int i, double value){
		this.timewindow_30sec_mid[i]=value;
	}
	/**
	 * ��300sʱ�䴰�ڻ����в�������
	 * 
	 * @param i      �����λ��
	 * @param value  �����ֵ
	 */
	public void insert5minMidWindow(int i, double value){
		this.timewindow_5min_mid[i]=value;
	}
	
	/**
	 * ��ȡ1sʱ�䴰����ָ��λ�õ�ֵ
	 * @param i ʱ�䴰��λ��
	 * @return  ����ָ��ʱ�䴰�ڵ�ֵ
	 */
	public double get1secWindow(int i){
		return this.timewindow_1sec[i];
	}
	/**
	 * ��ȡ5sʱ�䴰����ָ��λ�õ�ֵ
	 * @param i ʱ�䴰��λ��
	 * @return  ����ָ��ʱ�䴰�ڵ�ֵ
	 */
	public double get5secWindow(int i){
		return this.timewindow_5sec[i];
	}
	/**
	 * ��ȡ30sʱ�䴰����ָ��λ�õ�ֵ
	 * @param i ʱ�䴰��λ��
	 * @return  ����ָ��ʱ�䴰�ڵ�ֵ
	 */
	public double get30secWindow(int i){
		return this.timewindow_30sec[i];
	}
	/**
	 * ��ȡ300sʱ�䴰����ָ��λ�õ�ֵ
	 * @param i ʱ�䴰��λ��
	 * @return  ����ָ��ʱ�䴰�ڵ�ֵ
	 */
	public double get5minWindow(int i){
		return this.timewindow_5min[i];
	}
	/**
	 * ��ȡ1sʱ�䴰�ڻ�����ָ��λ�õ�ֵ
	 * @param i ʱ�䴰��λ��
	 * @return  ����ָ��ʱ�䴰�ڵ�ֵ
	 */
	public double get1secMidWindow(int i){
		return this.timewindow_1sec_mid[i];
	}
	/**
	 * ��ȡ5sʱ�䴰�ڻ�����ָ��λ�õ�ֵ
	 * @param i ʱ�䴰��λ��
	 * @return  ����ָ��ʱ�䴰�ڵ�ֵ
	 */
	public double get5secMidWindow(int i){
		return this.timewindow_5sec_mid[i];
	}
	/**
	 * ��ȡ30sʱ�䴰�ڻ�����ָ��λ�õ�ֵ
	 * @param i ʱ�䴰��λ��
	 * @return  ����ָ��ʱ�䴰�ڵ�ֵ
	 */
	public double get30secMidWindow(int i){
		return this.timewindow_30sec_mid[i];
	}
	/**
	 * ��ȡ300sʱ�䴰�ڻ�����ָ��λ�õ�ֵ
	 * @param i ʱ�䴰��λ��
	 * @return  ����ָ��ʱ�䴰�ڵ�ֵ
	 */
	public double get5minMidWindow(int i){
		return this.timewindow_5min_mid[i];
	}
	
	/**
	 * ��ȡ������бʱ�䴰��
	 * @return ����������бʱ�䴰��
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
	 * ��ȡ������бʱ�䴰�ڻ���
	 * @return ����������бʱ�䴰�ڻ���
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
	 * ��ȡ�ڵ��
	 * @return �������нڵ��
	 */
	public String getNodeId() {  
	    return nodeId;  
	}  
	 /**
	 * ���ýڵ��
	 * @param  nodeId  �ڵ��
	 */ 
	public void setNodeId(String nodeId) {  
	    this.nodeId = nodeId;  
	}  
	/**
	 * ��ȡ���ڵ�
	 * @return ���ظ��ڵ�
	 */  
	public PatternTreeNode getParent() {  
	    return parent;  
	}  
	/**
	 * ���ø��ڵ��
	 * @param  parent  ���ڵ�
	 */  
	public void setParentId(PatternTreeNode parent) {  
	    this.parent = parent;  
	}  
	/**
	 * ��ȡ��ǰʱ�䴰��
	 * @return ���ص�ǰʱ�䴰��
	 */
	public int getPresentTimeWindow() {  
	    return this.presentTimeWindow;  
	}  
	/**
	 * ���õ�ǰʱ�䴰��
	 * @param presentTimeWindow ��ǰʱ�䴰��
	 */ 
	public void setPresentTimeWindow(int presentTimeWindow) {  
	    this.presentTimeWindow = presentTimeWindow;  
	}
	/**
	 * ��ȡ��֦���
	 * @return ���ؼ�֦���
	 */
	public int getRemoveTag() {  
	    return this.removeTag;  
	}  
	/**
	 * ���ü�֦���
	 * @param removeTag   ��֦���
	 */   
	public void setRemoveTag(int removeTag) {  
	    this.removeTag = removeTag;  
	}
	/**
	 * ��ȡ��ѯ���
	 * @return ���ز�ѯ���
	 */
	public int getSelectTag(){  
	    return this.selectTag;  
	}  
	/**
	 * ���ò�ѯ���
	 * @param selectTag   ��ѯ���
	 */  
	public void setSelectTag(int selectTag){  
	    this.selectTag = selectTag;  
	}
	/**
	 * ��ȡ�������
	 * @return ���ش������
	 */
	public int getCreateTag(){  
	    return this.createTag;  
	}  
	/**
	 * ���ô������
	 *  @param createTag   �������
	 */  
	public void setCreateTag(int createTag){  
	    this.createTag = createTag;  
	}
	
	/**
	 * ��ȡ���5��1��ʱ�䴰�ڵĺ�
	 * @return ���5��1��ʱ�䴰�ڵĺ�
	 */
	public double get5secSupport()
	{
		int i;
		double num;
		num=0;
		for (i=0;i<this.num1secWindow();i++)
			num=num+this.get1secWindow(i);
		//����бʱ�䴰�ڻ����ж�ȡ
		for (i=0;i<5-this.num1secWindow();i++)
			num=num+this.get1secMidWindow(i);
		return num;
	}
	
	/**
	 * ��ȡ���6��5��ʱ�䴰�ڵĺ�
	 * @return ���6��5��ʱ�䴰�ڵĺ�
	 */
	public double get30secSupport()
	{
		int i;
		double num;
		num=0;
		for (i=0;i<this.num5secWindow();i++)
			num=num+this.get5secWindow(i);
		//����бʱ�䴰�ڻ����ж�ȡ
		for (i=0;i<6-this.num5secWindow();i++)
			num=num+this.get5secMidWindow(i);
		return num;
	}
	
	/**
	 * ��ȡ���10��30��ʱ�䴰�ڵĺ�
	 * @return ���10��30��ʱ�䴰�ڵĺ�
	 */
	public double get5minSupport()
	{
		int i;
		double num;
		num=0;
		for (i=0;i<this.num30secWindow();i++)
			num=num+this.get30secWindow(i);
		//����бʱ�䴰�ڻ����ж�ȡ
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
