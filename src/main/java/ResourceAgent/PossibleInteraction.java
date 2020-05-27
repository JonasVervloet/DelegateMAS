//package ResourceAgent;
//
//import Ants.Path;
//
//public class PossibleInteraction {
//
//    private int pickupTime;
//
//    private String vehicle;
//
//    private Path path;
//
//
//    public PossibleInteraction(int pickupTime, String vehicle, Path path) {
//        if (pickupTime < 0) {
//            System.out.println("PICK UP | NEGATIVE PICKUP TIME");
//        }
//        this.pickupTime = pickupTime;
//        this.vehicle = vehicle;
//        if (path == null) {
//            System.out.println("PICK UP | PATH NULLPOINTER");
//        }
//        this.path = path;
//    }
//
//    public int getPickupTime() {
//        return pickupTime;
//    }
//
//    public String getVehicle() {
//        return vehicle;
//    }
//
//    public Path getPath() {
//        return path;
//    }
//}
