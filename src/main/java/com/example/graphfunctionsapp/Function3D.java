package com.example.graphfunctionsapp;

public class Function3D {
    public double Basic_func1(double x, double y, double param1, double param2) {
        // Здесь можно изменить формулу на любую другую функцию
        return (Math.sin(x * param1) * Math.cos(y * param2));
    }

    public double Basic_func2(double x, double y, double param1, double param2) {
        // Здесь можно изменить формулу на любую другую функцию
        return (Math.sin(x * param1 + param2) * Math.cos(y + x));
    }

    public double Func_veronica(double x, double y, double param1, double param2) {
        // Здесь можно изменить формулу на любую другую функцию
        return(Math.sin(Math.PI * x) * Math.sin(Math.PI * y) * Math.exp(-0.1 * Math.PI * Math.PI * param1));
    }

    public double Func_liza(double x, double y, double param1, double param2) {
        // Здесь можно изменить формулу на любую другую функцию
            return (Math.sin(x*y*param1)*Math.cos(param2));
    }

    // Метод для вызова функции по названию
    public double callFunction(String functionName, double x, double y, double param1, double param2) {
        switch (functionName) {
            case "Basic_func1":
                return Basic_func1(x, y, param1, param2);
            case "Basic_func2":
                return Basic_func2(x, y, param1, param2);
//             Добавляем новые s
             case "Func_veronica":
                 return Func_veronica(x, y, param1, param2);
             case "Func_liza":
                 return Func_veronica(x, y, param1, param2);
            default:
                throw new IllegalArgumentException("Unknown function name: " + functionName);
        }
    }
}
