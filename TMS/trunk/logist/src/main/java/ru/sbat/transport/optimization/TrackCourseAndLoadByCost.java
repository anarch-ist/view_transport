package ru.sbat.transport.optimization;


import java.util.HashMap;

public class TrackCourseAndLoadByCost extends HashMap<TrackCourse, LoadCostOfTrackCourse>{
    /** Save available load by cost for track course
     *
     * @param invoiceContainer
     * @return pair of track course and the rest of load by cost
     */

    public TrackCourseAndLoadByCost getLoadByCostForTrackCourse(InvoiceContainer invoiceContainer){
        TrackCourseAndLoadByCost result = new TrackCourseAndLoadByCost();
        for(Invoice invoice: invoiceContainer){
            if(invoice.getDeliveryRoute() != null){
                for(TrackCourse trackCourse: invoice.getDeliveryRoute()){
                    LoadCostOfTrackCourse loadCostOfTrackCourse = new LoadCostOfTrackCourse(trackCourse.getLoadByCost());
                    loadCostOfTrackCourse = loadCostOfTrackCourse.getAvailableLoadCost(invoice, loadCostOfTrackCourse);
                    result.put(trackCourse, loadCostOfTrackCourse);
                }
            }
        }
        return  result;
    }
}
