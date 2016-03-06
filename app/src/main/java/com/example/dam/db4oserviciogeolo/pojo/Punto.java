package com.example.dam.db4oserviciogeolo.pojo;


import java.util.Date;

public class Punto {
/*
Clase posición
float latitud, longitud
Date fecha
 */

    private double latitud, longitud;
    private Date fecha;

    public Punto() {
        this(0,0, null);
    }

    public Punto(double latitud, double longitud, Date fecha) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Punto posicion = (Punto) o;

        if (Double.compare(posicion.latitud, latitud) != 0) return false;
        if (Double.compare(posicion.longitud, longitud) != 0) return false;
        return !(fecha != null ? !fecha.equals(posicion.fecha) : posicion.fecha != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitud);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitud);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (fecha != null ? fecha.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Punto{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                ", fecha=" + fecha +
                '}';
    }
}
