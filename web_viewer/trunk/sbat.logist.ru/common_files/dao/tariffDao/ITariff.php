<?php
namespace DAO;

interface ITariffEntity
{
    function insertTariff($data);

    function getLastInsertedID();

    function getTariffById($id);

    function updateTariff($tariffData, $id);
}