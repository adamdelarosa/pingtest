package sample;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ping {
    private static final Logger logger = Logger.getLogger(ping.class.getName());

    public void pinger() {
        ping p = new ping();
        p.run();
    }

    private void run() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new PingFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

    private class PingFrame extends JFrame {
        private static final long serialVersionUID = 1L;

        private JTextArea textArea;

        private PingFrame() {
            super.setName("Ping Frame");
            this.addComponents();
            super.setSize(400, 240);
        }

        private void addComponents() {
            super.setLayout(new BorderLayout());

            this.textArea = new JTextArea(10, 0);
            JScrollPane scrollPane = new JScrollPane(this.textArea);
            super.add(scrollPane, BorderLayout.NORTH);

            JButton button = new JButton("Run");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Thread(new Ping(PingFrame.this.textArea)).start();
                }
            });
            super.add(button, BorderLayout.SOUTH);
        }

        private class Ping implements Runnable {
            private JTextArea textArea;

            private Ping(JTextArea textArea) {
                this.textArea = textArea;
            }

            @Override
            public void run() {
                try {
                    List<String> commands = new ArrayList<>(10);
                    commands.add("ping");
                    commands.add("www.google.com");

                    ProcessBuilder builder = new ProcessBuilder();
                    builder.command(commands);
                    Process process = builder.start();

                    try (
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(process.getInputStream(), Charset.defaultCharset()))) {
                        String line = br.readLine();
                        while (null != line) {
                            final String text = line;
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    Ping.this.textArea.append(text);
                                    Ping.this.textArea.append("\n");
                                }
                            });
                            line = br.readLine();
                        }
                    }

                    process.waitFor();
                    process.destroy();
                } catch (IOException | InterruptedException x) {
                    logger.log(Level.SEVERE, "Error", x);
                }
            }
        }
    }
}