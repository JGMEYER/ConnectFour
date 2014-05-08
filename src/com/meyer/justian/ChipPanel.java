package com.meyer.justian;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ChipPanel extends JPanel
{
    private static final Color[] COLORS = { Color.WHITE, Color.RED, Color.BLUE };
    private int                  player;

    public ChipPanel(int player)
    {
        this.player = player;
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(COLORS[player]);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void setPlayer(int player)
    {
        this.player = player;
    }
}
