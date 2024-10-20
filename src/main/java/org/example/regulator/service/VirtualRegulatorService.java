package org.example.regulator.service;
import org.example.regulator.model.VirtualRegulator;

import java.util.ArrayList;
import java.util.List;

public class VirtualRegulatorService implements Regulator {

    private static final float MAX_TEMPERATURE = 1000.0f;
    private static final float MIN_TEMPERATURE = -200.0f;
    private VirtualRegulator virtualRegulator;

    public VirtualRegulatorService(VirtualRegulator virtualRegulator) {
        this.virtualRegulator = virtualRegulator;
    }

    @Override
    public void setTemperatura(List<Float> outData) {
        if (!virtualRegulator.isRunning()){
            for (float temp : outData) {
                virtualRegulator.setInData(temp);
            }
        }else {
            getValuesTemperature(outData);
        }
    }

    @Override
    public int getValuesTemperature(List<Float> outData){
        if (virtualRegulator.getOutData().isEmpty()) {
        return 4; // Ошибка: нет значений для получения
        } else {
            getLastThreeValues(outData);
            return 0;
        }
    }

    @Override
    public void deleteValuesTemperature(){
      virtualRegulator.setRunning(false);
    }

    @Override
    public int adjustTemp(byte operation, float inData, List<Float> outData, int offsetOut){
        int readCount = (operation >> 4) & 0b00001111; // Получаем 4-7 биты

        // Операция очистки
        if ((operation & 0b10000000) != 0) {
            deleteValuesTemperature();
        }

        // Добавление новых значений
        if ((operation & 0b01000000) != 0) {
            setTemperatura(outData);
        }

        // Операция получения значений температуры
        if ((operation & 0b00100000) != 0) {
            getValuesTemperature(outData);
        }

        // Операция задания температуры
        if ((readCount & 0b00000100) != 0) {
            checkingOnMinMaxTemperature(inData);
        }

        // Проверка резервированного бита
        if ((readCount & 0b00000001) != 0) {
            return 6; // Ошибка: резервированный бит не равен 1
        }
        return 0;
    }

    private int checkingOnMinMaxTemperature(float inData){
        if (inData < MIN_TEMPERATURE) {
            return 3; // Ошибка: слишком низкая температура
        }
        if (inData > MAX_TEMPERATURE) {
            return 5; // Ошибка: слишком высокая температура
        }
         virtualRegulator.setInData(inData);
        return 0;
    }

    private void getLastThreeValues(List<Float> outData){ // печать последних 3 значений
        if (outData.size() >= 3) {
            List<Float> array = new ArrayList<Float>();
            array.add(outData.get(outData.size()-1));
            array.add(outData.get(outData.size()-2));
            array.add(outData.get(outData.size()-3));

            System.out.println(array);
        } else {
            System.out.println(outData);
        }
    }
}
