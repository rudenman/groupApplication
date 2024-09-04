package dev.unoSoft;

import java.util.Objects;

public class LinePart {
    private final String value;
    private final int columnIndex;

    public LinePart(String value, int columnIndex) {
        this.value = value;
        this.columnIndex = columnIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinePart linePart = (LinePart) o;
        return Objects.equals(value, linePart.value) && Objects.equals(columnIndex, linePart.columnIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, columnIndex);
    }
}
