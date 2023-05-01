package com.cicdez.modelsimpler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ModelSimplerFrame {

    public static final JFrame FRAME = new JFrame("Model Simpler");

    public static final JTextField FOLDER = SwingComponentManager.field(new Rectangle(5, 25,
            550, 20));
    public static final JTextField ID = SwingComponentManager.field(new Rectangle(75, 55,
            300, 20));

    public static final JRadioButton ITEM = SwingComponentManager.radioButton("Item",
            new Rectangle(5, 85, 75, 20)),
            BLOCK = SwingComponentManager.radioButton("Block",
                    new Rectangle(165, 85, 75, 20)),
            ADVANCED_BLOCK = SwingComponentManager.radioButton("Advanced Block",
                    new Rectangle(325, 85, 150, 20))
            ;

    public static final JComboBox<String> ITEM_TYPES = SwingComponentManager.comboBox(
            new Rectangle(ITEM.getX(), ITEM.getY() + ITEM.getHeight() + 5, 150, 20),
            ItemType.allDisplayNames()),
            BLOCK_TYPES = SwingComponentManager.comboBox(
                    new Rectangle(BLOCK.getX(), BLOCK.getY() + BLOCK.getHeight() + 5, 150, 20),
                    BlockType.allDisplayNames()),
            ADVANCED_BLOCK_TYPES = SwingComponentManager.comboBox(
                    new Rectangle(ADVANCED_BLOCK.getX(), ADVANCED_BLOCK.getY() +
                            ADVANCED_BLOCK.getHeight() + 5, 150, 20),
                    AdvancedBlockType.allDisplayNames())
            ;

    public static final ButtonGroup TYPES_GROUP = new ButtonGroup();

    public static final JButton GENERATE = SwingComponentManager.button("Generate",
            new Rectangle(5, ITEM_TYPES.getY() + ITEM_TYPES.getHeight() + 10, 100, 20));

    public static final JTextArea FEEDBACK = new JTextArea(3, 50);

    static {
        TYPES_GROUP.add(ITEM);
        TYPES_GROUP.add(BLOCK);
        TYPES_GROUP.add(ADVANCED_BLOCK);

        ITEM_TYPES.setEnabled(false);
        BLOCK_TYPES.setEnabled(false);
        ADVANCED_BLOCK_TYPES.setEnabled(false);
    }

    public static void init() {
        FRAME.setSize(600, 250);
        FRAME.setLocationRelativeTo(null);
        FRAME.setResizable(false);
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setLayout(null);
        loadComponents();
        FRAME.setVisible(true);
    }

    public static void loadComponents() {
        FRAME.add(SwingComponentManager.label("Path to Your Mod folder " +
                        "(You can use: /src, /main, /resources, /assets or /{modId} directories):",
                new Rectangle(5, 5, 550, 20)));
        FRAME.add(FOLDER);
        FRAME.add(SwingComponentManager.label("Object ID:",
                new Rectangle(5, 55, 100, 20)));
        FRAME.add(ID);
        FRAME.add(ITEM);
        FRAME.add(BLOCK);
        FRAME.add(ADVANCED_BLOCK);
        ITEM.addActionListener(e -> {
            if (ITEM.isSelected()) {
                ITEM_TYPES.setEnabled(true);
                BLOCK_TYPES.setEnabled(false);
                ADVANCED_BLOCK_TYPES.setEnabled(false);

                FRAME.remove(INCLUDES_HORIZONTAL_COLUMN);
                FRAME.remove(CROP_STAGES_LABEL);
                FRAME.remove(CROP_STAGES_SPINNER);
                FRAME.remove(INCLUDES_VERTICAL_ORIENTED);

                FRAME.remove(INCREASE_ADVANCED_BLOCK_ID);
                FRAME.remove(MAKE_FULL_ADVANCED_BLOCK_COMPLEX);
                FRAME.remove(USE_CUSTOM_ADVANCED_BLOCK_TEXTURE);
            }
            debug();
        });
        BLOCK.addActionListener(e -> {
            if (BLOCK.isSelected()) {
                ITEM_TYPES.setEnabled(false);
                BLOCK_TYPES.setEnabled(true);
                ADVANCED_BLOCK_TYPES.setEnabled(false);

                FRAME.remove(INCREASE_ADVANCED_BLOCK_ID);
                FRAME.remove(MAKE_FULL_ADVANCED_BLOCK_COMPLEX);
                FRAME.remove(USE_CUSTOM_ADVANCED_BLOCK_TEXTURE);
            }
            debug();
        });
        ADVANCED_BLOCK.addActionListener(e -> {
            if (ADVANCED_BLOCK.isSelected()) {
                ITEM_TYPES.setEnabled(false);
                BLOCK_TYPES.setEnabled(false);
                ADVANCED_BLOCK_TYPES.setEnabled(true);

                FRAME.remove(INCLUDES_HORIZONTAL_COLUMN);
                FRAME.remove(CROP_STAGES_LABEL);
                FRAME.remove(CROP_STAGES_SPINNER);
                FRAME.remove(INCLUDES_VERTICAL_ORIENTED);

                FRAME.add(INCREASE_ADVANCED_BLOCK_ID);
                FRAME.add(MAKE_FULL_ADVANCED_BLOCK_COMPLEX);
                FRAME.add(USE_CUSTOM_ADVANCED_BLOCK_TEXTURE);
            }
            debug();
        });
        listenBlockTypes();
        listenAdvancedBlockTypes();
        FRAME.add(ITEM_TYPES);
        FRAME.add(BLOCK_TYPES);
        FRAME.add(ADVANCED_BLOCK_TYPES);
        GENERATE.addActionListener(e -> {
            try {
                if (ITEM.isSelected()) {
                    ItemFileManager.startItemModel(FOLDER.getText(), ID.getText(),
                            ItemType.byId(ITEM_TYPES.getSelectedIndex()));
                }

                if (BLOCK.isSelected()) {
                    BlockType type = BlockType.byId(BLOCK_TYPES.getSelectedIndex());
                    if (type == BlockType.COLUMN) {
                        BlockFileManager.startColumnModel(FOLDER.getText(),
                                ID.getText(), INCLUDES_HORIZONTAL_COLUMN.isSelected());
                    } else if (type == BlockType.CROP) {
                        BlockFileManager.startCropModel(FOLDER.getText(), ID.getText(),
                                CROP_STAGES_SPINNER.getValue().hashCode());
                    } else if (type == BlockType.ORIENTABLE_BOTTOM) {
                        BlockFileManager.startOrientedModel(FOLDER.getText(), ID.getText(),
                                INCLUDES_VERTICAL_ORIENTED.isSelected());
                    } else {
                        BlockFileManager.startBlockModel(FOLDER.getText(), ID.getText(), type);
                    }
                }

                if (ADVANCED_BLOCK.isSelected()) {
                    if (MAKE_FULL_ADVANCED_BLOCK_COMPLEX.isSelected()) {
                        AdvancedBlockFileManager.startFullComplex(FOLDER.getText(), ID.getText(),
                                USE_CUSTOM_ADVANCED_BLOCK_TEXTURE.isSelected());
                    } else {
                        AdvancedBlockFileManager.startAdvancedBlockModel(FOLDER.getText(), ID.getText(),
                                AdvancedBlockType.byId(ADVANCED_BLOCK_TYPES.getSelectedIndex()),
                                INCREASE_ADVANCED_BLOCK_ID.isSelected(),
                                USE_CUSTOM_ADVANCED_BLOCK_TEXTURE.isSelected());
                    }
                }

                if (!ITEM.isSelected() && !BLOCK.isSelected() && !ADVANCED_BLOCK.isSelected())
                    throw new IOException("You don't select Type");

                FEEDBACK.setText("Success!");
                FEEDBACK.setForeground(Color.GREEN);
            } catch (IOException exception) {
                FEEDBACK.setText(exception.getClass().getSimpleName() + ": " + exception.getMessage());
                FEEDBACK.setForeground(Color.RED);
            }
        });
        FRAME.add(GENERATE);
        FEEDBACK.setBackground(FRAME.getBackground());
        FEEDBACK.setBounds(5, GENERATE.getY() + GENERATE.getHeight() + 5, 300, 40);
        FEEDBACK.setEditable(false);
        FRAME.add(FEEDBACK);
    }

    public static void debug() {
        FRAME.setSize(601, 250);
        FRAME.setSize(600, 250);
    }

    private static final JCheckBox INCLUDES_HORIZONTAL_COLUMN = SwingComponentManager
            .checkBox("Includes Horizontal Version?", new Rectangle(BLOCK_TYPES.getX(),
                    BLOCK_TYPES.getY() + BLOCK_TYPES.getHeight() + 5, 250, 20));
    private static final JCheckBox INCLUDES_VERTICAL_ORIENTED = SwingComponentManager
            .checkBox("Includes Vertical Oriented?", new Rectangle(BLOCK_TYPES.getX(),
                    BLOCK_TYPES.getY() + BLOCK_TYPES.getHeight() + 5, 250, 20));
    private static final JLabel CROP_STAGES_LABEL = SwingComponentManager.label("Stages: ",
            new Rectangle(BLOCK_TYPES.getX(), BLOCK_TYPES.getY() + BLOCK_TYPES.getHeight() + 5,
                    50, 20));
    private static final JSpinner CROP_STAGES_SPINNER = new JSpinner(new SpinnerNumberModel(6,
            0, 16, 1));

    public static void listenBlockTypes() {
        CROP_STAGES_SPINNER.setBounds(CROP_STAGES_LABEL.getX() + CROP_STAGES_LABEL.getWidth() + 5,
                CROP_STAGES_LABEL.getY(), 75, 20);

        BLOCK_TYPES.addActionListener(e -> {
            int index = BLOCK_TYPES.getSelectedIndex();

            FRAME.remove(INCLUDES_HORIZONTAL_COLUMN);
            FRAME.remove(CROP_STAGES_LABEL);
            FRAME.remove(CROP_STAGES_SPINNER);
            FRAME.remove(INCLUDES_VERTICAL_ORIENTED);
            FRAME.remove(INCREASE_ADVANCED_BLOCK_ID);
            FRAME.remove(MAKE_FULL_ADVANCED_BLOCK_COMPLEX);

            if (index == BlockType.COLUMN.ordinal()) {
                FRAME.add(INCLUDES_HORIZONTAL_COLUMN);
            } else if (index == BlockType.ORIENTABLE_BOTTOM.ordinal()) {
                FRAME.add(INCLUDES_VERTICAL_ORIENTED);
            } else if (index == BlockType.CROP.ordinal()) {
                FRAME.add(CROP_STAGES_LABEL);
                FRAME.add(CROP_STAGES_SPINNER);
            }

            debug();
        });
    }

    private static final JCheckBox INCREASE_ADVANCED_BLOCK_ID = SwingComponentManager.checkBox(
            "Increase Id?", new Rectangle(ADVANCED_BLOCK.getX(), ADVANCED_BLOCK_TYPES.getY() +
                    ADVANCED_BLOCK_TYPES.getHeight() + 5, 150, 20));
    private static final JCheckBox MAKE_FULL_ADVANCED_BLOCK_COMPLEX = SwingComponentManager.checkBox(
            "Make full Complex?", new Rectangle(ADVANCED_BLOCK.getX(),
                    INCREASE_ADVANCED_BLOCK_ID.getY() + INCREASE_ADVANCED_BLOCK_ID.getHeight() + 5,
                    150, 20));
    private static final JCheckBox USE_CUSTOM_ADVANCED_BLOCK_TEXTURE = SwingComponentManager.checkBox(
            "Use Custom Texture?", new Rectangle(ADVANCED_BLOCK.getX(),
                    MAKE_FULL_ADVANCED_BLOCK_COMPLEX.getY() +
                            MAKE_FULL_ADVANCED_BLOCK_COMPLEX.getHeight() + 5,
                    150, 20));
    public static void listenAdvancedBlockTypes() {
        USE_CUSTOM_ADVANCED_BLOCK_TEXTURE.setSelected(true);
        MAKE_FULL_ADVANCED_BLOCK_COMPLEX.addActionListener(e -> {
            if (MAKE_FULL_ADVANCED_BLOCK_COMPLEX.isSelected()) {
                INCREASE_ADVANCED_BLOCK_ID.setEnabled(false);
                INCREASE_ADVANCED_BLOCK_ID.setSelected(true);
                ADVANCED_BLOCK_TYPES.setEnabled(false);
            } else {
                INCREASE_ADVANCED_BLOCK_ID.setEnabled(true);
                ADVANCED_BLOCK_TYPES.setEnabled(true);
            }
            debug();
        });
    }
}
