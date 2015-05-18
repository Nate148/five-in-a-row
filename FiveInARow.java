package fiveinarow;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
public class FiveInARow {

    public  static       Color      couleur;
    public  static       boolean    wait;
    public  static       boolean    canPlay;
    private static       int        turn;
    private static       int        recent;
    private static       JFrame     window;
    private static       JScrollBar bar;
    private static final JButton[]  square = new JButton[100];
    private static final Player[]   p      = new Player[2];
    private static final int[][]    board  = new int[10][10];
    
    public static void scrollDown() {
        bar.setValue(bar.getMaximum());
        waiting(50);
        window.setVisible(true);
    }
    //Waits i milliseconds or until button click if i = 0
    public static void waiting(int i) {
        wait = true;
        long time = System.currentTimeMillis();
        while(System.currentTimeMillis() - time < i && i > 0 || 
                wait && i == 0)  {System.out.print("");}
    }
    public static int[][] boardCopy() {
        int[][] b = new int[10][10];
        for (int i = 0; i < 10; i++)
            System.arraycopy(board[i], 0, b[i], 0, 10); 
        return b;
    }
    //Plays a piece at square i if open; works for human and AI
    public static void play(int i) {
        if (board[i / 10][i % 10] == 0) {
            //Empty square = 0; p1 square = 6; p2 square = 7
            board[i / 10][i % 10] = turn + 6;
            square[i].setBackground(p[turn].color);
            recent = i;
            wait = false;
        }
    }
    
    public static void main(String[] args) {
        //Welcome, choose number of players
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(1,1,0,0);  c.ipady = 15;
        JFrame settings = new JFrame();
        settings.setTitle("Game Settings");
        settings.setSize(250, 250);
        settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settings.setLocationRelativeTo(null);
        Container stuff = settings.getContentPane();
        stuff.setLayout(new GridBagLayout());
        JLabel welcome = new JLabel("Welcome to Five in a Row!");
        welcome.setFont(new Font("Serif", 24, 16));
        stuff.add(welcome, c);
        c.gridy = 1;
        stuff.add(new JLabel("Object of game: to place 5 tiles in a "
                + "line"), c);
        c.gridy = 2;
        stuff.add(new JLabel("How many humans wish to play?"), c);
        
        JPanel buttons = new JPanel(new GridBagLayout());
        String[] numbers = {"0", "1", "2"};
        JComboBox numPlayers = new JComboBox(numbers);
        numPlayers.setSelectedIndex(1);
        c.gridx = 0;  c.gridy = 0;  c.ipady = 1;
        buttons.add(numPlayers, c);
        JButton next = new JButton();
        next.add(new JLabel("Continue"));
        next.setAction(new Click(-1));
        c.gridx = 1;  c.ipady = 0;
        buttons.add(next, c);
        c.gridx = 0;  c.gridy = 3;
        stuff.add(buttons, c);
        settings.setVisible(true);
        waiting(0);
        int humans = numPlayers.getSelectedIndex();
        settings.setVisible(false);
        stuff.removeAll();
        
        //Set up player names and colors
        JButton[] colors = new JButton[4];
        for (int i = 0; i < 4; i++) colors[i] = new JButton();
        colors[0].setBackground(Color.WHITE);
        colors[1].setBackground(Color.PINK);
        colors[2].setBackground(Color.GREEN);
        colors[3].setBackground(Color.CYAN);
        for (JButton b : colors) 
            b.setAction(new Click(b.getBackground()));
        for (int i = 0; i < 2; i++) {
            if (i < humans) {
                //User interface for human player set-up
                c.gridy = 0;
                stuff.add(new JLabel("Player " + (i + 1) + 
                        ", enter your name:"), c);
                JTextArea input = new JTextArea("Player " + (i + 1));
                input.setCaretPosition(8);
                c.gridy = 1;
                stuff.add(input, c);
                c.gridy = 2;
                stuff.add(new JLabel("Pick a color to continue"), c);
                buttons = new JPanel(new GridBagLayout());
                c.gridy = 0;  c.ipady = 24;
                for (int j = 0; j < 4 - i; j++) {
                    c.gridx = j;
                    buttons.add(colors[j], c);
                }
                c.gridx = 0;  c.gridy = 3;
                stuff.add(buttons, c);
                c.ipady = 0;
                settings.setVisible(true);
                waiting(0);
                p[i] = new Player(input.getText(), couleur, true);
                settings.setVisible(false);
                stuff.removeAll();
            }
            else {
                //AI generates name and color
                JButton b = colors[(int)(Math.random() * (4 - i))];
                couleur = b.getBackground();
                p[i] = new Player("Al Inaro", couleur, false);
                if (humans == 0) p[i].name += " " + (i + 1);
            }
            //Remove chosen color before second player chooses
            if (i == 0)
                for (int j = 0; j < 4; j++)
                    if (colors[j].getBackground().equals(couleur))
                        for (int k = j; k < 3; k++)
                            colors[k] = colors[k + 1];
        }
        
        //Visual representation of player match-up
        JPanel[] jp = new JPanel[2];
        for (int i = 0; i < 2; i++) {
            c.fill = GridBagConstraints.CENTER;
            jp[i] = new JPanel(new GridBagLayout());
            c.gridy = 0;
            jp[i].add(new JLabel(p[i].name), c);
            c.gridy = 1;
            if (p[i].human) jp[i].add(new JLabel("Human"), c);
            else jp[i].add(new JLabel("AI"), c);
            jp[i].setBackground(p[i].color);
            c.gridy = i * 2;  c.ipady = 40;
            c.fill = GridBagConstraints.BOTH;
            stuff.add(jp[i], c);
            c.ipady = 0;
        }
        JPanel vs = new JPanel();
        vs.setBackground(Color.YELLOW);
        vs.add(new JLabel("versus"));
        c.gridy = 1;  c.ipadx = 175;
        stuff.add(vs, c);
        next.removeAll();
        next.add(new JLabel("                    Let's Do This"));
        c.gridy = 3;  c.ipadx = 0;
        stuff.add(next, c);
        stuff.setBackground(Color.DARK_GRAY);
        settings.setVisible(true);
        waiting(0);
        settings.setVisible(false);
        
        //Main game window
        window = new JFrame();
        window.setTitle("Five In A Row");
        window.setSize(500, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        Container grid = window.getContentPane();
        grid.setLayout(new GridBagLayout());
        grid.setBackground(Color.DARK_GRAY);
        c.fill = GridBagConstraints.BOTH;  c.ipady = 24;
        
        //10 x 10 board of tiles
        for (int i = 0; i < 100; i++) {
            square[i] = new JButton();
            square[i].setBackground(new Color(i, i%10*18, 150-i));
            square[i].setAction(new Click(i));
            c.gridx = i % 10;  c.gridy = i / 10;
            grid.add(square[i], c);
        }
        //Game report area
        JTextArea text = new JTextArea("Welcome to Five in a Row!\n");
        text.setFont(new Font("Serif", 16, 16));
        text.setBackground(new Color(250, 250, 150));
        text.append(p[0].name + " vs. " + p[1].name + "\n");
        text.setEditable(false);
        JScrollPane report = new JScrollPane(text);
        report.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bar = report.getVerticalScrollBar();
        bar.setValue(bar.getMaximum());
        c.gridwidth = 10;  c.ipady = 50;  c.gridx = 0;  c.gridy = 10;
        grid.add(report, c);
        canPlay = false;
        window.setVisible(true);
        turn = (int)(2 * Math.random());
        AI al = new AI(boardCopy());
        
        //Actual game takes place inside this loop
        while(!al.over()) {
            turn = 1 - turn;
            text.append(p[turn].name + "'s move");
            scrollDown();
            if (p[turn].human) {
                canPlay = true;
                waiting(0);
                canPlay = false;
            }
            else {
                waiting(200);
                al.set(boardCopy());
                if (turn == 0) al.invert();
                play(al.play());
            }
            text.append(": row " + (recent / 10 + 1) + 
                    ", column " + (recent % 10 + 1) + "\n");
            al.set(boardCopy());
            scrollDown();
        }
        
        if (al.winner() >= 0) text.append(p[turn].name + " wins!");
        else text.append("It's a tie!");
        scrollDown();
    }
}

//Actions for clicking buttons
class Click extends AbstractAction {
    private final int i;
    private final Color color;
    public Click(int n) {
        i = n;
        color = null;
    }
    public Click(Color c) {
        i = -2;
        color = c;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (i == -2) FiveInARow.couleur = color;
        if (i < 0) FiveInARow.wait = false;
        else if (FiveInARow.canPlay) FiveInARow.play(i);
    }
}

//Stores players
class Player {
    public String name;
    public final Color color;
    public final boolean human;
    public Player(String s, Color c, boolean b) {
        name = s;
        color = c;
        human = b;
    }
}

//AI methods for determining winner and running AI player's moves
class AI {
    private int[][] board;
    public AI(int[][] b) {board = b;}
    public void set(int[][] b) {board = b;}
    private int value(int i) {return board[i / 10][i % 10];}
    
    //Switch 6's and 7's; used in game between two AIs
    public void invert() {
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                if (board[i][j] > 0)
                    board[i][j] = 13 - board[i][j];
    }
    
    //freq() helper methods
    private boolean fit(int i, int d) {
        if (d + i % 10 < 10) return true;
        if (d == 40 && i < 60) return true;
        if (d == 44 && i < 60 && i % 10 < 6) return true;
        return (d == 36 && i < 60 && i % 10 > 3);
    }
    private int total(int i, int d) {
        if (!fit(i, d)) return -1;
        int total = 0;
        for (int i2 = i; i2 <= i + d; i2 += d / 4)
            total += value(i2);
        return total;
    }
    //Number of groups of five tiles that sum to int sum
    private int freq(int sum) {
        int freq = 0;
        for (int d = 4; d < 45; d += 4) {
            for (int i = 0; i < 100; i++)
                if (total(i, d) == sum) freq++;
            if (d == 4) d = 32;
        }
        return freq;
    }
    
    //For determining wins and ties
    public int winner() {
        if (freq(30) > 0) return 0;
        if (freq(35) > 0) return 1;
        return -1;
    }
    public boolean over() {
        if (winner() >= 0) return true;
        for (int[] row : board)
            for (int e : row)
                if (e == 0) return false;
        return true;
    }
    
    //Helper methods for best()
    private int[] trim(int[] big, int cut) {
        int[] small = new int[cut];
        System.arraycopy(big, 0, small, 0, cut);
        return small;
    }
    private int[] open() {
        int[] open = new int[100];
        int index = 0;
        for (int i = 0; i < 100; i++)
            if (value(i) == 0) {
                open[index] = i;
                index++;
            }
        return trim(open, index);
    }
    private AI newAI(int i, int p) {
        int[][] insert = new int[10][10];
        for (int i2 = 0; i2 < 100; i2++)
            insert[i2 / 10][i2 % 10] = value(i2);
        if (value(i) == 0) insert[i / 10][i % 10] = p;
        return new AI(insert);
    }
    //freq(6 * n): line of n opponent tiles; freq(7 * n): n own pieces
    private int score(AI test) {
        return 24 * (test.freq(28) - freq(28))
            + 12 * (test.freq(21) - freq(21) - test.freq(18) + freq(18))
            + 4 * (test.freq(14) - freq(14) - test.freq(12) + freq(12))
            + test.freq(7) - freq(7) - test.freq(6) + freq(6);
    }
    //Best positions to play in
    private int[] best(int goal) {
        int[] open = open();
        int[] yes = new int[100];
        int index = 0, max = 0;
        boolean doom = false;
        if (goal == 3 && freq(24) == 0)
            for (int e : open) {
                AI test = newAI(e, 6);
                if (test.freq(24) > 1) doom = true;
            }
        if (goal == 4)
            for (int e : open) {
                AI test = newAI(e, 7);
                max = (int)Math.max(max, score(test));
            }
        //For each open space, add to the array if add = true
        for (int e : open) {
            boolean add = false;
            AI test = newAI(e, 7);
            if (goal == 0) //Win
                if (test.winner() == 1) add = true;
            if (goal == 1) //Block
                if (test.freq(24) == 0 && freq(24) > 0) add = true;
            if (goal == 2) //Win next turn
                if (test.freq(28) > 1) add = true;
            if (goal == 3 && doom) //Avert impending doom
                for (int e2 : open) {
                    add = true;
                    AI test2 = test.newAI(e2, 6);
                    if (test2.freq(24) > 1 && test2.freq(28) == 0) {
                        add = false;
                        break;
                    }
                }
            if (goal == 4) //Play like a boss (regular gameplay)
                if (score(test) >= max * 8 / 10) add = true;
            if (add) {
                yes[index] = e;
                index++;
            }
        }
        return trim(yes, index);
    }
    
    private int pick(int[] options) {
        return options[(int)(Math.random() * options.length)];
    }
    //Returns int to be played on board
    public int play() {
        int[][] choice = new int[5][];
        boolean b = freq(14) + freq(12) > 5 || freq(21) + freq(18) > 3;
        for (int i = 0; i < 5; i++) {
            choice[i] = best(i);
            if (choice[i].length > 1 && i > 2 && b && false)
                return predict(choice[i]);
            if (choice[i].length > 0) return pick(choice[i]);
        }
        return pick(open());
    }
    
    //Following code is for potentially using recursive move prediction
    //However, runtime is too long and above score method is imperfect
    private double score(int moves, int play) {
        AI test = newAI(play, 7);
        if (test.over()) {
            int winner = test.winner();
            int invert = 1 - 2 * (moves % 2);
            if (winner < 0) return 0.0;
            return (1.0 - 2 * winner) * invert;
        }
        if (moves == 0) return 0.0;
        test.invert();
        int[] plays = new int[0];
        int i = -1;
        while (plays.length == 0) {
            i++;
            plays = best(i);
        }
        double score = 0.0;
        for (int e : plays)
            score += score(moves - 1, e);
        return score / plays.length;
    }
    private int predict (int[] options) {
        double best = -1.0;
        int choice = 0;
        for (int e : options) {
            double score = score(4, e);
            if (score > best) {
                choice = e;
                best = score;
            }
        }
        return choice;
    }
}
