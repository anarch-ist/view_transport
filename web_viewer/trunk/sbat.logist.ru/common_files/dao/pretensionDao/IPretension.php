<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface IPretension
{

    function getPretensions($requestIDExternal);

    function addPretension($requestIDExternal,$pretensionComment,$pretensionStatus,$pretensionCathegory,$pretensionPositionNumber,$pretensionSum);

    function updatePretension($pretensionID,$requestIDExternal, $pretensionComment,$pretensionCathegory,$pretensionPositionNumber,$pretensionSum);

    function closePretension($pretensionID, $requestIDExternal);

}