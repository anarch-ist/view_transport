<?php
namespace DAO;

interface IInvoicesForUserDAO
{
    function selectAllData();

    function selectDataByKey($keyword);
}