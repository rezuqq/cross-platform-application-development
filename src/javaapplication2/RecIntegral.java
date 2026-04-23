/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class RecIntegral implements Externalizable {
    private static final long serialVersionUID = 1L;
    
    static int counter = 0;
    int id;
    double a;
    double b;
    double h;
    double result;
    
    public RecIntegral() {
        
    }

    public RecIntegral(double a, double b, double h, double result) throws InvalidException{
        this.a = a;
        this.b = b;
        this.h = h;
        this.result = result;
        this.id = counter++;
    }
    
    private boolean isValid(double x) {
        return x >= 0.000001 && x <= 1_000_000;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(id);
        out.writeDouble(a);
        out.writeDouble(b);
        out.writeDouble(h);
        out.writeDouble(result);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        id = in.readInt();
        a = in.readDouble();
        b = in.readDouble();
        h = in.readDouble();
        result = in.readDouble();
        // Опционально: проверка корректности загруженных данных
        if (!isValid(a) || !isValid(b) || !isValid(h)) {
            throw new IOException("Некорректные значения a, b, h при загрузке");
        }
    }
    
    
}

    
