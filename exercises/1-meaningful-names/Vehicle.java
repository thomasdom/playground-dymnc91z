public class Vhcl {
    protected String license_number;
    protected String make;
    protected String vehicleModel;
    protected double priceInfo;

    public Vhcl(String l_n, String v_make, String v_model, double v_price) {
        license_number = l_n;
        make = v_make;
        vehicleModel = v_model;
        priceInfo = v_price;
    }

    public String getVhclName() {
        return make + " " + vehicleModel;
    }

    public double price() {
        return priceInfo;
    }

    public String toString() {
        String result;

        result = "License # : " + license_number + "\nVehicle Name : " + getVhclName() + "\nPrice : " + price;
        return result;
    }
}

public class TestVhcl {
    public static void main(String[] arg) {
        Vhcl[] comp1161_motors = new Vhcl[4];
        Vhcl v1 = new Vhcl("FG1000", "Honda", "Civic", 100000.00);
        Vhcl v2 = new Vhcl("GH7000", "Mercedez", "BClass", 125000.00);
        Vhcl v3 = new Vhcl("AY3000", "Bugatti", "Veyron", 300000.00);
        Vhcl v4 = new Vhcl("HI2000", "Nissan", "Sunny", 75000.00);
        Vhcl me = new Vhcl("BW1099", "Nissan", "Tida", 120000);

        comp1161_motors[0] = v1;
        comp1161_motors[1] = v2;
        comp1161_motors[2] = v3;
        comp1161_motors[3] = v4;

        for (Vhcl veh : comp1161_motors) {
            System.out.println();
            System.out.println(veh);
        }
    }
}
