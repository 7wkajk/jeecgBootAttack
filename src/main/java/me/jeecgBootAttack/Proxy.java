/*
 * Created by JFormDesigner on Thu Jan 30 18:51:35 CST 2025
 */

package me.jeecgBootAttack;

import java.awt.event.*;
import com.formdev.flatlaf.FlatIntelliJLaf;

import java.awt.*;
import javax.swing.*;

/**
 * @author ycp
 */
public class Proxy extends JFrame {

    private static String HOST = null;
    private static Integer PORT = null;


    public static String getHOST() {
        return HOST;
    }


    public static int getPORT() {
        return PORT;
    }

    public Proxy() {
        initComponents();
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton1);
        group.add(radioButton2);
        textField1.setText("127.0.0.1");
        textField2.setText("8080");
        radioButton2.setSelected(true);
        setResizable(false);
    }

    private void save(ActionEvent e) {
        String host = textField1.getText();
        if (radioButton1.isSelected()){
            if (textField1.getText().isEmpty() || textField2.getText().isEmpty()){
                JOptionPane.showMessageDialog(this,"HOST和PORT不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            }
            else {
                int port = Integer.parseInt(textField2.getText());
                if (port < 1 || port > 65535) {
                    JOptionPane.showMessageDialog(this,"端口号无效！", "错误", JOptionPane.ERROR_MESSAGE);
                }else {
                    HOST = host;
                    PORT = port;
                    JOptionPane.showMessageDialog(this,"Start HTTP @ /"+HOST+":"+PORT+" ...", "Proxy", JOptionPane.NO_OPTION);
                }
            }

        }
        else if (radioButton2.isSelected()){
            HOST = null;
            PORT = null;
            JOptionPane.showMessageDialog(this,"禁用代理", "Proxy", JOptionPane.NO_OPTION);
        }
    }

    private void exit(ActionEvent e) {
        dispose();
    }

    public void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        radioButton1 = new JRadioButton();
        radioButton2 = new JRadioButton();
        label1 = new JLabel();
        textField1 = new JTextField();
        label2 = new JLabel();
        textField2 = new JTextField();
        button1 = new JButton();
        button2 = new JButton();

        //======== this ========
        setTitle("Setting HttpProxy");
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- radioButton1 ----
        radioButton1.setText("\u542f\u7528");
        contentPane.add(radioButton1);
        radioButton1.setBounds(new Rectangle(new Point(55, 30), radioButton1.getPreferredSize()));

        //---- radioButton2 ----
        radioButton2.setText("\u7981\u7528");
        contentPane.add(radioButton2);
        radioButton2.setBounds(135, 30, 47, 21);

        //---- label1 ----
        label1.setText("Host\uff1a");
        contentPane.add(label1);
        label1.setBounds(new Rectangle(new Point(25, 85), label1.getPreferredSize()));

        //---- textField1 ----
        textField1.setText("127.0.0.1");
        contentPane.add(textField1);
        textField1.setBounds(75, 80, 150, textField1.getPreferredSize().height);

        //---- label2 ----
        label2.setText("Port\uff1a");
        contentPane.add(label2);
        label2.setBounds(25, 130, 39, 17);

        //---- textField2 ----
        textField2.setText("8080");
        contentPane.add(textField2);
        textField2.setBounds(75, 125, 150, 27);

        //---- button1 ----
        button1.setText("\u4fdd\u5b58");
        button1.addActionListener(e -> save(e));
        contentPane.add(button1);
        button1.setBounds(55, 180, 60, button1.getPreferredSize().height);

        //---- button2 ----
        button2.setText("\u9000\u51fa");
        button2.addActionListener(e -> exit(e));
        contentPane.add(button2);
        button2.setBounds(135, 180, 60, 27);

        contentPane.setPreferredSize(new Dimension(250, 245));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JLabel label1;
    private JTextField textField1;
    private JLabel label2;
    private JTextField textField2;
    private JButton button1;
    private JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on


}
