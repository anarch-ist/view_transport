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

    function updateRequest(RequestData $newRequest);

    function updateRequestStatus($userID, $requestIDExternal, $newRequestStatus, $datetime, $comment);
    //covered

    function deleteRequest(RequestData $Request);
    //later

    function addRequest(RequestData $Request);
    //later

    function getRequestStatuses(\PrivilegedUser $pUser);
    //unnecessary

    function getRequestsForRouteList($routeListID); //covered

    function getRequestHistoryByRequestIdExternal($requestIDExternal); //covered
}