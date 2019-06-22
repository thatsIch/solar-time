package de.thatsich.solartime.entity;


/**
 * Intermediate variables used in the sunrise equation
 * @see <a href="http://en.wikipedia.org/wiki/Sunrise_equation">Sunrise equation on Wikipedia</a>
 */
public class SolarEquationVariables {
    // Julian cycle (number of days since Jan 1st, 2000 12:00).
    private final double n;

    // solar mean anomaly
    private final double m;

    // ecliptic longitude
    private final double lambda;

    // Solar transit (hour angle for solar noon)
    private final double jtransit;

    // Declination of the sun
    private final double delta;

    public SolarEquationVariables(double n, double m, double lambda, double jtransit, double delta) {
        this.n = n;
        this.m = m;
        this.lambda = lambda;
        this.jtransit = jtransit;
        this.delta = delta;
    }

    public double getN() {
        return n;
    }

    public double getM() {
        return m;
    }

    public double getLambda() {
        return lambda;
    }

    public double getJtransit() {
        return jtransit;
    }

    public double getDelta() {
        return delta;
    }
}
