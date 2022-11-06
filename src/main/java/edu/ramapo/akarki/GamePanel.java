package edu.ramapo.akarki;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int appleEaten = 0;
    int appleX = 0;
    int appleY = 0;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() 
    {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        StartGame();
    }

    public void StartGame()
    {
        NewApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g)
    {
        // this is called by the library
        super.paintComponent(g);
        Draw(g);
    }

    /**
     * @param g
     */
    public void Draw(Graphics g)
    {
        if (running)
        {
            // drawing the matrix grid
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine((i * UNIT_SIZE), 0, (i * UNIT_SIZE), SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            // drawing the apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //drawing the snake
            for (int i = 0; i < bodyParts; i++) {
                // head of the snake
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // Score text
            g.setColor(Color.red);
            g.setFont(new Font("TimesRoman", Font.BOLD, 50));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + appleEaten, 
                    (SCREEN_WIDTH - metrics.stringWidth("Score: " + appleEaten))/ 2, 
                    g.getFont().getSize());
        }
        else 
        {
            GameOver(g);
        }
    }

    public void NewApple()
    {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT/ UNIT_SIZE)) * UNIT_SIZE;
    }

    public void Move()
    {
        // moving the snake 1 gird in the direction the head is facing
        for(int i = bodyParts; i>0;--i)
        {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch(direction)
        {
            case 'U':
            {
                y[0] = y[0] - UNIT_SIZE;
                break;
            }
            case 'D':
            {
                y[0] = y[0] + UNIT_SIZE;
                break;
            }
            case 'L':
            {
                x[0] = x[0] - UNIT_SIZE;
                break;
            }
            case 'R':
            {
                x[0] = x[0] + UNIT_SIZE;
                break;
            }
            
        }
    }
    
    public void CheckApple()
    {
        // checking for a collision with apple
        if(appleX == x[0] && appleY == y[0])
        {
            ++bodyParts;
            ++appleEaten;
            NewApple();
        }
    }

    public void CheckCollisions()
    {
        // checking if the head collides with body
        for(int i = bodyParts; i > 0; --i)
        {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        
        // checking if the head collides with left border
        if(x[0] < 0)
        {
            running = false;
        }
        
        // checking if the head collides with right border
        if(x[0] > SCREEN_WIDTH)
        {
            running = false;
        }

        // checking if the head collides with top border
        if(y[0] < 0)
        {
            running = false;
        }
        
        // checking if the head collides with bottom border
        if(y[0] > SCREEN_HEIGHT)
        {
            running = false;
        }
    
        if(running == false)
        {
            timer.stop();
        }
    }

    public void GameOver( Graphics g)
    {
        //game over text
        g.setColor(Color.red);
        g.setFont(new Font("TimesRoman", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", 
                (SCREEN_WIDTH - metrics1 .stringWidth("Game Over")) / 2, 
                SCREEN_HEIGHT / 2);
        // Score text
            g.setColor(Color.red);
            g.setFont(new Font("TimesRoman", Font.BOLD, 75));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Score: " + appleEaten, 
                    (SCREEN_WIDTH - metrics2.stringWidth("Score: " + appleEaten))/ 2, 
                    g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(running)
        {
            Move();
            CheckApple();
            CheckCollisions();
        }
        repaint();

    }
    
    public class MyKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch (e.getKeyCode()) 
            {
                case KeyEvent.VK_LEFT:
                {
                    // preventing a 180 turn
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                }

                case KeyEvent.VK_RIGHT:
                {
                    // preventing a 180 turn
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                }
                
                case KeyEvent.VK_UP:
                {
                    // preventing a 180 turn
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                }

                case KeyEvent.VK_DOWN:
                {
                    // preventing a 180 turn
                    if(direction != 'U')
                    {
                        direction = 'D';
                    }
                    break;
                }
            }
        }
    }

}
