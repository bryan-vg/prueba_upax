package com.mycompany.myapp.web.rest.errors;

import org.springframework.http.HttpStatus;

public class ExceptionApi extends Exception {

    private String mensaje;
    private HttpStatus httpStatus;

    public ExceptionApi(String mensaje, HttpStatus httpStatus) {
        this.mensaje = mensaje;
        this.httpStatus = httpStatus;
    }

    //constructor para las excepciones controladas
    public ExceptionApi(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
