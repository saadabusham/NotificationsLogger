package com.sedo.notificationlogger.data.models;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardBuilder;

public class LocalCupboard {

    private static Cupboard cupboard;

    static {
        getInstance().register(LoggerModel.class);
    }

    public static Cupboard getInstance() {
        if (cupboard == null) {
            cupboard = new CupboardBuilder().build();
        }
        return cupboard;
    }

    public static Cupboard getAnnotatedInstance() {
        return new CupboardBuilder(getInstance())
                .useAnnotations()
                .build();
    }

    private LocalCupboard() {
    }
}

