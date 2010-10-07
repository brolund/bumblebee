package com.agical.bumblebee.acceptance.swing.application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;

public class Application {

    private JFrame frame;
    private JPanel form;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private Thread thread;

    public Application() {
        start();
    }
    
    public Component getForm() {
        return form;
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
    private void start() {
        frame = new JFrame();
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(createMenu(), BorderLayout.NORTH);
        contentPane.add(createTree(), BorderLayout.WEST);
        contentPane.add(createFormPanel(), BorderLayout.CENTER);
        contentPane.add(createStatusBar(), BorderLayout.SOUTH);
        frame.setSize(600,400);
        frame.setVisible(true);
        waitForValidFrame();
    }

    public void stop() {
        frame.dispose();
    }
    
    private void waitForValidFrame() {
        try {
            long start = System.currentTimeMillis();
            long timeout = 5000;
            while(!frame.isActive() && start+timeout>System.currentTimeMillis()) {
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException( "", e );
        }
    }
    
    private JTextField createStatusBar() {
        return new JTextField("INFO It's easy to update your " +
                "documentation if you generate it");
    }

    private JTree createTree() {
        return new JTree();
    }

    
    private Component createFormPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form = new JPanel(new GridLayout(4,2, 4, 4));
        nameLabel = new JLabel("Name");
        form.add(nameLabel);
        form.add(new JTextField());
        emailLabel = new JLabel("Email");
        form.add(emailLabel);
        form.add(new JTextField());
        phoneLabel = new JLabel("Phone nr");
        form.add(phoneLabel);
        form.add(new JTextField());
        form.add(new JButton("Ok"));
        form.add(new JButton("Cancel"));
        panel.add(form);
        return panel;
    }

    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("File"));
        menuBar.add(new JMenu("Edit"));
        menuBar.add(new JMenu("View"));
        menuBar.add(new JMenu("About"));
        return menuBar;
    }

    public Component getNameLabel() {
        return nameLabel;
    }
    public Component getEmailLabel() {
        return emailLabel;
    }
    public Component getPhoneLabel() {
        return phoneLabel;
    }

}
