import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 作者：暗影夜光
 * 算术型计算器
 */
public class Calculator extends JFrame {
	private static final Insets INSETS = new Insets(2, 2, 2, 2);// 定义组件边距
	private static BigInteger intNum = BigInteger.ZERO;//存放输入数值的整数部分
	private static StringBuffer dec = new StringBuffer();//存放输入数值的小数部分
	private static BigDecimal result = BigDecimal.ZERO;//存放计算结果
	private static BigDecimal tempNum = BigDecimal.ZERO;//临时存储
	private static BigDecimal memoryNum = BigDecimal.ZERO;//存储功能相关变量
	private static boolean decPointEnable = false;//是否开启小数点
	private static boolean nextStepClear = false;//下一步操作是否清屏
	private static boolean minus = false;//当前数字是否为负数，用于整数部分为0的情况
	private static int optchar = 0;//当前运算符，0~3:+ - / *
	private static int calcScale = 9; //小数精度
	private static int lastStepOpt = 0;//上次操作，0为输入数字，1为使用算术操作符，2为求结果
	private static final int CHAR_PER_LINE = 10;//左侧屏幕每行显示数字个数
	
	//声明组件
	private static JButton[] numButtons = new JButton[10];// 数字按键0~9
	private static JButton[] optButtons = new JButton[4];// 算术操作符按键，0~3对应+-*/
	private static JButton memAddButtons = new JButton("M+");
	private static JButton memMultButtons = new JButton("Mx");
	private static JButton memReadButtons = new JButton("MR");
	private static JButton memSetButtons = new JButton("MS");
	private static JButton memClearButtons = new JButton("MC");
	private static JButton pointButton = new JButton(".");
	private static JButton equalButton = new JButton("=");
	private static JButton ceButton = new JButton("CE");
	private static JButton clearButton = new JButton("C");
	private static JButton signChangeButton = new JButton("±");
	private static JButton backSpaceButton = new JButton("←");
	private static JButton percentButton = new JButton("%");
	private static JButton reciprocButton = new JButton("1/x");
	private static JButton sqrtButton = new JButton("√");
	private static JLabel screen = new JLabel("0");
	private static JLabel memoryScreen = new JLabel("0");
	private static JLabel leftScreen = new JLabel("状态栏======");
	
	/**
	 * 构造方法
	 */
	public Calculator() {
		this.setLayout(new GridBagLayout());// 为了更方便地实现所要求的界面，使用GridBagLayout布局管理器

		// 初始化窗体组件
		for (int i = 0; i < 10; i++) {
			numButtons[i] = new JButton(i + "");
			numButtons[i].setForeground(Color.white);
			numButtons[i].setBackground(new Color(181, 136, 106));
			numButtons[i].setFont(new Font("Segoe UI", Font.BOLD, 30));
			//数字按键直接在此处注册监听器
			numButtons[i].addActionListener(new NumberListener());
		}

		optButtons[0] = new JButton("+");
		optButtons[1] = new JButton("-");
		optButtons[2] = new JButton("×");
		optButtons[3] = new JButton("÷");

		// 样式设置
		pointButton.setForeground(Color.white);
		pointButton.setBackground(new Color(181, 136, 106));
		pointButton.setFont(new Font("Segoe UI", Font.BOLD, 30));

		memAddButtons.setBackground(Color.pink);
		memClearButtons.setBackground(Color.pink);
		memMultButtons.setBackground(Color.pink);
		memSetButtons.setBackground(Color.pink);
		memReadButtons.setBackground(Color.pink);

		backSpaceButton.setBackground(new Color(249, 255, 183));
		clearButton.setBackground(new Color(249, 255, 183));
		ceButton.setBackground(new Color(249, 255, 183));

		equalButton.setBackground(new Color(188, 233, 255));
		equalButton.setFont(new Font("Segoe UI", Font.BOLD, 30));

		signChangeButton.setBackground(new Color(195, 210, 255));
		percentButton.setBackground(new Color(195, 210, 255));
		percentButton.setFont(new Font("Segoe UI", Font.BOLD, 30));
		reciprocButton.setBackground(new Color(195, 210, 255));
		sqrtButton.setBackground(new Color(195, 210, 255));
		sqrtButton.setFont(new Font("Segoe UI", Font.BOLD, 30));

		for (int i = 0; i < optButtons.length; i++) {
			optButtons[i].setBackground(new Color(255, 195, 233));
			optButtons[i].setFont(new Font("Segoe UI", Font.BOLD, 30));
		}

		screen.setHorizontalAlignment(JLabel.RIGHT);
		screen.setFont(new Font("Segoe UI", Font.PLAIN, 50));
		screen.setForeground(Color.WHITE);
		screen.setOpaque(true);
		screen.setBackground(Color.gray);

		memoryScreen.setHorizontalAlignment(JLabel.RIGHT);
		memoryScreen.setFont(new Font("Segoe UI", Font.BOLD, 20));
		memoryScreen.setOpaque(true);
		memoryScreen.setBackground(new Color(186, 215, 186));

		leftScreen.setVerticalAlignment(JLabel.NORTH);
		leftScreen.setOpaque(true);
		leftScreen.setBackground(Color.white);
		leftScreen.setFont(new Font("宋体", Font.BOLD, 13));
		//样式设置结束
		
		
		//注册监听器
		pointButton.addActionListener(new PointListener());
		
		backSpaceButton.addActionListener(new ClearListener());
		ceButton.addActionListener(new ClearListener());
		clearButton.addActionListener(new ClearListener());
		
		memAddButtons.addActionListener(new MemoryListener());
		memClearButtons.addActionListener(new MemoryListener());
		memMultButtons.addActionListener(new MemoryListener());
		memReadButtons.addActionListener(new MemoryListener());
		memSetButtons.addActionListener(new MemoryListener());
		
		signChangeButton.addActionListener(new ChangeSignListener());
		reciprocButton.addActionListener(new ReciprocListener());
		sqrtButton.addActionListener(new SqrtListener());
		equalButton.addActionListener(new EqualListener());
		percentButton.addActionListener(new PercentListener());
		
		for (int i=0;i<optButtons.length;i++) {
			optButtons[i].addActionListener(new OptListener());
		}
		
		
		// 添加组件，第一行
		gbcAddComponent(this, screen, 0, 0, 6, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH);
		// 第二行
		gbcAddComponent(this, leftScreen, 0, 1, 1, 7,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, memoryScreen, 1, 1, 5, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		// 第三行
		gbcAddComponent(this, memSetButtons, 1, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, memReadButtons, 2, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, memAddButtons, 3, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, memMultButtons, 4, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, memClearButtons, 5, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		// 第四行
		gbcAddComponent(this, backSpaceButton, 1, 3, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, clearButton, 2, 3, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, ceButton, 3, 3, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH);
		gbcAddComponent(this, signChangeButton, 4, 3, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, reciprocButton, 5, 3, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		// 第五行
		gbcAddComponent(this, numButtons[7], 1, 4, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, numButtons[8], 2, 4, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, numButtons[9], 3, 4, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, optButtons[3], 4, 4, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, percentButton, 5, 4, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		// 第六行
		gbcAddComponent(this, numButtons[4], 1, 5, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, numButtons[5], 2, 5, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, numButtons[6], 3, 5, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, optButtons[2], 4, 5, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, sqrtButton, 5, 5, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		// 第七行
		gbcAddComponent(this, numButtons[1], 1, 6, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, numButtons[2], 2, 6, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, numButtons[3], 3, 6, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, optButtons[1], 4, 6, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, equalButton, 5, 6, 1, 2,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		// 第八行
		gbcAddComponent(this, numButtons[0], 1, 7, 2, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, pointButton, 3, 7, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, optButtons[0], 4, 7, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		//组件添加完毕
	}
	
	/**
	 * 程序入口点
	 */
	public static void main(String[] args) {
		Calculator frame = new Calculator();
		frame.setTitle("算术型计算器 By:暗影夜光");
		frame.setSize(480, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/**
	 * 将组件添加到GridBagLayout中
	 */
	private static void gbcAddComponent(Container container,
			Component component, int gridx, int gridy, int gridwidth,
			int gridheight, int anchor, int fill) 
	{
		//新建一个网格包对象
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy,
				gridwidth, gridheight, 1.0, 1.0, anchor, fill, INSETS, 0, 0);
		// 将其添加到容器中
		container.add(component, gbc);
	}
	
	/**
	 * 自动清屏
	 * 判断清屏标记，如果为真则清屏
	 * 重置清屏标记
	 */
	private static void autoClearScreen() {
		//是否应该清屏
		if (nextStepClear == true) {
			ClearInputNumber();
			nextStepClear = false;
		}
	}
	
	/**
	 * 进行计算
	 * 取得操作符
	 * 将输入缓存和tempNum运算后写入result
	 */
	private static void compute() {
		switch (optchar) {
		case 0:
			result = formatToBigDecimal(result.add(getInputNumber()));//加
			break;
		case 1:
			result = formatToBigDecimal(result.subtract(getInputNumber()));
			break;
		case 2:
			result = formatToBigDecimal(result.multiply(getInputNumber()));
			break;
		case 3:
			if (getInputNumber().signum() == 0) {
				JOptionPane.showMessageDialog(null, "除数为0", "错误", JOptionPane.ERROR_MESSAGE);
			} else {
				//抛出异常再四舍五入
				try {
					result = formatToBigDecimal(result.divide(getInputNumber()));
				} catch(RuntimeException ex) {
					result = formatToBigDecimal(result.divide(getInputNumber(),calcScale,BigDecimal.ROUND_HALF_UP));
				}
			}break;
		}
	}
	
	/**
	 * 刷新状态信息
	 * 过长的数字会导致布局发生严重变形
	 * 所以在显示信息之前，先对要显示的信息进行处理
	 */
	private static void refreshLeftScreen() {
		//更人性化的提示信息
		String curOpt = "";
		String curMinus = "";
		StringBuffer newIntNum = new StringBuffer(intNum.toString());
		StringBuffer newDec = new StringBuffer(dec);
		StringBuffer newResult = new StringBuffer(formatToString(result));
		
		//取得换行次数
		int intNumL = newIntNum.length() / CHAR_PER_LINE;
		int decL = newDec.length() / CHAR_PER_LINE;
		int resultL = newResult.length() / CHAR_PER_LINE;
		
		switch (optchar) {
		case 0:
			curOpt = "<font size=\"20\" color=\"blue\">加</font>";
			break;
		case 1:
			curOpt = "<font size=\"20\" color=\"purple\">减</font>";
			break;
		case 2:
			curOpt = "<font size=\"20\" color=\"teal\">乘</font>";
			break;
		case 3:
			curOpt = "<font size=\"20\" color=\"fuchsia\">除</font>";
			break;
		}
		if (minus) {
			curMinus = "<font color=\"red\">负数</font>";
		} else {
			curMinus = "<font color=\"green\">非负数</font>";
		}
		
		/*
		 * 自动换行的实现
		 * 换行标签插入位置：(i+1)*CHAR_PER_LINE+4*i
		 * 因为<br>标签长度为4，所以每次偏移4
		 */
		for (int i = 0; i < intNumL; i++) {
			newIntNum.insert((i + 1) * CHAR_PER_LINE + i * 4, "<br>");
		}
		for (int i = 0; i < decL; i++) {
			newDec.insert((i + 1) * CHAR_PER_LINE + i * 4, "<br>");
		}
		for (int i = 0; i < resultL; i++) {
			newResult.insert((i + 1) * CHAR_PER_LINE + i * 4, "<br>");
		}
		
		
		leftScreen.setText("<html>" + "整数缓冲区:" + "<br>" + newIntNum + "<br>" + "小数缓冲区:" + "<br>" + newDec + "<br>" + "<br>" + curMinus + "<br>" + "===========" + "<br>" + "算术操作：" + "<br>" + curOpt + "<br>" + "===========" + "<br>" + "结果缓冲区" + "<br>" + newResult + "</html>");
	}
	
	/**
	 * 格式化小数到文本
	 * 能去除末尾的0，将科学计数法转换为标准计数法
	 * 科学计数法表示数字时，将无法拆分整数和小数部分
	 * 某些赋值会抛出异常，部分计算会出错
	 * 所以，所有算术、存储操作之前先调用此方法格式化数据
	 */
	private static String formatToString(BigDecimal bd) {
		return bd.stripTrailingZeros().toPlainString();
	}
	
	/**
	 * 格式化小数
	 * 去除末尾的0，将科学计数法转换为标准计数法
	 */
	private static BigDecimal formatToBigDecimal(BigDecimal bd) {
		return new BigDecimal(bd.stripTrailingZeros().toPlainString());
	}
	
	/**
	 * 将结果显示在屏幕上
	 */
	private static void showResultOnScreen() {
		screen.setText(formatToString(result));
	}
	
	/**
	 * 将输入缓冲区中的数字显示在屏幕上
	 */
	private static void showInputNumberOnScreen() {
		if (decPointEnable) {
			//是否有小数部分
			if (minus && intNum.signum() == 0) {
				//如果整数部分为0，符号为负，则添加负号
				screen.setText("-" + intNum.toString() + "." + dec);
			} else {
				screen.setText(intNum.toString() + "." + dec);
			}
		} else {
			//只有整数
			if (minus && intNum.signum() == 0) {
				//如果整数部分为0，符号为负，则添加负号
				screen.setText("-" + intNum.toString());
			} else {
				screen.setText(intNum.toString());
			}
		}
	}
	
	/**
	 * 将数字自动拆分并写入输入缓存中
	 */
	private static void writeToInputCache(BigDecimal d) {
		if (d.toString().charAt(0) == '-') {
			minus = true;
		} else {
			minus = false;
		}
		
		//分割文本得到整数和小数部分
		String[] temp = formatToString(d).split("\\.");
		dec.setLength(0);//清除旧数据
		if (temp.length > 1) { //如果存在小数部分
			intNum = new BigInteger(temp[0]);
			decPointEnable = true;
			dec.append(temp[1]);
		} else {
			intNum = new BigInteger(formatToString(d));
			decPointEnable = false;
		}
	}
	
	/**
	 * 取得屏幕显示的数字
	 */
	private static BigDecimal getNumberFromScreen() {
		return new BigDecimal(screen.getText());
	}
	
	/**
	 * 取得完整的所输入的数字
	 */
	private static BigDecimal getInputNumber() {
		//如果是负数
		if (intNum.signum() == -1) {
			minus = true;
			//拼接数字
			return formatToBigDecimal(new BigDecimal(intNum).subtract(new BigDecimal("0." + dec)));
		} else	if (intNum.signum() == 1) {
			//正数
			minus = false;
			return formatToBigDecimal(new BigDecimal(intNum).add(new BigDecimal("0." + dec)));
		}
		//如果整数部分为0，直接判断符号标记
		if (minus) {
			// 负数
			return formatToBigDecimal(new BigDecimal(intNum).subtract(new BigDecimal("0." + dec)));
		}
		return 	formatToBigDecimal(new BigDecimal(intNum).add(new BigDecimal("0." + dec)));
	}
	
	/**
	 * 清除输入的内容
	 */
	private static void ClearInputNumber() {
		intNum = BigInteger.ZERO;
		dec.setLength(0);
		decPointEnable = false;
		minus = false;
	}
	
	/**
	 * 清除全部运算数据
	 */
	private static void ClearAll() {
		result = BigDecimal.ZERO;
		tempNum = BigDecimal.ZERO;
		optchar = 0;
		ClearInputNumber();
	}
	
	/**
	 * 数字键事件处理
	 */
	class NumberListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (lastStepOpt == 2) {
				//如果已经求和，数据全清
				ClearAll();
			}
			autoClearScreen();
			
			//取得按钮数字
			String curPress = e.getActionCommand();
			//判断是否进入小数模式
			if (decPointEnable == false) {
				//如果不是，则操作整数部分
				//正加负减
				if (minus) {
					intNum = intNum.multiply(BigInteger.TEN).subtract(new BigInteger(curPress));
				} else {
					intNum = intNum.multiply(BigInteger.TEN).add(new BigInteger(curPress));
				}
			} else {
				//小数部分，连接字符串
				dec.append(curPress);
			}
			showInputNumberOnScreen();
			lastStepOpt = 0;
			refreshLeftScreen();
		}
	}
	
	/**
	 * 小数点键事件处理
	 */
	class PointListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			lastStepOpt = 0;
			autoClearScreen();
			
			decPointEnable = true;//核心操作，开启小数点
			showInputNumberOnScreen();
			refreshLeftScreen();
		}
	}
	
	/**
	 * 清除键事件处理
	 */
	class ClearListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			autoClearScreen();
			//取得按键
			String curPress = e.getActionCommand();
			//退格
			lastStepOpt = 0;
			if (curPress == "←") {
				if (decPointEnable == true) {
					//如果小数部分不为空
					if (dec.length() != 0) {
						dec.deleteCharAt(dec.length()-1);
					} else {
						//如果为空，关闭小数模式
						decPointEnable = false;
					}
				} else {
					//如果是整数模式
					//如果整数为0则去除负号;
					if (intNum.signum() == 0) {
						minus = false;
					}
					intNum = intNum.divide(BigInteger.TEN);//除以10
				}
			} else if (curPress == "C") { //清除全部数据
				ClearAll();
			} else if (curPress == "CE"){ //清屏
				ClearInputNumber();
			}
			showInputNumberOnScreen();
			refreshLeftScreen();
		}
	}
	
	/**
	 * 存储功能事件处理
	 */
	class MemoryListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String curPress = e.getActionCommand();
			if (curPress == "MS") {
				memoryNum = formatToBigDecimal(getNumberFromScreen());
			} else if (curPress == "MR") {
				lastStepOpt = 0;
				writeToInputCache(memoryNum);
				showInputNumberOnScreen();
			} else if (curPress == "M+") {
				memoryNum = formatToBigDecimal(memoryNum.add(getNumberFromScreen()));
			} else if (curPress == "Mx") {
				memoryNum = formatToBigDecimal(memoryNum.multiply(getNumberFromScreen()));
			} else if (curPress == "MC") {
				memoryNum = BigDecimal.ZERO;
			}
			nextStepClear = true;//下次操作可以清屏了
			memoryScreen.setText(memoryNum.toString());
			refreshLeftScreen();
		}
	}
	
	/**
	 * 切换正负事件处理
	 */
	class ChangeSignListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			lastStepOpt = 0;
			//取得数值
			tempNum = getNumberFromScreen();
			if (tempNum.signum() != 0) {
				//如果整数部分不是0
				tempNum = tempNum.negate();
				//翻转符号后重设正负标记
				if (tempNum.signum() == -1) {
					minus = true;
				} else {
					minus = false;
				}
				writeToInputCache(tempNum);
			} else {
				//如果是0，直接变更正负标记
				if (minus) {
					minus = false;
				} else {
					minus = true;
				}
			}
			tempNum = BigDecimal.ZERO;
			showInputNumberOnScreen();
			refreshLeftScreen();
		}
	}
	
	/**
	 * 取倒数事件处理
	 */
	class ReciprocListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (getNumberFromScreen().signum() != 0) {
				lastStepOpt = 0;
				//0不作处理
				try {
					tempNum = formatToBigDecimal(BigDecimal.ONE.divide(getNumberFromScreen()));
				} catch (RuntimeException ex) {
					tempNum = formatToBigDecimal(BigDecimal.ONE.divide(getNumberFromScreen(),calcScale,BigDecimal.ROUND_HALF_UP));
				}
				writeToInputCache(tempNum);
				tempNum = BigDecimal.ZERO;
				showInputNumberOnScreen();
			}
			nextStepClear = true;
			refreshLeftScreen();
		}
	}
	
	/**
	 * 开平方事件处理
	 * 使用了double型数据和Math类的sqrt方法
	 * 结果可能不精确
	 */
	class SqrtListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			lastStepOpt = 0;
			if (getNumberFromScreen().signum() != -1) {
				//会损失精度
				writeToInputCache(new BigDecimal(Math.sqrt(getNumberFromScreen().doubleValue())));
			} else {
				JOptionPane.showMessageDialog(null, "负数是不可以开平方的", "错误", JOptionPane.ERROR_MESSAGE);
			}
			showInputNumberOnScreen();
			nextStepClear = true;
			refreshLeftScreen();
		}
	}
	
	/**
	 * 算术操作符按键事件
	 */
	class OptListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (lastStepOpt == 0) {
				//如果上次操作为数值输入
				compute();
				showResultOnScreen();
			}
			if (optButtons[0] == e.getSource()) {
				optchar = 0;
			} else if (optButtons[1] == e.getSource()) {
				optchar = 1;
			} else if (optButtons[2] == e.getSource()) {
				optchar = 2;
			} else if (optButtons[3] == e.getSource()) {
				optchar = 3;
			}
			//ClearInputNumber();//真实的计算器好像不会清除……
			lastStepOpt = 1;
			nextStepClear = true;
			refreshLeftScreen();
		}
	}
	
	/**
	 * 等号按键事件
	 */
	class EqualListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			lastStepOpt = 2;
			compute();
			showResultOnScreen();
			nextStepClear = true;
			refreshLeftScreen();
		}
	}
	
	/**
	 * 百分号按键事件
	 * 将当前输入缓冲区中的数值除以100
	 * 然后与结果相乘
	 * 最后写回输入缓冲区
	 */
	class PercentListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			writeToInputCache(getInputNumber().divide(new BigDecimal("100")).multiply(result));
			
			showInputNumberOnScreen();
			refreshLeftScreen();
		}
	}
}
