<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface IPointEntity
{
    function selectAllPointIDAndPointName();

    function selectPointByID($id);

    function updatePoint(PointEntity $newPoint);

    function deletePoint(PointEntity $Point);

    function addPoint(PointEntity $Point);

    function selectPointByUserID($userID);
}



//pointID
//pointName
//region
//timeZone
//docs
//comments
//openTime
//closeTime
//district
//locality
//mailIndex
//address
//email
//phoneNumber
//pointTypeID