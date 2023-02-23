//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package guiApp;

import adversarialSearch.AdversarialSearch;
import adversarialSearch.MinimaxSearch;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import metrics.Metrics;
import othello.OthelloGame;
import othello.OthelloState;
import position.XYLocation;

public class OthelloApp {
    public OthelloApp() {
    }

    public JFrame constructApplicationFrame() {
        JFrame frame = new JFrame();
        JPanel panel = new OthelloApp.OthelloPanel();
        frame.add(panel, "Center");
        frame.setDefaultCloseOperation(3);
        return frame;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException var2) {
            var2.printStackTrace();
        } catch (InstantiationException var3) {
            var3.printStackTrace();
        } catch (IllegalAccessException var4) {
            var4.printStackTrace();
        } catch (UnsupportedLookAndFeelException var5) {
            var5.printStackTrace();
        }

        JFrame frame = (new OthelloApp()).constructApplicationFrame();
        frame.setSize(800, 800);
        frame.setTitle("Othello");
        frame.setVisible(true);
    }

    private static class OthelloPanel extends JPanel implements ActionListener {
        private static final long serialVersionUID = 1L;
        JButton clearButton;
        JButton proposeButton;
        JButton[] squares;
        JLabel statusBar;
        OthelloGame game;
        OthelloState currState;
        Metrics searchMetrics;

        OthelloPanel() {
            this.setLayout(new BorderLayout());
            JToolBar tbar = new JToolBar();
            tbar.setFloatable(false);
            tbar.add(Box.createHorizontalGlue());
            this.clearButton = new JButton("Clear");
            this.clearButton.addActionListener(this);
            tbar.add(this.clearButton);
            this.proposeButton = new JButton("Propose Move");
            this.proposeButton.addActionListener(this);
            tbar.add(this.proposeButton);
            this.add(tbar, "North");
            JPanel spanel = new JPanel();
            spanel.setLayout(new GridLayout(8, 8));
            this.add(spanel, "Center");
            this.squares = new JButton[64];
            Font f = new Font("SansSerif", 0, 32);

            for(int i = 0; i < 64; ++i) {
                JButton square = new JButton("");
                square.setFont(f);
                square.setBackground(Color.GREEN);
                square.addActionListener(this);
                this.squares[i] = square;
                spanel.add(square);
            }

            this.statusBar = new JLabel(" ");
            this.statusBar.setBorder(BorderFactory.createEtchedBorder());
            this.add(this.statusBar, "South");
            this.game = new OthelloGame();
            this.actionPerformed((ActionEvent)null);
        }

        public void actionPerformed(ActionEvent ae) {
            this.searchMetrics = null;
            int i;
            if (ae != null && ae.getSource() != this.clearButton) {
                if (!this.game.isTerminal(this.currState)) {
                    if (ae.getSource() == this.proposeButton) {
                        this.proposeMove();
                    } else {
                        for(i = 0; i < 64; ++i) {
                            if (ae.getSource() == this.squares[i]) {
                                this.currState = this.game.getResult(this.currState, new XYLocation(i % 8, i / 8));
                            }
                        }
                    }
                }
            } else {
                this.currState = this.game.getInitialState();
            }

            for(i = 0; i < 64; ++i) {
                String val = this.currState.getValue(i % 8, i / 8);
                if (val == "B") {
                    this.blackDisc(i);
                } else if (val == "W") {
                    this.whiteDisc(i);
                } else {
                    val = "";
                    this.squares[i].setText(val);
                }
            }

            this.updateStatus();
        }

        private void proposeMove() {
            AdversarialSearch<OthelloState, XYLocation> search = MinimaxSearch.createFor(this.game);
            XYLocation action = (XYLocation)search.makeDecision(this.currState);
            this.searchMetrics = search.getMetrics();
            this.currState = this.game.getResult(this.currState, action);
        }

        private void updateStatus() {
            String statusText;
            if (this.game.isTerminal(this.currState)) {
                if (this.currState.getBlackScore() > this.currState.getWhiteScore()) {
                    statusText = "BLACK has won :-)";
                } else if (this.currState.getWhiteScore() > this.currState.getBlackScore()) {
                    statusText = "WHITE has won :-)";
                } else {
                    statusText = "No winner...";
                }
            } else {
                statusText = "Next player to  move: " + this.game.getPlayer(this.currState);
            }

            if (this.searchMetrics != null) {
                statusText = statusText + "    " + this.searchMetrics;
            }

            this.statusBar.setText(statusText);
        }

        private void blackDisc(int index) {
            ImageIcon icon = new ImageIcon("blackDisc.png");
            Image image = icon.getImage();
            Image modifiedImage = image.getScaledInstance(80, 53, 4);
            icon = new ImageIcon(modifiedImage);
            this.squares[index].setIcon(icon);
        }

        private void whiteDisc(int index) {
            ImageIcon icon = new ImageIcon("whiteDisc.png");
            Image image = icon.getImage();
            Image modifiedImage = image.getScaledInstance(53, 50, 4);
            icon = new ImageIcon(modifiedImage);
            this.squares[index].setIcon(icon);
        }
    }
}
