package com.example.asia.jmpro;

import com.example.asia.jmpro.logic.validation.EmailValidator;

import junit.framework.TestCase;

import org.junit.Test;

public class EmailValidatorUnitTest extends TestCase {

    private EmailValidator sut; // system under test

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        sut = new EmailValidator();
    }

    @Test
    public void testThatValidEmailIsReportedAsValid() {
        boolean result = sut.validate("joannasia.maciak@gmail.com");

        assertTrue(result);
    }

    @Test
    public void testThatEmailWithoutDomainIsInvalid() {
        boolean result = sut.validate("jkbturek@gmail");

        assertFalse(result);
    }

    @Test
    public void testThatEmailWithoutUsernameIsInvalid() {
        boolean result = sut.validate("@gmail.com");

        assertFalse(result);
    }

    @Test
    public void testThatEmailWithoutAtSignIsInvalid() {
        boolean result = sut.validate("joannasia.maciakgmail.com");

        assertFalse(result);
    }

}
