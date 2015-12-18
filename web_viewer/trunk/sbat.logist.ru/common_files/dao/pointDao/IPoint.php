<?php
namespace DAO;

interface IPointEntity
{
    function selectPoints();

    function selectPointByID($id);

    function updatePoint($newPoint);

    function deletePoint($Point);

    function addPoint($Point);
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