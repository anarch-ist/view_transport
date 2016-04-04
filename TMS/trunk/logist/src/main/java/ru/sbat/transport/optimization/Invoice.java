package ru.sbat.transport.optimization;


import javafx.beans.property.*;
import ru.sbat.transport.optimization.location.DeliveryRoute;
import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.TrackCourse;
import ru.sbat.transport.optimization.optimazerException.IncorrectRequirement;
import ru.sbat.transport.optimization.user.MarketAgent;
import ru.sbat.transport.optimization.utils.InvoiceType;

import java.util.*;

public class Invoice implements Comparable<Invoice>{
    private final ObjectProperty<Request> request = new SimpleObjectProperty<>();
    private final ObjectProperty<Point> addressOfWarehouse = new SimpleObjectProperty<>();
    private final DoubleProperty weight = new SimpleDoubleProperty(); //масса
    private final DoubleProperty volume = new SimpleDoubleProperty();//объем
    private final IntegerProperty countOfBoxes = new SimpleIntegerProperty();//кол-во коробок
    private final ObjectProperty<MarketAgent> marketAgent = new SimpleObjectProperty<>();
    private final IntegerProperty priority = new SimpleIntegerProperty();
    private final ObjectProperty<DeliveryRoute> deliveryRoute = new SimpleObjectProperty<>();
    private final ObjectProperty<Date> creationDate = new SimpleObjectProperty<>();
    private final DoubleProperty cost = new SimpleDoubleProperty();
    private final ObjectProperty<InvoiceType> invoiceType = new SimpleObjectProperty<>();
    private final ObjectProperty<Date> realDepartureDate = new SimpleObjectProperty<>();
    private final ObjectProperty<List<PartOfDeliveryRoute>> partsOfDeliveryRoute = new SimpleObjectProperty<>();

    public Invoice() {
    }

    public double getCost() {
        return cost.get();
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    public DoubleProperty costProperty() {
        return cost;
    }

    public Date getCreationDate() {
        return creationDate.get();
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate.set(creationDate);
    }

    public ObjectProperty<Date> creationDateProperty() {
        return creationDate;
    }

    public DeliveryRoute getDeliveryRoute() {
        return deliveryRoute.get();
    }

    public void setDeliveryRoute(DeliveryRoute deliveryRoute) {
        this.deliveryRoute.set(deliveryRoute);
    }

    public ObjectProperty<DeliveryRoute> deliveryRouteProperty() {
        return deliveryRoute;
    }

    public int getPriority() {
        return priority.get();
    }

    public void setPriority(int priority) {
        this.priority.set(priority);
    }

    public IntegerProperty priorityProperty() {
        return priority;
    }

    public MarketAgent getMarketAgent() {
        return marketAgent.get();
    }

    public void setMarketAgent(MarketAgent marketAgent) {
        this.marketAgent.set(marketAgent);
    }

    public ObjectProperty<MarketAgent> marketAgentProperty() {
        return marketAgent;
    }

    public int getCountOfBoxes() {
        return countOfBoxes.get();
    }

    public void setCountOfBoxes(int countOfBoxes) {
        this.countOfBoxes.set(countOfBoxes);
    }

    public IntegerProperty countOfBoxesProperty() {
        return countOfBoxes;
    }

    public double getVolume() {
        return volume.get();
    }

    public void setVolume(double volume) {
        this.volume.set(volume);
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    public double getWeight() {
        return weight.get();
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public DoubleProperty weightProperty() {
        return weight;
    }

    public Point getAddressOfWarehouse() {
        return addressOfWarehouse.get();
    }

    public void setAddressOfWarehouse(Point addressOfWarehouse) {
        this.addressOfWarehouse.set(addressOfWarehouse);
    }

    public ObjectProperty<Point> addressOfWarehouseProperty() {
        return addressOfWarehouse;
    }

    public Request getRequest() {
        return request.get();
    }

    public void setRequest(Request request) {
        this.request.set(request);
    }

    public ObjectProperty<Request> requestProperty() {
        return request;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType.get();
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType.set(invoiceType);
    }

    public ObjectProperty<InvoiceType> invoiceTypeProperty() {
        return invoiceType;
    }

    public Date getRealDepartureDate() {
        return realDepartureDate.get();
    }

    public void setRealDepartureDate(Date realDepartureDate) {
        this.realDepartureDate.set(realDepartureDate);
    }

    public ObjectProperty<Date> realDepartureDateProperty() {
        return realDepartureDate;
    }

    public List<PartOfDeliveryRoute> getPartsOfDeliveryRoute() {
        return partsOfDeliveryRoute.get();
    }

    public ObjectProperty<List<PartOfDeliveryRoute>> partsOfDeliveryRouteProperty() {
        return partsOfDeliveryRoute;
    }

    public void setPartsOfDeliveryRoute(List<PartOfDeliveryRoute> partOfDeliveryRoute) {
        this.partsOfDeliveryRoute.set(partOfDeliveryRoute);
    }

    /** determines week day of invoice's creation date
     *
     * @return day of week from date
     */
    public int getWeekDay(){
        return this.getCreationDate().getDay() + 1;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "request=" + request +
                ", addressOfWarehouse=" + addressOfWarehouse +
                ", weight=" + weight +
                ", volume=" + volume +
                ", countOfBoxes=" + countOfBoxes +
                ", marketAgent=" + marketAgent +
                ", priority=" + priority +
                ", route=" + deliveryRoute +
                ", creationDate=" + creationDate +
                ", cost=" + cost +
                ", invoiceType=" + invoiceType +
                ", realDepartureDate=" + realDepartureDate +
                '}';
    }

    @Override
    public int compareTo(Invoice o) {
        if(this.getRequest().getPlannedDeliveryDate().before(o.getRequest().getPlannedDeliveryDate()))
            return 1;
        else if(this.getRequest().getPlannedDeliveryDate().equals(o.getRequest().getPlannedDeliveryDate()))
            return 0;
        else
        return 0;
    }

    public List<Integer> calculateNumbersOfWeeksBetweenDates(Date departureDate, Date deliveryDate, Invoice invoice, TrackCourse startTrackCourse, TrackCourse endTrackCourse) throws IncorrectRequirement {
        List<Integer> result = new ArrayList<>();
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(departureDate);
        int numberOfDepartureDate = calendarStart.get(Calendar.WEEK_OF_YEAR);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(deliveryDate);
        int numberOfDeliveryDate = calendarEnd.get(Calendar.WEEK_OF_YEAR);
        while (numberOfDepartureDate != numberOfDeliveryDate){
            result.add(numberOfDepartureDate);
            numberOfDepartureDate++;
        }
        result.add(numberOfDeliveryDate);
        if(!isFittingDateForInvoiceDeparture(invoice.getCreationDate(), startTrackCourse)){
            result.remove(0);
        }if(result.size() != 0 && !isFittingDateForInvoiceDelivery(invoice.getRequest().getPlannedDeliveryDate(), endTrackCourse)){
            result.remove(result.size() - 1);
        }
        return result;
    }

    public boolean isFittingDateForInvoiceDeparture(Date date, TrackCourse trackCourse) throws IncorrectRequirement {
        int invoiceDayOfWeek = date.getDay() + 1;
        int routeDayOfWeek = trackCourse.getRoute().getWeekDayOfActualDepartureDateInRoutePoint(trackCourse.getStartTrackCourse());
        int timeOfInvoice = date.getHours() * 60 + date.getMinutes() + trackCourse.getStartTrackCourse().getLoadingOperationsTime();
        int departureTime = trackCourse.getRoute().getActualDepartureTimeFromRoutePoint(trackCourse.getStartTrackCourse());
        if(routeDayOfWeek != 1) {
            if ((invoiceDayOfWeek > routeDayOfWeek) || (invoiceDayOfWeek == 1) || (invoiceDayOfWeek == routeDayOfWeek && timeOfInvoice >= departureTime)) {
                return false;
            }
        }else if(routeDayOfWeek == 1){
            if((invoiceDayOfWeek == 1 && timeOfInvoice >= departureTime)){
                return false;
            }
        }
        return true;
    }

    public boolean isFittingDateForInvoiceDelivery(Date date, TrackCourse trackCourse) throws IncorrectRequirement {
        int invoiceDayOfWeek = date.getDay() + 1;
        int routeDayOfWeek = trackCourse.getRoute().getWeekDayOfActualArrivalDateInRoutePoint(trackCourse.getEndTrackCourse());
        int timeOfInvoice = date.getHours() * 60 + date.getMinutes();
        int deliveryTime = trackCourse.getRoute().getActualArrivalTimeInRoutePoint(trackCourse.getEndTrackCourse()) +  + trackCourse.getEndTrackCourse().getLoadingOperationsTime();
//        System.out.println(deliveryTime + " " + timeOfInvoice + " " + routeDayOfWeek);
        if(routeDayOfWeek != 1) {
            if (((invoiceDayOfWeek != 1) && (invoiceDayOfWeek < routeDayOfWeek)) || (invoiceDayOfWeek == routeDayOfWeek && timeOfInvoice < deliveryTime)) {
                return false;
            }
        }else if(routeDayOfWeek == 1){
            if((invoiceDayOfWeek == 1 && timeOfInvoice < deliveryTime) || invoiceDayOfWeek > routeDayOfWeek){
                return false;
            }
        }
        return true;
    }
}
