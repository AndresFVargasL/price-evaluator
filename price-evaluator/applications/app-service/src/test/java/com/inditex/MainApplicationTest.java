package com.inditex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MainApplicationTest {

    @Test
    void main_shouldStartApplicationWithoutExceptions() {
        // Verificamos que el arranque del contexto no rompa
        assertDoesNotThrow(() -> MainApplication.main(new String[]{}));
    }
}
