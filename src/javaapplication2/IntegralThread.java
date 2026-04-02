/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;

/**
 *
 * @author admin
 */
public class IntegralThread extends Thread {
    private double a, b, h;
    private double result;

    public IntegralThread(double a, double b, double h) {
        this.a = a;
        this.b = b;
        this.h = h;
    }

    @Override
    public void run() {
        System.out.println("Thread " + this.getName() + " started: " + a + " → " + b);
        double sum = 0;
        for (double x = a; x < b; x += h) {
            double x2 = Math.min(x + h, b);
            sum += (1.0 / x + 1.0 / x2) / 2.0 * (x2 - x);
        }
        result = sum;
        System.out.println("Thread " + this.getName() + " finished. Result = " + result);
    }

    public double getResult() {
        return result;
    }
}
