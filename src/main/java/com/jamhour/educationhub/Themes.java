package com.jamhour.educationhub;

import atlantafx.base.theme.*;
import lombok.Getter;

@Getter
public enum Themes {
    PRIMER_LIGHT(new PrimerLight()),
    NORD_LIGHT(new NordLight()),
    CUPERTINO_LIGHT(new CupertinoLight()),
    PRIMER_DARK(new PrimerDark()),
    CUPERTINO_DARK(new CupertinoDark()),
    NORD_DARK(new NordDark()),
    DRACULA(new Dracula());

    private final Theme theme;

    Themes(Theme theme) {
        this.theme = theme;
    }

    @Override
    public String toString() {
        return switch (this) {
            case PRIMER_LIGHT -> "Primer Light";
            case PRIMER_DARK -> "Primer Dark";
            case CUPERTINO_LIGHT -> "Cupertino Light";
            case CUPERTINO_DARK -> "Cupertino Dark";
            case NORD_LIGHT -> "Nord Light";
            case NORD_DARK -> "Nord Dark";
            case DRACULA -> "Dracula";
        };
    }
}
