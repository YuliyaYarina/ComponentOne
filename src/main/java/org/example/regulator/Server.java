package org.example.regulator;

import org.example.regulator.service.Regulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        List<Float> outData = new ArrayList<>(Arrays.asList(5f, 10.5f, 20.1f, 40.5f, 41.9f));
        // Создать и включить регулятор
        Regulator regulator = Regulator.create();
        // Задать температуру
        regulator.setTemperatura(outData);
        System.out.println("Температуры: " + regulator.getValuesTemperature());

        // Очистить значения температуры
        regulator.deleteValuesTemperature();
        System.out.println("Температуры после очистки: " + regulator.getValuesTemperature());

        // Завершим работу
        Regulator.shutDown();

        System.out.println(regulator.adjustTemp((byte) 0b11100101, -300, outData, 5));
    }
}