<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface IRequestEntity
{
    /**
     * @return array of RequestData
     */
    function selectRequests(); //covered
    //unnecessary

    function selectRequestByID($id);
    //unnecessary

    function updateRequest($newRequest);

    function updateRequestStatus($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment, $vehicleNumber, $goodCost ,$hoursAmount);
    //covered

    function deleteRequest($Request);
    //later

    function addRequest($requestData);
    //later

    function getRequestStatuses(\PrivilegedUser $pUser);
    //unnecessary

    function getRequestsForRouteList($routeListID); //covered

    function getRequestHistoryByRequestIdExternal($requestIDExternal); //covered
}