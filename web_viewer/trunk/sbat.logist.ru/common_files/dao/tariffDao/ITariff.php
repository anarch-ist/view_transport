<?php
namespace DAO;

interface ITariffEntity
{
    function insertNewTariff($data);

    function getLastInsertedID();
}