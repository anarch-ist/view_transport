<?php
namespace DAO;

interface IExchaneLog
{
    function selectLastTenTransactions();

    function selectLastTransaction();

    function selectLastXTransactions($howMany);

}
