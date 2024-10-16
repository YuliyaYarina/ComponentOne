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
    public void setTemperatura(List<Float> temperatura) {
        if (!virtualRegulator.isRunning()){
            for (float temp : temperatura) {
                virtualRegulator.setInData(temp);
            }
        }else {
            getValuesTemperature();
        }
    }

    @Override
    public List<Float> getValuesTemperature(){
        return virtualRegulator.getOutData();
    }

    @Override
    public void deleteValuesTemperature(){
      virtualRegulator.setRunning(false);
    }

    @Override
    public int adjustTemp(byte operation, float inData, List<Float> outData, int offsetOut){
        int opCode = operation & 0b11100000; // Получаем первые три бита
        int readCount = (operation >> 4) & 0b00001111; // Получаем 4-7 биты

        // Операция очистки
        if ((operation & 0b10000000) != 0) {
            deleteValuesTemperature();
        }

        // Добавление новых значений
        if ((opCode & 0b01000000) != 0) {
            setTemperatura(outData);
        }

        // Операция получения значений температуры
        if ((opCode & 0b00100000) != 0) {
            if (virtualRegulator.getOutData().isEmpty()) {
                return 4; // Ошибка: нет значений для получения
            }
            int sizeToCopy = Math.min(readCount, virtualRegulator.getOutData().size());
            int startIndex = Math.max(0, virtualRegulator.getOutData().size() - sizeToCopy + offsetOut);

            for (int i = 0; i < sizeToCopy; i++) {
                if (startIndex + i < virtualRegulator.getOutData().size()) {
                    outData.add(virtualRegulator.getOutData().get(startIndex + i));
                }
            }
        }

        // Операция задания температуры
        if ((operation & 0b00000100) != 0) {
            if (inData < MIN_TEMPERATURE) {
                return 3; // Ошибка: слишком низкая температура
            }
            if (inData > MAX_TEMPERATURE) {
                return 5; // Ошибка: слишком высокая температура
            }
            virtualRegulator.setInData(inData);
        }

        // Проверка резервированного бита
        if ((operation & 0b00000001) != 0) {
            return 6; // Ошибка: резервированный бит не равен 1
        }

        return 0;
    }
}
