package com.Sec;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;
import java.security.Key;

public class DesktopUI {

    private final File root = new File("/");
    public Path pathDirectToEncrypt = root.toPath();
    public Path pathDirectToDecrypt = root.toPath();

    private JList<File> listSecretFile = new JList<File>();

    public DesktopUI() {
        initUI();
    }

    public void setPathDirectToEncrypt(Path pathDirectToEncrypt) {
        this.pathDirectToEncrypt = pathDirectToEncrypt;

    }
    public void setPathDirectToDecrypt(Path pathDirectToDecrypt){
        this.pathDirectToDecrypt = pathDirectToDecrypt;
    }

    public void setListSecretFile(String pathSecretFolder) {
        File secretFolder = new File(pathSecretFolder);
        File[] listFiles = secretFolder.listFiles();
        DefaultListModel<File> listModel = new DefaultListModel<>();
        System.out.println("init DefaultListMode done");
        assert listFiles != null;
        for (int i = 0; i < listFiles.length; i++) {
            listModel.insertElementAt(listFiles[i], i);
        }
        System.out.println("insert data to mode done");
        listSecretFile.setModel(listModel);

    }

    private void initUI() {
        JFrame frame = new JFrame("TestProject");
        //make TopMenuBar
        JMenuBar menuBar = new JMenuBar();
        JMenu optionMenu = new JMenu("Option");
        JMenuItem chooseSecretFolder = new JMenuItem("Choose Secret Folder");
        JMenuItem chooseDecryptFolder = new JMenuItem("Choose Folder To Decrypt");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpItem = new JMenuItem("?Help");
        JMenuItem aboutItem = new JMenuItem("About");
        listSecretFile.setCellRenderer(new SimpleFileCellRenderer());
        //config chooseFolderToDecrytp
        chooseDecryptFolder.setToolTipText("Choose destination folder to decrypt");
        chooseDecryptFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,InputEvent.CTRL_MASK));
        chooseDecryptFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeJFileChoose(2);
                System.out.println("PathDirecChoose: " + String.valueOf(pathDirectToDecrypt));
            }
        });

        // config chooseSecretFolder
        chooseSecretFolder.setToolTipText("Choose destinaion folder to encrypt");
        chooseSecretFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
        chooseSecretFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeJFileChoose(1);
                setListSecretFile(String.valueOf(pathDirectToEncrypt));
                //setDes(String.valueOf(pathDirectToEncrypt));
                System.out.println("PathDirecChoose: " + String.valueOf(pathDirectToEncrypt));

            }
        });

        //add ActionListener for exit Item
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        optionMenu.add(chooseSecretFolder);
        optionMenu.add(chooseDecryptFolder);
        optionMenu.add(exitItem);
        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);
        menuBar.add(optionMenu);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);

        //make JTextField to show PathFile choosed and view Tree
        JTextField pathFileJText = new JTextField();
        JTree leftTree = new JTree(new FileTreeModel(root));
        //rightTree = new JTree(new FileTreeModel(des));
        //setListSecretFile(String.valueOf(pathDirectToEncrypt));

        setListSecretFile(String.valueOf(pathDirectToEncrypt));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JScrollPane leftPane = new JScrollPane(leftTree);
        JScrollPane rightPane = new JScrollPane(listSecretFile);
        // make a popupMenu
        JPopupMenu popupMenu = new JPopupMenu();
        JPopupMenu popupMenu1 = new JPopupMenu();
        JMenuItem encryptFile = new JMenuItem("Encrypt File");
        JMenuItem decryptFile = new JMenuItem("Decrypt File");
        popupMenu.add(encryptFile);
        popupMenu1.add(decryptFile);
        //add TreeSelectionListener for tree system
        leftTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Object object = e.getPath().getLastPathComponent();
                File file = new File(object.toString());
                System.out.println("Name File : " + file.getName());
                pathFileJText.setText(object.toString());


                leftTree.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseReleased(e);
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());

                        }
                    }
                });
            }

        });
        listSecretFile.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                listSecretFile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e1) {
                        super.mouseReleased(e1);
                        if (e1.getButton() == MouseEvent.BUTTON3) {
                            popupMenu1.show(e1.getComponent(), e1.getX(), e1.getY());
                        }
                    }
                });
            }
        });

        //encryptfile .inputfile get from Jtext outputfile = des+ namefileinput+".encrypt"
        encryptFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textJtext = pathFileJText.getText();
                File inputFile = new File(textJtext);

                JPanel panel = new JPanel(new BorderLayout(5, 5));
                JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
                label.add(new JLabel("Password", SwingConstants.RIGHT));
                label.add(new JLabel("Confirm", SwingConstants.RIGHT));
                panel.add(label, BorderLayout.WEST);
                JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
                JPasswordField password1 = new JPasswordField();
                controls.add(password1);
                JPasswordField password2 = new JPasswordField();
                controls.add(password2);
                panel.add(controls, BorderLayout.CENTER);
                String stringPassword1;
                String stringPassword2;
                int n = 0;
                do {
                    password1.setText("");
                    password2.setText("");
                    if (n == 0) {
                        JOptionPane.showMessageDialog(frame, panel, "Password", JOptionPane.QUESTION_MESSAGE);
                        n++;
                        stringPassword1 = String.valueOf(password1.getPassword());
                        stringPassword2 = String.valueOf(password2.getPassword());
                    } else {
                        JOptionPane.showMessageDialog(frame, panel, "Check your password", JOptionPane.ERROR_MESSAGE);
                        stringPassword1 = String.valueOf(password1.getPassword());
                        stringPassword2 = String.valueOf(password2.getPassword());
                    }


                } while (!stringPassword1.equals(stringPassword2));

                if (!stringPassword1.equals("")) {
                    CryptoUtils.encrypt(stringPassword1,inputFile, pathDirectToEncrypt,1);
                }
                leftTree.updateUI();
                setListSecretFile(String.valueOf(pathDirectToEncrypt));


            }
        });
        decryptFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selectedDirect1 = null;
                Path pathfileDecrypt = listSecretFile.getSelectedValue().toPath();
                File inputFile = new File(String.valueOf(pathfileDecrypt));
                System.out.println("File decrypt : "+inputFile);
                JPanel panel = new JPanel(new BorderLayout(5, 5));
                JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
                label.add(new JLabel("Password", SwingConstants.RIGHT));
                label.add(new JLabel("Confirm", SwingConstants.RIGHT));
                panel.add(label, BorderLayout.WEST);
                JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
                JPasswordField password1 = new JPasswordField();
                controls.add(password1);
                JPasswordField password2 = new JPasswordField();
                controls.add(password2);
                panel.add(controls, BorderLayout.CENTER);
                String stringPassword1;
                String stringPassword2;
                int n = 0;
                do {
                    password1.setText("");
                    password2.setText("");
                    if (n == 0) {
                        JOptionPane.showMessageDialog(frame, panel, "Password", JOptionPane.QUESTION_MESSAGE);
                        n++;
                        stringPassword1 = String.valueOf(password1.getPassword());
                        stringPassword2 = String.valueOf(password2.getPassword());
                    } else {
                        JOptionPane.showMessageDialog(frame, panel, "Check your password", JOptionPane.ERROR_MESSAGE);
                        stringPassword1 = String.valueOf(password1.getPassword());
                        stringPassword2 = String.valueOf(password2.getPassword());
                    }


                } while (!stringPassword1.equals(stringPassword2));

                if (!stringPassword1.equals("")) {
                    CryptoUtils.encrypt(stringPassword1,inputFile, pathDirectToDecrypt,2);
                }
                leftTree.updateUI();
                setListSecretFile(String.valueOf(pathDirectToEncrypt));




            }
        });

        //show all componets
        splitPane.setLeftComponent(leftPane);
        splitPane.setRightComponent(rightPane);
        frame.add(pathFileJText, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);

    }
    public void makeJFileChoose(int mode){
        File selectedDirect = null;
        JFileChooser fileChooser = new JFileChooser(String.valueOf(pathDirectToEncrypt));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(fileChooser);
        selectedDirect = fileChooser.getSelectedFile();
        if (mode == 1){
        setPathDirectToEncrypt(selectedDirect.toPath());
        }else setPathDirectToDecrypt(selectedDirect.toPath());
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                DesktopUI desktopUI = new DesktopUI();
            }
        });

    }
}


//                int lastIndex = 0;
//                String fileName = null;
//                    lastIndex = textJtext.lastIndexOf('/');
//                    fileName = textJtext.substring(lastIndex+1);
//                File outputFile = new File(pathDirectToEncrypt+"/"+inputFile.getName()+".encrypt");
