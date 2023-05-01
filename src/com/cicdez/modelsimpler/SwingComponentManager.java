package com.cicdez.modelsimpler;

import javax.swing.*;
import java.awt.*;

public final class SwingComponentManager {
    private SwingComponentManager() {}

    public static JLabel label(String text, Rectangle bound) {
        JLabel label = new JLabel(text);
        label.setBounds(bound);
        return label;
    }
    public static JButton button(String text, Rectangle bound) {
        JButton button = new JButton(text);
        button.setBounds(bound);
        return button;
    }
    public static JTextField field(Rectangle bound) {
        JTextField field = new JTextField();
        field.setBounds(bound);
        return field;
    }
    public static JRadioButton radioButton(String text, Rectangle bound) {
        JRadioButton button = new JRadioButton(text);
        button.setBounds(bound);
        return button;
    }
    @SafeVarargs
    public static <R> JComboBox<R> comboBox(Rectangle bound, R... items) {
        JComboBox<R> box = new JComboBox<>(items);
        box.setBounds(bound);
        return box;
    }
    public static JCheckBox checkBox(String text, Rectangle bound) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setBounds(bound);
        return checkBox;
    }
}
