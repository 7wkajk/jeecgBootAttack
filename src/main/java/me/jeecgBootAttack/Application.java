package me.jeecgBootAttack;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("all")
public class Application {
    public static void main(String[] args) throws Exception {
        // 使用 flatlaf 的皮肤
        FlatIntelliJLaf.setup();
//        IntelliJTheme.setup(Application.class.getResourceAsStream("/solarized_light_theme.theme.json"));
//        按钮和切换按钮的角弧直径(默认为6)
//        UIManager.put( "Button.arc", 5 );
//        文本字段的角弧直径(默认为6)
        UIManager.put("TextComponent.arc",5);
//        箭头的箭头类型，chevron：人字形，triangle：三角形
        UIManager.put("Component.arrowType","triangle");
//        外焦距边框宽度
        UIManager.put("Component.focusWidth",1);
//        内焦点边框宽度
        UIManager.put("Component.innerFocusWidth",0);
//        显示助记符
        UIManager.put("Component.hideMnemonics",false);
//        是否显示滚动条的按钮
        UIManager.put("ScrollBar.showButtons",false);
//        设置滚动条的宽度
        UIManager.put("ScrollBar.width",10);
//        在选项卡之间显示分隔线
        UIManager.put("TabbedPane.showTabSeparators",true);
//        选项卡之间显示分隔线设置为全高
        UIManager.put("TabbedPane.tabSeparatorsFullHeight",true);
//        设置所选标签背景
        UIManager.put("TabbedPane.selectedBackground", Color.white);
        new jeecgBootAttack().start();
    }
}
