import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * ���ߣ���Ӱҹ��
 * �����ͼ�����
 */
public class Calculator extends JFrame {
	private static final Insets INSETS = new Insets(2, 2, 2, 2);// ��������߾�
	private static BigInteger intNum = BigInteger.ZERO;//���������ֵ����������
	private static StringBuffer dec = new StringBuffer();//���������ֵ��С������
	private static BigDecimal result = BigDecimal.ZERO;//��ż�����
	private static BigDecimal tempNum = BigDecimal.ZERO;//��ʱ�洢
	private static BigDecimal memoryNum = BigDecimal.ZERO;//�洢������ر���
	private static boolean decPointEnable = false;//�Ƿ���С����
	private static boolean nextStepClear = false;//��һ�������Ƿ�����
	private static boolean minus = false;//��ǰ�����Ƿ�Ϊ������������������Ϊ0�����
	private static int optchar = 0;//��ǰ�������0~3:+ - / *
	private static int calcScale = 9; //С������
	private static int lastStepOpt = 0;//�ϴβ�����0Ϊ�������֣�1Ϊʹ��������������2Ϊ����
	private static final int CHAR_PER_LINE = 10;//�����Ļÿ����ʾ���ָ���
	
	//�������
	private static JButton[] numButtons = new JButton[10];// ���ְ���0~9
	private static JButton[] optButtons = new JButton[4];// ����������������0~3��Ӧ+-*/
	private static JButton memAddButtons = new JButton("M+");
	private static JButton memMultButtons = new JButton("Mx");
	private static JButton memReadButtons = new JButton("MR");
	private static JButton memSetButtons = new JButton("MS");
	private static JButton memClearButtons = new JButton("MC");
	private static JButton pointButton = new JButton(".");
	private static JButton equalButton = new JButton("=");
	private static JButton ceButton = new JButton("CE");
	private static JButton clearButton = new JButton("C");
	private static JButton signChangeButton = new JButton("��");
	private static JButton backSpaceButton = new JButton("��");
	private static JButton percentButton = new JButton("%");
	private static JButton reciprocButton = new JButton("1/x");
	private static JButton sqrtButton = new JButton("��");
	private static JLabel screen = new JLabel("0");
	private static JLabel memoryScreen = new JLabel("0");
	private static JLabel leftScreen = new JLabel("״̬��======");
	
	/**
	 * ���췽��
	 */
	public Calculator() {
		this.setLayout(new GridBagLayout());// Ϊ�˸������ʵ����Ҫ��Ľ��棬ʹ��GridBagLayout���ֹ�����

		// ��ʼ���������
		for (int i = 0; i < 10; i++) {
			numButtons[i] = new JButton(i + "");
			numButtons[i].setForeground(Color.white);
			numButtons[i].setBackground(new Color(181, 136, 106));
			numButtons[i].setFont(new Font("Segoe UI", Font.BOLD, 30));
			//���ְ���ֱ���ڴ˴�ע�������
			numButtons[i].addActionListener(new NumberListener());
		}

		optButtons[0] = new JButton("+");
		optButtons[1] = new JButton("-");
		optButtons[2] = new JButton("��");
		optButtons[3] = new JButton("��");

		// ��ʽ����
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
		leftScreen.setFont(new Font("����", Font.BOLD, 13));
		//��ʽ���ý���
		
		
		//ע�������
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
		
		
		// ����������һ��
		gbcAddComponent(this, screen, 0, 0, 6, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH);
		// �ڶ���
		gbcAddComponent(this, leftScreen, 0, 1, 1, 7,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, memoryScreen, 1, 1, 5, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		// ������
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
		// ������
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
		// ������
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
		// ������
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
		// ������
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
		// �ڰ���
		gbcAddComponent(this, numButtons[0], 1, 7, 2, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, pointButton, 3, 7, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		gbcAddComponent(this, optButtons[0], 4, 7, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		//���������
	}
	
	/**
	 * ������ڵ�
	 */
	public static void main(String[] args) {
		Calculator frame = new Calculator();
		frame.setTitle("�����ͼ����� By:��Ӱҹ��");
		frame.setSize(480, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/**
	 * �������ӵ�GridBagLayout��
	 */
	private static void gbcAddComponent(Container container,
			Component component, int gridx, int gridy, int gridwidth,
			int gridheight, int anchor, int fill) 
	{
		//�½�һ�����������
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy,
				gridwidth, gridheight, 1.0, 1.0, anchor, fill, INSETS, 0, 0);
		// ������ӵ�������
		container.add(component, gbc);
	}
	
	/**
	 * �Զ�����
	 * �ж�������ǣ����Ϊ��������
	 * �����������
	 */
	private static void autoClearScreen() {
		//�Ƿ�Ӧ������
		if (nextStepClear == true) {
			ClearInputNumber();
			nextStepClear = false;
		}
	}
	
	/**
	 * ���м���
	 * ȡ�ò�����
	 * �����뻺���tempNum�����д��result
	 */
	private static void compute() {
		switch (optchar) {
		case 0:
			result = formatToBigDecimal(result.add(getInputNumber()));//��
			break;
		case 1:
			result = formatToBigDecimal(result.subtract(getInputNumber()));
			break;
		case 2:
			result = formatToBigDecimal(result.multiply(getInputNumber()));
			break;
		case 3:
			if (getInputNumber().signum() == 0) {
				JOptionPane.showMessageDialog(null, "����Ϊ0", "����", JOptionPane.ERROR_MESSAGE);
			} else {
				//�׳��쳣����������
				try {
					result = formatToBigDecimal(result.divide(getInputNumber()));
				} catch(RuntimeException ex) {
					result = formatToBigDecimal(result.divide(getInputNumber(),calcScale,BigDecimal.ROUND_HALF_UP));
				}
			}break;
		}
	}
	
	/**
	 * ˢ��״̬��Ϣ
	 * ���������ֻᵼ�²��ַ������ر���
	 * ��������ʾ��Ϣ֮ǰ���ȶ�Ҫ��ʾ����Ϣ���д���
	 */
	private static void refreshLeftScreen() {
		//�����Ի�����ʾ��Ϣ
		String curOpt = "";
		String curMinus = "";
		StringBuffer newIntNum = new StringBuffer(intNum.toString());
		StringBuffer newDec = new StringBuffer(dec);
		StringBuffer newResult = new StringBuffer(formatToString(result));
		
		//ȡ�û��д���
		int intNumL = newIntNum.length() / CHAR_PER_LINE;
		int decL = newDec.length() / CHAR_PER_LINE;
		int resultL = newResult.length() / CHAR_PER_LINE;
		
		switch (optchar) {
		case 0:
			curOpt = "<font size=\"20\" color=\"blue\">��</font>";
			break;
		case 1:
			curOpt = "<font size=\"20\" color=\"purple\">��</font>";
			break;
		case 2:
			curOpt = "<font size=\"20\" color=\"teal\">��</font>";
			break;
		case 3:
			curOpt = "<font size=\"20\" color=\"fuchsia\">��</font>";
			break;
		}
		if (minus) {
			curMinus = "<font color=\"red\">����</font>";
		} else {
			curMinus = "<font color=\"green\">�Ǹ���</font>";
		}
		
		/*
		 * �Զ����е�ʵ��
		 * ���б�ǩ����λ�ã�(i+1)*CHAR_PER_LINE+4*i
		 * ��Ϊ<br>��ǩ����Ϊ4������ÿ��ƫ��4
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
		
		
		leftScreen.setText("<html>" + "����������:" + "<br>" + newIntNum + "<br>" + "С��������:" + "<br>" + newDec + "<br>" + "<br>" + curMinus + "<br>" + "===========" + "<br>" + "����������" + "<br>" + curOpt + "<br>" + "===========" + "<br>" + "���������" + "<br>" + newResult + "</html>");
	}
	
	/**
	 * ��ʽ��С�����ı�
	 * ��ȥ��ĩβ��0������ѧ������ת��Ϊ��׼������
	 * ��ѧ��������ʾ����ʱ�����޷����������С������
	 * ĳЩ��ֵ���׳��쳣�����ּ�������
	 * ���ԣ������������洢����֮ǰ�ȵ��ô˷�����ʽ������
	 */
	private static String formatToString(BigDecimal bd) {
		return bd.stripTrailingZeros().toPlainString();
	}
	
	/**
	 * ��ʽ��С��
	 * ȥ��ĩβ��0������ѧ������ת��Ϊ��׼������
	 */
	private static BigDecimal formatToBigDecimal(BigDecimal bd) {
		return new BigDecimal(bd.stripTrailingZeros().toPlainString());
	}
	
	/**
	 * �������ʾ����Ļ��
	 */
	private static void showResultOnScreen() {
		screen.setText(formatToString(result));
	}
	
	/**
	 * �����뻺�����е�������ʾ����Ļ��
	 */
	private static void showInputNumberOnScreen() {
		if (decPointEnable) {
			//�Ƿ���С������
			if (minus && intNum.signum() == 0) {
				//�����������Ϊ0������Ϊ��������Ӹ���
				screen.setText("-" + intNum.toString() + "." + dec);
			} else {
				screen.setText(intNum.toString() + "." + dec);
			}
		} else {
			//ֻ������
			if (minus && intNum.signum() == 0) {
				//�����������Ϊ0������Ϊ��������Ӹ���
				screen.setText("-" + intNum.toString());
			} else {
				screen.setText(intNum.toString());
			}
		}
	}
	
	/**
	 * �������Զ���ֲ�д�����뻺����
	 */
	private static void writeToInputCache(BigDecimal d) {
		if (d.toString().charAt(0) == '-') {
			minus = true;
		} else {
			minus = false;
		}
		
		//�ָ��ı��õ�������С������
		String[] temp = formatToString(d).split("\\.");
		dec.setLength(0);//���������
		if (temp.length > 1) { //�������С������
			intNum = new BigInteger(temp[0]);
			decPointEnable = true;
			dec.append(temp[1]);
		} else {
			intNum = new BigInteger(formatToString(d));
			decPointEnable = false;
		}
	}
	
	/**
	 * ȡ����Ļ��ʾ������
	 */
	private static BigDecimal getNumberFromScreen() {
		return new BigDecimal(screen.getText());
	}
	
	/**
	 * ȡ�������������������
	 */
	private static BigDecimal getInputNumber() {
		//����Ǹ���
		if (intNum.signum() == -1) {
			minus = true;
			//ƴ������
			return formatToBigDecimal(new BigDecimal(intNum).subtract(new BigDecimal("0." + dec)));
		} else	if (intNum.signum() == 1) {
			//����
			minus = false;
			return formatToBigDecimal(new BigDecimal(intNum).add(new BigDecimal("0." + dec)));
		}
		//�����������Ϊ0��ֱ���жϷ��ű��
		if (minus) {
			// ����
			return formatToBigDecimal(new BigDecimal(intNum).subtract(new BigDecimal("0." + dec)));
		}
		return 	formatToBigDecimal(new BigDecimal(intNum).add(new BigDecimal("0." + dec)));
	}
	
	/**
	 * ������������
	 */
	private static void ClearInputNumber() {
		intNum = BigInteger.ZERO;
		dec.setLength(0);
		decPointEnable = false;
		minus = false;
	}
	
	/**
	 * ���ȫ����������
	 */
	private static void ClearAll() {
		result = BigDecimal.ZERO;
		tempNum = BigDecimal.ZERO;
		optchar = 0;
		ClearInputNumber();
	}
	
	/**
	 * ���ּ��¼�����
	 */
	class NumberListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (lastStepOpt == 2) {
				//����Ѿ���ͣ�����ȫ��
				ClearAll();
			}
			autoClearScreen();
			
			//ȡ�ð�ť����
			String curPress = e.getActionCommand();
			//�ж��Ƿ����С��ģʽ
			if (decPointEnable == false) {
				//������ǣ��������������
				//���Ӹ���
				if (minus) {
					intNum = intNum.multiply(BigInteger.TEN).subtract(new BigInteger(curPress));
				} else {
					intNum = intNum.multiply(BigInteger.TEN).add(new BigInteger(curPress));
				}
			} else {
				//С�����֣������ַ���
				dec.append(curPress);
			}
			showInputNumberOnScreen();
			lastStepOpt = 0;
			refreshLeftScreen();
		}
	}
	
	/**
	 * С������¼�����
	 */
	class PointListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			lastStepOpt = 0;
			autoClearScreen();
			
			decPointEnable = true;//���Ĳ���������С����
			showInputNumberOnScreen();
			refreshLeftScreen();
		}
	}
	
	/**
	 * ������¼�����
	 */
	class ClearListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			autoClearScreen();
			//ȡ�ð���
			String curPress = e.getActionCommand();
			//�˸�
			lastStepOpt = 0;
			if (curPress == "��") {
				if (decPointEnable == true) {
					//���С�����ֲ�Ϊ��
					if (dec.length() != 0) {
						dec.deleteCharAt(dec.length()-1);
					} else {
						//���Ϊ�գ��ر�С��ģʽ
						decPointEnable = false;
					}
				} else {
					//���������ģʽ
					//�������Ϊ0��ȥ������;
					if (intNum.signum() == 0) {
						minus = false;
					}
					intNum = intNum.divide(BigInteger.TEN);//����10
				}
			} else if (curPress == "C") { //���ȫ������
				ClearAll();
			} else if (curPress == "CE"){ //����
				ClearInputNumber();
			}
			showInputNumberOnScreen();
			refreshLeftScreen();
		}
	}
	
	/**
	 * �洢�����¼�����
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
			nextStepClear = true;//�´β�������������
			memoryScreen.setText(memoryNum.toString());
			refreshLeftScreen();
		}
	}
	
	/**
	 * �л������¼�����
	 */
	class ChangeSignListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			lastStepOpt = 0;
			//ȡ����ֵ
			tempNum = getNumberFromScreen();
			if (tempNum.signum() != 0) {
				//����������ֲ���0
				tempNum = tempNum.negate();
				//��ת���ź������������
				if (tempNum.signum() == -1) {
					minus = true;
				} else {
					minus = false;
				}
				writeToInputCache(tempNum);
			} else {
				//�����0��ֱ�ӱ���������
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
	 * ȡ�����¼�����
	 */
	class ReciprocListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (getNumberFromScreen().signum() != 0) {
				lastStepOpt = 0;
				//0��������
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
	 * ��ƽ���¼�����
	 * ʹ����double�����ݺ�Math���sqrt����
	 * ������ܲ���ȷ
	 */
	class SqrtListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			lastStepOpt = 0;
			if (getNumberFromScreen().signum() != -1) {
				//����ʧ����
				writeToInputCache(new BigDecimal(Math.sqrt(getNumberFromScreen().doubleValue())));
			} else {
				JOptionPane.showMessageDialog(null, "�����ǲ����Կ�ƽ����", "����", JOptionPane.ERROR_MESSAGE);
			}
			showInputNumberOnScreen();
			nextStepClear = true;
			refreshLeftScreen();
		}
	}
	
	/**
	 * ���������������¼�
	 */
	class OptListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (lastStepOpt == 0) {
				//����ϴβ���Ϊ��ֵ����
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
			//ClearInputNumber();//��ʵ�ļ��������񲻻��������
			lastStepOpt = 1;
			nextStepClear = true;
			refreshLeftScreen();
		}
	}
	
	/**
	 * �ȺŰ����¼�
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
	 * �ٷֺŰ����¼�
	 * ����ǰ���뻺�����е���ֵ����100
	 * Ȼ���������
	 * ���д�����뻺����
	 */
	class PercentListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			writeToInputCache(getInputNumber().divide(new BigDecimal("100")).multiply(result));
			
			showInputNumberOnScreen();
			refreshLeftScreen();
		}
	}
}
