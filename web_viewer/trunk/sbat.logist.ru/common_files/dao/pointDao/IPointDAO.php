<?php
namespace DAO;

interface IPointDAO
{
    function selectPoints();

    function selectPointByID($id);

    function updatePoint($newPoint);

    function deletePoint($Point);

    function addPoint($Point);
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