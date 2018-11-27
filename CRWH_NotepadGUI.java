/*
*By: Caesar R. Watts-Hall
*Class: JAVA 1
*Instructor: Dr.Primo
*Assignment: Notepad GUI
*Date: 11/15/2018
*Due: 11/15/2018 @11:55PM
*/

//START
package crwh_notepadgui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.undo.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.datatransfer.*;

public class CRWH_NotepadGUI extends JFrame implements ActionListener, DocumentListener {

    JMenu crwhFileMenu, crwhEditMenu, crwhFormatMenu, crwhHelpMenu;
    JPopupMenu CRWHMenu;

    JMenuItem crwhMenu_Undo, crwhMenu_Cut, crwhMenu_Copy, crwhMenu_Paste,
              crwhMenu_Delete, crwhMenu_SelectAll;

    JMenuItem crwhFMenu_New, crwhFMenu_Open, crwhFMenu_Save, crwhFMenu_SaveAs,
              crwhFMenu_Exit;

    JMenuItem crwhEMenu_Undo, crwhEMenu_Cut, crwhEMenu_Copy, crwhEMenu_Paste,
              crwhEMenu_Delete, crwhEMenu_Find, crwhEMenu_FindNext, crwhEMenu_Replace,
              crwhEMenu_GoTo, crwhEMenu_SelectAll, crwhEMenu_TimeDate;

    JCheckBoxMenuItem crwhFMTMenu_Encase;
    JMenuItem crwhFMTMenu_Font;
    JMenuItem crwhHPMenu_HelpForm, crwhHPMenu_About;
    JFrame crwhFrame;
    JTextArea crwhCentralArea;
    JLabel crwhJLabel;
    JLabel crwhJBar;
    Toolkit crwhTools = Toolkit.getDefaultToolkit();
    Clipboard crwhClips = crwhTools.getSystemClipboard();
    protected UndoManager crwhUndo = new UndoManager();
    protected UndoableEditListener crwhUndoForm = new UndoHandler();
    String crwhOldValues;
    boolean crwhNewFile = true;
    File crwhCurrentState;

    public CRWH_NotepadGUI() { //The Beginning
        super("Untitled - CRWH Notepad");
        Font font = new Font("Dialog", Font.PLAIN, 12);
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }

        //Menu Captions
        JMenuBar crwhMenuBar = new JMenuBar();
        crwhFileMenu = new JMenu("File");
        crwhFMenu_New = new JMenuItem("New");
        crwhFMenu_New.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        crwhFMenu_New.addActionListener(this);

        crwhFMenu_Open = new JMenuItem("Open");
        crwhFMenu_Open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        crwhFMenu_Open.addActionListener(this);

        crwhFMenu_Save = new JMenuItem("Save");
        crwhFMenu_Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        crwhFMenu_Save.addActionListener(this);

        crwhFMenu_SaveAs = new JMenuItem("Save As...");
        crwhFMenu_SaveAs.addActionListener(this);

        crwhFMenu_Exit = new JMenuItem("Exit");
        crwhFMenu_Exit.addActionListener(this);

        crwhEditMenu = new JMenu("Edit");
        crwhEditMenu.addMenuListener(new MenuListener() {
            public void menuCanceled(MenuEvent e) {
                checkMenuItemEnabled();
            }

            public void menuDeselected(MenuEvent e) {
                checkMenuItemEnabled();
            }

            public void menuSelected(MenuEvent e) {
                checkMenuItemEnabled();
            }
        });

        crwhEMenu_Undo = new JMenuItem("Undo");
        crwhEMenu_Undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        crwhEMenu_Undo.addActionListener(this);
        crwhEMenu_Undo.setEnabled(false);

        crwhEMenu_Cut = new JMenuItem("Cut");
        crwhEMenu_Cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        crwhEMenu_Cut.addActionListener(this);

        crwhEMenu_Copy = new JMenuItem("Copy");
        crwhEMenu_Copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        crwhEMenu_Copy.addActionListener(this);

        crwhEMenu_Paste = new JMenuItem("Paste");
        crwhEMenu_Paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        crwhEMenu_Paste.addActionListener(this);

        crwhEMenu_Delete = new JMenuItem("Delete");
        crwhEMenu_Delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        crwhEMenu_Delete.addActionListener(this);

        crwhEMenu_Find = new JMenuItem("Find:");
        crwhEMenu_Find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        crwhEMenu_Find.addActionListener(this);

        crwhEMenu_FindNext = new JMenuItem("Find Next:");
        crwhEMenu_FindNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        crwhEMenu_FindNext.addActionListener(this);

        crwhEMenu_Replace = new JMenuItem("Replace");
        crwhEMenu_Replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
        crwhEMenu_Replace.addActionListener(this);

        crwhEMenu_GoTo = new JMenuItem("Go To:");
        crwhEMenu_GoTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
        crwhEMenu_GoTo.addActionListener(this);

        crwhEMenu_SelectAll = new JMenuItem("Select All");
        crwhEMenu_SelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        crwhEMenu_SelectAll.addActionListener(this);

        crwhEMenu_TimeDate = new JMenuItem("Date|Time");
        crwhEMenu_TimeDate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        crwhEMenu_TimeDate.addActionListener(this);

        crwhFormatMenu = new JMenu("Format");
        crwhFMTMenu_Encase = new JCheckBoxMenuItem("Word Wrap:");
        crwhFMTMenu_Encase.setState(true);
        crwhFMTMenu_Encase.addActionListener(this);

        crwhFMTMenu_Font = new JMenuItem("Font:");
        crwhFMTMenu_Font.addActionListener(this);

        crwhHelpMenu = new JMenu("Help");

        crwhHPMenu_HelpForm = new JMenuItem("View:");
        crwhHPMenu_HelpForm.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        crwhHPMenu_HelpForm.addActionListener(this);

        crwhHPMenu_About = new JMenuItem("About My Notepad:");
        crwhHPMenu_About.addActionListener(this);

        crwhMenuBar.add(crwhFileMenu);
        crwhFileMenu.add(crwhFMenu_New);
        crwhFileMenu.add(crwhFMenu_Open);
        crwhFileMenu.add(crwhFMenu_Save);
        crwhFileMenu.add(crwhFMenu_SaveAs);
        crwhFileMenu.addSeparator();
        crwhFileMenu.add(crwhFMenu_Exit);

        crwhMenuBar.add(crwhEditMenu);
        crwhEditMenu.add(crwhEMenu_Undo);
        crwhEditMenu.addSeparator();
        crwhEditMenu.add(crwhEMenu_Cut);
        crwhEditMenu.add(crwhEMenu_Copy);
        crwhEditMenu.add(crwhEMenu_Paste);
        crwhEditMenu.add(crwhEMenu_Delete);
        crwhEditMenu.addSeparator();
        crwhEditMenu.add(crwhEMenu_Find);
        crwhEditMenu.add(crwhEMenu_FindNext);
        crwhEditMenu.add(crwhEMenu_Replace);
        crwhEditMenu.add(crwhEMenu_GoTo);
        crwhEditMenu.addSeparator();
        crwhEditMenu.add(crwhEMenu_SelectAll);
        crwhEditMenu.add(crwhEMenu_TimeDate);

        crwhMenuBar.add(crwhFormatMenu);
        crwhFormatMenu.add(crwhFMTMenu_Encase);
        crwhFormatMenu.addSeparator();
        crwhFormatMenu.add(crwhFMTMenu_Font);

        crwhMenuBar.add(crwhHelpMenu);
        crwhHelpMenu.add(crwhHPMenu_HelpForm);
        crwhHelpMenu.addSeparator();
        crwhHelpMenu.add(crwhHPMenu_About);
        this.setJMenuBar(crwhMenuBar);

        //The Central Editor [JTextArea]
        crwhCentralArea = new JTextArea(20, 50);
        JScrollPane scroller = new JScrollPane(crwhCentralArea);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scroller, BorderLayout.CENTER);
        crwhCentralArea.setWrapStyleWord(true);
        crwhCentralArea.setLineWrap(true);
        crwhOldValues = crwhCentralArea.getText();

        crwhCentralArea.getDocument().addUndoableEditListener(crwhUndoForm);
        crwhCentralArea.getDocument().addDocumentListener(this);

        CRWHMenu = new JPopupMenu();
        crwhMenu_Undo = new JMenuItem("Undo");
        crwhMenu_Cut = new JMenuItem("Cut");
        crwhMenu_Copy = new JMenuItem("Copy");
        crwhMenu_Paste = new JMenuItem("Paste");
        crwhMenu_Delete = new JMenuItem("Delete");
        crwhMenu_SelectAll = new JMenuItem("Select All");

        crwhMenu_Undo.setEnabled(false);

        CRWHMenu.add(crwhMenu_Undo);
        CRWHMenu.addSeparator();
        CRWHMenu.add(crwhMenu_Cut);
        CRWHMenu.add(crwhMenu_Copy);
        CRWHMenu.add(crwhMenu_Paste);
        CRWHMenu.add(crwhMenu_Delete);
        CRWHMenu.addSeparator();
        CRWHMenu.add(crwhMenu_SelectAll);

        crwhMenu_Undo.addActionListener(this);
        crwhMenu_Cut.addActionListener(this);
        crwhMenu_Copy.addActionListener(this);
        crwhMenu_Paste.addActionListener(this);
        crwhMenu_Delete.addActionListener(this);
        crwhMenu_SelectAll.addActionListener(this);

        crwhCentralArea.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    CRWHMenu.show(e.getComponent(), e.getX(), e.getY());
                }
                checkMenuItemEnabled();
                crwhCentralArea.requestFocus();
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    CRWHMenu.show(e.getComponent(), e.getX(), e.getY());
                }
                checkMenuItemEnabled();
                crwhCentralArea.requestFocus();
            }
        });

        crwhJLabel = new JLabel(" ");
        this.add(crwhJLabel, BorderLayout.SOUTH);
        crwhJBar = new JLabel("||       Ln 1, Col 1  ", JLabel.RIGHT);
        this.add(new JScrollPane(crwhCentralArea), BorderLayout.CENTER);
        this.add(crwhJBar, BorderLayout.SOUTH);
        this.add(new JLabel("  "), BorderLayout.EAST);
        this.add(new JLabel("  "), BorderLayout.WEST);

        crwhCentralArea.addCaretListener(
                new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                int lineNumber = 0, column = 0, pos = 0;

                try {
                    pos = crwhCentralArea.getCaretPosition();
                    lineNumber = crwhCentralArea.getLineOfOffset(pos);
                    column = pos - crwhCentralArea.getLineStartOffset(lineNumber);
                } catch (Exception excp) {
                }
                if (crwhCentralArea.getText().length() == 0) {
                    lineNumber = 0;
                    column = 0;
                }
                crwhJBar.setText("||       Ln " + (lineNumber + 1) + ", Col " + (column + 1));
            }
        });
        this.setLocation(100, 100);
        this.setSize(650, 550);
        this.setVisible(true);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                exitWindowChoose();
            }
        });

        checkMenuItemEnabled();
        crwhCentralArea.requestFocus();
    }

    public void checkMenuItemEnabled() //Determines if the items in [EDIT] are to be properly handled:
    //via Cut, Copy, and Delete
    {
        String selectText = crwhCentralArea.getSelectedText();
        if (selectText == null) {
            crwhEMenu_Cut.setEnabled(false);
            crwhMenu_Cut.setEnabled(false);
            crwhEMenu_Copy.setEnabled(false);
            crwhMenu_Copy.setEnabled(false);
            crwhEMenu_Delete.setEnabled(false);
            crwhMenu_Delete.setEnabled(false);
        } else {
            crwhEMenu_Cut.setEnabled(true);
            crwhMenu_Cut.setEnabled(true);
            crwhEMenu_Copy.setEnabled(true);
            crwhMenu_Copy.setEnabled(true);
            crwhEMenu_Delete.setEnabled(true);
            crwhMenu_Delete.setEnabled(true);
        }

        Transferable contents = crwhClips.getContents(this);
        if (contents == null) {
            crwhEMenu_Paste.setEnabled(false);
            crwhMenu_Paste.setEnabled(false);
        } else {
            crwhEMenu_Paste.setEnabled(true);
            crwhMenu_Paste.setEnabled(true);
        }
    }

    public void exitWindowChoose() //Before closing the application
    {
        crwhCentralArea.requestFocus();
        String currentValue = crwhCentralArea.getText();
        if (currentValue.equals(crwhOldValues) == true) {
            System.exit(0);
        } else {
            int exitChoose = JOptionPane.showConfirmDialog(this,
                    "Your file has not been saved, do you want to save it?",
                    "Exit prompt", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (exitChoose) {
                case JOptionPane.YES_OPTION:
                    //boolean isSave=false;
                    if (crwhNewFile) {
                        String str = null;
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fileChooser.setApproveButtonText("Are you positive?");
                        fileChooser.setDialogTitle("Save As");

                        int result = fileChooser.showSaveDialog(this);

                        if (result == JFileChooser.CANCEL_OPTION) {
                            crwhJLabel.setText("You didn't save the file");
                            return;
                        }

                        File saveFileName = fileChooser.getSelectedFile();

                        if (saveFileName == null || saveFileName.getName().equals("")) {
                            JOptionPane.showMessageDialog(this,
                                    "Illegal file name", "Illegal file name",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            try {
                                FileWriter fw = new FileWriter(saveFileName);
                                BufferedWriter bfw = new BufferedWriter(fw);
                                bfw.write(crwhCentralArea.getText(), 0,
                                        crwhCentralArea.getText().length());
                                bfw.flush();
                                fw.close();

                                crwhNewFile = false;
                                crwhCurrentState = saveFileName;
                                crwhOldValues = crwhCentralArea.getText();

                                this.setTitle(saveFileName.getName() + " - CRWH NotePad");
                                crwhJLabel.setText("Currently open file:"
                                        + saveFileName.getAbsoluteFile());
                                //isSave=true;
                            } catch (IOException ioException) {
                            }
                        }
                    } else {
                        try {
                            FileWriter fw = new FileWriter(crwhCurrentState);
                            BufferedWriter bfw = new BufferedWriter(fw);
                            bfw.write(crwhCentralArea.getText(), 0,
                                    crwhCentralArea.getText().length());
                            bfw.flush();
                            fw.close();
                            //isSave=true;
                        } catch (IOException ioException) {
                        }
                    }
                    System.exit(0);
                case JOptionPane.NO_OPTION:
                    System.exit(0);
                default:
            }
        }
    }

    public void find() //Finding what the user, wants to find.
    {
        final JDialog findDialog = new JDialog(this, "Find", false);
        Container con = findDialog.getContentPane();
        con.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel findContentLabel = new JLabel("Find What：");
        final JTextField findText = new JTextField(15);
        JButton findNextButton = new JButton("Find the next：");
        final JCheckBox matchCheckBox = new JCheckBox("Case sensitive");
        ButtonGroup bGroup = new ButtonGroup();
        final JRadioButton upButton = new JRadioButton("Up");
        final JRadioButton downButton = new JRadioButton("Down");
        downButton.setSelected(true);
        bGroup.add(upButton);
        bGroup.add(downButton);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findDialog.dispose();
            }
        });

        findNextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int k = 0, m = 0;
                final String str1, str2, str3, str4, strA, strB;
                str1 = crwhCentralArea.getText();
                str2 = findText.getText();
                str3 = str1.toUpperCase();
                str4 = str2.toUpperCase();
                if (matchCheckBox.isSelected()) {
                    strA = str1;
                    strB = str2;
                } else {
                    strA = str3;
                    strB = str4;
                }
                if (upButton.isSelected()) {
                    if (crwhCentralArea.getSelectedText() == null) {
                        k = strA.lastIndexOf(strB, crwhCentralArea.getCaretPosition() - 1);
                    } else {
                        k = strA.lastIndexOf(strB, crwhCentralArea.getCaretPosition() - findText.getText().length() - 1);
                    }
                    if (k > -1) {
                        crwhCentralArea.setCaretPosition(k);
                        crwhCentralArea.select(k, k + strB.length());
                    } else {
                        JOptionPane.showMessageDialog(null, "Undetectable", "Search", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (downButton.isSelected()) {
                    if (crwhCentralArea.getSelectedText() == null) {
                        k = strA.indexOf(strB, crwhCentralArea.getCaretPosition() + 1);
                    } else {
                        k = strA.indexOf(strB, crwhCentralArea.getCaretPosition() - findText.getText().length() + 1);
                    }
                    if (k > -1) {
                        crwhCentralArea.setCaretPosition(k);
                        crwhCentralArea.select(k, k + strB.length());
                    } else {
                        JOptionPane.showMessageDialog(null, "Undetectable", "Search", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel directionPanel = new JPanel();
        directionPanel.setBorder(BorderFactory.createTitledBorder("Direction"));
        directionPanel.add(upButton);
        directionPanel.add(downButton);
        panel1.setLayout(new GridLayout(2, 1));
        panel1.add(findNextButton);
        panel1.add(cancel);
        panel2.add(findContentLabel);
        panel2.add(findText);
        panel2.add(panel1);
        panel3.add(matchCheckBox);
        panel3.add(directionPanel);
        con.add(panel2);
        con.add(panel3);
        findDialog.setSize(410, 180);
        findDialog.setResizable(false);
        findDialog.setLocation(230, 280);
        findDialog.setVisible(true);
    }

    public void replace() {
        final JDialog replaceDialog = new JDialog(this, "Replace", false);
        Container con = replaceDialog.getContentPane();
        con.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel findContentLabel = new JLabel("Find content：");
        final JTextField findText = new JTextField(15);
        JButton findNextButton = new JButton("Find the next:");
        JLabel replaceLabel = new JLabel("Replaced by：");
        final JTextField replaceText = new JTextField(15);
        JButton replaceButton = new JButton("Replace");
        JButton replaceAllButton = new JButton("Replace All");
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                replaceDialog.dispose();
            }
        });
        final JCheckBox matchCheckBox = new JCheckBox("Case sensitive");
        ButtonGroup bGroup = new ButtonGroup();
        final JRadioButton upButton = new JRadioButton("Up");
        final JRadioButton downButton = new JRadioButton("Down");
        downButton.setSelected(true);
        bGroup.add(upButton);
        bGroup.add(downButton);

        findNextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int k = 0, m = 0;
                final String str1, str2, str3, str4, strA, strB;
                str1 = crwhCentralArea.getText();
                str2 = findText.getText();
                str3 = str1.toUpperCase();
                str4 = str2.toUpperCase();
                if (matchCheckBox.isSelected()) {
                    strA = str1;
                    strB = str2;
                } else {
                    strA = str3;
                    strB = str4;
                }
                if (upButton.isSelected()) {
                    if (crwhCentralArea.getSelectedText() == null) {
                        k = strA.lastIndexOf(strB, crwhCentralArea.getCaretPosition() - 1);
                    } else {
                        k = strA.lastIndexOf(strB, crwhCentralArea.getCaretPosition() - findText.getText().length() - 1);
                    }
                    if (k > -1) {
                        crwhCentralArea.setCaretPosition(k);
                        crwhCentralArea.select(k, k + strB.length());
                    } else {
                        JOptionPane.showMessageDialog(null, "Undetectable", "Search", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (downButton.isSelected()) {
                    if (crwhCentralArea.getSelectedText() == null) {
                        k = strA.indexOf(strB, crwhCentralArea.getCaretPosition() + 1);
                    } else {
                        k = strA.indexOf(strB, crwhCentralArea.getCaretPosition() - findText.getText().length() + 1);
                    }
                    if (k > -1) {
                        crwhCentralArea.setCaretPosition(k);
                        crwhCentralArea.select(k, k + strB.length());
                    } else {
                        JOptionPane.showMessageDialog(null, "Undetectable", "Search", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        replaceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (replaceText.getText().length() == 0 && crwhCentralArea.getSelectedText() != null) {
                    crwhCentralArea.replaceSelection("");
                }
                if (replaceText.getText().length() > 0 && crwhCentralArea.getSelectedText() != null) {
                    crwhCentralArea.replaceSelection(replaceText.getText());
                }
            }
        });

        replaceAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                crwhCentralArea.setCaretPosition(0);
                int k = 0, m = 0, replaceCount = 0;
                if (findText.getText().length() == 0) {
                    JOptionPane.showMessageDialog(replaceDialog, "Please fill in 'Find' content",
                            "Prompt", JOptionPane.WARNING_MESSAGE);
                    findText.requestFocus(true);
                    return;
                }
                while (k > -1) {
                    final String str1, str2, str3, str4, strA, strB;
                    str1 = crwhCentralArea.getText();
                    str2 = findText.getText();
                    str3 = str1.toUpperCase();
                    str4 = str2.toUpperCase();
                    if (matchCheckBox.isSelected()) {
                        strA = str1;
                        strB = str2;
                    } else {
                        strA = str3;
                        strB = str4;
                    }
                    if (upButton.isSelected()) {
                        if (crwhCentralArea.getSelectedText() == null) {
                            k = strA.lastIndexOf(strB, crwhCentralArea.getCaretPosition() - 1);
                        } else {
                            k = strA.lastIndexOf(strB, crwhCentralArea.getCaretPosition() - findText.getText().length() - 1);
                        }
                        if (k > -1) {
                            crwhCentralArea.setCaretPosition(k);
                            crwhCentralArea.select(k, k + strB.length());
                        } else {
                            if (replaceCount == 0) {
                                JOptionPane.showMessageDialog(replaceDialog,
                                        "My Apologies, but I cannot find what you are looking for",
                                        "CRWH Notepad", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(replaceDialog, "Replacement Successful! "
                                        + "Matches found: " + "( " + replaceCount + " )",
                                        "Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    } else if (downButton.isSelected()) {
                        if (crwhCentralArea.getSelectedText() == null) {
                            k = strA.indexOf(strB, crwhCentralArea.getCaretPosition() + 1);
                        } else {
                            k = strA.indexOf(strB, crwhCentralArea.getCaretPosition()
                                    - findText.getText().length() + 1);
                        }
                        if (k > -1) {
                            crwhCentralArea.setCaretPosition(k);
                            crwhCentralArea.select(k, k + strB.length());
                        } else {
                            if (replaceCount == 0) {
                                JOptionPane.showMessageDialog(replaceDialog,
                                        "My Apologies, but I cannot find what you are looking for",
                                        "CRWH Notepad", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(replaceDialog, "Replacement Successful! "
                                        + "Matches found: " + "( " + replaceCount + " )",
                                        "Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                    if (replaceText.getText().length() == 0 && crwhCentralArea.getSelectedText() != null) {
                        crwhCentralArea.replaceSelection("");
                        replaceCount++;
                    }

                    if (replaceText.getText().length() > 0 && crwhCentralArea.getSelectedText() != null) {
                        crwhCentralArea.replaceSelection(replaceText.getText());
                        replaceCount++;
                    }
                }
            }
        });

        JPanel directionPanel = new JPanel();
        directionPanel.setBorder(BorderFactory.createTitledBorder("Direction"));
        directionPanel.add(upButton);
        directionPanel.add(downButton);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayout(2, 1));
        panel1.add(findContentLabel);
        panel1.add(findText);
        panel1.add(findNextButton);
        panel4.add(replaceButton);
        panel4.add(replaceAllButton);
        panel2.add(replaceLabel);
        panel2.add(replaceText);
        panel2.add(panel4);
        panel3.add(matchCheckBox);
        panel3.add(directionPanel);
        panel3.add(cancel);
        con.add(panel1);
        con.add(panel2);
        con.add(panel3);
        replaceDialog.setSize(420, 220);
        replaceDialog.setResizable(false);
        replaceDialog.setLocation(230, 280);
        replaceDialog.setVisible(true);
    }

    void GoTo() {
        {
            int lineNumber = 0;
            try {
                lineNumber = crwhCentralArea.getLineOfOffset(crwhCentralArea.getCaretPosition()) + 1;
                String tempStr = JOptionPane.showInputDialog(crwhFrame, "Enter Line Number:", "" + lineNumber);
                if (tempStr == null) {
                    return;
                }
                lineNumber = Integer.parseInt(tempStr);
                crwhCentralArea.setCaretPosition(crwhCentralArea.getLineStartOffset(lineNumber - 1));
            } catch (Exception e) {
            }
        }
    }

    public void font() {
        final JDialog fontDialog = new JDialog(this, "Font-Settings", false);
        Container con = fontDialog.getContentPane();
        con.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel fontLabel = new JLabel("Font：");
        fontLabel.setPreferredSize(new Dimension(100, 20));
        JLabel styleLabel = new JLabel("Glyph：");
        styleLabel.setPreferredSize(new Dimension(100, 20));
        JLabel sizeLabel = new JLabel("Size：");
        sizeLabel.setPreferredSize(new Dimension(100, 20));
        final JLabel sample = new JLabel("Caesar R. Watts-Hall Notepad");
        final JTextField fontText = new JTextField(9);
        fontText.setPreferredSize(new Dimension(200, 20));
        final JTextField styleText = new JTextField(8);
        styleText.setPreferredSize(new Dimension(200, 20));
        final int style[] = {Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD + Font.ITALIC};
        final JTextField sizeText = new JTextField(5);
        sizeText.setPreferredSize(new Dimension(200, 20));
        JButton okButton = new JButton("Positive?");
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fontDialog.dispose();
            }
        });
        Font currentFont = crwhCentralArea.getFont();
        fontText.setText(currentFont.getFontName());
        fontText.selectAll();

        switch (currentFont.getStyle()) {
            case Font.PLAIN:
                styleText.setText("Conventional");
                break;
            case Font.BOLD:
                styleText.setText("Bold");
                break;
            case Font.ITALIC:
                styleText.setText("Italic");
                break;
            case Font.BOLD + Font.ITALIC:
                styleText.setText("Bold Italic");
                break;
            default:
                break;
        }
        styleText.selectAll();
        String str = String.valueOf(currentFont.getSize());
        sizeText.setText(str);
        sizeText.selectAll();
        final JList fontList, styleList, sizeList;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final String fontName[] = ge.getAvailableFontFamilyNames();
        fontList = new JList(fontName);
        fontList.setFixedCellWidth(86);
        fontList.setFixedCellHeight(20);
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final String fontStyle[] = {"Conventional", "Bold", "Italic", "Bold Italic"};
        styleList = new JList(fontStyle);
        styleList.setFixedCellWidth(86);
        styleList.setFixedCellHeight(20);
        styleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        switch (currentFont.getStyle()) {
            case Font.PLAIN:
                styleList.setSelectedIndex(0);
                break;
            case Font.BOLD:
                styleList.setSelectedIndex(1);
                break;
            case Font.ITALIC:
                styleList.setSelectedIndex(2);
                break;
            case Font.BOLD + Font.ITALIC:
                styleList.setSelectedIndex(3);
                break;
            default:
                break;
        }
        final String fontSize[] = {"8", "9", "10", "11", "12", "14", "16",
            "18", "20", "22", "24", "26", "28", "36",
            "48", "72"};
        sizeList = new JList(fontSize);
        sizeList.setFixedCellWidth(43);
        sizeList.setFixedCellHeight(20);
        sizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                fontText.setText(fontName[fontList.getSelectedIndex()]);
                fontText.selectAll();
                Font sampleFont1 = new Font(fontText.getText(),
                        style[styleList.getSelectedIndex()],
                        Integer.parseInt(sizeText.getText()));
                sample.setFont(sampleFont1);
            }
        });

        styleList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                int s = style[styleList.getSelectedIndex()];
                styleText.setText(fontStyle[s]);
                styleText.selectAll();
                Font sampleFont2 = new Font(fontText.getText(),
                        style[styleList.getSelectedIndex()],
                        Integer.parseInt(sizeText.getText()));
                sample.setFont(sampleFont2);
            }
        });

        sizeList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                sizeText.setText(fontSize[sizeList.getSelectedIndex()]);
                //sizeText.requestFocus();
                sizeText.selectAll();
                Font sampleFont3 = new Font(fontText.getText(),
                        style[styleList.getSelectedIndex()],
                        Integer.parseInt(sizeText.getText()));
                sample.setFont(sampleFont3);
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Font okFont = new Font(fontText.getText(),
                        style[styleList.getSelectedIndex()],
                        Integer.parseInt(sizeText.getText()));
                crwhCentralArea.setFont(okFont);
                fontDialog.dispose();
            }
        });
        JPanel samplePanel = new JPanel();
        samplePanel.setBorder(BorderFactory.createTitledBorder("Example"));
        samplePanel.add(sample);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        panel2.add(fontText);
        panel2.add(styleText);
        panel2.add(sizeText);
        panel2.add(okButton);
        panel3.add(new JScrollPane(fontList));
        panel3.add(new JScrollPane(styleList));
        panel3.add(new JScrollPane(sizeList));
        panel3.add(cancel);
        con.add(panel1);
        con.add(panel2);
        con.add(panel3);
        con.add(samplePanel);
        fontDialog.setSize(350, 340);
        fontDialog.setLocation(200, 200);
        fontDialog.setResizable(false);
        fontDialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == crwhFMenu_New) {
            crwhCentralArea.requestFocus();
            String currentValue = crwhCentralArea.getText();
            boolean isTextChange = !(currentValue.equals(crwhOldValues));
            if (isTextChange) {
                int saveChoose = JOptionPane.showConfirmDialog(this,
                        "Your file is not saved, would you like to save?",
                        "Tips", JOptionPane.YES_NO_CANCEL_OPTION);
                switch (saveChoose) {
                    case JOptionPane.YES_OPTION:
                        String str = null;
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fileChooser.setDialogTitle("Save As");
                        int result = fileChooser.showSaveDialog(this);
                        if (result == JFileChooser.CANCEL_OPTION) {
                            crwhJLabel.setText("You did not select any files");
                            return;
                        }
                        File saveFileName = fileChooser.getSelectedFile();
                        if (saveFileName == null || saveFileName.getName().equals("")) {
                            JOptionPane.showMessageDialog(this,
                                    "Illegal file name", "Illegal file name",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            try {
                                FileWriter fw = new FileWriter(saveFileName);
                                BufferedWriter bfw = new BufferedWriter(fw);
                                bfw.write(crwhCentralArea.getText(), 0,
                                        crwhCentralArea.getText().length());
                                bfw.flush();
                                bfw.close();
                                crwhNewFile = false;
                                crwhCurrentState = saveFileName;
                                crwhOldValues = crwhCentralArea.getText();
                                this.setTitle(saveFileName.getName() + " - CRWH Notepad");
                                crwhJLabel.setText("Currently open File："
                                        + saveFileName.getAbsoluteFile());
                            } catch (IOException ioException) {
                            }
                        }
                        break;
                    case JOptionPane.NO_OPTION:
                        crwhCentralArea.replaceRange("", 0,
                                crwhCentralArea.getText().length());
                        crwhJLabel.setText("New file");
                        this.setTitle("Untitled - CRWH Notepad");
                        crwhNewFile = true;
                        crwhUndo.discardAllEdits();
                        crwhEMenu_Undo.setEnabled(false);
                        crwhOldValues = crwhCentralArea.getText();
                        break;
                    case JOptionPane.CANCEL_OPTION:
                    default:
                        break;
                }
            } else {
                crwhCentralArea.replaceRange("", 0,
                        crwhCentralArea.getText().length());
                crwhJLabel.setText("New file");
                this.setTitle("Untitled - CRWH Notepad");
                crwhNewFile = true;
                crwhUndo.discardAllEdits();
                crwhEMenu_Undo.setEnabled(false);
                crwhOldValues = crwhCentralArea.getText();
            }
        } else if (e.getSource() == crwhFMenu_Open) {
            crwhCentralArea.requestFocus();
            String currentValue = crwhCentralArea.getText();
            boolean isTextChange = !(currentValue.equals(crwhOldValues));
            if (isTextChange) {
                int saveChoose = JOptionPane.showConfirmDialog(this,
                        "Your file has not been saved, would you like to save it?",
                        "Tips", JOptionPane.YES_NO_CANCEL_OPTION);
                switch (saveChoose) {
                    case JOptionPane.YES_OPTION: {
                        String str = null;
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fileChooser.setDialogTitle("Save As");
                        int result = fileChooser.showSaveDialog(this);
                        if (result == JFileChooser.CANCEL_OPTION) {
                            crwhJLabel.setText("No files were selected");
                            return;
                        }
                        File saveFileName = fileChooser.getSelectedFile();
                        if (saveFileName == null || saveFileName.getName().equals("")) {
                            JOptionPane.showMessageDialog(this,
                                    "Illegal file name", "Illegal file name",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            try {
                                FileWriter fw = new FileWriter(saveFileName);
                                BufferedWriter bfw = new BufferedWriter(fw);
                                bfw.write(crwhCentralArea.getText(), 0,
                                        crwhCentralArea.getText().length());
                                bfw.flush();
                                bfw.close();
                                crwhNewFile = false;
                                crwhCurrentState = saveFileName;
                                crwhOldValues = crwhCentralArea.getText();
                                this.setTitle(saveFileName.getName() + " - CRWH Notepad");
                                crwhJLabel.setText("Currently open File："
                                        + saveFileName.getAbsoluteFile());
                            } catch (IOException ioException) {
                            }
                        }
                        break;
                    }
                    case JOptionPane.NO_OPTION: {
                        String str = null;
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fileChooser.setDialogTitle("Open File");
                        int result = fileChooser.showOpenDialog(this);
                        if (result == JFileChooser.CANCEL_OPTION) {
                            crwhJLabel.setText("No files were selected");
                            return;
                        }
                        File fileName = fileChooser.getSelectedFile();
                        if (fileName == null || fileName.getName().equals("")) {
                            JOptionPane.showMessageDialog(this,
                                    "Illegal file name", "Illegal file name",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            try {
                                FileReader fr = new FileReader(fileName);
                                BufferedReader bfr = new BufferedReader(fr);
                                crwhCentralArea.setText("");
                                while ((str = bfr.readLine()) != null) {
                                    crwhCentralArea.append(str);
                                }
                                this.setTitle(fileName.getName() + " - CRWH Notepad");
                                crwhJLabel.setText("Currently open File："
                                        + fileName.getAbsoluteFile());
                                fr.close();
                                crwhNewFile = false;
                                crwhCurrentState = fileName;
                                crwhOldValues = crwhCentralArea.getText();
                            } catch (IOException ioException) {
                            }
                        }
                        break;
                    }
                    default:
                }
            } else {
                String str = null;
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setDialogTitle("Open File");
                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.CANCEL_OPTION) {
                    crwhJLabel.setText("No files were selected");
                    return;
                }
                File fileName = fileChooser.getSelectedFile();
                if (fileName == null || fileName.getName().equals("")) {
                    JOptionPane.showMessageDialog(this, "Illegal file name",
                            "Illegal file name", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        FileReader fr = new FileReader(fileName);
                        BufferedReader bfr = new BufferedReader(fr);
                        crwhCentralArea.setText("");
                        while ((str = bfr.readLine()) != null) {
                            crwhCentralArea.append(str);
                        }
                        this.setTitle(fileName.getName() + " - CRWH Notepad");
                        crwhJLabel.setText("Currently open File："
                                + fileName.getAbsoluteFile());
                        fr.close();
                        crwhNewFile = false;
                        crwhCurrentState = fileName;
                        crwhOldValues = crwhCentralArea.getText();
                    } catch (IOException ioException) {
                    }
                }
            }
        } else if (e.getSource() == crwhFMenu_Save) {
            crwhCentralArea.requestFocus();
            if (crwhNewFile) {
                String str = null;
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setDialogTitle("Save");
                int result = fileChooser.showSaveDialog(this);
                if (result == JFileChooser.CANCEL_OPTION) {
                    crwhJLabel.setText("No files were selected");
                    return;
                }
                File saveFileName = fileChooser.getSelectedFile();
                if (saveFileName == null || saveFileName.getName().equals("")) {
                    JOptionPane.showMessageDialog(this, "Illegal file name",
                            "Illegal file name", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        FileWriter fw = new FileWriter(saveFileName);
                        BufferedWriter bfw = new BufferedWriter(fw);
                        bfw.write(crwhCentralArea.getText(), 0,
                                crwhCentralArea.getText().length());
                        bfw.flush();
                        bfw.close();
                        crwhNewFile = false;
                        crwhCurrentState = saveFileName;
                        crwhOldValues = crwhCentralArea.getText();
                        this.setTitle(saveFileName.getName() + " - CRWH Notepad");
                        crwhJLabel.setText("Currently open File："
                                + saveFileName.getAbsoluteFile());
                    } catch (IOException ioException) {
                    }
                }
            } else {
                try {
                    FileWriter fw = new FileWriter(crwhCurrentState);
                    BufferedWriter bfw = new BufferedWriter(fw);
                    bfw.write(crwhCentralArea.getText(), 0,
                            crwhCentralArea.getText().length());
                    bfw.flush();
                    fw.close();
                } catch (IOException ioException) {
                }
            }
        } else if (e.getSource() == crwhFMenu_SaveAs) {
            crwhCentralArea.requestFocus();
            String str = null;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setDialogTitle("Save As");
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.CANCEL_OPTION) {
                crwhJLabel.setText("No files were selected");
                return;
            }
            File saveFileName = fileChooser.getSelectedFile();
            if (saveFileName == null || saveFileName.getName().equals("")) {
                JOptionPane.showMessageDialog(this, "Illegal file name",
                        "Illegal file name", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    FileWriter fw = new FileWriter(saveFileName);
                    BufferedWriter bfw = new BufferedWriter(fw);
                    bfw.write(crwhCentralArea.getText(), 0,
                            crwhCentralArea.getText().length());
                    bfw.flush();
                    fw.close();
                    crwhOldValues = crwhCentralArea.getText();
                    this.setTitle(saveFileName.getName() + "  - CRWH Notepad");
                    crwhJLabel.setText("Currently open File:"
                            + saveFileName.getAbsoluteFile());
                } catch (IOException ioException) {
                }
            }
        } else if (e.getSource() == crwhFMenu_Exit) {
            int exitChoose = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?", "Exit Tips",
                    JOptionPane.OK_CANCEL_OPTION);
            if (exitChoose == JOptionPane.OK_OPTION) {
                System.exit(0);
            } else {
            }
        } else if (e.getSource() == crwhEMenu_Undo || e.getSource() == crwhMenu_Undo) {
            crwhCentralArea.requestFocus();
            if (crwhUndo.canUndo()) {
                try {
                    crwhUndo.undo();
                } catch (CannotUndoException ex) {
                    System.out.println("Unable to undo:" + ex);
                }
            }
            if (!crwhUndo.canUndo()) {
                crwhEMenu_Undo.setEnabled(false);
            }

        } else if (e.getSource() == crwhEMenu_Cut || e.getSource() == crwhMenu_Cut) {
            crwhCentralArea.requestFocus();
            String text = crwhCentralArea.getSelectedText();
            StringSelection selection = new StringSelection(text);
            crwhClips.setContents(selection, null);
            crwhCentralArea.replaceRange("", crwhCentralArea.getSelectionStart(),
                    crwhCentralArea.getSelectionEnd());
            checkMenuItemEnabled();

        } else if (e.getSource() == crwhEMenu_Copy || e.getSource() == crwhMenu_Copy) {
            crwhCentralArea.requestFocus();
            String text = crwhCentralArea.getSelectedText();
            StringSelection selection = new StringSelection(text);
            crwhClips.setContents(selection, null);
            checkMenuItemEnabled();

        } else if (e.getSource() == crwhEMenu_Paste || e.getSource() == crwhMenu_Paste) {
            crwhCentralArea.requestFocus();
            Transferable contents = crwhClips.getContents(this);

            if (contents == null) {
                return;
            }
            String text = "";
            try {
                text = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception exception) {
            }

            crwhCentralArea.replaceRange(text,
                    crwhCentralArea.getSelectionStart(),
                    crwhCentralArea.getSelectionEnd());
            checkMenuItemEnabled();

        } else if (e.getSource() == crwhEMenu_Delete || e.getSource() == crwhMenu_Delete) {
            crwhCentralArea.requestFocus();
            crwhCentralArea.replaceRange("", crwhCentralArea.getSelectionStart(),
                    crwhCentralArea.getSelectionEnd());
            checkMenuItemEnabled();
        } else if (e.getSource() == crwhEMenu_Find) {
            crwhCentralArea.requestFocus();
            find();
        } else if (e.getSource() == crwhEMenu_FindNext) {
            crwhCentralArea.requestFocus();
            find();
        } else if (e.getSource() == crwhEMenu_Replace) {
            crwhCentralArea.requestFocus();
            replace();
        } else if (e.getSource() == crwhEMenu_GoTo) {
            crwhCentralArea.requestFocus();
            GoTo();
        } else if (e.getSource() == crwhEMenu_TimeDate) {
            crwhCentralArea.requestFocus();
            Calendar rightNow = Calendar.getInstance();
            Date date = rightNow.getTime();
            crwhCentralArea.insert(date.toString(), crwhCentralArea.getCaretPosition());
        } else if (e.getSource() == crwhEMenu_SelectAll || e.getSource() == crwhMenu_SelectAll) {
            crwhCentralArea.selectAll();
        } else if (e.getSource() == crwhFMTMenu_Encase) {
            if (crwhFMTMenu_Encase.getState()) {
                crwhCentralArea.setLineWrap(true);
            } else {
                crwhCentralArea.setLineWrap(false);
            }

        } else if (e.getSource() == crwhFMTMenu_Font) {
            crwhCentralArea.requestFocus();
            font();

        } else if (e.getSource() == crwhHPMenu_HelpForm) {
            crwhCentralArea.requestFocus();
            JOptionPane.showMessageDialog(this, "Press F1 for online assistance.", "Help",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == crwhHPMenu_About) {
            crwhCentralArea.requestFocus();
            JOptionPane.showMessageDialog(this,
                      "================================================================================\n"
                    + " By : Caesar Raishawn Watts-Hall                                                \n"
                    + " Creation Date : November 15. 2018                                              \n"
                    + " E-mail : Crwatts-hall@htu.edu                                                  \n"
                    + " If there are any issues, please contact me at my email above. Thank You.       \n"
                    + "================================================================================\n"
                    + " File Tab -                                                                     \n"
                    + " * New: Creates a new .txt Document.                                            \n"
                    + " * Open: Opens a .txt document in an available pane.                            \n"
                    + " * Save: Saves all open(ed) 'txt' documents in the available pane.              \n"
                    + " * Save As: Saves the 'txt' document in its current format.                     \n"
                    + " * Exit: Saves the opened 'txt' document and closes the application.            \n"
                    + "================================================================================\n"
                    + " Edit Tab -                                                                     \n"
                    + " * Undo: Un-does the recent activity.                                           \n"
                    + " * Cut: Cuts out any or all of the selected text.                               \n"
                    + " * Copy: Copies the selected text.                                              \n"
                    + " * Paste: Contiues from (Copy) and pastes the copied text in the desired spot.  \n"
                    + " * Find: Finds a word (and its duplicates) from within the txt document.        \n"
                    + " * Find Next: Finds the specified word within the txt document.                 \n"
                    + " * Replace: Replaces a selected string for a substitutionary string.            \n"
                    + " * Go To: Goes to a specific line withtin the document.                         \n"
                    + " * Select All: Selects all within the txt document.                             \n"
                    + " * Time|Date: Showcases the current time and date.                              \n"
                    + "================================================================================\n"
                    + " Format Tab -                                                                   \n"
                    + " * Word Wrap: Encases the txt document in the given area for writing.           \n"
                    + " * Font: Changes the font and size.                                             \n"
                    + "================================================================================\n",
                    "CRWH Notepad", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void removeUpdate(DocumentEvent e) {
        crwhEMenu_Undo.setEnabled(true);
    }

    public void insertUpdate(DocumentEvent e) {
        crwhEMenu_Undo.setEnabled(true);
    }

    public void changedUpdate(DocumentEvent e) {
        crwhEMenu_Undo.setEnabled(true);
    }

    class UndoHandler implements UndoableEditListener {

        public void undoableEditHappened(UndoableEditEvent uee) {
            crwhUndo.addEdit(uee.getEdit());
        }
    }

    public static void main(String args[]) {
        CRWH_NotepadGUI notepad = new CRWH_NotepadGUI();
        notepad.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
//END