package utils;

import algorithms.Edge;
import algorithms.Graph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import java.util.*;


public class GraphImageRenderer {

    public static void renderGraph(
            Graph graph,
            Collection<Edge> mstEdges,
            String outputPngPath
    ) {
        int W = 900, H = 700;
        int R = Math.min(W, H) / 2 - 80;
        Point center = new Point(W/2, H/2);

        int n = graph.V();
        Map<Integer, Point> pos = new HashMap<>();
        for (int i = 0; i < n; i++) {
            double angle = 2*Math.PI * i / n - Math.PI/2;
            int x = center.x + (int)(R * Math.cos(angle));
            int y = center.y + (int)(R * Math.sin(angle));
            pos.put(i, new Point(x, y));
        }

        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,W,H);

        g2.setColor(new Color(40,40,40));
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("Graph Visualization", 20, 30);

        Set<String> mstSet = new HashSet<>();
        if (mstEdges != null) {
            for (Edge e : mstEdges) {
                int a = e.either(), b = e.other(a);
                String key = key(a,b);
                mstSet.add(key);
            }
        }

        g2.setStroke(new BasicStroke(2f));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));

        Set<String> drawn = new HashSet<>();
        for (Edge e : graph.allEdges()) {
            int a = e.either(), b = e.other(a);
            String k = key(a,b);
            if (drawn.contains(k)) continue;
            drawn.add(k);

            Point p1 = pos.get(a), p2 = pos.get(b);
            if (mstSet.contains(k)) continue;

            g2.setColor(new Color(180,180,180));
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            int mx = (p1.x + p2.x)/2, my = (p1.y + p2.y)/2;
            g2.setColor(new Color(130,130,130));
            g2.drawString(String.valueOf(e.weight()), mx+5, my-5);
        }

        if (mstEdges != null) {
            g2.setStroke(new BasicStroke(3.5f));
            g2.setColor(new Color(30,170,70));
            for (Edge e : mstEdges) {
                int a = e.either(), b = e.other(a);
                Point p1 = pos.get(a), p2 = pos.get(b);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                int mx = (p1.x + p2.x)/2, my = (p1.y + p2.y)/2;
                g2.drawString(String.valueOf(e.weight()), mx+6, my-6);
            }
        }

        int d = 26;
        for (int v = 0; v < n; v++) {
            Point p = pos.get(v);
            int x = p.x - d/2, y = p.y - d/2;
            g2.setColor(new Color(50,90,200));
            g2.fill(new Ellipse2D.Double(x, y, d, d));
            g2.setColor(Color.WHITE);
            String name = graph.nameOf(v);
            drawCentered(g2, name, p.x, p.y+5);
        }

        g2.dispose();
        try {
            File out = new File(outputPngPath);
            out.getParentFile().mkdirs();
            ImageIO.write(img, "png", out);
            System.out.println("Saved image: " + out.getPath());
        } catch (Exception ex) {
            System.err.println("Cannot save image: " + ex.getMessage());
        }
    }

    private static void drawCentered(Graphics2D g2, String s, int x, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(s);
        int a = fm.getAscent();
        g2.drawString(s, x - w/2, y + a/2 - 4);
    }
    private static String key(int a, int b) {
        int x = Math.min(a,b), y = Math.max(a,b);
        return x + ":" + y;
    }
}

