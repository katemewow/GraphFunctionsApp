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

    // Метод для вызова функции по названию
    public double callFunction(String functionName, double x, double y, double param1, double param2) {
        switch (functionName) {
            case "Basic_func1":
                return Basic_func1(x, y, param1, param2);
            case "Basic_func2":
                return Basic_func2(x, y, param1, param2);
            // Добавляем новые функции по мере необходимости
            // case "Basic_func3":
            //     return Basic_func3(x, y, param1, param2);
            default:
                throw new IllegalArgumentException("Unknown function name: " + functionName);
        }
    }
}
