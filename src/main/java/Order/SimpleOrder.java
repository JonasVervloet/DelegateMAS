package Order;

import com.github.rinde.rinsim.core.model.pdp.Parcel;
import com.github.rinde.rinsim.geom.Point;

/*
A simple order. This order has a destination that it
    should reach. An AGV agent will have to pick up
    the package and deliver it at the destination.
 */
public class SimpleOrder extends Parcel {


    /*
    Constructor
     */
    public SimpleOrder(Point startPosition, Point destination) {
        super(
                Parcel.builder(
                        startPosition, destination
                ).serviceDuration(20)
                .buildDTO()
        );
    }
}
