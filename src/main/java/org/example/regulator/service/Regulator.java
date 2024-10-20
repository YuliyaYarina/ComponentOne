package org.example.regulator.service;

import org.example.regulator.model.VirtualRegulator;

import java.util.List;

public interface Regulator {

    void setTemperatura(List<Float> temperatura);

    int getValuesTemperature(List<Float> outData);

    void deleteValuesTemperature();

    int adjustTemp(byte operation, float inData, List<Float> outData, int offsetOut);

    static Regulator create() {
        return Holder.INSTANCE;
    }

    static void shutDown() {
        if (Holder.INSTANCE != null) {
            Holder.INSTANCE.deleteValuesTemperature();
            Holder.INSTANCE = null;
            System.out.println("Регулятор завершил работу.");
        } else {
            System.out.println("Регулятор уже выключен.");
        }
    }

    class Holder {
        private static Regulator INSTANCE = new VirtualRegulatorService(new VirtualRegulator());
    }
}
